package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;

public class ChildProductViewHolder extends ChildViewHolder {
    TextView mProductInstanceView;
    public ChildProductViewHolder(View itemView){
        super(itemView);
        mProductInstanceView = (TextView) itemView.findViewById(R.id.tv_product_name);
    }
    public void bind(ProductInstance productInstance){
        mProductInstanceView.setText(productInstance.getProduct().getName());
    }
}
