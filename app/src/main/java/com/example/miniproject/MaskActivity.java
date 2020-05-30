package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;

public class MaskActivity extends AppCompatActivity {

    Bitmap input_bmp;
    Bitmap maskPic;
    PaintClass paint;
    byte[] input_byte_array;
    byte[] mask_byte_array;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mask);

        paint = new PaintClass(this, null);

        paint = findViewById(R.id.paint);
        final EditText url = findViewById(R.id.editText);
        Button send = findViewById(R.id.button4);
        ImageView background = findViewById(R.id.imageView3);

        // get image from previous activity
        Bundle extras = getIntent().getExtras();
        try {
            input_byte_array = extras.getByteArray("image");
            input_bmp = BitmapFactory.decodeByteArray(input_byte_array, 0, input_byte_array.length);
        } catch(NullPointerException e) {
            e.getMessage();
        }

        background.setImageBitmap(input_bmp);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user picture bitmap is in input_bmp, mask is in maskPic obtained below
                paint.setDrawingCacheEnabled(true);
                maskPic = paint.getDrawingCache();

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                maskPic.compress(Bitmap.CompressFormat.PNG, 100, bout);
                mask_byte_array = bout.toByteArray();

                paint.destroyDrawingCache();

                //send maskPic and input_bmp to server
                String address = url.getText().toString();
                
                //send result to resultActivity
                Intent resAct = new Intent(getApplicationContext(), ResultActivity.class);
                resAct.putExtra("result", mask_byte_array);
                startActivity(resAct);
            }
        });
    }
}
