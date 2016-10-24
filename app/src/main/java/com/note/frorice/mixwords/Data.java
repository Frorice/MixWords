package com.note.frorice.mixwords;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.note.frorice.mixwords.IndexActivity.idxtivity;
import static com.note.frorice.mixwords.wordsBookActivity.wbActivity;

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
            wordsList = new ArrayList<>();
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


            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                isStar = cursor.getInt(1);
                isDone = cursor.getInt(2);
                String langType = cursor.getString(3);
                bookName = cursor.getString(5);
                String interpretation = cursor.getString(4);

                wordsList.add(new words(name, isStar, isDone, bookName, langType, interpretation));
            }
            cursor.close();
            database.close();
            return wordsList;
        }
        catch (Exception exp){
            Toast.makeText(wbActivity, exp.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }finally {
            return wordsList;
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
            Toast.makeText(wbActivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(wbActivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(wbActivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public List<WordsBook> getWordsBooks(){
        try {
            wordsBooks = new ArrayList<>();
            database = databaseHelper.getWritableDatabase();
            String sql = "select * from books ";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String bookName = cursor.getString(0);
                String creator = cursor.getString(1);
                String createDate = cursor.getString(2);

                wordsBooks.add(new WordsBook(bookName, creator, createDate));
            }
            cursor.close();
            database.close();
        }
        catch (Exception exp){
            Toast.makeText(idxtivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {
            return wordsBooks;
        }

    }

    public void insertWordsBook(String bookName, String creator){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(new Date());
            database = databaseHelper.getWritableDatabase();
            String sql = "insert into books(bookName, creator, createDate) values('"
                + bookName + "','" + creator + "','"+ date +  "')";
            database.execSQL(sql);
            database.close();
        }
        catch (Exception exp){
            Toast.makeText(idxtivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteWordsBook(String bookName){
        try{
            database = databaseHelper.getWritableDatabase();
            String sql1 = "delete from books where bookName = '"+ bookName + "'";
            String sql2 = "delete from words where bookName = '"+ bookName + "'";
            database.execSQL(sql1);
            database.execSQL(sql2);
            database.close();
        }
        catch (Exception exp){
            Toast.makeText(idxtivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
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
            Toast.makeText(idxtivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}

class CreateSQLiteDatabase extends SQLiteOpenHelper {

    public CreateSQLiteDatabase
        (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
    }


    public void onCreate(SQLiteDatabase db) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        String sql1 = "CREATE TABLE IF NOT EXISTS books("
            + "bookName TEXT(64) CONSTRAINT bp PRIMARY KEY ASC ON CONFLICT FAIL CONSTRAINT bn NOT NULL CONSTRAINT bu UNIQUE DEFAULT '单词本',"
            + "creator TEXT(32) NOT NULL DEFAULT 'frorice',"
            + "createDate TEXT(16) NOT NULL DEFAULT '"+ date + "')";

        String sql2 = "CREATE TABLE IF NOT EXISTS words("
            + "name TEXT(64) CONSTRAINT m PRIMARY KEY ASC ON CONFLICT FAIL CONSTRAINT n NOT NULL CONSTRAINT u UNIQUE,"
            + "isStar BOOLEAN(2) NOT NULL DEFAULT 0,"
            + "isDone Boolean(2) NOT NULL DEFAULT 0,"
            + "langType TEXT(20) NOT NULL,"
            + "interpretation TEXT(512) NOT NULL,"
            + "bookName TEXT(64) NOT NULL,"
            + "foreign key(bookName) references books(bookName))"
            ;

        db.execSQL(sql1);
        db.execSQL(sql2);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

class GetPostUtil {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String params) {
        String result = "";
        BufferedReader in = null;
        try
        {
            String urlName = url + "?" + params;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet())
            {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += "\n" + line;
            }
        }
        catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += "\n" + line;
            }
        }
        catch (Exception e)
        {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
