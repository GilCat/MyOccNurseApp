package com.myoccnurse.app;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public String Phone;

    {
        Phone = "17372168773";
    }

    public String Email;

    {
        Email = "myoccnurse@gmail.com";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPermissionGranted()){
                    String[] send_To = {Email};
                    composeEmail(send_To,"Please book a call for the next hour");
                }

                Snackbar.make(view, "You are about to send an email the nurse", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
            }

        });

        FloatingActionButton fabCall = findViewById(R.id.fabCall);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    call_action();
                    Snackbar.make(view, "You are calling the nurse...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else
                    {
                     Snackbar.make(view, "Something is wrong! Please call (737)-261-8773", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

            }
        });

        FloatingActionButton fabVideo = findViewById(R.id.fabVideo);
         fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(isPermissionGranted()){
                        video_action();
                    }else
                    {
                        Snackbar.make(view, "Something is wrong! Please call (737)-261-8773", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    //startActivity(intent);
                }catch (Exception e)
                {
                    Snackbar.make(view, "Error in Video Module"+ e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });

        FloatingActionButton fabTxtSms = findViewById(R.id.fabSendSms);
        fabTxtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You are about to send a message to MyOccNurse ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                composeSMS("You are about to send a message to MyOccNurse");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_settings:
                Toast.makeText(this,"MyOccNurse V 1.0",Toast.LENGTH_LONG).show();
                return  true;
            case R.id.action_video:
                video_action();
                //Toast.makeText(this,"This will trigger Video Call",Toast.LENGTH_LONG).show();
            case R.id.action_call:
                Toast.makeText(this,"You are about to make a call to MyOccNurse",Toast.LENGTH_LONG).show();
                if(isPermissionGranted()){
                    call_action();
                }
                return  true;
            case R.id.action_send_sms:
                Toast.makeText(this,"This will send a message",Toast.LENGTH_LONG).show();
                composeSMS("Can you please help me! I need to talk to a nurse.");
                return  true;
            case R.id.action_sendto:
                Toast.makeText(this,"You are about to email MyOccNurse",Toast.LENGTH_LONG).show();
                if(isPermissionGranted()){
                    String[] send_To = {Email};
                    composeEmail(send_To,"Please book a call for the next hour");
                }

                return  true;
            case R.id.action_about:
                Toast.makeText(this,"MyOccNurseApp Version 1.0 powered by GILCATs",Toast.LENGTH_LONG).show();
                return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void video_action(){
        Intent videoIntent = new Intent(this, com.twilio.video.quickstart.activity.VideoActivity.class);
        startActivity(videoIntent);
    }


    public void call_action(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Phone));
        startActivity(callIntent);
    }


    public  boolean isPermissionGranted() {

            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Requesting permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.MANAGE_OWN_CALLS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_CALL_LOG}, 1);
                return false;
            }

    }


    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);


        try {
            startActivity(Intent.createChooser(intent, "send mail"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No mail app found!!!", Toast.LENGTH_SHORT);
        } catch (Exception ex) {
            Toast.makeText(this, "Unexpected Error!!!", Toast.LENGTH_SHORT);
        }


    }


    public void composeSMS(String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:"+ Phone)); // only text apps should handle this
        intent.putExtra("sms_body" , subject);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No sms app found!!!", Toast.LENGTH_SHORT);
        } catch (Exception ex) {
            Toast.makeText(this, "Unexpected Error!!!", Toast.LENGTH_SHORT);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            }


            // other 'case' lines to check for other
            // permissions this app might request
    }
}