package com.nikhilkumar.mopidevi.pricetracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.Toast;

public class ListTab extends Fragment {

    public static List<Products> productsList = new ArrayList<Products>();

    public static ListView list;

    public static ListViewAdapter listViewAdapter;

    public static View view;

    public static Context context ;

    public static MySQLiteHelper obj;

    public static String[][] table;

    public static Bitmap[] bitmap;

    public static int[] id = new int[30];
    public static String[] title = new String[30];
    public static String[] url = new String[30];
    public static String[] init_price = new String[30];
    public static String[] cur_price = new String[30];

    public static Drawable d[];

    public static String img_path[];

    public static int len,i;


    private OnFragmentInteractionListener mListener;

    public ListTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_list_items, container, false);
        getData();
        return view;
    }

    public static void getData()
    {

        productsList.clear();

        obj = new MySQLiteHelper(context);
        obj.open();
        obj.read();

        table = obj.getTable();

        len = obj.getLength();

        bitmap = new Bitmap[len];

        img_path = new String[len];

        for(i=0;i<len;i++) {
            id[i] = Integer.parseInt(table[i][0]);
            url[i] = table[i][1];
            title[i] = table[i][2];
            init_price[i] = table[i][3];
            cur_price[i] = table[i][4];
            img_path[i] = table[i][5];
        }

        for(int i=0;i<len;i++){
            Products products = new Products(id[i],title[i],url[i],init_price[i],cur_price[i],img_path[i]);
            productsList.add(products);
        }

        // Locate the ListView in listview_layout.xml
        list = ( ListView ) view.findViewById(R.id.listview);

        // Pass results to ListViewAdapter Class
        listViewAdapter = new ListViewAdapter(context, R.layout.listview_layout,
                productsList);


        // Binds the Adapter to the ListView
        list.setAdapter(listViewAdapter);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = list.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                listViewAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = listViewAdapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Products selectedItem = listViewAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                listViewAdapter.remove(selectedItem);

                              //delete the selected item from database

                               if(obj.delete(selectedItem.getId())==1)
                               {
                                //Deleted Successfully
                                   Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                               } else {
                                //Cannot delete, try again
                                   Toast.makeText(context, "Cannot delete, try again", Toast.LENGTH_LONG).show();
                               }

                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                listViewAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }


    public static ListViewAdapter getAdapter() {
        return listViewAdapter;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
