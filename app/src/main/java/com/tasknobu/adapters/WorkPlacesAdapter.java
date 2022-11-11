package com.tasknobu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tasknobu.R;
import com.tasknobu.activities.WorkPlaceDataAct;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.model.SuccessResGetWorkPlace;

import java.util.List;

/**
 * Created by Ravindra Birla on 04,August,2021
 */
public class WorkPlacesAdapter extends RecyclerView.Adapter<WorkPlacesAdapter.OfferViewHolder> {

    private List<SuccessResGetWorkPlace.Result> workList;

    private Context context;

    public WorkPlacesAdapter(Context context, List<SuccessResGetWorkPlace.Result> workList)
    {
        this.context = context;
        this.workList = workList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.task_detail_item, parent, false);
        OfferViewHolder viewHolder = new OfferViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        TextView tvShortDescription = holder.itemView.findViewById(R.id.tvDestination1);
        TextView tvDetail = holder.itemView.findViewById(R.id.tvDetail);

        tvShortDescription.setText(workList.get(position).getWorkPlaceDescriptionLong());

        tvDetail.setOnClickListener(v ->
                {
                    context.startActivity(new Intent(context, WorkPlaceDataAct.class).putExtra("id",workList.get(position).getWorkPlaceId()));
                }
                );

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
