package ru.alexandertsebenko.shoplist2.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.db.DataSource;

public class MainActivity extends AppCompatActivity {

    Button btnLaunchShopList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLaunchShopList = (Button)findViewById(R.id.btn_start_shop_list);
        DataSource dataSource = new DataSource(this);

    }

    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.btn_start_shop_list:
                intent = new Intent(this, ShopListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_search_activity:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
