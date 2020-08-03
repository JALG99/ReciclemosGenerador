package com.example.reciclemosdemo.Inicio;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Entities.Bolsalist;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Productolist;
import com.example.reciclemosdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterProducto extends RecyclerView.Adapter<AdapterProducto.ViewHolderDetalle> {

    ArrayList<Productolist> listProducto;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onAdd(int codigo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {this.onItemClickListener = onItemClickListener;}

    public AdapterProducto(ArrayList<Productolist> listProducto){
        this.listProducto = listProducto;
    }

    @NonNull
    @Override
    public ViewHolderDetalle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producto,null,false);
        return new ViewHolderDetalle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDetalle holder, int position) {
        holder.asignarProducto(listProducto.get(position));
    }

    @Override
    public int getItemCount() {
        return listProducto.size();
    }

    public void filterList(ArrayList<Productolist> filteredList){
        listProducto = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolderDetalle extends RecyclerView.ViewHolder {

        ImageView imgProducto, imgTipo;
        TextView txtNombre, txtTipo, txtContenido, txtPeso, txtPuntos, txtCantidad;
        ImageButton imageButton;
        int cod;

        public ViewHolderDetalle(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            imgTipo = itemView.findViewById(R.id.imgTipo);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtContenido = itemView.findViewById(R.id.txtContenido);
            txtPeso = itemView.findViewById(R.id.txtPeso);
            txtPuntos = itemView.findViewById(R.id.txtTotalPuntos);
            txtCantidad = itemView.findViewById(R.id.txtTotalCantidad);
            imageButton = itemView.findViewById(R.id.imageButton);
        }

        public void asignarProducto(Productolist listProducto) {
            cod = listProducto.getCodigo();
            txtNombre.setText(listProducto.getNombre());
            txtTipo.setText(listProducto.getCategoria());
            switch (listProducto.getCodcontenido()){
                case 1:
                    imgTipo.getDrawable().setTint(Color.parseColor("#99EFEFEF"));
                    break;
                case 2:
                    imgTipo.getDrawable().setTint(Color.parseColor("#99808080"));
                    break;
                case 3:
                    imgTipo.getDrawable().setTint(Color.parseColor("#0C4A7C"));
                    break;
                case 4:
                    imgTipo.getDrawable().setTint(Color.parseColor("#FFC32C"));
                    break;
            }
            txtContenido.setText((int) listProducto.getContenido() + " " + listProducto.getAbreviatura());
            if(listProducto.getPeso() <= 500.0) {
                txtPeso.setText(listProducto.getPeso() + " gr");
            }else{
                txtPeso.setText(listProducto.getPeso()/1000.0 + " kg");
            }
            txtPuntos.setText("+10 pts");
            Picasso.get()
                    .load(listProducto.getUrlImage())
                    .error(R.drawable.login)
                    .into(imgProducto);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onAdd(cod);
                        }
                    }
                }
            });
        }

    }
}
