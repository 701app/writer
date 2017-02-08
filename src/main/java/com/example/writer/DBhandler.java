package com.example.writer;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBhandler {

    /* 创建数据库，自动识别是否创建 */
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.saokewriter.db/databases/saokewriter.db", null);

    //插入Book表数据
    public void insert(String book_name, int book_count,
                       int book_cover, String book_author) {

        String book_table = "create table if not exists Book(" +
                "book_id integer primary key autoincrement," +
                "book_name text," +
                "book_date integer," +
                "book_count text," +
                "book_cover blob," +
                "book_author text)";
        db.execSQL(book_table);

        Date date = new Date();
        long datetime = date.getTime();

        ContentValues values = new ContentValues();
        values.put("book_name", book_name);
        values.put("book_date", datetime);
        values.put("book_count", book_count);
        values.put("book_cover", img(book_cover));
        values.put("book_author", book_author);
        db.insert("Book", null, values);

        Book_infomation_insert(1,"作品设定","");
        Book_infomation_insert(2,"角色设定","");
        Book_infomation_insert(3,"故事大纲","");
        Book_infomation_insert(4,"分卷大纲","");
    }

    //删除Book表数据
    public void delete(int[] book_id) {
        //删除条件
        String whereClause = "book_id=?";
        //删除条件的删除值数组
        String[] where = new String[book_id.length];
        for (int i = 0; i < book_id.length; i++) {
            where[i] = String.valueOf(book_id[i]);
        }
        db.delete("Book", whereClause, where);
    }

    //修改Book表数据
    public void update(int book_id, String book_name, int book_count,
                       int book_cover, String book_author) {

        Date date = new Date();
        long datetime = date.getTime();

        ContentValues values = new ContentValues();
        values.put("book_name", book_name);
        values.put("book_date", datetime);
        values.put("book_count", book_count);
        values.put("book_cover", img(book_cover));
        values.put("book_author", book_author);

        //修改条件
        String whereClause = "id=?";
        //修改添加参数
        String[] where = {String.valueOf(book_id)};
        //修改
        db.update("Book", values, whereClause, where);
    }

    //遍历Book表数据
    public void query(TextView id,TextView name,TextView date,TextView count, ImageView cover,TextView author) {
        //查询获得游标
        Cursor cursor = db.query("Book", null, null, null, null, null, null);

        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);

                id.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("book_id"))));
                name.setText(cursor.getString(cursor.getColumnIndex("book_name")));
                count.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("book_count"))));
                author.setText(cursor.getString(cursor.getColumnIndex("book_author")));

                //从SQLite读取日期数据
                try {
                    String myDate =cursor.getString(cursor.getColumnIndex("book_date"));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date d = format.parse(myDate);
                    date.setText(String.valueOf(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //从SQLite读取图片数据
                ByteArrayInputStream book_cover =
                        new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndex("book_cover")));
                cover.setBackgroundDrawable(Drawable.createFromStream(book_cover,"img"));

            }
        }
    }

    //查询一条Book表数据
    public void queryone(String book_id,
                         TextView id,TextView name,TextView date,TextView count, ImageView cover,TextView author) {
        //查询获得游标
        Cursor cursor = db.query("Book", null, null, null, null, null, null);

        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);

                if (book_id.equals(String.valueOf(cursor.getInt(cursor.getColumnIndex("book_id"))))){

                    name.setText(cursor.getString(cursor.getColumnIndex("book_name")));
                    count.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("book_count"))));
                    author.setText(cursor.getString(cursor.getColumnIndex("book_author")));

                    //从SQLite读取日期数据
                    try {
                        String myDate =cursor.getString(cursor.getColumnIndex("book_date"));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date d = format.parse(myDate);
                        date.setText(String.valueOf(d));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //从SQLite读取图片数据
                    ByteArrayInputStream book_cover =
                            new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndex("book_cover")));
                    cover.setBackgroundDrawable(Drawable.createFromStream(book_cover,"img"));
                }

            }
        }
    }

    //插入Book_infomation表数据
    public void Book_infomation_insert(int infomation_order, String infomation_title, String infomation_content) {

        String infomation_table = "create table if not exists Book_infomation(" +
                "infomation_id integer primary key autoincrement," +
                "infomation_order integer," +
                "infomation_title text," +
                "infomation_content text)";
        db.execSQL(infomation_table);

        ContentValues values = new ContentValues();
        values.put("infomation_order", infomation_order);
        values.put("infomation_title", infomation_title);
        values.put("infomation_content", infomation_content);
        db.insert("Book_infomation", null, values);
    }

    //封面图片转bitmap
    private byte[] img(int id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //非Activity文件调用getResources()
    private Resources getResources() {
        Resources mResources = null;
        mResources = getResources();
        return mResources;
    }

}
