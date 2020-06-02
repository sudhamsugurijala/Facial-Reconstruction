package com.example.miniproject;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    ImageView result=null;
    Button home=null;
    Button saveImg=null;
    Bitmap out_bmp=null;
    byte[] result_byte_array=null;

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
            out_bmp = BitmapFactory.decodeByteArray(result_byte_array, 0, result_byte_array.length);
        } catch(NullPointerException e) {
            e.getMessage();
        }

        result.setImageBitmap(out_bmp);

        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if disk is writable
                String state = Environment.getExternalStorageState();
                if(Environment.MEDIA_MOUNTED.equals(state)) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File fileDir = new File(root + "/phi_faces");
                    fileDir.mkdirs();

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fname = "Result-"+timeStamp+".png";

                    File file = new File(fileDir, fname);
                    if(file.exists()) file.delete();
                    try {
                        FileOutputStream fout = new FileOutputStream(file);
                        out_bmp.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        fout.flush();
                        fout.close();
                        Toast.makeText(ResultActivity.this, "Image Saved!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ResultActivity.this, "External Storage Not Writable!", Toast.LENGTH_LONG).show();
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
