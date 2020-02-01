package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.MapView;

public class MapViewActivity extends Activity {
    private static final String TAG = "noahedu.MapViewActivity";
    private MapView mMapView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        mContext = this;
        mMapView = findViewById(R.id.mapview);
        mMapView.setSelectInterface(new MapView.SelectInterface() {
            @Override
            public void onSelect(MapView.ProvinceItem item) {
                if (item != null) {
                    Log.v(TAG, "onSelect, item = " + item + "; id = " + item.getId() + "; title = " + item.getTitle());
                    Utils.showToast(mContext, "id = " + item.getId() + "; title = " + item.getTitle());
                }
            }
        });
    }
}