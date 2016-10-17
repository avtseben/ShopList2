package ru.alexandertsebenko.shoplist2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.People;

public class ContactsAdapter extends BaseAdapter {

    public interface OnContactClickListener {
        void onContactClicked(People peopleObj);
    }
    private OnContactClickListener listener;

    List<People> mPeoples;
    Context mContext;

    public ContactsAdapter(List<People> data, Context context){
        this.mPeoples = data;
        this.mContext = context;
    }
    public void setListener(OnContactClickListener listener){
        this.listener = listener;
    }
    @Override
    public int getCount() {
        return mPeoples.size();
    }

    @Override
    public Object getItem(int i) {
        return mPeoples.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_contacts,viewGroup,false);//Инфэйтим
        }
        final People people = (People) getItem(position);
        ((TextView) convertView.findViewById(R.id.contact_name)).setText(people.getFullName());
        ((TextView) convertView.findViewById(R.id.contact_number)).setText(people.getNumber());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onContactClicked(people);
            }
        });
        return convertView;
    }
}
