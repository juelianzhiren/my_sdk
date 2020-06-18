package com.ztq.sdk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.PieImageView;

/**
 * Created by Danny 姜.
 */
public class PieImageActivity extends BaseActivity {
    private PieImageView pieImageView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_image);

        pieImageView = findViewById(R.id.pieImageView);
        pieImageView.setProgress(45);

        mBtn = findViewById(R.id.activity_pie_image_btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pieImageView.getProgress() <= 95) {
                    pieImageView.setProgress(pieImageView.getProgress() + 5);
                }
            }
        });
    }
}