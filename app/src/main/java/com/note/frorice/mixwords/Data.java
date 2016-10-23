package com.note.frorice.mixwords;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanfa on 2016/10/21.
 */

public class Data {
    private List<words> wordsList;
    private List<WordsBook> wordsBooks;
    private SQLiteDatabase database;
    private CreateSQLiteDatabase databaseHelper;

    public Data(CreateSQLiteDatabase databaseHelper){
        this.databaseHelper = databaseHelper;

    }

    public List<words> getWords(String bookName, int isDone, int isStar){

        try {
            database = databaseHelper.getWritableDatabase();
            //构造查询语句
            String sql = "select * from words where ";
            if (bookName != null) {
                sql += "bookName = '" + bookName + "'and";
            }
            switch (isDone) {
                case 1:
                case 0:
                    sql += "isDone = " + isDone + "and";
                    break;
            }
            switch (isStar) {
                case 1:
                case 0:
                    sql += "isStar = " + isStar + "and";
                    break;
            }
            sql = sql.substring(0, sql.length() - 3);

            Cursor cursor = database.rawQuery(sql, null);
            wordsList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                isStar = cursor.getInt(1);
                isDone = cursor.getInt(2);
                bookName = cursor.getString(4);
                String langType = cursor.getString(3);
                String interpretation = cursor.getString(5);

                wordsList.add(new words(name, isStar, isDone, bookName, langType, interpretation));
            }
            cursor.close();
            database.close();
            return wordsList;
        }
        catch (Exception exp){
            throw exp;
        }

    }

    public void insertWord(String name, String bookName, String langType, String interpretation){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "insert into words(name, isStar, isDone, langType, bookName, interpretation) values('"
                + name + "', 0, 0,'" + langType + "','" +  bookName + "','" + interpretation + "')";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public void deleteWord(String name){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "delete from words where name = '"+ name + "'";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public void updateWord(String name, String isStar, String isDone){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "update words set ";

            if (isStar != null) {
                sql += "isStar = " + isStar + ",";
            }

            if (isDone != null) {
                sql += "isDone = " + isDone;
            }else{
                sql = sql.substring(0, sql.length() - 1);
            }

            sql +=  " where name = '"+ name + "'";

            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public List<WordsBook> getWordsBooks(){
        try {
            database = databaseHelper.getWritableDatabase();
            String sql = "select * from books ";
            Cursor cursor = database.rawQuery(sql, null);
            wordsBooks = new ArrayList<>();
            while (cursor.moveToNext()) {
                String bookName = cursor.getString(0);
                String creator = cursor.getString(1);
                String createDate = cursor.getString(2);

                wordsBooks.add(new WordsBook(bookName, creator, createDate));
            }
            cursor.close();
            database.close();
            return wordsBooks;
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public void insertWordsBook(String bookName, String creator){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "insert into books(bookName, creator, createDate) values('"
                + bookName + "','" + creator + "',getDate()" +  ")";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public void deleteWordsBook(String bookName){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "delete from books where bookName = '"+ bookName + "'";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }

    public void updateWordsBook(String oldBookName, String newBookName){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql = "update book set bookName = '" + newBookName + "' where bookName = '" + oldBookName + "'";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            throw exp;
        }
    }
}

class CreateSQLiteDatabase extends SQLiteOpenHelper {

    public CreateSQLiteDatabase
        (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
    }


    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE IF NOT EXISTS books("
            + "bookName TEXT(64) CONSTRAINT bp PRIMARY KEY ASC ON CONFLICT FAIL CONSTRAINT bn NOT NULL CONSTRAINT bu UNIQUE,"
            + "creator TEXT(32) NOT NULL DEFAULT 'frorice',"
            + "createDate date NOT NULL DEFAULT getDate()";

        String sql2 = "CREATE TABLE IF NOT EXISTS words("
            + "name TEXT(64) CONSTRAINT m PRIMARY KEY ASC ON CONFLICT FAIL CONSTRAINT n NOT NULL CONSTRAINT u UNIQUE,"
            + "isStar BOOLEAN(2) NOT NULL DEFAULT 0,"
            + "isDone Boolean(2) NOT NULL DEFAULT 0,"
            + "langType TEXT(20) NOT NULL,"
            + "bookName String(20) foreign key references books(bookName) NOT NULL UNIQUE CONSTRAINT b DEFAULT 单词本,"
            + "interpretation TEXT(512) NOT NULL)";

        db.execSQL(sql1);
        db.execSQL(sql2);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

