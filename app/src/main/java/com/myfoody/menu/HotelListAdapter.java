package com.myfoody.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myfoody.R;
import com.myfoody.model.HotelDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HotelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HotelDetails> hotelDetails;

    public HotelListAdapter(Context context, List<HotelDetails> hotelDetailsList) {
        this.context = context;
        hotelDetails = hotelDetailsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_rest_list_layout, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class HotelViewHolder extends RecyclerView.ViewHolder {
        private TextView hotelName, hotelMinOrder, orderNow, address, quickBites;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.txt_rest_name);
            hotelMinOrder = itemView.findViewById(R.id.txt_min_order);
            orderNow = itemView.findViewById(R.id.txt_order_now);
            address = itemView.findViewById(R.id.txt_rest_addr);
            quickBites = itemView.findViewById(R.id.txt_rest_spl);
        }
    }
}