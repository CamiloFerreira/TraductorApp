package com.lfserver.tk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lfserver.tk.Model.Palabras_class;

import java.util.ArrayList;

public class AdapterModel extends RecyclerView.Adapter<AdapterModel.ViewHolder> {

    private ArrayList<Palabras_class> modeloList;

    public AdapterModel(ArrayList<Palabras_class> aModelo){
        this.modeloList = aModelo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.assignarDatos(modeloList.get(position));
    }

    @Override
    public int getItemCount() {
        return modeloList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView palabra;
        TextView significado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            palabra = (TextView) itemView.findViewById(R.id.palabra);
            significado = (TextView) itemView.findViewById(R.id.significado);

        }

        public void assignarDatos(Palabras_class modelo) {
            palabra.setText(modelo.getPal());
            significado.setText(modelo.getSig());
        }
    }
}
