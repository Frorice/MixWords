package com.note.frorice.mixwords;

import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * Created by fanfa on 2016/10/21.
 */

public class Model {
    private List<words> wordsList;
    private List<WordsBook> wordsBooksList;
    private Data data;
    private GetPostUtil getPostUtil;

    public Model(CreateSQLiteDatabase databaseHelper){
        this.data = new Data(databaseHelper);
        this.getPostUtil = new GetPostUtil();
    }

    public List<words> getStarWords(){
        wordsList = data.getWords(null,1,2);

        return wordsList;
    }

    public List<words> getUndoneWords(){
        wordsList = data.getWords(null,2,0);

        return wordsList;
    }

    public List<words> getDoneWords(){
        wordsList = data.getWords(null,2,1);

        return wordsList;
    }

    public List<words> getWordsByBookName(String bookName){
        wordsList = data.getWords(bookName,2,2);
        return wordsList;
    }

    //传入当前的单词状态（isStar 0 非星标，1 星标）
    public void toggleStar(String wordName, String isStar){
        switch (isStar){
            case "0": isStar = "1";break;
            case "1": isStar = "0";break;
        }
        data.updateWord(wordName, isStar, null);
    }

    //传入想要设置的单词状态（isDone 0 未完成，1 已完成）
    public void toggleDone(String wordName, String isDone){
        data.updateWord(wordName, null, isDone);
    }

    public void addWord(String name, String bookName, String langType, String interpretation){
        data.insertWord(name, bookName, langType, interpretation);
    }

    public void deleteWord(String name){
        data.deleteWord(name);
    }

    public List<WordsBook> getWordsBooks(){
        wordsBooksList = data.getWordsBooks();
        return wordsBooksList;
    }

    public void updateWordsBook(String oldBookName, String newBookName){
        data.updateWordsBook(oldBookName, newBookName);
    }

    public void createWordsBook(String bookName, String creator){
        data.insertWordsBook(bookName, creator);
    }

    public void deleteWordsBook(String bookName){
        data.deleteWordsBook(bookName);
    }

    public void sendGet(String url, String params, Handler handler){
        new Thread(new RunHttpRequest("GET", url, params, handler));
    }

    public void sendPost(String url, String params, Handler handler){
        new Thread(new RunHttpRequest("POST", url, params, handler));
    }
}

class RunHttpRequest implements Runnable{
    private String url;
    private String params;
    private String requestType;
    private Handler handler;

    public RunHttpRequest(String requestType, String url, String params, Handler handler){
        super();
        this.requestType = requestType;
        this.url    = url;
        this.params = params;
        this.handler = handler;
    }

    @Override
    public void run() {
        Message m = new Message();
        m.what = 0x123;

        if(requestType.equals("GET")){
            m.obj = GetPostUtil.sendGet(url, params);
        }
        if(requestType.equals("POST")){
            m.obj = GetPostUtil.sendPost(url, params);
        }

        handler.sendMessage(m);
    }
}
