package com.forbitbd.fin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.forbitbd.myfin.SimpleToaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleToaster.SimpleToast(this,"Hello");

//        Intent intent = new Intent(getApplicationContext(),Fin)
    }
}