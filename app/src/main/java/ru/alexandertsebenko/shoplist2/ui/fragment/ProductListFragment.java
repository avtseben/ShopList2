package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.ui.adapter.ParentItem;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.ui.adapter.ShopListAdapter;

public class ProductListFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<ParentItem> mParentItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list,container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    //Формируем списко для адаптера
    //TODO:сохранять список при изменении ориентации
    private List<ParentItem> prepareList(Product product) {
        //Первый продукт в списке
        if(mParentItemList == null) mParentItemList = new ArrayList<>();
        boolean productAdded = false;
        for(ParentItem pi : mParentItemList){
            //Если продукты такой категории уже есть в списке
            if(pi.getName().equals(product.getCategory())) {
                pi.addProductToList(product);
                productAdded = true;
                break;
            }
        }
        //Если в предыдущем цыкле не нашлось в списке катаегории куда "положить"
        //продукт то создаём эту категорию и кладём в неё продукт
        if(!productAdded) {
            mParentItemList.add(new ParentItem(product.getCategory(),product));
        }
        return mParentItemList;
    }
    public void addProduct(Product product) {
        Toast.makeText(getContext(),product.getName(),Toast.LENGTH_SHORT).show();
        reloadAdapter(product);
    }
    public void reloadAdapter(Product product) {
        ShopListAdapter adapter = new ShopListAdapter(getContext(),prepareList(product));
        mRecyclerView.setAdapter(adapter);
    }
}
