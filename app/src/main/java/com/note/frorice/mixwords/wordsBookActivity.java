package com.note.frorice.mixwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class wordsBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<words> wordsList;
    private WordsAdapter wordsAdapter;

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
                initStarWordsActivity(bundle);
                break;
            case "undoneWords" :
                initUnDoneWordsActivity(bundle);
                break;
            case "doneWords" :
                initDoneWordsActivity(bundle);
                break;
            case "wordsBook" :
                initBookWordsActivity(bundle);
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

    private void initWordsData(){
        wordsList = new ArrayList<>();
        //添加单词
        wordsList.add(new words("dog","true","true","狗"));
        wordsList.add(new words("pig","true","false","猪"));
        wordsList.add(new words("cat","false","true","猫"));
        wordsList.add(new words("tiger","false","false","虎"));
    }

    private void initStarWordsActivity(Bundle bundle){
        this.setTitle(getResources().getString(R.string.title_activity_star_words));
    }

    private void initUnDoneWordsActivity(Bundle bundle){
        this.setTitle(getResources().getString(R.string.title_activity_undone));
    }

    private void initDoneWordsActivity(Bundle bundle){
        this.setTitle(getResources().getString(R.string.title_activity_done));
    }

    private void initBookWordsActivity(Bundle bundle){
        this.setTitle(bundle.getString("title"));
        //实现单词列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView     = (RecyclerView) findViewById(R.id.wordsRecyclerView);

        //获取单词本中的单词
        initWordsData();

        wordsAdapter = new WordsAdapter(wordsList, wordsBookActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(wordsAdapter);
    }
}
