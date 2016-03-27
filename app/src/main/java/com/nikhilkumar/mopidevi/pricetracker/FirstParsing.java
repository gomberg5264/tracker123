package com.nikhilkumar.mopidevi.pricetracker;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by NIKHIL on 08-Feb-15.
 */
public class FirstParsing extends Service {

    public IBinder onBind(Intent intent) {

        return null;
    }

    private Handler mHandler;

    String url;
    Element title;
    Element price;
    Elements price_element;
    String price_text;
    Element img;
    String imgSrc;

    Bitmap bitmap;


    String img_path;

    MySQLiteHelper obj  = new MySQLiteHelper(this);

    int i;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler = new Handler();

        url = intent.getStringExtra("url");

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    try {

                        String ua = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";

                        // Connect to the web site
                        Document document = Jsoup.connect(url).userAgent(ua).get();

                        title = document.getElementById("productTitle");

                        if(title == null ) {
                            title = document.getElementById("product-title");
                        }

                        price = document.getElementById("priceblock_ourprice");

                        if (price == null) {
                            price = document.getElementById("priceblock_saleprice");
                            price_text = price.text();
                        }

                        if(price!=null)
                        {
                           price_text = price.text();
                        }
                        else{
                            price_element = document.select("b:contains(Price:)");
                            //Log.d("price <b> ", price_element.toString());
                            Element el = price_element.first();
                            price_text = ((TextNode) el.nextSibling()).text();
                        }


                        img = document.getElementById("landingImage");

                        if(img!=null) {
                            imgSrc = img.attr("data-old-hires");
                        }
                        else {
                            img = document.getElementById("detailImg");
                            imgSrc = img.attr("src");
                        }

                        // Download image from URL
                        InputStream input = new java.net.URL(imgSrc).openStream();
                        // Decode Bitmap
                        bitmap = BitmapFactory.decodeStream(input);

                        img_path = obj.saveToInternalStorage(bitmap, title.text());

                        obj.open();

                        if(obj.add(url,title.text(),price_text,price_text, img_path)==0) {

                            //make toast that already exists
                            mHandler.post(new ToastRunnable("Already Exists"));

                        }
                        else {
                            mHandler.post(new ToastRunnable("Product Added"));

                            ListTab.getData();
                            ListTab.getAdapter().notifyDataSetChanged();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        mHandler.post(new ToastRunnable("Enter URL!"));
                    }
                    catch (SocketException e) {
                        e.printStackTrace();
                        mHandler.post(new ToastRunnable("Oops!! Net Problem!!"));
                    }
                    catch (UnknownHostException e) {
                        e.printStackTrace();
                        mHandler.post(new ToastRunnable("Unknown Host"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        mHandler.post(new ToastRunnable("IO Exception"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        stopSelf(startId);

        obj.close();

        return START_STICKY;
    }


    private class ToastRunnable implements Runnable {
        String mText;

        public ToastRunnable(String text) {
            mText = text;
        }

        @Override
        public void run(){
            Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
        }
    }

}

