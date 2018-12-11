package com.example.photos70.androidphotos70;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkForPermission();

        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(startIntent);
            }
        });
    }

    public void checkForPermission(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            System.out.println("Permission granted");

        }else{
            System.out.println("no permission");

            Toast.makeText(MainActivity.this,"permission needed to load images from storage",Toast.LENGTH_LONG).show();

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        }
    }
}
