package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class MorphActivity extends AppCompatActivity {

    Bitmap input_bmp;
    byte[] input_byte_array;
    EditText url;
    EditText port;
    TextView error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.morph);
        ImageView img = findViewById(R.id.imageView);
        url = findViewById(R.id.editText2);
        port = findViewById(R.id.portNummorph);
        Button send = findViewById(R.id.button4);

        Bundle extras = getIntent().getExtras();
        input_byte_array = extras.getByteArray("image");
        input_bmp = BitmapFactory.decodeByteArray(input_byte_array,0, input_byte_array.length);

        img.setImageBitmap(input_bmp);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = url.getText().toString();
                String portnum = port.getText().toString();

                String postURL = addr+":"+portnum+"/phi_morph";

                RequestBody postBodyImg = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", "input.jpg", RequestBody.create(MediaType.parse("image/*jpg"), input_byte_array))
                        .build();

                postRequest(postURL, postBodyImg);
            }
        });
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build();

    Call post(String url, RequestBody postBody, Callback callback) {
        Request req = new Request.Builder().url(url).post(postBody).build();
        Call call = client.newCall(req);
        call.enqueue(callback);
        return call;
    }

    void postRequest(String postURL, RequestBody postBody) {

        error = findViewById(R.id.errorText);
        error.setText("Connecting to Server ...");
        error.setTextColor(Color.BLACK);

        post(postURL, postBody,new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                call.cancel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText("Error! Could not connect to server!");
                        error.setTextColor(Color.RED);
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        error.setText("Connection Successful!");
                        error.setTextColor(Color.GREEN);

                        try {
                            String resp = response.body().string();
                            System.out.println(resp);
                            if(resp.equals("Try another Photo")) {
                                error.setText("Error! Try another photo");
                                error.setTextColor(Color.RED);
                            }
                            else {
                                byte[] resImg = Base64.decode(resp, Base64.DEFAULT);

                                Intent resAct = new Intent(getApplicationContext(), ResultActivity.class);
                                resAct.putExtra("result", resImg);
                                startActivity(resAct);
                            }
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.run();
            }
        });
    }
}
