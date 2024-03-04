package com.pedro.streamer.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pedro.streamer.MainActivity;
import com.pedro.streamer.R;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    Button btnStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnStream= (Button) findViewById(R.id.btnStream);

        btnStream.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnStream -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }

    }
}