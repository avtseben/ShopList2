package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.People;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.ui.adapter.ContactsAdapter;
import ru.alexandertsebenko.shoplist2.utils.DateBuilder;
import ru.alexandertsebenko.shoplist2.utils.MyApplication;

/**
 * Фрагмент со списком контактов
 */
public class SendFragment extends Fragment {

    private List<People> mPeoples;

    private ListView mPeoplesListView;
    private ContactsAdapter mAdapter;
    private Button mSendButton;
    private TextView mShopListName;
    private TextView mShopListDate;
    private ShopList mShopList;

    public SendFragment() {}

    public static SendFragment newInstance(ShopList ShopListPOJO) {
        SendFragment sf = new SendFragment();
        Bundle args = new Bundle();
        args.putParcelable(ProductListFragment.SHOP_LIST_POJO, ShopListPOJO);
        sf.setArguments(args);
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send,container, false);
        mSendButton = (Button) view.findViewById(R.id.send_button2);
        mShopListName = (TextView) view.findViewById(R.id.tv_list_name2);
        mShopListDate = (TextView) view.findViewById(R.id.tv_list_date2);
        mPeoplesListView = (ListView) view.findViewById(R.id.contact_list);
        mPeoples = readContacts();
        mAdapter = new ContactsAdapter(mPeoples,view.getContext());
        mAdapter.setListener(new ContactsAdapter.OnContactClickListener() {
            @Override
            public void onContactClicked(People peopleObj) {
                if(!peopleObj.isSelected()) {
                    peopleObj.setSelected(true);
                    mSendButton.setVisibility(View.VISIBLE);
                } else {
                    peopleObj.setSelected(false);
                }

                mAdapter.notifyDataSetChanged();
            }
        });
        mPeoplesListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        ShopList ShopListPojo = getArguments().getParcelable(ProductListFragment.SHOP_LIST_POJO);
        if(ShopListPojo != null){
            mShopList = ShopListPojo;
            mShopListName.setText(mShopList.getName());
            mShopListDate.setText(DateBuilder.timeTitleBuilder(mShopList.getDateMilis()));
        }

    }


    public List<People> readContacts(){
        List<People> peoples = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name );

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("Phone" + phone);
                        peoples.add(new People(name,phone));
                    }
                    pCur.close();
                }
            }
        }
        return peoples;
    }
    private void setupListView() {
    }
}
