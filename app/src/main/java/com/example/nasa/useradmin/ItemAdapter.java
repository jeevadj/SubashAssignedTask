package com.example.nasa.useradmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by hp on 06-02-2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public int layoutId ;
    public static ArrayList<Adapter> itemlist;
    static Context context1;

    public ItemAdapter(int layoutId, ArrayList<Adapter> itemlist) {
        this.layoutId = layoutId;
        this.itemlist = itemlist;
    }


    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        final ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
           ImageView imageView=holder.img;
           TextView textView = holder.tv;
        System.out.println("boww"+itemlist.get(position).getUrl());
        textView.setText(itemlist.get(position).getText());
        if(itemlist.get(position).getUrl().isEmpty()){
            imageView.setVisibility(View.GONE);
        }else{
            Glide.with(context1).load(itemlist.get(position).getUrl()).asBitmap().into(imageView);
        }


    }

    @Override
    public int getItemCount() {
        return itemlist != null?itemlist.size():0;
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView img;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            context1 = itemView.getContext();
            img = (ImageView)itemView.findViewById(R.id.cardImage);
            tv=(TextView)itemView.findViewById(R.id.cardText);
            System.out.println("boww"+itemlist.size());
        }
    }
}
