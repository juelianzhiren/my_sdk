package com.ztq.sdk.activity;

import android.os.Bundle;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * retrofit demo activity
 */
public class RetrofitActivity extends BaseActivity{
    private static final String TAG = "noahedu.RetrofitActivity";
    private final static String BASE_URL = "https://resource.youxuepai.com/";//服务器的IP地址与端口号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        addListener();
    }

    private void addListener() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetDataUsedbyRetrofit();
            }
        });
    }

    private void getNetDataUsedbyRetrofit() {
        //创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //将返回值转换成bean
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建调用接口
        HelloService helloService = retrofit.create(HelloService.class);
        //调用接口方法，访问url
        Call<ResponseInfo> call = helloService.testHttpGet("27125087","2022-10-01", "2022-10-13");
        //请求处理
        call.enqueue(new Callback<ResponseInfo>() {
            @Override
            //请求成功
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                Log.v(TAG, "onResponse, body msg = " + response.body().data);
            }

            @Override
            //请求失败
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                Log.e(TAG, "onFailure, msg = " + t.getMessage());
            }
        });
    }

    interface HelloService {
        @GET("ures/mistakesCollection/getQuestionInfoForSubject?client_key=44e0dd42a53a83be8981a1e5635b5a1b&sign=3789066E206685D11819072967CED642&rand=5f6acb5a-9007-4211-9d2e-5802b3032998&machine_no=7120935572777209013&phaseid=2")
        Call<ResponseInfo> testHttpGet(@Query("userid") String userid, @Query("start_date") String start_date, @Query("end_date") String end_date);
    }
    static class ResponseInfo {
        private List<Info> data;

        public List<Info> getData() {
            return data;
        }

        public void setData(List<Info> data) {
            this.data = data;
        }
    }

    static class Info {
        private int total;
        private int normal_num;
        private int difficult_num;
        private int accuracy;
        private int right;
        private String subjectid;
        private int easy_num;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getNormal_num() {
            return normal_num;
        }

        public void setNormal_num(int normal_num) {
            this.normal_num = normal_num;
        }

        public int getDifficult_num() {
            return difficult_num;
        }

        public void setDifficult_num(int difficult_num) {
            this.difficult_num = difficult_num;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(int accuracy) {
            this.accuracy = accuracy;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public String getSubjectid() {
            return subjectid;
        }

        public void setSubjectid(String subjectid) {
            this.subjectid = subjectid;
        }

        public int getEasy_num() {
            return easy_num;
        }

        public void setEasy_num(int easy_num) {
            this.easy_num = easy_num;
        }

        @Override
        public String toString() {
            return "total = " + total + "; normal_num = " + normal_num + "; difficult_num = " + difficult_num + "; accuracy = " + accuracy + "; right = " + right + "; subjectid = " + subjectid + "; easy_num = " + easy_num;
        }
    }
}