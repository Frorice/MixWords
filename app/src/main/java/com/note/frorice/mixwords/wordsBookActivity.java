package com.note.frorice.mixwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class wordsBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWords);
        setSupportActionBar(toolbar);
        //根据主activity传递的activity名称取数据来设置当前activity
        Bundle bundle = this.getIntent().getExtras();
        String intentData = bundle.getString("targetIntent");
        switch(intentData){
            case "starWords" :
                this.setTitle(getResources().getString(R.string.title_activity_star_words));
                break;
            case "undoneWords" :
                this.setTitle(getResources().getString(R.string.title_activity_undone));
                break;
            case "doneWords" :
                this.setTitle(getResources().getString(R.string.title_activity_done));
                break;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
}
