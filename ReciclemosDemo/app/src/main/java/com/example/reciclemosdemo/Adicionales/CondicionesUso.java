package com.example.reciclemosdemo.Adicionales;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.reciclemosdemo.R;

public class CondicionesUso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones_uso);

        TextView textview5;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        textview5 = findViewById(R.id.textView5);

        textview5.setMovementMethod(new ScrollingMovementMethod());

        getWindow().setLayout((int) (width), (int) (height));
    }
}
