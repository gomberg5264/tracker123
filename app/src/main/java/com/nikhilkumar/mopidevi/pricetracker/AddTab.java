package com.nikhilkumar.mopidevi.pricetracker;

import android.app.Activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AddTab extends Fragment implements View.OnClickListener {

    BroadcastReceiver  receiver;

    View view;
    public static String receivedUrl;
    Button mButton;
    EditText textBox;

    String url;

    private OnFragmentInteractionListener mListener;

    public AddTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);

        mButton = (Button) view.findViewById(R.id.button);
        textBox = (EditText) view.findViewById(R.id.editText);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                    receivedUrl = intent.getStringExtra("key");

                }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("receiveURL"));

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        url = textBox.getText().toString();

                        Intent i = new Intent(getActivity(), FirstParsing.class);
                        i.putExtra("url", url);
                        getActivity().startService(i);

                       // commented code is useful for checking if a service is running or not
                       /* if(isMyServiceRunning(Checking.class))
                        {
                            Log.d("Checking.class is running","");
                        }else {
                            Log.d("Checking.class is not running","You got lot more work to do!!!");
                        }
                        */
                    }
                });

        return view;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onResume()
    {
        super.onResume();
        //Log.v("onResume()"+receivedUrl,"");
        textBox.setText(receivedUrl);
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


    @Override
    public void onClick(View v) {

    }

    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
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
