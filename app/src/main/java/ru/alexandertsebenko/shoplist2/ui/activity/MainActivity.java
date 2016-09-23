package ru.alexandertsebenko.shoplist2.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.alexandertsebenko.shoplist2.R;

public class MainActivity extends AppCompatActivity {

    Button btnLaunchShopList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLaunchShopList = (Button)findViewById(R.id.btn_start_shop_list);

    }
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_start_shop_list:
                intent = new Intent(this, ShopListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
