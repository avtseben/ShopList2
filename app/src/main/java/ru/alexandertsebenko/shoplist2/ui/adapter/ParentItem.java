package ru.alexandertsebenko.shoplist2.ui.adapter;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import ru.alexandertsebenko.shoplist2.datamodel.Product;

public class ParentItem implements ParentListItem {

    List<Product> mProductList;
    String mDrawableIdName;

    public ParentItem(String drawableIdName) {
        mDrawableIdName = drawableIdName;
    }
    public ParentItem(List productList) {
        mProductList = productList;
    }

    public void addProductToList (Product product) {
        mProductList.add(product);
    }

    public String getName() {
        return mDrawableIdName;
    }

    public void setProductList(List<Product> productList) {
        mProductList = productList;
    }

    @Override
    public List<?> getChildItemList() {
        return mProductList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
