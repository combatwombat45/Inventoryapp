package com.example.inventory;

import android.content.Context;
import android.graphics.Color;
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
        TextView listItemID = (TextView)view.findViewById(R.id.list_item_id);
        TextView listItemName = (TextView)view.findViewById(R.id.list_item_name);
        TextView listItemCount = (TextView)view.findViewById(R.id.list_item_count);
        TextView listItemUnit = (TextView)view.findViewById(R.id.list_item_unit);

        String unit = list.get(position).getUnit();
        listItemName.setText(list.get(position).getName());
        listItemID.setText(list.get(position).getID());
        listItemCount.setText(Integer.toString(list.get(position).getCount()));
        listItemUnit.setText(unit);

        Integer listItemMinCount = list.get(position).getMinimumCount();
        System.out.println("BLAH");
        System.out.println(listItemMinCount);
        System.out.println(list.get(position).getCount());
        if (listItemMinCount > list.get(position).getCount()) {
            System.out.println("This item is nearly all out. Go get some more");
            listItemCount.setTextColor(Color.RED);
            listItemName.setTextColor(Color.RED);
        }
        return view;
    }
}