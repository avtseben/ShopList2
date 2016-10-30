package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.People;
import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.Pinstance;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.net.Client;
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
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                People me = new People("ShopList User","89133166336");
                PeoplePleaseBuy ppb = new PeoplePleaseBuy(
                        me.getNumber(),
                        getNumbersOfSelectedPeoples(mPeoples),
                        getProdList(mShopList));
                System.out.println(ppbToString(ppb));
                //Test code
                Client c = new Client();
                c.postPpb(ppb);

//                sendSms(ppbToString(ppb),ppb.getToWho());//TODO uncomment in future
            }
        });
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
    private List<People> getSelectedPeoples(List<People> peopleList){
        ArrayList selectedPeoples = new ArrayList<People>();
        for(People p : peopleList) {
            if(p.isSelected()) {
                selectedPeoples.add(p);
            }
        }
        return selectedPeoples;
    }
    private List<String> getNumbersOfSelectedPeoples(List<People> peopleList){
        ArrayList numbers = new ArrayList<String>();
        for(People p : peopleList) {
            if(p.isSelected()) {
                numbers.add(p.getNumber());
            }
        }
        return numbers;
    }

    /**
     * Возвращает все покупки в списке
     * TODO нужно добавлять в Объект ShopList список ProductInstanes
     * а не доставать его из БД
     * @param shopList
     * @return
     */
/*    private List<ProductInstance> getProdList(ShopList shopList){
        DataSource dataSource = new DataSource(getContext());
        dataSource.open();
        return dataSource.getProductInstancesByShopListId(shopList.getId());
    }*/
    private List<Pinstance> getProdList(ShopList shopList){
        DataSource dataSource = new DataSource(getContext());
        dataSource.open();
        return dataSource.getPinstancesByShopListId(shopList.getId());
    }


    /**
     * Возвращает тескт для SMS сообщения
     * @param ppb
     * @return
     */
    private String ppbToString(PeoplePleaseBuy ppb){
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.sms_head) + "\n");
        sb.append("\n");
        for (Pinstance pi : ppb.getPil()){
            sb.append(pi.getProduct() + ", " + pi.getQuantity() + " " + pi.getMeasure() + "\n");
        }
        return sb.toString();
    }
    private void sendSms(String msg, List<String> numberList){
        SmsManager smsManager = SmsManager.getDefault();
        for (String n : numberList) {
            try {
                smsManager.sendTextMessage(n, null, msg, null, null);
            }
            catch (Exception e) {
                Toast.makeText(getContext(),"SMS send failed",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void setupListView() {
    }
}
