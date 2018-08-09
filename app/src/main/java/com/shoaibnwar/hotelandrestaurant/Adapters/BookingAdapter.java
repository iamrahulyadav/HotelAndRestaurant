package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.R;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by gold on 8/9/2018.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    JSONArray table;

    public BookingAdapter(JSONArray tableArray)
    {
        this.table = tableArray;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, check_in_date, check_out_date, fare;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.c_name);
            check_in_date = (TextView) view.findViewById(R.id.check_in_date);
            check_out_date = (TextView) view.findViewById(R.id.check_out_date);
            fare = (TextView) view.findViewById(R.id.fare);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_detail_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String detailName = "";
        String detailCheckIn = "";
        String detailCheckOut = "";
        String detailFare = "";
        try {
            detailName = table.getJSONObject(position).get("CName").toString();
            detailCheckIn = table.getJSONObject(position).get("CHKIN").toString();
            detailCheckOut = table.getJSONObject(position).get("CHKOUT").toString();
            detailFare = table.getJSONObject(position).get("Fare").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //format date
        String dateCheckIn = detailCheckIn.split("T")[0];
        String dateCheckOut = detailCheckOut.split("T")[0];

        String timeCheckIn = detailCheckIn.split("T")[1];
        String timeCheckOut = detailCheckOut.split("T")[1];

        holder.title.setText(detailName);
        holder.check_in_date.setText(dateCheckIn + " (" + timeCheckIn + ")");
        holder.check_out_date.setText(dateCheckOut + " (" + timeCheckOut + ")");
        holder.fare.setText(detailFare);
    }

    @Override
    public int getItemCount() {
        return table.length();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }
}

