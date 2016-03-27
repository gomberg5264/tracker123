package com.nikhilkumar.mopidevi.pricetracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by NIKHIL on 27-Mar-15.
 */

public class ListViewAdapter extends ArrayAdapter<Products> {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Products> productsList;
    private SparseBooleanArray mSelectedItemsIds;

    public ListViewAdapter(Context context, int resourceId,
                           List<Products> productsList) {
        super(context, resourceId, productsList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.productsList = productsList;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView title;
        TextView init_price;
        TextView cur_price;
        ImageView img;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.tit);
            holder.init_price = (TextView) view.findViewById(R.id.init);
            holder.cur_price = (TextView) view.findViewById(R.id.cur);
            // Locate the ImageView in listview_item.xml
            holder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.title.setText(productsList.get(position).getTitle());
        holder.init_price.setText("Initial Price    : "+productsList.get(position).getInit_price());
        holder.cur_price.setText( "Current Price : "+productsList.get(position).getCur_price());

        // Capture position and set to the ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(productsList.get(position).getImg_path());
        holder.img.setImageBitmap(bitmap);
        return view;
    }

    @Override
    public void remove(Products object) {
        productsList.remove(object);
        notifyDataSetChanged();
    }

    public List<Products> getProductsList() {

        return productsList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {

        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;
    }
}
