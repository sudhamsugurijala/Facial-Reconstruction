package com.example.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView picture = null;
    Button mask = null;
    Button morph = null;
    TextView alert = null;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner choice = findViewById(R.id.spinnerObj);
        Button select = findViewById(R.id.button);
        mask = findViewById(R.id.button2);
        morph = findViewById(R.id.button3);
        alert = findViewById(R.id.textView2);

        alert.setVisibility(View.INVISIBLE);
        mask.setVisibility(View.INVISIBLE);
        morph.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choice.setAdapter(adapter);

        select.setOnClickListener(new View.OnClickListener() {
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

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send picture to next activity
                Drawable drawable = picture.getDrawable();
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bout);
                byte[] b = bout.toByteArray();

                Intent reconstruct = new Intent(getApplicationContext(), MaskActivity.class);
                reconstruct.putExtra("image", b);
                startActivity(reconstruct);
            }
        });

        morph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send picture to next activity
                Drawable drawable = picture.getDrawable();
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bout);
                byte[] b = bout.toByteArray();

                Intent phi = new Intent(getApplicationContext(), MorphActivity.class);
                phi.putExtra("image", b);
                startActivity(phi);
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
                        Bitmap pic = (Bitmap) data.getExtras().get("data");
                        picture = findViewById(R.id.imageView);
                        picture.setImageBitmap(pic);
                        //uri = data.getData();
                        //cropFunct();
                    }
                    break;
                case 1:
                    if(resultCode == RESULT_OK && data != null) {
                        uri = data.getData();
                        picture = findViewById(R.id.imageView);
                        picture.setImageURI(uri);
                    }
                    break;
                case 2: // kept for crop feature
                    Bundle extras = data.getExtras();
                    Bitmap pic = extras.getParcelable("data");
                    picture = findViewById(R.id.imageView);
                    picture.setImageBitmap(pic);
            }
            mask.setVisibility(View.VISIBLE);
            morph.setVisibility(View.VISIBLE);
            alert.setVisibility(View.VISIBLE);
        }
    }

    private void cropFunct() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 2);

        } catch(ActivityNotFoundException e) {
            Toast.makeText(this, "Crop not supported", Toast.LENGTH_LONG).show();
        }
    }
}
