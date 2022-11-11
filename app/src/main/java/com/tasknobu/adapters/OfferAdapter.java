package com.tasknobu.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tasknobu.R;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.utils.CheckboxCheckedListener;

import java.util.List;

/**
 * Created by Ravindra Birla on 04,August,2021
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private List<SuccessResGetTask.TaskDetail> taskDetails;

    private Context context;

    private CheckboxCheckedListener checkboxCheckedListener;

    public OfferAdapter(Context context, List<SuccessResGetTask.TaskDetail> taskDetails,CheckboxCheckedListener checkboxCheckedListener)
    {
        this.context = context;
        this.taskDetails = taskDetails;
        this.checkboxCheckedListener = checkboxCheckedListener;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.task_description, parent, false);
        OfferViewHolder viewHolder = new OfferViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {

        CheckBox checkbox1 = holder.itemView.findViewById(R.id.checkbox1);

        TextView tvTitle = holder.itemView.findViewById(R.id.tvDescr);
        tvTitle.setText(Html.fromHtml(taskDetails.get(position).getTaskDescriptionSp()));

        if(taskDetails.get(position).getChecked().equalsIgnoreCase("1"))
        {
            checkbox1.setChecked(true);
        }else
        {
            checkbox1.setChecked(false);
        }

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    checkboxCheckedListener.checkboxClick(position,"1",taskDetails.get(position).getId());
                }
                else
                {
                    checkboxCheckedListener.checkboxClick(position,"0",taskDetails.get(position).getId());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return taskDetails.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public OfferViewHolder(View itemView) {
            super(itemView);
        }
    }

}
