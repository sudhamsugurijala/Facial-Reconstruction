package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mask=null;
    Button morph=null;
    TextView alert=null;
    Button next=null;
    EditText url=null;
    EditText port=null;

    String urlVal="";
    String portVal="";
    String fullUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        next = findViewById(R.id.button);
        mask = findViewById(R.id.button2);
        morph = findViewById(R.id.button3);
        alert = findViewById(R.id.textView4);
        url = findViewById(R.id.editTextTextPersonName);
        port = findViewById(R.id.editTextTextPersonName2);

        mask.setVisibility(View.INVISIBLE);
        morph.setVisibility(View.INVISIBLE);
        alert.setVisibility(View.INVISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlVal = url.getText().toString();
                portVal = port.getText().toString();

                if(urlVal.equals("") || portVal.equals(""))  {
                    Toast.makeText(MainActivity.this, "Please enter necessary information",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    mask.setVisibility(View.VISIBLE);
                    morph.setVisibility(View.VISIBLE);
                    alert.setVisibility(View.VISIBLE);
                }
            }
        });

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send url to next activity
                fullUrl = "http://"+urlVal+":"+portVal+"/reconstruct";

                Intent reconstruct = new Intent(getApplicationContext(), MaskActivity.class);
                reconstruct.putExtra("url", fullUrl);
                startActivity(reconstruct);
            }
        });

        morph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send url to next activity
                fullUrl = "http://"+urlVal+":"+portVal+"/phi_morph";

                Intent phi = new Intent(getApplicationContext(), MorphActivity.class);
                phi.putExtra("url", fullUrl);
                startActivity(phi);
            }
        });
    }
/*
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
    }*/
}
