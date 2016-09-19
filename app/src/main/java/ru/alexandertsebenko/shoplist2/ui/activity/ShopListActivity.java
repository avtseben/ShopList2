package ru.alexandertsebenko.shoplist2.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.ui.fragment.ProductListFragment;

public class ShopListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_list_activity);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_fragment_container, new ProductListFragment(), "PROD_LIST");
        ft.addToBackStack(null);
        ft.commit();
    }
}
