package com.example.tp7.Taches;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.databinding.ItemtacheBinding;

public class TacheViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    private final ItemtacheBinding ui;
    private TacheAdapter.OnItemClickListener listener;
    public TacheViewHolder(@NonNull ItemtacheBinding ui) {
        super(ui.getRoot());
        this.ui=ui;
        itemView.setOnClickListener(this);
    }
    public void setTache(Tache ex){
        ui.textView1.setText(ex.getNom());
        ui.textView2.setText(ex.getJour());
        ui.textView3.setText(String.valueOf(ex.getH())+":"+String.valueOf(ex.getM()));

    }
    public void setOnItemClickListener(TacheAdapter.OnItemClickListener l) {
        this.listener= l;

    }
    @Override
    public void onClick(View v) {
        if(listener !=null)
            listener.onItemClick(getAdapterPosition());
    }
}