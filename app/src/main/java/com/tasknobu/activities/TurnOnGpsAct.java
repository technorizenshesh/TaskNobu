package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.tasknobu.R;
import com.tasknobu.databinding.ActivityTurnOnGpsBinding;

public class TurnOnGpsAct extends AppCompatActivity {

    ActivityTurnOnGpsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_turn_on_gps);

        binding.btnLogin.setOnClickListener(v ->
                {
                    startActivity(new Intent(TurnOnGpsAct.this,AddLocationAct.class));
                }
        );

    }
}