package com.tasknobu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tasknobu.R;
import com.tasknobu.activities.WorkPlaceDataAct;
import com.tasknobu.model.SuccessResGetWorkPlace;
import com.tasknobu.model.SuccessResWorkplaceData;

import java.util.List;

/**
 * Created by Ravindra Birla on 04,August,2021
 */
public class WorkPlacesDataAdapter extends RecyclerView.Adapter<WorkPlacesDataAdapter.OfferViewHolder> {

    private List<SuccessResWorkplaceData.Result> workList;

    private Context context;

    public WorkPlacesDataAdapter(Context context, List<SuccessResWorkplaceData.Result> workList)
    {
        this.context = context;
        this.workList = workList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.workplace_data_item, parent, false);
        OfferViewHolder viewHolder = new OfferViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        TextView tvShortDescription = holder.itemView.findViewById(R.id.tvShort);
        TextView tvlongDescription = holder.itemView.findViewById(R.id.tvLongDescription);
        TextView tvDetail = holder.itemView.findViewById(R.id.tvDetail);

        tvShortDescription.setText(workList.get(position).getWorkPlacesDataName());
        tvlongDescription.setText(workList.get(position).getWorkPlacesDataValue());

    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public OfferViewHolder(View itemView) {
            super(itemView);
        }
    }

}
