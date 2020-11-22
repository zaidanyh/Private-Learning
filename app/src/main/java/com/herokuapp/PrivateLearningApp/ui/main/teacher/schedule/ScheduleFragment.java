package com.herokuapp.PrivateLearningApp.ui.main.teacher.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.ui.main.teacher.TeacherViewModel;

public class ScheduleFragment extends Fragment {

    private TeacherViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);
        return inflater.inflate(R.layout.fragment_schedule_teacher, container, false);
    }
}