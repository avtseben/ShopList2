package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    RecyclerView mRecyclerView;
    List<ParentItem> mParentItemList;
    ShopListAdapter mAdapter;
    ShopList mShopList;
    DataSource mDataSource;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(container.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_product_list,container, false);

        mDataSource = new DataSource(getContext());
        mDataSource.open();
        setUpRecyclerView(view);
        setUpItemTouchHelper(ShopListActivity.LIST_PREPARE_STATE);

        if(true){//TODO если в Интенте нет имеющегося списка
            createNewShopList();
        }
        mShopList = new ShopList(-1,1,"new List");
        return view;
    }
    private void createNewShopList(){
        long date = System.currentTimeMillis();
        String listName = "newList";
        long id = mDataSource.addNewShopList(listName,date);
        mShopList = new ShopList(id,date,listName);
    }
    private void setUpRecyclerView(View view){
        mParentItemList = new ArrayList<ParentItem>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_product_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ShopListAdapter(getContext(),mParentItemList);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    //Формируем списко для адаптера
    //TODO:сохранять список при изменении ориентации
    private List<ParentItem> addProductToList(Product product) {
        boolean productAdded = false;
        for(ParentItem pi : mParentItemList){
            //Если продукты такой категории уже есть в списке
            if(pi.getName().equals(product.getCategory())) {
                pi.addProductInsToList(createProductInstance(product));
                productAdded = true;
                break;
            }
        }
        //Если в предыдущем цыкле не нашлось в списке катаегории куда "положить"
        //продукт то создаём эту категорию и кладём в неё продукт
        if(!productAdded) {
            mParentItemList.add(new ParentItem(product.getCategory(),product.getImage(),
                    createProductInstance(product)));
        }
        return mParentItemList;
    }
    private ProductInstance createProductInstance(Product product){
        int quantity = 1;
        String measure = "штука";
        int state = ProductInstance.IN_LIST;
        long id = mDataSource.addProductInstance(
                mShopList.getId(),
                product.getId(),
                quantity,measure,state);

        return new ProductInstance(id,product,quantity,measure,state);//TODO: хардкод заглушка
        //экземпляр покупки 1 штука
    }
    public void addProduct(Product product) {
        mAdapter = new ShopListAdapter(getContext(),addProductToList(product));
        mRecyclerView.setAdapter(mAdapter);
    }
    public void saveList(){
        Toast.makeText(getContext(),"saveListCalled",Toast.LENGTH_SHORT).show();

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
/*    private class SaveListTask extends AsyncTask<>{

        List<ProductInstance> productInstances;
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        List, Void, Boolean} {

    }*/
}

