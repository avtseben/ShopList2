package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.ui.activity.ShopListActivity;
import ru.alexandertsebenko.shoplist2.ui.adapter.ChildProductViewHolder;
import ru.alexandertsebenko.shoplist2.ui.adapter.ParentItem;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.ui.adapter.ShopListAdapter;

public class ProductListFragment extends Fragment {



    public static final int LIST_PREPARE_STATE = 1;
    public static final int DO_SHOPPING_STATE = 2;
    public static final String SHOP_LIST_POJO = "slpojo";
    RecyclerView mRecyclerView;
    List<ParentItem> mParentItemList;
    ShopListAdapter mAdapter;
    ShopList mShopList;
    DataSource mDataSource;
    public static int mState;

    public static ProductListFragment newInstance(ShopList ShopListPOJO) {
        ProductListFragment plf = new ProductListFragment();
        Bundle args = new Bundle();
        args.putParcelable(SHOP_LIST_POJO, ShopListPOJO);
        plf.setArguments(args);
        return plf;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSource = new DataSource(getContext());
        mDataSource.open();
        mParentItemList = new ArrayList<>();
        //Есть ли в аргументах готовый списко продуктов ил составляем новый
        ShopList ShopListPojo = getArguments().getParcelable(SHOP_LIST_POJO);
        if(ShopListPojo != null){
            restoreListFromDb(ShopListPojo);
        } else {
        createNewShopList();//TODO создавать список только если добавили продукт. И удалять списко если в нём не осталось продуктов (все удалили)
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list,container, false);

        setUpRecyclerView(view);
        setUpItemTouchHelper(ShopListActivity.LIST_PREPARE_STATE);
        setupSendButton(view);
//        setupFab(view);
        return view;
    }
    private void createNewShopList(){
        long date = System.currentTimeMillis();
        String listName = getResources().getString(R.string.defaultListName);
        long id = mDataSource.addNewShopList(listName,date);
        mShopList = new ShopList(id,date,listName);
    }
    private void setUpRecyclerView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ShopListAdapter(getContext(),mParentItemList);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    private void restoreListFromDb(ShopList ShopListPojo) {
        mShopList = ShopListPojo;
        List<ProductInstance> pil = mDataSource.getProductInstancesByShopListId(mShopList.getId());
        for(ProductInstance pinst : pil) {
            smartAdd(pinst);
        }
    }
    //Формируем списко для адаптера
    //TODO:сохранять список при изменении ориентации
    private void addProductToList(Product product) {
        smartAdd(createProductInstance(product));
    }
    private void smartAdd(ProductInstance pinst){
        String category = pinst.getProduct().getCategory();
        String imageName = pinst.getProduct().getImage();
        boolean productAdded = false;
        for(ParentItem pi : mParentItemList){
            //Если продукты такой категории уже есть в списке
            if(pi.getName().equals(category)) {
                pi.addChild(pinst);
                productAdded = true;
                break;
            }
        }
        //Если в предыдущем цыкле не нашлось в списке катаегории куда "положить"
        //продукт то создаём эту категорию и кладём в неё продукт
        if(!productAdded) {
            mParentItemList.add(new ParentItem(category,imageName,pinst));
        }
    }
    private ProductInstance createProductInstance(Product product){
        int quantity = 1;
        String measure = "штука";
        String uuid = UUID.randomUUID().toString();
        int state = ProductInstance.IN_LIST;
        long id = mDataSource.addProductInstance(
                mShopList.getId(),
                product.getId(),
                quantity,measure,state,uuid);


        return new ProductInstance(id,product,quantity,measure,state);//TODO: хардкод заглушка
        //экземпляр покупки 1 штука
    }
    public void addProduct(Product product) {
        addProductToList(product);
        mAdapter = new ShopListAdapter(getContext(),mParentItemList);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void setUpItemTouchHelper(final int state){

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0/*drag drop не нужен*/, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
                /*задаём направление свайпа которое слушаем*/) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                                   RecyclerView.ViewHolder viewHolder,
                                                   RecyclerView.ViewHolder target) {
                        //Нам не нужен Drag&Prop
                        return false;
                    }
                    @Override
                    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        if (!viewHolder.getClass().equals(ChildProductViewHolder.class)) {
                            return 0;//Если это не ChildItem тоесть не сам продукт
                            //то никаких свайпов. Свайп разрешен только для продуктов
                        }
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }
                    @Override
                    public void onSwiped (RecyclerView.ViewHolder viewHolder, int direction) {
                        if(viewHolder.getClass().equals(ChildProductViewHolder.class)) {
                            int position = viewHolder.getAdapterPosition();
                            ProductInstance pi = ((ProductInstance)mAdapter.getListItem(position));
                            if(direction == ItemTouchHelper.RIGHT &&
                                    state == ShopListActivity.DO_SHOPPING_STATE) {
                                mAdapter.deleteProductInstance(position);
                                //TODO сдесь нужно класть в адаптер корзины
                                pi.setState(ProductInstance.IN_BASKET);
                                mDataSource.updateProductInstanceState(pi.getId(),
                                        ProductInstance.IN_BASKET);
                            } else if(direction == ItemTouchHelper.LEFT) {
                                mAdapter.deleteProductInstance(position);
                                pi.setState(ProductInstance.DELETED);//TODO возможно state DELETED не нужен - просто удалять
                                mDataSource.deleteProductInstanceById(pi.getId());
                            }
                        }
                    }
                };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupSendButton(View view){
        Button btn = (Button) view.findViewById(R.id.send_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onSendButtonClicked(mShopList);
                }
            }
        });
    }
    private OnSendButtonClickListener listener;
    public interface OnSendButtonClickListener {
        void onSendButtonClicked(ShopList shopListPojo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (OnSendButtonClickListener) context;
    }
}

