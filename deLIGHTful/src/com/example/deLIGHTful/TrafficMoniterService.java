package com.example.deLIGHTful;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by snehas on 6/27/2015.
 */
public class TrafficMoniterService extends Service {
    public static boolean startState;
    Double latitude;
    Double longitude;
    Double off = 0.1449275362;

    //Methods to bind to activities
    public class LocalBinder extends Binder {
        TrafficMoniterService getService() {
            return TrafficMoniterService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startState = true;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        startState = false;
    }

    public void trafficData() {
        Integer traffic =null;
        if (latitude != null && longitude != null) {
            String box = String.format("&boundingBox=%f,%f,%f,%f",
                    latitude+off, longitude-off, latitude-off,longitude+off);
            String sURL = "http://www.mapquestapi.com/traffic/v2/incidents?"
                    +"key=IzBkIF3FkmE4xtAgCPma5oTFZbR2FAcV"
                    +box
                    +"&filters=incidents,congestion"
                    +"&inFormat=kvp"
                    +"&outFormat=json";
            // Define the server endpoint to send the HTTP request to
            try {
                URL url = new URL(sURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Indicate that we want to write to the HTTP request bod

                // Reading from the HTTP response body
                String jresponse = convertStreamToString(urlConnection.getInputStream());
                traffic = 0;
                System.out.println(jresponse);
                JSONObject obj = new JSONObject(jresponse);
                if (obj== null) {
                    System.out.println("json obj is null");
                    return;
                }

                JSONArray incidents = obj.getJSONArray("incidents");
                if (incidents.length() == 0) {
                    displayLights(0);
                }
                for (int i = 0; i < incidents.length(); i++) {
                    if (incidents.getJSONObject(i) == null) {
                        System.out.println("json object incident is null");
                        return;
                    }
                    int type = incidents.getJSONObject(i).getInt("type");
                    int severity = incidents.getJSONObject(i).getInt("severity");
                    if (type == 3) {
                        // congestion
                        traffic += severity;
                    } else if (type == 4) {
                        //incident or accident
                        if (severity > 2 && traffic < 5) {
                            traffic += severity;
                        }
                    }
                }
            }
            catch(MalformedURLException exe){
                System.out.println("Malformed URL Exception " + exe);
                return;
            }
            catch(IOException exe){
                System.out.println("IO Exception " + exe);
                return;
            }
            catch(JSONException exe){
                System.out.println("JSON Exception " + exe );
                return;

            }
            displayLights(traffic);
        }
    }


    public void displayLights(int traffic) {
        System.out.println("Traffic level was " + traffic);
        Toast.makeText(this, "Traffic level is " + traffic, Toast.LENGTH_LONG).show();
        return;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }



}
