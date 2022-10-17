package com.ztq.sdk.mvvm.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.databinding.ActivityMvvmdemoBinding;
import com.ztq.sdk.mvvm.viewmodel.MVVMDataViewModel;

public class MVVMActivity extends Activity {
    private MVVMDataViewModel userViewModel;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMvvmdemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvmdemo);
        userViewModel = new MVVMDataViewModel();
        binding.setUserViewModel(userViewModel);
        binding.setHandlers(this);

        tvData = binding.tvData;
    }

    public void onClickShowToastName(View view) {
        Toast.makeText(this, tvData.getText().toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickLoadData(View view) {
        userViewModel.loadUserData();
    }
}