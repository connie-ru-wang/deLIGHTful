package com.example.deLIGHTful;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;

public class SetupActivity extends Activity implements OnMapReadyCallback {
    private boolean marker;
    private Double longtitude;
    private Double latitude;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);
        Button clear = (Button) findViewById(R.id.clear);
        Button submit = (Button) findViewById(R.id.submit);
        clear.setEnabled(false);
        submit.setEnabled(false);
        marker = false;

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        Button clear = (Button) findViewById(R.id.clear);
        Button submit = (Button) findViewById(R.id.submit);
        clear.setEnabled(true);
        submit.setEnabled(true);
        map.setMyLocationEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if(marker == false) {
                    mMap.addMarker(new MarkerOptions().position(point).draggable(true));
                    marker= true;
                    latitude = point.latitude;
                    longtitude = point.longitude;
                    System.out.println(latitude);
                    System.out.println(longtitude);
                }

            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //TODO: Flash corresponding light for marker
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                longtitude = marker.getPosition().longitude;
                latitude = marker.getPosition().latitude;

            }
        });
    }

    public void submit(View view){
        if (latitude != null && longtitude != null) {
            Intent intent = new Intent();
            intent.putExtra("longitude", longtitude);
            intent.putExtra("latitude", latitude);
            System.out.println(longtitude);
            System.out.println(latitude);
            setResult(0, intent);
            finish();
        }
        else{
            //no marker on map
            setResult(1);
        }
    }

    public void clear(View view){
        mMap.clear();
        marker = false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        setResult(1);
        finish();
    }
}