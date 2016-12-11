package ru.alexandertsebenko.shoplist2.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.ui.adapter.SearchAutoCompleteAdapter;
import ru.alexandertsebenko.shoplist2.ui.fragment.FirstRunFragment;
import ru.alexandertsebenko.shoplist2.ui.fragment.SendFragment;
import ru.alexandertsebenko.shoplist2.ui.fragment.ProductListFragment;
import ru.alexandertsebenko.shoplist2.ui.fragment.TopFragment;

public class ShopListActivity extends AppCompatActivity implements
        TopFragment.OnNewListButtonClickListener,
        TopFragment.OnShopListItemClickListener,
        FirstRunFragment.OnSaveClicked,
        ProductListFragment.OnSendButtonClickListener{

    public static final int LIST_PREPARE_STATE = 1;
    public static final int DO_SHOPPING_STATE = 2;
    private static final String LIST_FRAGMENT_TAG = "slft";
    private static final String SEND_FRAGMENT_TAG = "sndft";
    private static final String TOP_FRAGMENT_TAG = "tft";
    private static final String FIRST_RUN_FRAGMENT = "frf";
    public static final String MY_NUMBER_PREF = "mynumber";
    public static final String FIRST_RUN_PREF = "firstrun";
    public static final String MY_FULLNAME_PREF = "myfullname";
    public static int mState;

    private AutoCompleteTextView mAcTextView;
    private SearchAutoCompleteAdapter mSearchAdapter;
    private FragmentManager mFragManager;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        mFragManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragManager.beginTransaction();

        //Работа с SharedPreference
        prefs = getSharedPreferences("ru.alexandertsebenko.shoplist2", MODE_PRIVATE);
        if (prefs.getBoolean(FIRST_RUN_PREF, true)) {
            FirstRunFragment frf = new FirstRunFragment();
            ft.replace(R.id.fl_shoplistfragment_container, frf, FIRST_RUN_FRAGMENT);
            ft.addToBackStack(null);
            ft.commit();
            frf.addSharedPrefsToFragment(prefs);
        } else {
            runTopFragment();
        }
    }

    private void runTopFragment() {
        FragmentTransaction ft = mFragManager.beginTransaction();
        ft.replace(R.id.fl_shoplistfragment_container, new TopFragment(), TOP_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**В фрагменте списка нажали кнопку отправить список
     * переходим к фрагменту формирования списка людей кому отправлять
     * список
     */
    @Override
    public void onSendButtonClicked(ShopList shopListPojo) {
        FragmentTransaction ft = mFragManager.beginTransaction();
        SendFragment sf = SendFragment.newInstance(shopListPojo);
        ft.replace(R.id.fl_shoplistfragment_container, sf, SEND_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onNewListClicked() {
        mState = LIST_PREPARE_STATE;
        ProductListFragment plf = ProductListFragment.newInstance(null);
        FragmentTransaction ft = mFragManager.beginTransaction();
        ft.replace(R.id.fl_shoplistfragment_container, plf, LIST_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * выбран список создаём экземпляр ProductListFragment и передаём
     * ему объект - список
     */
    @Override
    public void onItemClicked(ShopList shopListObj) {
        mState = DO_SHOPPING_STATE;
        ProductListFragment plf = ProductListFragment.newInstance(shopListObj);
        FragmentTransaction ft = mFragManager.beginTransaction();
        ft.replace(R.id.fl_shoplistfragment_container, plf, LIST_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *
     */
    @Override
    public void onSaveClicked() {
        runTopFragment();
    }
}
