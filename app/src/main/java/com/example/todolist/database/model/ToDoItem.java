package com.example.todolist.database.model;

public class ToDoItem {

    public static final String TABLE_NAME = "todo_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String toDoValue;
    private String timeStamp;

    public ToDoItem() {
        //default constructor
    }

    public ToDoItem(int id, String toDoValue, String timeStamp) {
        this.id = id;
        this.toDoValue = toDoValue;
        this.timeStamp = timeStamp;
    }

    //sql query to create a table
    public final static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" ( "
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TODO +" TEXT NOT NULL, "
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + " );";

    //sql query for deleting table while upgrading version
    public final static String DELETE_TABLE=" DROP TABLE IF EXISTS " + TABLE_NAME ;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToDoValue() {
        return toDoValue;
    }

    public void setToDoValue(String toDoValue) {
        this.toDoValue = toDoValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
