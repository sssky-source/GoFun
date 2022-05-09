package com.coolweather.gofun.fragment.Map.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Map.bean.TypeItem;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapterone extends RecyclerView.Adapter<TypeAdapterone.ViewHolder> {

    private List<TypeItem> itemList = new ArrayList<>();
    private Context context;

    public TypeAdapterone(List<TypeItem> typeItems,Context context){
        this.itemList = typeItems;
        this.context = context;
    }
    @NonNull
    @Override
    public TypeAdapterone.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d("one","2222222222");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapterone.ViewHolder holder, int position) {
        TypeItem typeItem = itemList.get(position);
        holder.textView.setText(typeItem.getType());
        holder.imageView.setImageResource(typeItem.getImage());
        Log.d("one","1111111111111");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_type);
            textView  = itemView.findViewById(R.id.tv_type);
        }
    }
}
