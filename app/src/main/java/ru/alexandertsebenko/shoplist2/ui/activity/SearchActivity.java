package ru.alexandertsebenko.shoplist2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.Dictionary;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.db.DbHelper;

public class SearchActivity extends Activity{

    private TextView mTextView;
    private Button mButton;
    private EditText mEditView;
    private ListView mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mTextView = (TextView) findViewById(R.id.textField);
        mEditView = (EditText) findViewById(R.id.edit_query);
        mList = (ListView) findViewById(R.id.list);
        mButton = (Button) findViewById(R.id.btn_start_search);
        Dictionary.getInstance().ensureLoaded(getResources());



    }

    public void onClick(View view) {

        String query = mEditView.getText().toString();
        mTextView.setText(getString(R.string.search_results, query));
        DataSource dataSource = new DataSource(this);
        dataSource.open();
        Cursor cursor = dataSource.getMatches(query);
        cursor.moveToFirst();
        try {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{DbHelper.COLUMN_NAME},
                    new int[]{android.R.id.text1},
                    0);
            mList.setAdapter(adapter);
        } catch (IllegalStateException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }


    }
    private void launchWord(Dictionary.Word theWord) {
        Intent next = new Intent();
        next.setClass(this, WordActivity.class);
        next.putExtra("word", theWord.word);
        next.putExtra("definition", theWord.definition);
        startActivity(next);
    }
    class WordAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        private final List<Dictionary.Word> mWords;
        private final LayoutInflater mInflater;
        public WordAdapter(List<Dictionary.Word> words) {
            mWords = words;
            mInflater = (LayoutInflater) SearchActivity.this.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        public int getCount() {
            return mWords.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView :
                    createView(parent);
            bindView(view, mWords.get(position));
            return view;
        }
        private TwoLineListItem createView(ViewGroup parent) {
            TwoLineListItem item = (TwoLineListItem) mInflater.inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            item.getText2().setSingleLine();
            item.getText2().setEllipsize(TextUtils.TruncateAt.END);
            return item;
        }
        private void bindView(TwoLineListItem view, Dictionary.Word word) {
            view.getText1().setText(word.word);
            view.getText2().setText(word.definition);
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            launchWord(mWords.get(position));
        }
    }
}
