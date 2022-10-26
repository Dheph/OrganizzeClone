package com.example.organizzeclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.models.Movimentation;

import java.util.List;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterMovimentation extends RecyclerView.Adapter<AdapterMovimentation.MyViewHolder> {

    List<Movimentation> movimentations;
    Context context;

    public AdapterMovimentation(List<Movimentation> movimentations, Context context) {
        this.movimentations = movimentations;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentation, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentation movimentation = movimentations.get(position);

        holder.titulo.setText(movimentation.getDescription());
        holder.valor.setText(String.valueOf(movimentation.getValue()));
        holder.categoria.setText(movimentation.getCategory());
        holder.valor.setTextColor(context.getResources().getColor(R.color.revenue_accent));


        if (movimentation.getType() != null && movimentation.getType().equals("expense")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.accent));
            holder.valor.setText("-" + movimentation.getValue());
        }
    }


    @Override
    public int getItemCount() {
        return movimentations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
        }

    }

}
