package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.Arrays;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.ui.adapter.ParentItem;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.ui.adapter.ShopListAdapter;

public class ProductListFragment extends Fragment {

    ShopListAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list,container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ShopListAdapter(getContext(), loadData());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

/*    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((ShopListAdapter)mRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }*/

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //Code for inser data to RecyclerView
    private List<ParentItem> loadData() {
        Product beef = new Product("beef");
        Product cheese = new Product("cheese");
        Product salsa = new Product("salsa");
        Product tortilla = new Product("tortilla");

        ParentItem taco = new ParentItem(Arrays.asList(beef, cheese, salsa, tortilla));
        ParentItem quesadilla = new ParentItem(Arrays.asList(cheese, tortilla));
        List<ParentItem> allCateg = Arrays.asList(taco, quesadilla);
        return allCateg;
    }
}
