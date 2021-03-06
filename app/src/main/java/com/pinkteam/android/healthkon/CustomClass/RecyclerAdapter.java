package com.pinkteam.android.healthkon.CustomClass;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinkteam.android.healthkon.Controllers.RunRecordDetailActivity;
import com.pinkteam.android.healthkon.Models.Journey;
import com.pinkteam.android.healthkon.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Journey> mJourneys ;
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("E, HH:mm dd/MM/yyyy");
    String layoutName;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView journeyname_Textview;
        TextView date_Textview;
        TextView distance_Textview;
        TextView mtime_Textview;
        LinearLayout item;
        RatingBar mRatingBar;

        public ViewHolder(@NonNull View view, String layoutName) {
            super(view);
            if(layoutName.equals("detail")){
                journeyname_Textview = (TextView) view.findViewById(R.id.journeyname_txtview);
                date_Textview = (TextView) view.findViewById(R.id.date_txtview);
                distance_Textview = (TextView) view.findViewById(R.id.distance_txtview);
                item = (LinearLayout) view.findViewById(R.id.journey_item);
                mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            }else {
                journeyname_Textview = (TextView) itemView.findViewById(R.id.journeyname_txtview2);
                date_Textview = (TextView) itemView.findViewById(R.id.date_textview2);
                distance_Textview = (TextView) itemView.findViewById(R.id.distance_textview2);
                mtime_Textview = (TextView) itemView.findViewById(R.id.time_textview2);
                item = (LinearLayout) view.findViewById(R.id.journey_item);
            }
        }
    }


    public RecyclerAdapter(Context mContext, ArrayList<Journey> mJourneys, String layoutName) {
        this.layoutName = layoutName;
        this.mContext = mContext;
        this.mJourneys = mJourneys;
    }
    @NotNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if(layoutName.equals("detail")){
            view = inflater.inflate(R.layout.running_record_item_layout, parent, false);
        }else {
            view = inflater.inflate(R.layout.running_record_item_layout_2, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view, layoutName);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerAdapter.ViewHolder holder, int position) {
        Journey journey = mJourneys.get(position);

        holder.journeyname_Textview.setText(journey.getmName());
        holder.date_Textview.setText(dateFormat.format(journey.getmDate()));
        float distance = journey.getmDistance();
        holder.distance_Textview.setText(String.format("%.2fkm", distance));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RunRecordDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("journeyID", journey.getmJourneyId());
                i.putExtras(b);
                mContext.startActivity(i);
            }
        });
        if(layoutName.equals("home")){
            final long hours = journey.getmDuration() / 3600;
            final long minutes = (journey.getmDuration() % 3600) / 60;
            final long seconds = journey.getmDuration() % 60;
            holder.mtime_Textview.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }else {
            holder.mRatingBar.setRating(journey.getmRating());
        }
    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }
}
