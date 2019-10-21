package com.jmit.wmbreciever;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Object> buses_list;
    private RecyclerView thisrecycler;

    public void AddIntoBusesList(Object bus)
    {
        if(buses_list.contains(bus))
            return;
        buses_list.addLast(bus);
        notifyItemChanged(buses_list.size()-1);

        thisrecycler.getLayoutManager().scrollToPosition(buses_list.size()-1);

    }

    public RecyclerAdapter(Context context , LinkedList<Object> buses_list , RecyclerView thisrecycler)
    {
        this.context = context;
        this.buses_list = buses_list;
        this.thisrecycler = thisrecycler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Bus bus = (Bus) buses_list.get(position);
        holder.bus=bus;
        holder.textView.setText(bus.getName()+" "+bus.getLatitude()+" "+bus.getLongitude());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("name",holder.bus.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buses_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        Bus bus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
        }
    }
}
