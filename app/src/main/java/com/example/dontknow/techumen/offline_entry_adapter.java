package com.example.dontknow.techumen;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dont know on 27-08-2017.
 */

public class offline_entry_adapter  extends RecyclerView.Adapter<offline_entry_adapter.MyViewHolder>{

    private ArrayList<TeDatabase> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;
        TextView remaining;
        TextView eventname;
        TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.idoffline_entry_name);
            this.email = (TextView) itemView.findViewById(R.id.idoffline_entry_email);
            this.phone = (TextView) itemView.findViewById(R.id.idoffline_entry_phone);
            this.remaining = (TextView) itemView.findViewById(R.id.idoffline_entry_remaining);
            this.eventname = (TextView)itemView.findViewById(R.id.idoffline_entry_eventname);
            this.status = (TextView) itemView.findViewById(R.id.idoffline_entry_status);
        }
    }
    public offline_entry_adapter(ArrayList<TeDatabase> data)
    {
        this.dataset = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_entry_row,parent,false);
        view.setOnLongClickListener(offline.myOnLongClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView name = holder.name;
        TextView email = holder.email;
        TextView phone = holder.phone;
        TextView remaining = holder.remaining;
        TextView eventname = holder.eventname;
        TextView status = holder.status;
        if(dataset.get(position).get_status().equals("Not Uploaded"))
            status.setTextColor(Color.RED);
        else
            status.setTextColor(Color.GREEN);

        name.setText(dataset.get(position).get_name());
        email.setText(dataset.get(position).get_email());
        phone.setText(dataset.get(position).get_phone());
        remaining.setText(dataset.get(position).get_remaining());
        eventname.setText(dataset.get(position).get_eventname());
        status.setText(dataset.get(position).get_status());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
