package com.note.frorice.mixwords;

/**
 * Created by fanfa on 2016/10/20.
 */

public class words {
    private String name;
    private String isStar;
    private String isDone;
    private String interpretation;

    /**
     * 构造函数
     */
    public words(String name, String isStar, String isDone, String interpretation){
        this.name   = name;
        this.isStar = isStar;
        this.isDone = isDone;
        this.interpretation = interpretation;
    }

    public String getName(){
        return name;
    }

    public String getIsStar(){
        return isStar;
    }

    public String getIsDone(){
        return isDone;
    }

    public String getInterpretation(){
        return interpretation;
    }

}
