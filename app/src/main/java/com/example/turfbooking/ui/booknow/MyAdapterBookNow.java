package com.example.turfbooking.ui.booknow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfbooking.R;
import com.example.turfbooking.ui.approveturf.ItemClickListener;
import com.example.turfbooking.ui.approveturf.TurfModel;

public class MyAdapterBookNow extends RecyclerView.Adapter<MyAdapterBookNow.MyViewHolder>{

    // 4- Handling the Click Events
    public ItemClickListener clickListener;


    // 1- Data Source
    private TurfModel[] listData;

    public MyAdapterBookNow(TurfModel[] listData) {
        this.listData = listData;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // 2- View Holder Class:
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView2;
        public TextView textView3, textView4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView2 = itemView.findViewById(R.id.turf_name);
            this.textView3 = itemView.findViewById(R.id.turf_status);
            this.textView4 = itemView.findViewById(R.id.approve_turf);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (clickListener != null){
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }



    // 3- Implementing the Methods
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_item,parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.textView2.setText(listData[position].getName().toString());
        holder.textView3.setText(listData[position].getPin().toString());
        holder.textView4.setText("Book Now");
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

}
