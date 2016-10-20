package com.note.frorice.mixwords;


/**
 * Created by frorice on 2016/10/20.
 */

public class WordsBook {
    private String    id;
    private String title;
    private String author;
    private String recordDate;

    /**
     * 构造函数
     */
    public WordsBook(String id, String title, String author, String date){
        this.id = id;
        this.title = title;
        this.author = author;
        this.recordDate = date;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getRecordDate(){
        return recordDate;
    }
}
