package com.uguke.demo.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.uguke.android.app.GeneralActivity;
import com.uguke.android.app.SimpleActivity;
import com.uguke.android.app.SupportActivity;
import com.uguke.android.widget.CommonToolbar;

/**
 * @author LeiJue
 */
@SuppressLint("Registered")
public class ListActivity extends SimpleActivity {

//    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragmentBackground(R.color.background);
        //setNativeContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        //loadRootFragment(R.id.content, new TestFragment());

        //start(new SSFragment());


//        new CommonToolbar(this);

showLoading();

//        mToolbar.setTitle("测试标题");

//        startActivity(new Intent(this, MainActivity.class));

//        Log.e("数据","Mac地址：" +  NetworkUtils.getWifiMacAddress());
//        Log.e("数据","AndroidId：" +  DeviceUtils.getAndroidId(this));
//        Log.e("数据","BuildId：" +  DeviceUtils.getBuildId());
//        Log.e("数据","BuildId：" +  DeviceUtils.getModel());
//        Log.e("数据","BuildId：" +  DeviceUtils.getVendor());
//        Log.e("数据","BuildId：" +  NetworkUtils.getDomainAddress("192.168.1.1"));


//        OkGo.<String>get("https://m.iqiyi.com/dianshiju/")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        //Log.e("数据", response.body());
//
//                        String xpath= "//*[@id=\"app\"]/div[2]/div/div/div[2]/div[3]/section/ul/li[1]/div[2]/div";
//                        String doc = response.body();
//                        Log.e("数据", doc);
//                        JXDocument jxDocument = JXDocument.create(doc);
//
//                        List<Object> rs = jxDocument.sel(xpath);
//                        Log.e("数据", "长度" + rs.size() + "");
//                        for (Object o:rs){
//                            if (o instanceof Element){
//                                int index = ((Element) o).siblingIndex();
//                                System.out.println(index);
//                                Log.e("数据", "百度：" + index);
//                            }
//
//                            Log.e("数据", "百度：" + o.toString());
//                            System.out.println(o.toString());
//                        }
//                    }
//                });



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
