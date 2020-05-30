package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MaskActivity extends AppCompatActivity {

    Bitmap input_bmp;
    Bitmap maskPic;
    PaintClass paint;
    byte[] input_byte_array;
    byte[] mask_byte_array;
    EditText url;
    EditText port;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mask);

        paint = new PaintClass(this, null);

        paint = findViewById(R.id.paint);
        url = findViewById(R.id.editText);
        port = findViewById(R.id.editText3);
        ImageView background = findViewById(R.id.imageView3);
        Button send = findViewById(R.id.button4);

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

                String addr = url.getText().toString();
                String portnum = port.getText().toString();

                String postURL = addr+":"+portnum+"/reconstruct";

                RequestBody postBodyImg = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", "input.jpg", RequestBody.create(MediaType.parse("image/*jpg"), input_byte_array))
                        .addFormDataPart("mask", "mask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), mask_byte_array))
                        .build();

                postRequest(postURL, postBodyImg);
            }
        });
    }

    void postRequest(String postURL, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();

        Request req = new Request.Builder().url(postURL).post(postBody).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                call.cancel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView error = findViewById(R.id.errorText2);
                        error.setText("Error! Could not connect to server!");
                        error.setTextColor(Color.RED);
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView error = findViewById(R.id.errorText2);
                        error.setText("Connection Successful");
                        error.setTextColor(Color.GREEN);
                    }
                });
            }
        });

    }
}
