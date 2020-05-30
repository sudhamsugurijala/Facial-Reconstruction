package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    Bitmap out_bmp;
    byte[] result_byte_array;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        ImageView result = findViewById(R.id.imageView2);
        Button home = findViewById(R.id.button5);

        Bundle extras = getIntent().getExtras();
        try {
            result_byte_array = extras.getByteArray("result");
            out_bmp = BitmapFactory.decodeByteArray(result_byte_array, 0, result_byte_array.length);
        } catch(NullPointerException e) {
            e.getMessage();
        }

        result.setImageBitmap(out_bmp);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(retHome);
            }
        });
    }
}
