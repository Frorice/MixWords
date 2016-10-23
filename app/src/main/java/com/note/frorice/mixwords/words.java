package com.note.frorice.mixwords;

/**
 * Created by fanfa on 2016/10/20.
 */

public class words {
    private String name;
    private int isStar;
    private int isDone;
    private String langType;
    private String bookName;
    private String interpretation;

    /**
     * 构造函数
     */
    public words(String name, int isStar, int isDone, String langType, String bookName ,String interpretation){
        this.name   = name;
        this.isStar = isStar;
        this.isDone = isDone;
        this.langType = langType;
        this.bookName = bookName;
        this.interpretation = interpretation;
    }

    public String getName(){
        return name;
    }

    public int getIsStar(){
        return isStar;
    }

    public int getIsDone(){
        return isDone;
    }

    public String getInterpretation(){
        return interpretation;
    }

}
