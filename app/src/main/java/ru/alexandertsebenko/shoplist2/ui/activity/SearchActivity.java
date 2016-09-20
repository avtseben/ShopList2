package ru.alexandertsebenko.shoplist2.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.db.DbHelper;

public class SearchActivity extends AppCompatActivity{

    private ListView mList;
    private DataSource mDataSource;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mList = (ListView) findViewById(R.id.list);
        mDataSource = new DataSource(this);
        mDataSource.open();

        Intent intent = getIntent();
        if(intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }
    private void showResults(String query) {
        mCursor = mDataSource.getMatches(query);
        try {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    mCursor,
                    new String[]{DbHelper.COLUMN_NAME},
                    new int[]{android.R.id.text1},
                    0);
            mList.setAdapter(adapter);
        } catch (IllegalStateException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_record);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!= searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);//Устанавливает иконку в раскрывающемся SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showResults(newText);
                return true;
            }
        });
        return true;
    }
    //И без этого работает
/*    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_record:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Эксперименты с памятью - никакой разницы, закрывать или не закрывать курсор и БД
        mDataSource.close();
        if(mCursor != null) {
            mCursor.close();
        }
    }
}
