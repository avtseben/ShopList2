package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.utils.DateBuilder;

public class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.ListViewHolder> {

    public interface OnClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnClickListener listener;
    private List<ShopList> mResults;

    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public TopListAdapter(List<ShopList> shopLists) {
        mResults = shopLists;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_list, parent, false);
        ListViewHolder lvHolder = new ListViewHolder(view);
        return lvHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        int MEDIUM_BAG = 5;
        int BIG_BAG = 10;
        int listSize = mResults.get(position).getProdList().size();

        if(1 <= listSize && listSize < MEDIUM_BAG) {
            holder.image.setImageResource(R.drawable.bag_1);
        } else if(MEDIUM_BAG <= listSize && listSize < BIG_BAG ) {
            holder.image.setImageResource(R.drawable.bag_2);
        } else if(BIG_BAG <= listSize) {
            holder.image.setImageResource(R.drawable.bag_3);
        }
        holder.name.setText(mResults.get(position).getName());
        holder.date.setText(DateBuilder.timeTitleBuilder(mResults
                .get(position)
                .getDateMilis()));
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public ShopList getListItem(int position) {
        return mResults.get(position);
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView date;

        public ListViewHolder(View itemView) {
            super(itemView);
            image = ((ImageView) itemView.findViewById(R.id.image_in_shopList_title));
            name = ((TextView) itemView.findViewById(R.id.tv_list_name));
            date = ((TextView) itemView.findViewById(R.id.tv_list_date));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(view,position);
                        }
                    }
                }
            });
        }
    }
}

