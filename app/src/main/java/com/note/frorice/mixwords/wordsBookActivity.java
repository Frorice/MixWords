package com.note.frorice.mixwords;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class wordsBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<words> wordsList;
    private WordsAdapter wordsAdapter;
    private Model model;
    private String translationString = null;
    private String bookName;
    private String queryWord;
    public static wordsBookActivity wbActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wbActivity = this;
        setContentView(R.layout.activity_words_book);
        //初始化ui组件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWords);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        setFabOnclick(fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDataBase();
        initActivity();
    }

    private void setFabOnclick(FloatingActionButton fab){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View   dialog = inflater.inflate(R.layout.add_word_dialog,(ViewGroup) findViewById(R.id.dialog));

                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(wordsBookActivity.this);
                builder.setTitle("添加单词");
                builder.setView(dialog);

                final EditText editText = (EditText) dialog.findViewById(R.id.et);

                ImageButton searchButton = (ImageButton) dialog.findViewById(R.id.searchButton);
                final TextView wordMeanView = (TextView) dialog.findViewById(R.id.mean);
                final TextView queryWordView = (TextView) dialog.findViewById(R.id.queryWord);
                final TextView pronunUKView = (TextView) dialog.findViewById(R.id.pronun_UK);
                final TextView pronunUSAView = (TextView) dialog.findViewById(R.id.pronun_USA);

                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        if(msg.what == 0x123){
                            try {
                                translationString = msg.obj.toString();
                                JSONObject translationObj = new JSONObject(translationString);
                                JSONObject transBasicObj = new JSONObject(translationObj.getString("basic"));
                                if(translationObj.has("basic")){
                                    queryWordView.setText(translationObj.getString("query"));
                                    if(transBasicObj.has("uk-phonetic")){
                                        String pronunUk = transBasicObj.getString("uk-phonetic");
                                        if(pronunUk.length()>=15){
                                            pronunUKView.setText("英 【"+pronunUk+"】");
                                        }else{
                                            pronunUKView.setText("英 【"+pronunUk+"】\n");
                                        }

                                    }
                                    if(transBasicObj.has("us-phonetic")){
                                        String pronunUs = transBasicObj.getString("us-phonetic");
                                        pronunUSAView.setText("美 【"+pronunUs+"】");
                                    }
                                    String explainStr = transBasicObj.getString("explains");
                                    String[] explains = explainStr.substring(1,explainStr.length()-1).split(",");
                                    explainStr = "";
                                    for(int i=0;i<explains.length;i++){
                                        explainStr += explains[i]+"\n";
                                    }
                                    wordMeanView.setText(explainStr);
                                }else{
                                    wordMeanView.setText("无法对该单词进行有效的翻译!");
                                }

                            }catch (JSONException exp){
                                exp.printStackTrace();
                            }

                        }
                    }
                };

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryWord = editText.getText().toString();
                        if(queryWord.length()!=0) {
                            model.sendGet("http://fanyi.youdao.com/openapi.do", "keyfrom=frorice-note&key=274502307&type=data&doctype=json&version=1.1&q=" + queryWord, handler);
                        }else{
                            Toast.makeText(wordsBookActivity.this, "请输入单词！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定",  new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(translationString!=null){
                            //将单词翻译保存到数据库
                            model.addWord(queryWord,bookName,"english",translationString);
                            //刷新单词列表
                            initActivity();
                        }
                        //保存完置空，防止下次查询空数据影响
                        translationString = null;
                    }
                });
                builder.show();
            }
        });
    }
    private void initDataBase(){
        String databaseName = "mixwords";
        CreateSQLiteDatabase databaseHelper =
                new CreateSQLiteDatabase(this, databaseName, null, 1001);

        model = new Model(databaseHelper);
    }

    private void initActivity(){
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
        bookName = bundle.getString("title");
        this.setTitle(bookName);

        //实现单词列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView     = (RecyclerView) findViewById(R.id.wordsRecyclerView);


        //获取单词本中的单词
        wordsList = model.getWordsByBookName(bookName);

        wordsAdapter = new WordsAdapter(wordsList, wordsBookActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(wordsAdapter);
    }

    private static Map handleTranslation(String translation) throws JSONException{
        JSONObject jsonObject = new JSONObject(translation);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;
    }
}
