package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MorphActivity extends AppCompatActivity {

    ImageView display=null;
    Button go=null;
    Button send=null;
    Spinner choice=null;
    TextView error=null;
    String url=null;

    Bitmap pic=null;
    Bitmap cropTemp=null;
    ByteArrayOutputStream bout=null;
    byte[] image_byte_array=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morph);

        display = findViewById(R.id.imageView);
        choice = findViewById(R.id.spinnerObj2);
        go = findViewById(R.id.button6);
        send = findViewById(R.id.button4);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choice.setAdapter(adapter);

        error = findViewById(R.id.errorText);
        error.setTextColor(Color.BLUE);
        error.setText("Image size must not exceed 500 kb!");

        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ch = choice.getSelectedItem().toString();
                if (ch.equals("Camera")) {
                    Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(picture, 0);
                } else if (ch.equals("Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, 1);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(image_byte_array == null) {
                    Toast.makeText(MorphActivity.this, "Please Choose an image", Toast.LENGTH_LONG).show();
                }
                else {
                    RequestBody postBodyImg = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("image", "input.png", RequestBody.create(MediaType.parse("image/*png"), image_byte_array))
                            .build();

                    postRequest(url, postBodyImg);
                }
            }
        });
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build();

    Call post(String url, RequestBody postBody, Callback callback) {
        Request req = new Request.Builder().url(url).post(postBody).build();
        Call call = client.newCall(req);
        call.enqueue(callback);
        return call;
    }

    void postRequest(String postURL, RequestBody postBody) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch(requestCode) {
                case 0:
                    if(resultCode == RESULT_OK && data != null) {
                        cropTemp = (Bitmap) data.getExtras().get("data");

                        // crop center of image into the to-be sent bitmap (for 1:1 ratio)
                        if(cropTemp.getWidth() >= cropTemp.getHeight()) {
                            pic = Bitmap.createBitmap(cropTemp,
                                    (cropTemp.getWidth()/2) - (cropTemp.getHeight()/2), 0,
                                    cropTemp.getHeight(), cropTemp.getHeight());
                        }
                        else {
                            pic = Bitmap.createBitmap(cropTemp,
                                    (cropTemp.getHeight()/2) - (cropTemp.getWidth()/2), 0,
                                    cropTemp.getWidth(), cropTemp.getWidth());
                        }
                        display.setImageBitmap(pic);

                        bout = new ByteArrayOutputStream();
                        pic.compress(Bitmap.CompressFormat.PNG, 100, bout);
                        image_byte_array = bout.toByteArray();
                    }
                    break;
                case 1:
                    if(resultCode == RESULT_OK && data != null) {
                        Uri uri = data.getData();
                        display.setImageURI(uri);

                        cropTemp = ((BitmapDrawable)display.getDrawable()).getBitmap();

                        // crop center of image into the to-be sent bitmap (for 1:1 ratio)
                        if(cropTemp.getWidth() >= cropTemp.getHeight()) {
                            pic = Bitmap.createBitmap(cropTemp,
                                    (cropTemp.getWidth()/2) - (cropTemp.getHeight()/2), 0,
                                    cropTemp.getHeight(), cropTemp.getHeight());
                        }
                        else {
                            pic = Bitmap.createBitmap(cropTemp,
                                    (cropTemp.getHeight()/2) - (cropTemp.getWidth()/2), 0,
                                    cropTemp.getWidth(), cropTemp.getWidth());
                        }
                        display.setImageBitmap(pic);

                        bout = new ByteArrayOutputStream();
                        pic.compress(Bitmap.CompressFormat.PNG, 100, bout);
                        image_byte_array = bout.toByteArray();
                    }
                    break;
                case 2: // kept for crop feature
                    Bundle extras = data.getExtras();
                    pic = extras.getParcelable("data");
                    display.setImageBitmap(pic);
            }
        }
    }
}