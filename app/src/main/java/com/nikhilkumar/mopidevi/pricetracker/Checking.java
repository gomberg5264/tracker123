package com.nikhilkumar.mopidevi.pricetracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by NIKHIL on 08-Feb-15.
 */
public class Checking extends Service {

    public IBinder onBind(Intent intent) {

        return null;
    }

    private Handler mHandler;

    String[][] table;
    String[] url = new String[30];
    String[] prices = new String[30];
    String[] titles = new String[30];
    Element price;
    String price_text;

    MySQLiteHelper obj;

    int i;
    int len;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //notifyUser("Title","Price");

        obj  = new MySQLiteHelper(this);

        obj.read();

        table = obj.getTable();
        len= obj.getLength();

        for(i=0;i<len;i++) {

            url[i] = table[i][1];
            titles[i] = table[i][2];
            prices[i] = table[i][3];
            //Log.d(url[i],"url"+i);
            //Log.d(titles[i],"titles"+i);
            //Log.d(prices[i],"prices"+i);
        }

        try {

            i=0;

            while(i<len)
            {
                // Connect to the web site
                String ua = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";

                Document document = Jsoup.connect(url[i]).userAgent(ua).get();

                price = document.getElementById("priceblock_ourprice");

                if (price == null) {
                    price = document.getElementById("priceblock_saleprice");
                }

                if(price!=null)
                {
                  price_text = price.toString();
                }
                else {
                    Elements price_element = document.select("b:contains(Price:)");
                    Element el = price_element.first();
                    price_text = ((TextNode) el.nextSibling()).text();
                }

                if(Integer.parseInt(price_text) < Integer.parseInt(prices[i])) {
                    //update this price in database and send a notification
                    obj.update(i,price_text);

                    //code to send a local notification
                    notifyUser(titles[i],price_text);
                }

                i++;
            }

        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
     catch (Exception ex) {
        ex.printStackTrace();
    }

        stopSelf(startId);
        obj.close();
        return START_STICKY;
    }

    public void notifyUser(String title, String price){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
Notification n  = new Notification.Builder(this)
        .setContentTitle(title)
        .setContentText("Price dropped to "+price)
        .setSmallIcon(R.drawable.icon_notif)
        .setContentIntent(pIntent)
        .setAutoCancel(true).build();

NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

notificationManager.notify(0, n);
        }
        }

