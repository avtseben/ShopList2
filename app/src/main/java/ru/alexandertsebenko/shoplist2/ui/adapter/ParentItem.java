package ru.alexandertsebenko.shoplist2.ui.adapter;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;

public class ParentItem implements ParentListItem {

    List<ProductInstance> mProductInstanceList;
    String mDrawableIdName;

    public ParentItem(String drawableIdName, ProductInstance productInstance) {
        mDrawableIdName = drawableIdName;
        mProductInstanceList = new ArrayList<>();
        addProductInsToList(productInstance);
    }
/*    public ParentItem(List productList) {
        mProductList = productList;
    }*/

    public void addProductInsToList (ProductInstance productInstance) {
        mProductInstanceList.add(productInstance);
    }
    public void removeChildFromParent(ProductInstance productInstance){
       mProductInstanceList.remove(productInstance);
    }

    public String getName() {
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
