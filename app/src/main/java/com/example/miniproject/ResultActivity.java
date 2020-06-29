package com.example.miniproject;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {

    ImageView result=null;
    Button home=null;
    Button saveImg=null;
    Bitmap out_bmp=null;
    File sample=null;
    FileOutputStream f_out=null;
    byte[] result_byte_array=null;

    private File createImageFile() throws IOException { // storing image
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmm").format(Calendar.getInstance().getTime());
        String imageFileName = "sample_" + timeStamp + ".png";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);
        return image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        result = findViewById(R.id.imageView2);
        home = findViewById(R.id.button5);
        saveImg = findViewById(R.id.button8);

        Bundle extras = getIntent().getExtras();
        try {
            result_byte_array = extras.getByteArray("result");
            out_bmp = BitmapFactory.decodeByteArray(result_byte_array,
                    0,
                    result_byte_array.length);
        } catch(NullPointerException e) {
            e.getMessage();
        }

        result.setImageBitmap(out_bmp);

        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sample = createImageFile();
                    f_out = new FileOutputStream(sample.getAbsolutePath());
                    out_bmp.compress(Bitmap.CompressFormat.PNG, 100, f_out);
                    Toast.makeText(ResultActivity.this,
                            "Image Saved at " + sample.getAbsolutePath(),
                            Toast.LENGTH_LONG).show();

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(retHome);
            }
        });
    }
}
