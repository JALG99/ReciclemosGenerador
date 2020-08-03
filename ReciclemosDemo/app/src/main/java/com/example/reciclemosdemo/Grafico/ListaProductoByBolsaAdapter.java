package com.example.reciclemosdemo.Grafico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Entities.CategoriaDivided;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.R;

import java.util.ArrayList;


public class ListaProductoByBolsaAdapter extends RecyclerView.Adapter<ListaProductoByBolsaAdapter.ViewHolder> {

    private ArrayList<CategoriaDivided> dataset;
    private Context context;
    private OnNoteListener canonNoteListener;

    public ListaProductoByBolsaAdapter(Context context)
    {
        this.context=context;
        dataset= new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist,parent,false);

        return new ViewHolder(view,canonNoteListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){

        CategoriaDivided c = dataset.get(position);
        holder.txt_Contador.setText(Integer.toString(c.getCantidad()));
        holder.txt_PesoContrador.setText(Integer.toString(c.getPeso()));
        holder.txt_PuntosContador.setText(Integer.toString(c.getPuntos()));
        int [] imagenes = {R.drawable.plastico,R.drawable.vidrio,R.drawable.metal,R.drawable.papelcarton};
        if(c.getTipo().equals("Plastico"))
        {
            holder.img_ImageTipo.setImageResource(imagenes[0]);
        }else if(c.getTipo().equals("Vidrio"))
        {
            holder.img_ImageTipo.setImageResource(imagenes[1]);
        }else if(c.getTipo().equals("Metal"))
        {
            holder.img_ImageTipo.setImageResource(imagenes[2]);
        }else{
            holder.img_ImageTipo.setImageResource(imagenes[3]);
        }



    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }


    public void adicionarProductoBolsas(ArrayList<CategoriaDivided> listaProductos, OnNoteListener canonNoteListener){
        dataset.addAll(listaProductos);
        notifyDataSetChanged();
        this.canonNoteListener=canonNoteListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txt_Contador;
        private TextView txt_PesoContrador;
        private TextView txt_PuntosContador;
        private ImageView img_ImageTipo;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView ,OnNoteListener onNoteListener ){
            super(itemView);
            txt_Contador=(TextView) itemView.findViewById(R.id.txtCountProductsPlastic);
            txt_PesoContrador=(TextView) itemView.findViewById(R.id.txtPesoProductsPlastic);
            txt_PuntosContador=(TextView) itemView.findViewById(R.id.txtCantidadMetalCount);
            img_ImageTipo = itemView.findViewById(R.id.imagePlastic);
            this.onNoteListener=onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.oneNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void oneNoteClick(int position);
    }
}
