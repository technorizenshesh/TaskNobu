package com.tasknobu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tasknobu.R;
import com.tasknobu.model.SuccessResGetTask;

import java.util.List;

/**
 * Created by Ravindra Birla on 04,August,2021
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.OfferViewHolder> {

    private List<SuccessResGetTask.TaskQuestionnaire> taskDetails;

    private Context context;

    public QuestionsAdapter(Context context, List<SuccessResGetTask.TaskQuestionnaire> taskDetails)
    {
        this.context = context;
        this.taskDetails = taskDetails;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.questions_list_item, parent, false);
        OfferViewHolder viewHolder = new OfferViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {

        EditText etQuestions = holder.itemView.findViewById(R.id.etQuestion);

        TextView tvQuestion = holder.itemView.findViewById(R.id.tvQuestion);

        tvQuestion.setText(taskDetails.get(position).getQuestionSp());

        etQuestions.setText(taskDetails.get(position).getQuestionnairesAnswer());

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
