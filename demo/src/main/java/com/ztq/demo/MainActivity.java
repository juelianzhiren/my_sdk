package com.ztq.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.derry.ioc_annotation_lib.InjectTool;
import com.derry.ioc_annotation_lib.annation.BindView;
import com.derry.ioc_annotation_lib.annation.Click;
import com.derry.ioc_annotation_lib.annation.ContentView;
import com.derry.ioc_annotation_lib.annation_common.OnClickCommon;
import com.derry.ioc_annotation_lib.annation_common.OnClickLongCommon;
import com.derry.kt_coroutines.R;

@ContentView(R.layout.activity_main_java) // 模仿 XUtils
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "noahedu.MainActivity";
    @Deprecated
    String str2;

    @BindView(R.id.bt_test1)  // 模仿 XUtils
    private Button button1; // Dagger2 为什么不能写private

    @BindView(R.id.bt_test2) // 模仿 XUtils
    private Button button2;

    @BindView(R.id.bt_test3) // 模仿 XUtils
    private Button button3;

    @Deprecated
    String str;

    @Deprecated
    private void derry() {

    }

    @Deprecated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InjectTool.inject(this); // 我们的用法
        // Xutils.x(this); // XUtils的用法
        // Butterknife.bind(this); // Butterknife

        Toast.makeText(this, button1.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, button2.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, button3.getText(), Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.bt_test3) // 模仿 XUtils
    private void show() {
        Toast.makeText(this, "show is run", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.bt_test4) // 模仿 XUtils
    private void show2() {
        Toast.makeText(this, "show2 is run", Toast.LENGTH_SHORT).show();
    }


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 下面开始写，兼容事件版本
    @Deprecated
    @OnClickCommon(R.id.bt_t1) // 点击事件
    private void test111() {

        Toast.makeText(this, "兼容事件 点击", Toast.LENGTH_SHORT).show();

        /*Button button = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        button.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });*/

        // 以后有更多更多的事件，是不是，事件有通用代码
        // 事件 事件源 事件处理器

        // 总结 事件 三要数
        // TODO 1 订阅方式     setOnClickListener setOnLongClickListener setOnDragListener
        // TODO 2 事件源对象   View.OnClickListener  View.OnLongClickListener
        // TODO 3 事件执行方法 onClick onLongClick   最终的事件消费
    }

    @OnClickLongCommon(R.id.bt_t2) // 长按事件
    private boolean test222() {
        Log.v(TAG, "test222");
        Toast.makeText(this, "兼容事件 长按", Toast.LENGTH_SHORT).show();
        return false;
    }

    // 例如：拖拽事件

    // A1 事件

    // A2 事件

    // ...

}