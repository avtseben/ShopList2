package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;

public class ShopListAdapter extends ExpandableRecyclerAdapter<ParentCategoryViewHolder,ChildProductViewHolder> {

    LayoutInflater mInflater;

    public ShopListAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ParentCategoryViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_category_parent,viewGroup,false);
        return new ParentCategoryViewHolder(view);
    }

    @Override
    public ChildProductViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_product_child,viewGroup,false);
        return new ChildProductViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ParentCategoryViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        ParentItem prodCat = (ParentItem) parentListItem;
        parentViewHolder.bind(prodCat);
    }

    @Override
    public void onBindChildViewHolder(ChildProductViewHolder childProductViewHolder, int position, Object childListItem) {
        Product product = (Product) childListItem;
        childProductViewHolder.bind(product);
    }

}
