package com.example.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.database.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper  extends SQLiteOpenHelper {

    //db version current-one
    private static final int DB_VERSION = 1;

    //db name
    private static final String DB_NAME = "todo_note";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ToDoItem.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(ToDoItem.DELETE_TABLE);
        //no upgrades to perform for now
      onCreate(db);
    }


    public List<ToDoItem> getAllItems()
    {
        List<ToDoItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + ToDoItem.TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do{
                    ToDoItem toDoItem = new ToDoItem();
                    toDoItem.setId(cursor.getInt(cursor.getColumnIndex(ToDoItem.COLUMN_ID)));
                    toDoItem.setToDoValue(cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_TODO)));
                    toDoItem.setTimeStamp(cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_TIMESTAMP)));
                    items.add(toDoItem);

            }while(cursor.moveToNext());
        }

        db.close(); //closing db connection

        return  items;

    }

    public int getItemsCount()
    {
        String countQuery = " SELECT COUNT( " + ToDoItem.COLUMN_ID +" ) FROM " + ToDoItem.TABLE_NAME + " ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        int count = cursor.getInt(cursor.getColumnIndex("COUNT(" + ToDoItem.COLUMN_ID +")"));

        cursor.close();

        return  count;
    }

    public ToDoItem getItem(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String specific_Item[] = { ToDoItem.COLUMN_ID,ToDoItem.COLUMN_TODO,ToDoItem.COLUMN_TIMESTAMP};
        String selection = ToDoItem.COLUMN_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                ToDoItem.TABLE_NAME,
                specific_Item,
                selection,
                selectionArgs,null,null,null
        );

        if(cursor!=null) cursor.moveToFirst();
        else
            return null;

        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId(cursor.getInt(cursor.getColumnIndex(ToDoItem.COLUMN_ID)));
        toDoItem.setToDoValue(cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_TODO)));
        toDoItem.setTimeStamp(cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_TIMESTAMP)));

        db.close();

        return toDoItem;

    }

    public long insertItemWithId(ToDoItem toDoItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ToDoItem.COLUMN_TODO, toDoItem.getToDoValue());
        value.put(ToDoItem.COLUMN_ID, toDoItem.getId());
        value.put(ToDoItem.COLUMN_TIMESTAMP, toDoItem.getTimeStamp());

        long Id = db.insert(ToDoItem.TABLE_NAME,null,value);
        db.close();
        return  Id ;

    }

    public long insertItem(String itemValue )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ToDoItem.COLUMN_TODO, itemValue);

        long Id = db.insert(ToDoItem.TABLE_NAME,null,value);
        db.close();
        return  Id ;
    }

    public int updateItem(ToDoItem toDoItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoItem.COLUMN_TODO,toDoItem.getToDoValue());
        int  id= db.update(ToDoItem.TABLE_NAME,values,ToDoItem.COLUMN_ID + " = ? ", new String[]{String.valueOf(toDoItem.getId())});

        return id;


    }



    public int deleteItem(ToDoItem toDoItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int Id = db.delete(ToDoItem.TABLE_NAME,ToDoItem.COLUMN_ID +" = ? ",
                new String []{String.valueOf(toDoItem.getId())});
        db.close();
        return Id;
    }
}

