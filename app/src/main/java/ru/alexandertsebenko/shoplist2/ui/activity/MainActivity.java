package ru.alexandertsebenko.shoplist2.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ProdCategory;
import ru.alexandertsebenko.shoplist2.utils.ProductXmlParser;

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
            case R.id.btn_test:
                Toast.makeText(this,"!!",Toast.LENGTH_SHORT).show();
                InputStream in = getResources().openRawResource(R.raw.products);
                ProductXmlParser parser = new ProductXmlParser();
                try {
                    List list = parser.parse(in);
                    for (Object o : list){
                        ProdCategory prodCategory = (ProdCategory)o;
                        System.out.println(prodCategory.getName());
                        System.out.println(prodCategory.getImage());
                        for (String s : prodCategory.getProductNames()){
                            System.out.println("  " + s);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
