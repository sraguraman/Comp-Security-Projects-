package com.example.sidharthraguraman.securitypermissions;


import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/*

 Normal Permissions:
 <uses-permission android:name="android.permission.SET_WALLPAPER" />
 <uses-permission android:name="android.permission.SET_WALLPAPER_HINTS"/>
 <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
 <uses-permission android:name="android.permission.INTERNET" />

 Dangerous Permissions:
 <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
 <uses-permission android:name="android.permission.READ_CONTACTS"/>

 Special Permissions:
 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

 */

public class MainActivity extends AppCompatActivity {
    Button main_button;
    ImageView i_view;
    WallpaperManager wm;
    Bitmap bm1;
    Bitmap bm2;
    DisplayMetrics dm;
    int width;
    int height;
    BitmapDrawable bd;
    Button music_button;
    TextView t_view;
    AudioManager audioM;
    Button location_button;
    TextView t_view2;
    Button contacts_button;
    TextView t_view3;
    int DRAW_OVER_OTHER_APPS = 3294;
    View overlay_image;
    WindowManager wm2;

    String[] namesToAdd = {"Muhammad", "Hamza"};
    String[] phoneNumbers = {"4089652234", "2357862231"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 101);

        }

        main_button = findViewById(R.id.main_button);

        i_view = findViewById(R.id.i_view);

        wm = WallpaperManager.getInstance(getApplicationContext());

        bd = (BitmapDrawable) i_view.getDrawable();

        bm1 = bd.getBitmap();

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetScreenWidthHeight();

                SetBitmapSize();

                wm = WallpaperManager.getInstance(MainActivity.this);

                try {

                    wm.setBitmap(bm2);

                    wm.suggestDesiredDimensions(width, height);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        audioM = (AudioManager) MainActivity.this.getSystemService(AUDIO_SERVICE);
        music_button = findViewById(R.id.music_button);
        t_view = findViewById(R.id.t_view);
        t_view.setTextSize(16);
        t_view.setTextColor(Color.RED);
        t_view.setTypeface(Typeface.DEFAULT_BOLD);

        music_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioM.setStreamVolume(AudioManager.STREAM_MUSIC, audioM.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                int currentVolume = audioM.getStreamVolume(AudioManager.STREAM_MUSIC);
                t_view.setText("Max Volume: " + currentVolume);

            }
        });

        location_button = findViewById(R.id.location_button);
        t_view2 = findViewById(R.id.t_view2);
        t_view2.setTextSize(16);
        t_view2.setTextColor(Color.RED);
        t_view2.setTypeface(Typeface.DEFAULT_BOLD);

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                t_view2.setText("Longitude:" + longitude + "\n" + "Latitude: " + latitude);
            }
        });

        contacts_button = findViewById(R.id.contacts_button);
        t_view3 = findViewById(R.id.t_view3);
        t_view3.setTextSize(16);
        t_view3.setTextColor(Color.RED);
        t_view3.setTypeface(Typeface.DEFAULT_BOLD);

        contacts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                }

                for (int i = 0; i < namesToAdd.length; i++) {
                    writeContact(namesToAdd[i], phoneNumbers[i]);
                }

                t_view3.setText("Contacts added");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APPS);
        } else {
            startView();
        }
    }

    public void GetScreenWidthHeight() {

        dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;

        height = dm.heightPixels;

    }

    public void SetBitmapSize() {

        bm2 = Bitmap.createScaledBitmap(bm1, width, height, false);

    }

    public void writeContact(String displayName, String number) {
        ArrayList contentProviderOperations = new ArrayList();
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName).build());
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APPS) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                startView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void startView() {
        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.x = 100;
        params.y = 100;

        overlay_image = LayoutInflater.from(MainActivity.this).inflate(R.layout.single_image,null);
        wm2 = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm2.addView(overlay_image, params);
    }
}

