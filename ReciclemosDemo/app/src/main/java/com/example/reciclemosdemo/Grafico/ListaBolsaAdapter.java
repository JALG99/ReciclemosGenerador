package com.example.reciclemosdemo.Grafico;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.R;

public class ListaBolsaAdapter extends RecyclerView.Adapter<ListaBolsaAdapter.ViewHolder> {

    private ArrayList<Bolsa> dataset;
    private Context context;
    private Dialog myDialog;
    private int pos;

    public ListaBolsaAdapter(Context context)
    {
        this.context=context;
        dataset= new ArrayList<>();
        pos = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bolsalist,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item_bolsas.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),ProductByBolsa.class);
            intent.putExtra("posicion",dataset.get(viewHolder.getAdapterPosition()).getCodigo());
            context.startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Bolsa c = dataset.get(position);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        Date recojo = c.getRecojoFecha();
        if(recojo != null){
            String recojoFecha = formatter.format(recojo);
            holder.txt_RecojoFecha.setText("Recojo : "+recojoFecha);
        }else{
            holder.txt_RecojoFecha.setText("Recojo : Pendiente");
        }
        if(c.getObservaciones().equals("null") || c.getObservaciones().equals(null)){
            holder.txt_Observacion.setText("Observaciones : NO");
        }else{
            holder.txt_Observacion.setText("Observaciones : SI");
        }
        String creadoFecha = formatter.format(c.getCreadoFecha());
        holder.txt_Bolsa.setText("Bolsa "+Integer.toString(dataset.size() - position));
        holder.txt_CreadoFecha.setText("Creada : "+creadoFecha);

    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }


    public void adicionarListaCancion(ArrayList<Bolsa> listaCancion){
        dataset.addAll(listaCancion);
        notifyDataSetChanged();
    }


    public int getPos() {
        return pos;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout item_bolsas;
        private TextView txt_Bolsa;
        private TextView txt_RecojoFecha;
        private TextView txt_CreadoFecha;
        private ImageView imageBolsaIcon;
        private TextView txt_Observacion;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView  ){
            super(itemView);
            item_bolsas = (RelativeLayout) itemView.findViewById(R.id.item_LastBolsa);
            txt_Bolsa=(TextView) itemView.findViewById(R.id.txtBolsa);
            txt_CreadoFecha=(TextView) itemView.findViewById(R.id.txtCreadoFecha);
            txt_RecojoFecha=(TextView) itemView.findViewById(R.id.txtRecoojoFecha);
            imageBolsaIcon = itemView.findViewById(R.id.imageBolsaIcon);
            txt_Observacion = itemView.findViewById(R.id.txtObservaciones);

        }

    }

    public interface OnNoteListener{
        void oneNoteClick(int position);
    }
}