package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reciclemosdemo.R;
import com.google.android.material.tabs.TabLayout;


public class TendenciaActivity extends Fragment implements  TabLayout.OnTabSelectedListener{
    private ViewPager viewPager;
    private ViewTrendingAdapter adapter;
    private TabLayout tabLayout;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tendencia,container,false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayoutTendencia2);
       // tabLayout.addTab(tabLayout.newTab().setText("Semana"));
        tabLayout.addTab(tabLayout.newTab().setText("Mes"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager =view.findViewById(R.id.viewPagerTendencia2);
        adapter = new ViewTrendingAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
