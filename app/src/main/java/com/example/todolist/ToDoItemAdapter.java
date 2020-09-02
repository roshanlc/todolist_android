package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.database.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ViewHolder> {
    private List<ToDoItem> toDoItems;

    public ToDoItemAdapter(List<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ToDoItem toDoItem = toDoItems.get(position);
        String val = toDoItem.getToDoValue();
        holder.itemValue.setText(val);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return toDoItems.size();
    }


    public  static  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemValue;
        public ViewHolder(View v)
        {
            super(v);
            itemValue = v.findViewById(R.id.itemValue);
        }
    }
}
