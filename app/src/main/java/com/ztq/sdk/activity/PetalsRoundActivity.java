package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.PetalsInRoundView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ztq
 * Date: 2019/12/20 12:45
 * Description: ${DESCRIPTION}
 */
public class PetalsRoundActivity extends Activity {
    private static final String TAG = "noahedu.PetalsRoundActivity";
    private Context mContext;
    private PetalsInRoundView mPetalsInRoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petals_round);

        mPetalsInRoundView = findViewById(R.id.petals_round_view);
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        mPetalsInRoundView.setPetalNameList(list);
    }
}