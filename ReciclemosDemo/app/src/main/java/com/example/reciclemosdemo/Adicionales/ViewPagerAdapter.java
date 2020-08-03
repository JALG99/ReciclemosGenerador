package com.example.reciclemosdemo.Adicionales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.reciclemosdemo.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4, R.drawable.slide5, R.drawable.slide6};
    private Integer[] titulos = {R.string.titulo1, R.string.titulo2, R.string.titulo3, R.string.titulo4, R.string.titulo5, R.string.titulo6};
    private Integer[] explicaciones = {R.string.explicacion1, R.string.explicacion2, R.string.explicacion3, R.string.explicacion4, R.string.explicacion5, R.string.explicacion6};

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        TextView sliderTitulo = view.findViewById(R.id.sliderTitulo);
        sliderTitulo.setText(titulos[position]);

        TextView sliderExplicacion = view.findViewById(R.id.sliderExplicacion);
        sliderExplicacion.setText(explicaciones[position]);

        ImageView sliderImage = view.findViewById(R.id.sliderImage);
        sliderImage.setImageResource(images[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
