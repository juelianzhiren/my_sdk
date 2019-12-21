package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.entity.PetalsInfo;
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

        PetalsInfo info = new PetalsInfo();
        info.setName("三年级上册一");
        List<PetalsInfo.PetalEntity> list = new ArrayList<>();
        PetalsInfo.PetalEntity entity = new PetalsInfo.PetalEntity();
        List<String> childList = new ArrayList<>();
        childList.add("A1");
        childList.add("A2");
        childList.add("A3");
        entity.setName("A");
        entity.setChildList(childList);
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("B");
        childList = new ArrayList<>();
        childList.add("B1");
        childList.add("B2");
        childList.add("B3");
        entity.setChildList(childList);
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("C");
        childList = new ArrayList<>();
        childList.add("C1");
        childList.add("C2");
        childList.add("C3");
        entity.setChildList(childList);
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("D");
        childList = new ArrayList<>();
        childList.add("我");
        childList.add("们");
        childList.add("是");
        entity.setChildList(childList);
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("E");
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("F");
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("G");
        list.add(entity);
        entity = new PetalsInfo.PetalEntity();
        entity.setName("H");
        list.add(entity);
        info.setPetalList(list);
        mPetalsInRoundView.setPetalsInfo(info);

        mPetalsInRoundView.setOnInnerCircleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "innner click");
            }
        });
    }
}