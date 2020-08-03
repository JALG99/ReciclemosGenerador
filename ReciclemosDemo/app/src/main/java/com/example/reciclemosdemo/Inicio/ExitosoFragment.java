package com.example.reciclemosdemo.Inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reciclemosdemo.R;

public class ExitosoFragment extends Fragment {



    public static ExitosoFragment newInstance(String param1, String param2) {
        ExitosoFragment fragment = new ExitosoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.exitoso, container, false);
        ((RegistrarBolsaActivity) getActivity()).creatingUpdate();
        return v;
    }


}