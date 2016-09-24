package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;

public class ChildProductViewHolder extends ChildViewHolder {
    TextView mProductView;
    public ChildProductViewHolder(View itemView){
        super(itemView);
        mProductView = (TextView) itemView.findViewById(R.id.tv_product_name);
    }
    public void bind(Product product){
        mProductView.setText(product.getName());
    }
}
