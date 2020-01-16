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
        entity.setName("A类");
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
        entity.setName("第三类");
        childList = new ArrayList<>();
        childList.add("C1");
        childList.add("C2");
        childList.add("C3");
        entity.setChildList(childList);
        list.add(entity);

        entity = new PetalsInfo.PetalEntity();
        entity.setName("我们");
        childList = new ArrayList<>();
        childList.add("我");
        childList.add("们");
        childList.add("是");
        entity.setChildList(childList);
        list.add(entity);

        entity = new PetalsInfo.PetalEntity();
        entity.setName("我拉拉的");
        childList = new ArrayList<>();
        childList.add("大");
        childList.add("东西范德萨发生");
        childList.add("的");
        entity.setChildList(childList);
        list.add(entity);

        entity = new PetalsInfo.PetalEntity();
        entity.setName("F");
        childList = new ArrayList<>();
        childList.add("有");
        childList.add("西");
        childList.add("都");
        entity.setChildList(childList);
        list.add(entity);

        entity = new PetalsInfo.PetalEntity();
        entity.setName("G");
        childList = new ArrayList<>();
        childList.add("人");
        childList.add("东");
        childList.add("他");
        entity.setChildList(childList);
        list.add(entity);

        entity = new PetalsInfo.PetalEntity();
        entity.setName("H");
        childList = new ArrayList<>();
        childList.add("大小洗");
        childList.add("东西");
        childList.add("的");
        entity.setChildList(childList);
        list.add(entity);

        info.setPetalList(list);
        mPetalsInRoundView.setPetalsInfo(info);
        mPetalsInRoundView.setHighlightIndex(1, 1);
        mPetalsInRoundView.setInnerCircleClickListener(new PetalsInRoundView.InnerCircleClickListener() {
            @Override
            public void onClick() {
                Log.v(TAG, "innner click");
            }
        });
        mPetalsInRoundView.setSectorClickListener(new PetalsInRoundView.SectorClickListener() {
            @Override
            public void onClick(int groupIndex, int childIndex, boolean isFromUser) {
                Log.v(TAG, "SectorClickListener, groupIndex = " + groupIndex + "; childIndex = " + childIndex);
                mPetalsInRoundView.setHighlightIndex(groupIndex, childIndex);
            }
        });
        mPetalsInRoundView.setPetalClickListener(new PetalsInRoundView.PetalClickListener() {
            @Override
            public void onClick(int groupIndex, boolean isFromUser) {
                Log.v(TAG, "PetalClickListener, groupIndex = " + groupIndex);
                mPetalsInRoundView.setHighlightIndex(groupIndex, -1);
            }
        });
    }
}