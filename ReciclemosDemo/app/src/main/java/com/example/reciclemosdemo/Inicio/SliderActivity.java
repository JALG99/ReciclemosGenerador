package com.example.reciclemosdemo.Inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.reciclemosdemo.R;
import com.example.reciclemosdemo.Adicionales.ViewPagerAdapter;

public class SliderActivity extends AppCompatActivity {

    ViewPager vipaInicio;
    LinearLayout sliderDots;
    private int dotscount;
    private ImageView[] dots;
    private Button btnRegistrar, btnIniSes;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        vipaInicio = findViewById(R.id.vipaInicio);
        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        btnIniSes = findViewById(R.id.btnIniSes);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        vipaInicio.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();

        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);

            sliderDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        vipaInicio.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnRegistrar.setOnClickListener(nOnClickListener);
        btnIniSes.setOnClickListener(mOnClickListener);

    }


    private View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent registeractivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registeractivity);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent loginactivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginactivity);
        }
    };
}
