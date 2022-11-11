package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.tasknobu.R;
import com.tasknobu.databinding.ActivityProfileBinding;

public class ProfileAct extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        binding.header.imgHeader.setOnClickListener(v -> finish());
    }
}