package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;

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
        ProductInstance productInstance = (ProductInstance) childListItem;
        childProductViewHolder.bind(productInstance);
    }

    @Override
    public Object getListItem(int position) {
        return super.getListItem(position);
    }

    public void deleteProductInstance(int position) {
        //Возможно это и некрасиво но работает.
        //метод возвращает масив из двух об
        Object[] parentItemWithPosition =
        removeCildItem(position);

        ParentItem pI = (ParentItem) parentItemWithPosition[0];
        if(pI.getChildItemList().size() == 0){
            int parentPosition = (Integer)parentItemWithPosition[1];
            getParentItemList().remove(parentPosition);
            notifyParentItemRemoved(parentPosition);
        }
    }
    /**
     * Метод удалаяет child елемент из данных адаптера
     * и уведомляет адаптер об изменениях.
     * В итоге возвращает ссылку на родительский элемент
     * из которого был удален child элемент
     * @param position
     * @return
     */
    private Object[] removeCildItem(int position){
        int childPosition = 0;
        int parentPosition = 0;
        boolean parenFound = false;
        for(int i = position; i > 0; i--) {
            if(mItemList.get(i).getClass().equals(ParentWrapper.class)) {
                if(!parenFound){
                    parenFound = true;
                }
                parentPosition++;
            } else if(!parenFound) {
                childPosition++;
            }
        }
        childPosition--;
        ParentItem p = (ParentItem)getParentItemList().get(parentPosition);
        List<ProductInstance> childList = p.getChildItemList();
        childList.remove(childPosition);
        notifyChildItemRemoved(parentPosition,childPosition);
        HashMap<Integer,ParentItem> map = new HashMap<>();
        ArrayList<Object> l = new ArrayList<>();
        Object[] o = {p,Integer.valueOf(parentPosition)};
        return o;
    }
}
