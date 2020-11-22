package com.herokuapp.PrivateLearningApp.ui.main.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.herokuapp.PrivateLearningApp.R;

public class ScheduleFragment extends Fragment {

    private StudentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(ScheduleFragment.this).get(StudentViewModel.class);
        return inflater.inflate(R.layout.fragment_schedule_student, container, false);
    }
}