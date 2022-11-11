package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.tasknobu.R;
import com.tasknobu.databinding.ActivityHelpBinding;

public class HelpAct extends AppCompatActivity {
    ActivityHelpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_help);
         binding.header.tvHeader.setText("Help");
         binding.header.imgHeader.setOnClickListener(v -> finish());
    }
}