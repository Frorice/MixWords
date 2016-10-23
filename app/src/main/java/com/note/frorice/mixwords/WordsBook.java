package com.note.frorice.mixwords;


/**
 * Created by frorice on 2016/10/20.
 */

public class WordsBook {
    private String bookName;
    private String creator;
    private String createDate;

    /**
     * 构造函数
     */
    public WordsBook(String bookName, String creator, String createDate){
        this.bookName = bookName;
        this.creator = creator;
        this.createDate = createDate;
    }



    public String getBookName(){
        return bookName;
    }

    public String getCreator(){
        return creator;
    }

    public String getCreateDate(){
        return createDate;
    }
}
