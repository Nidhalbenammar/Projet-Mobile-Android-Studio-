package com.example.tp7.Taches;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.databinding.ItemtacheBinding;

import java.util.ArrayList;

public class TacheAdapter extends RecyclerView.Adapter<TacheViewHolder> {
    ArrayList<Tache> list=new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)

    {
        this.listener=onItemClickListener;
    }

    public TacheAdapter(ArrayList<Tache> list){
        this.list=list;
    }
    @NonNull
    @Override
    public TacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemtacheBinding binding=ItemtacheBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TacheViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull TacheViewHolder holder, int position) {
        Tache tache=list.get(position);
        holder.setTache(tache);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}