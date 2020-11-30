package com.ztq.sdk.mvp.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.activity.BaseActivity;
import com.ztq.sdk.mvp.presenter.LoginPresenter;
import com.ztq.sdk.mvp.presenter.LoginPresenterImpl;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:MVP模式中View层对应一个activity，这里是登陆的activity
 * demo的代码流程：Activity做了一些UI初始化的东西并需要实例化对应
 * LoginPresenter的引用和实现 LoginView的接口，监听界面动作，
 * Go按钮按下后即接收到查询天气的事件，在onClick里接收到即通过LoginPresenter
 * 的引用把它交给LoginPresenter处理。LoginPresenter接收到了登陆的逻辑就知道要登陆了，
 * 然后LoginPresenter显示进度条并且把逻辑交给我们的Model去处理，也就是这里面的LoginModel，
 * （LoginModel的实现类LoginModelImpl），同时会把OnLoginFinishedListener也就是LoginPresenter
 * 自身传递给我们的Model（LoginModel）。LoginModel处理完逻辑之后，结果通过
 * OnLoginFinishedListener回调通知LoginPresenter，LoginPresenter再把结果返回给view层的Activity，
 * 最后activity显示结果
 */
public class LoginMVPActivity extends BaseActivity implements LoginView, View.OnClickListener, CancelAdapt {
    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private LoginPresenter presenter;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mvp);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mTv = findViewById(R.id.login_mvp_tv);
        findViewById(R.id.button).setOnClickListener(this);

        presenter = new LoginPresenterImpl(this);

        mTv.setText(Html.fromHtml("3．用<img src=\"http://192.168.1.56/2/100/102/7/150810/大同步独立试卷-北师大-小学-数学-五年级-上学期----四川省/试题文件/图片/尖子生题库BS版-五上-数学-050-02.jpg\"/>和<img src=\"http://192.168.1.56/2/100/102/7/150810/大同步独立试卷-北师大-小学-数学-五年级-上学期----四川省/试题文件/图片/尖子生题库BS版-五上-数学-050-03.jpg\"/>拼成的对称图形是(\u000E\u0001\u000E\u0001)。"));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        username.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void navigateToHome() {
        // TODO  startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show();
        hideProgress();
//        finish();
    }

    @Override
    public void onClick(View v) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }
}