package com.example.todolist;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;

import com.example.todolist.database.DatabaseHelper;
import com.example.todolist.database.model.ToDoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private List<ToDoItem> toDoItems = new ArrayList<>();
    private ToDoItemAdapter toDoItemAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private EditText toDo;
    private  boolean deletedForReal=true;

    ToDoItem deletedItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        databaseHelper = new DatabaseHelper(this);
        Window window = MainActivity.this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));

        initialData();

        FloatingActionButton addItem = findViewById(R.id.addToDoItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewToDoItem();
            }
        });

        recyclerView = findViewById(R.id.recyclerList);

        toDoItemAdapter = new ToDoItemAdapter(toDoItems);


        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayout);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(toDoItemAdapter);

    }


    public void initialData() {

         toDoItems = databaseHelper.getAllItems();
    }



    public void addNewToDoItem() {
        //to add a new to do item to List


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_box, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)
                .setCancelable(true)
                .setIcon(R.drawable.ic_add)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                    toDo = (EditText) dialogView.findViewById(R.id.toDoValue);
                                     String item = toDo.getText().toString();
                                    if(item !=null || item !="")
                                    {
                                        long success = databaseHelper.insertItem(item);

                                        if(success >0)
                                        {
                                            ToDoItem temp = databaseHelper.getItem((int) success);
                                            toDoItems.add(temp);
                                            int tempPostion  = toDoItems.size() -1;
                                            toDoItemAdapter.notifyItemInserted(tempPostion);
                                           toDoItemAdapter.notifyDataSetChanged();

                                        }
                                    }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });


        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
//                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
//                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();


            switch (direction) {
                case ItemTouchHelper.LEFT:
                    //delete action is done here

                    final ToDoItem removeItem = toDoItems.get(position);
                    toDoItemAdapter.notifyItemRemoved(position);
                    toDoItems.remove(position);
                    setDeletedTrue();

                    if(deletedForReal)
                    {
                        databaseHelper.deleteItem(removeItem);
                    }

                    Snackbar.make(recyclerView, "Todo Item Deleted!", BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setDeletedFalse();
                                    toDoItems.add(position,removeItem);
                                    toDoItemAdapter.notifyItemInserted(position);
                                    Log.d("info", removeItem.getId() +" , " + removeItem.getToDoValue() +" , " + removeItem.getTimeStamp() );
                               long id =  databaseHelper.insertItemWithId(removeItem);
                                    Log.d("info", " status of re-insertion = " +id);


                                }
                            })
                            .show();


                     break;

            }

        }
    };

private void  setDeletedFalse()
{
    deletedForReal = false;
}

private void  setDeletedTrue()
    {
        deletedForReal = true;
    }


}

