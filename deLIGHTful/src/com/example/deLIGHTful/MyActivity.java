package com.example.deLIGHTful;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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

            double longitude = data.getDoubleExtra("longitude", 0.);
            double latitude = data.getDoubleExtra("latitude", 0.);
            Toast.makeText(this, "Longitude was " + longitude + " and latitude was " + latitude, Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(this, "No marker selected.", Toast.LENGTH_SHORT).show();

        }
    }
}
