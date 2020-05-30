package com.example.miniproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;

public class MorphActivity extends AppCompatActivity {

    Bitmap input_bmp;
    byte[] input_byte_array;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.morph);
        ImageView img = findViewById(R.id.imageView);
        EditText url = findViewById(R.id.editText2);
        Button send = findViewById(R.id.button4);

        Bundle extras = getIntent().getExtras();
        input_byte_array = extras.getByteArray("image");
        input_bmp = BitmapFactory.decodeByteArray(input_byte_array,0, input_byte_array.length);

        img.setImageBitmap(input_bmp);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send picture to server
                
                //send result to resultActivity
                
            }
        });
    }
}
