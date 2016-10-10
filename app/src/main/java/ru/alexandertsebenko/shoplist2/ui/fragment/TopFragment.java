package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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
import ru.alexandertsebenko.shoplist2.ui.adapter.TopListAdapter;

/**
 * Created by avtseben on 06.10.2016.
 */
public class TopFragment extends Fragment {

    private DataSource mDataSource;
    private List<ShopList> mTopList;
    private RecyclerView mRecyclerView;
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

        return view;
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
                ShopList sl = mTopList.get(position);
                Toast.makeText(itemView.getContext(), "Click on " + sl.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);


/*        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);*/
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
        mDataSource.deleteShopListById(mTopList.get(position).getId());
        mTopList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

}
