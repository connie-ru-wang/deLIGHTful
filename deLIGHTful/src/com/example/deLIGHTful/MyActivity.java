package com.example.deLIGHTful;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyActivity extends Activity {
    TrafficMoniterService mBoundService;

    boolean mIsBound;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent(this, TrafficMoniterService.class);
        if(TrafficMoniterService.startState == false) {
            startService(intent);
        }
        Button b1 = (Button) findViewById(R.id.setup);
        b1.setEnabled(false);
        doBindService();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    public void startSetup(View view){
        Intent intent = new Intent(this, SetupActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==0)
        {

            mBoundService.longitude = data.getDoubleExtra("longitude", 0.);
            mBoundService.latitude = data.getDoubleExtra("latitude", 0.);
            Toast.makeText(this, "Longitude was " + mBoundService.longitude + " and latitude was " + mBoundService.latitude, Toast.LENGTH_SHORT).show();
            mBoundService.trafficData();
        }
        else{

            Toast.makeText(this, "No marker selected.", Toast.LENGTH_SHORT).show();

        }
    }



    //Methods to bind/unbind service
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((TrafficMoniterService.LocalBinder)service).getService();
            Button b1 = (Button) findViewById(R.id.setup);
            b1.setEnabled(true);


        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(MyActivity.this, TrafficMoniterService.class), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }




}
