package com.example.turfbooking.ui.mybooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfbooking.R;
import com.example.turfbooking.ui.approveturf.ItemClickListener;
import com.example.turfbooking.ui.approveturf.TurfModel;

import java.util.concurrent.ExecutionException;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>{

    // 4- Handling the Click Events
    public ItemClickListener clickListener;


    // 1- Data Source
    private TurfBookingModel[] listData;

    public MyBookingAdapter(TurfBookingModel[] listData) {
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
            this.textView2 = itemView.findViewById(R.id.created_by);
            this.textView3 = itemView.findViewById(R.id.booking_status);
            this.textView4 = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (clickListener != null){
                try {
                    clickListener.onClick(view, getAdapterPosition());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    // 3- Implementing the Methods
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_item_my_booking,parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.textView2.setText(listData[position].getCreatedBy().toString());
        holder.textView3.setText(listData[position].getBookingStatus().toString());
        holder.textView4.setText(listData[position].getBookingDate().toString().split("GMT")[0]);
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

}
