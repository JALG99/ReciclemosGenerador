package com.example.reciclemosdemo.Inicio;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Entities.Bolsalist;
import com.example.reciclemosdemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDetalle extends RecyclerView.Adapter<AdapterDetalle.ViewHolderDetalle> {

    ArrayList<Bolsalist> listDetalle;
    private String tipo;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onDelete(int position, int codigo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {this.onItemClickListener = onItemClickListener;}

    public AdapterDetalle(ArrayList<Bolsalist> listDetalle, String tipo){
        this.listDetalle = listDetalle;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolderDetalle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detalle,null,false);
        return new ViewHolderDetalle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDetalle holder, int position) {
        holder.asignarDetalle(listDetalle.get(position));
    }

    @Override
    public int getItemCount() {
        return listDetalle.size();
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

        public void asignarDetalle(Bolsalist bolsalist) {
            cod = bolsalist.getCodigo();
            txtNombre.setText(bolsalist.getNombre());
            txtTipo.setText(bolsalist.getCategoria());
            switch (bolsalist.getCodcontenido()){
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

            if(tipo.equals("view"))
                imageButton.setVisibility(View.GONE);

            txtContenido.setText((int) bolsalist.getContenido() + " " + bolsalist.getAbreviatura());
            if(bolsalist.getPeso() <= 500.0) {
                txtPeso.setText(bolsalist.getPeso() + " gr");
            }else{
                txtPeso.setText(bolsalist.getPeso()/1000.0 + " kg");
            }
            txtPuntos.setText("+" + bolsalist.getPuntos() + " pts");
            Picasso.get()
                    .load(bolsalist.getUrlImage())
                    .error(R.drawable.login)
                    .into(imgProducto);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DELETE PROBOLSA
                }
            };
            txtCantidad.setText("Cant " + bolsalist.getCantidad());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onDelete(position, cod);
                        }
                    }
                }
            });
        }

    }
}
