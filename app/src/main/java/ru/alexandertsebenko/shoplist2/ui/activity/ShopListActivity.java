package ru.alexandertsebenko.shoplist2.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.ui.adapter.SearchAutoCompleteAdapter;
import ru.alexandertsebenko.shoplist2.ui.fragment.ProductListFragment;

public class ShopListActivity extends AppCompatActivity{

    private AutoCompleteTextView mAcTextView;
    private SearchAutoCompleteAdapter mSearchAdapter;
    private FragmentManager mFragManager;
    private final String LIST_FRAGMENT_TAG = "slft";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        //Список продуктов представлен во фрагменте
        mFragManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragManager.beginTransaction();
        ft.replace(R.id.fl_fragment_container, new ProductListFragment(), LIST_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        setSearchBoxInActionBar();
        return true;
    }
    //TODO:Разобраться как правильно раскрывать поисковый View
    //при нажатии на эконку лупы в меню
    //TODO:добавить голосовой поиск
    //TODO:добавить крестик в AutoCompleteTextView чтобы можно было
    // удалить слово в одно нажатие
    private void setSearchBoxInActionBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_USE_LOGO
                | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_HOME_AS_UP);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.actionbar_search, null);
        mAcTextView =  (AutoCompleteTextView) v.findViewById(R.id.search_box);

        mSearchAdapter = new SearchAutoCompleteAdapter(this);
        mAcTextView.setAdapter(mSearchAdapter);
        mAcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getItemAtPosition(position);
                mAcTextView.setText("");
                productSelected(product);
            }
        });
        actionBar.setCustomView(v);
    }
    private void productSelected(Product product) {
        ProductListFragment plf = (ProductListFragment) mFragManager.findFragmentByTag(LIST_FRAGMENT_TAG);
        //Всё. Продукт мы нашли, теперь передаём его фрагменту которы отвечает за работу со списком:
        //запись в БД, RecyclerView и тд
        plf.addProduct(product);
    }


}
