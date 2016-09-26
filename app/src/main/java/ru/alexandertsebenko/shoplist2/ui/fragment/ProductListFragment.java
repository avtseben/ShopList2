package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.ui.adapter.ChildProductViewHolder;
import ru.alexandertsebenko.shoplist2.ui.adapter.ParentItem;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.ui.adapter.ShopListAdapter;

public class ProductListFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<ParentItem> mParentItemList;
    ShopListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list,container, false);

        setUpRecyclerView(view);
        setUpItemTouchHelper();
        return view;
    }
    private void setUpRecyclerView(View view){
        mParentItemList = new ArrayList<ParentItem>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ShopListAdapter(getContext(),mParentItemList);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    //Формируем списко для адаптера
    //TODO:сохранять список при изменении ориентации
    private List<ParentItem> addProductToList(Product product) {
        boolean productAdded = false;
        for(ParentItem pi : mParentItemList){
            //Если продукты такой категории уже есть в списке
            if(pi.getName().equals(product.getCategory())) {
                pi.addProductInsToList(createProductInstance(product));
                productAdded = true;
                break;
            }
        }
        //Если в предыдущем цыкле не нашлось в списке катаегории куда "положить"
        //продукт то создаём эту категорию и кладём в неё продукт
        if(!productAdded) {
            mParentItemList.add(new ParentItem(product.getCategory(),
                    createProductInstance(product)));
        }
        return mParentItemList;
    }
    private ProductInstance createProductInstance(Product product){
        return new ProductInstance(1,product,1,"штука");//TODO: хардкод заглушка
        //экземпляр покупки 1 штука
    }
    public void addProduct(Product product) {
        mAdapter = new ShopListAdapter(getContext(),addProductToList(product));
        mRecyclerView.setAdapter(mAdapter);
    }
    private void deleteItem(int position) {
        ProductInstance pri = (ProductInstance)mAdapter.getListItem(position);
        Toast.makeText(getContext(),"remove pos: " + pri.getProduct().getName(),Toast.LENGTH_SHORT).show();
    }
    private void setUpItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0/*drag drop не нужен*/, ItemTouchHelper.LEFT
                /*задаём направление свайпа которое слушаем*/) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                                   RecyclerView.ViewHolder viewHolder,
                                                   RecyclerView.ViewHolder target) {
                        //Нам не нужен Drag&Prop
                        return false;
                    }
                    @Override
                    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        if (!viewHolder.getClass().equals(ChildProductViewHolder.class)) {
                            return 0;//Если это не ChildItem тоесть не сам продукт
                            //то никаких свайпов. Свайп разрешен только для продуктов
                        }
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }
                    @Override
                    public void onSwiped (RecyclerView.ViewHolder viewHolder, int direction) {
                        if(viewHolder.getClass().equals(ChildProductViewHolder.class)) {
                            int position = viewHolder.getAdapterPosition();
                            deleteItem(position);
                        }
                    }
                };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}

