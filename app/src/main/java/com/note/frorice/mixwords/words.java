package com.note.frorice.mixwords;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.note.frorice.mixwords.wordsBookActivity.wbActivity;

/**
 * Created by fanfa on 2016/10/20.
 */

public class words {
    private String name;//单词名
    private int isStar;//星标单词
    private int isDone;//是否完成
    private String langType;//语言类型
    private String bookName;//单词本名称
    private String interpretation;//单词解释

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
        String explainStr = interpretation.replaceAll("sup","\'");
        try{
            JSONObject explainObj = new JSONObject(explainStr);
            explainStr = explainObj.getString("basic");
        }catch (JSONException exp){
            Toast.makeText(wbActivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

        return explainStr;
    }

}
