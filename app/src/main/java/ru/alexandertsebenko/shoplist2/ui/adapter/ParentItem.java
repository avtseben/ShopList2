package ru.alexandertsebenko.shoplist2.ui.adapter;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;

public class ParentItem implements ParentListItem {

    List<ProductInstance> mProductInstanceList;
    String mName;
    String mDrawableIdName;

    public ParentItem(String name, String drawableIdName, ProductInstance productInstance) {
        mName = name;
        mDrawableIdName = drawableIdName;
        mProductInstanceList = new ArrayList<>();
        addChild(productInstance);
    }
/*    public ParentItem(List productList) {
        mProductList = productList;
    }*/

    public void addChild(ProductInstance productInstance) {
        mProductInstanceList.add(productInstance);
    }
    public void removeChildFromParent(ProductInstance productInstance){
       mProductInstanceList.remove(productInstance);
    }

    public String getName() {
        return mName;
    }
    public String getImageName(){
        return mDrawableIdName;
    }

    @Override
    public List<ProductInstance> getChildItemList() {
        return mProductInstanceList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
