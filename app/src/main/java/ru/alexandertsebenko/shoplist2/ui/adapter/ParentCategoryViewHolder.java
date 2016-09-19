package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.utils.ResourceManipulator;

public class ParentCategoryViewHolder extends ParentViewHolder {
    ImageView mCategoryImageView;
    public ParentCategoryViewHolder(View itemView){
        super(itemView);
        mCategoryImageView = (ImageView)itemView.findViewById(R.id.iv_category);
    }
    public void bind(ParentItem prodCat) {
        //Set Image of Category
        int resId;
        try {
            resId = ResourceManipulator.getResIdByStringName(itemView.getContext(),
                    prodCat.getName(), "drawable");
            mCategoryImageView.setImageResource(resId);
        } catch (NullPointerException e) {
            mCategoryImageView.setImageResource(R.drawable.vegetables);
            Log.e(getClass().getSimpleName(),"Cannot find drawable resource!");
        }
    }
}
