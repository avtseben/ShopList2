package ru.alexandertsebenko.shoplist2.ui.activity;

/*
* Use thi for test
* */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ru.alexandertsebenko.shoplist2.R;

public class WordActivity extends Activity {
    private TextView mWord;
    private TextView mDefinition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        mWord = (TextView) findViewById(R.id.word);
        mDefinition = (TextView) findViewById(R.id.definition);
        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String definition = intent.getStringExtra("definition");
        mWord.setText(word);
        mDefinition.setText(definition);
    }
}