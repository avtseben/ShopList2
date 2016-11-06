package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.net.Client;
import ru.alexandertsebenko.shoplist2.ui.activity.ShopListActivity;
import ru.alexandertsebenko.shoplist2.ui.adapter.TopListAdapter;
import ru.alexandertsebenko.shoplist2.utils.MyApplication;

public class TopFragment extends Fragment {

    public interface OnShopListItemClickListener {
        void onItemClicked(ShopList shopListObj);
    }
    public interface OnNewListButtonClickListener {
        void onNewListClicked();
    }
    public OnShopListItemClickListener listenerShopListSelected;
    public OnNewListButtonClickListener listenerNewList;

    private DataSource mDataSource;
    private List<ShopList> mTopList;
    private RecyclerView mRecyclerView;
    private Button mGetButton;
    private TopListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(container.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_top,container, false);

        mDataSource = new DataSource(getContext());
        mDataSource.open();
        setUpRecyclerView(view);
        setUpItemTouchHelper();

        final SharedPreferences pref = this
                .getActivity()
                .getSharedPreferences("ru.alexandertsebenko.shoplist2", Context.MODE_PRIVATE);
        ImageView btn = (ImageView) view.findViewById(R.id.btn_newList);
        mGetButton = (Button) view.findViewById(R.id.get_list_button);
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client c = new Client();
                c.getPpb("9139209220");
                System.out.println(pref.getString(ShopListActivity.MY_NUMBER_PREF,null));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerNewList.onNewListClicked();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listenerShopListSelected == null) {
            this.listenerShopListSelected = (OnShopListItemClickListener) context;
        }
        if(listenerNewList == null) {
            this.listenerNewList = (OnNewListButtonClickListener) context;
        }
    }

    private void setUpRecyclerView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_top);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,false));
        mTopList = mDataSource.getAllLists();
        mAdapter = new TopListAdapter(mTopList);
        mAdapter.setOnItemClickListener(new TopListAdapter.OnClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                listenerShopListSelected.onItemClicked(mTopList.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
    public void setUpItemTouchHelper(){

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0/*drag drop не нужен*/, ItemTouchHelper.UP
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
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
            @Override
            public void onSwiped (RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItemFromPosition(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
    private void deleteItemFromPosition(int position){
        mDataSource.deleteShopListById(mTopList.get(position).getId());//TODO удалять не только сам список но и экземпляры покупок привязанные к этому списку
        mTopList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

}
