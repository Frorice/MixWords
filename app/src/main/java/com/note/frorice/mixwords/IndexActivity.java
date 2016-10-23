package com.note.frorice.mixwords;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private List<WordsBook> wordsBookList;
    private WordsBookAdapter wordsBookAdapter;
    private Model model;
    private String databaseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarIndex);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(IndexActivity.this);
                builder.setTitle("新建单词本");
                builder.setMessage("单词本名称：");
                final EditText editText= new EditText(IndexActivity.this);
                builder.setView(editText,30,20,30,20);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.createWordsBook(editText.getText().toString(),"frorice");
                    }
                });
                builder.show();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initDataBase();
        initWordsBooksData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(IndexActivity.this,wordsBookActivity.class);
        if (id == R.id.nav_star) {
            // 跳转到星标单词,传递数据{targetIntent:starWords}
            Bundle bundle = new Bundle();
            bundle.putString("targetIntent","starWords");
            intent.putExtras(bundle);

            startActivity(intent);
        } else if (id == R.id.nav_clear) {
            //跳转到未完成单词,传递数据{targetIntent:undoneWords}
            Bundle bundle = new Bundle();
            bundle.putString("targetIntent","undoneWords");
            intent.putExtras(bundle);

            startActivity(intent);
        } else if (id == R.id.nav_done) {
            //跳转到已完成单词,传递数据{targetIntent:doneWords}
            Bundle bundle = new Bundle();
            bundle.putString("targetIntent","doneWords");
            intent.putExtras(bundle);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initWordsBooksData() {
        //实现单词本列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView     = (RecyclerView) findViewById(R.id.indexRecyclerView);

        //获取单词本数据
        wordsBookList = new ArrayList<>();
        //添加新闻
        wordsBookList.add(new WordsBook("小王子单词","frorice","2016-10-20"));
        wordsBookList.add(new WordsBook("日语","frorice","2016-10-20"));
        wordsBookList.add(new WordsBook("德语","frorice","2016-10-20"));
        wordsBookList.add(new WordsBook("我的单词本","frorice","2016-10-20"));

        wordsBookAdapter = new WordsBookAdapter(wordsBookList,IndexActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(wordsBookAdapter);

    }

    private void initDataBase(){
        String databaseName = "mixwords";
        CreateSQLiteDatabase databaseHelper =
                new CreateSQLiteDatabase(this, databaseName, null, 1001);

        model = new Model(databaseHelper);
    }
}
