package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.db.DataSource;

public class SearchAutoCompleteAdapter extends BaseAdapter implements Filterable{


    private final Context mContext;
    private List<Product> mResults;
    private DataSource mDataSource;

    public SearchAutoCompleteAdapter(Context context) {

        mDataSource = new DataSource(context);
        mDataSource.open();
        mContext = context;
        mResults = new ArrayList<Product>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.search_dropdown_item,null);//Инфэйтим
        }
        Product product = (Product) getItem(position);
        ((TextView) convertView.findViewById(R.id.text1)).setText(product.getName());
        ((TextView) convertView.findViewById(R.id.text2)).setText(product.getCategory());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Product> products = findProduct(constraint.toString());
                    filterResults.values = products;
                    filterResults.count = products.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<Product>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
    //TODO:пеменять LIKE запрос так чтобы патерны были в начале слова
    private List<Product> findProduct(String query) {
        return mDataSource.getProductByNameMatches(query);
    }
}

