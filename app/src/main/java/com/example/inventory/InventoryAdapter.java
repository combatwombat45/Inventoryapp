package com.example.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InventoryAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<InventoryObject> list = new ArrayList<InventoryObject>();
    private Context context;



    public InventoryAdapter(ArrayList<InventoryObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row, null);
        }

        //Handle TextView and display string from your list
        TextView listItemName = (TextView)view.findViewById(R.id.list_item_name);
        TextView listItemCount = (TextView)view.findViewById(R.id.list_item_count);
        String unit = list.get(position).getUnit();
        listItemName.setText(list.get(position).getName());
        listItemCount.setText(Integer.toString(list.get(position).getCount()) + " " + unit);

        //Handle buttons and add onClickListeners
//        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
//        Button addBtn = (Button)view.findViewById(R.id.add_btn);

//        deleteBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });
//        addBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                notifyDataSetChanged();
//            }
//        });

        return view;
    }
}