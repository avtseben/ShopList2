package ru.alexandertsebenko.shoplist2.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;
import ru.alexandertsebenko.shoplist2.ui.activity.ShopListActivity;

/**
 * Created by avtseben on 06.11.2016.
 */
public class FirstRunFragment extends Fragment {

    public interface OnSaveClicked {
        void onSaveClicked();
    }
    public OnSaveClicked listenerSaveClick;

    private SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_run,container, false);

        final EditText numberView = (EditText) view.findViewById(R.id.editTextSetMyNumber);
        Button saveBtn = (Button) view.findViewById(R.id.buttonSavePrefs);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true){ //TODO добавить проверку на корректность введенного номера
                    mPrefs.edit().putString(ShopListActivity.MY_NUMBER_PREF, numberView.getText().toString()).commit();
                    mPrefs.edit().putBoolean(ShopListActivity.FIRST_RUN_PREF, false).commit();
                }
                listenerSaveClick.onSaveClicked();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listenerSaveClick == null) {
            this.listenerSaveClick= (OnSaveClicked) context;
        }
    }

    public void addSharedPrefsToFragment(SharedPreferences prefs) {
        mPrefs = prefs;
    }



}
