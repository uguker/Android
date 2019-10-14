package com.uguke.demo.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uguke.android.app.SupportActivity;
import com.uguke.android.util.DeviceUtils;
import com.uguke.android.util.NetworkUtils;

import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;

import java.util.List;
/**
 * @author LeiJue
 */
@SuppressLint("Registered")
public class ListActivity extends SupportActivity {

//    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setNativeContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        loadRootFragment(R.id.content, new TestFragment());


        start(new SSFragment());

        mToolbar.setTitle("测试标题");

        startActivity(new Intent(this, MainActivity.class));

        Log.e("数据","Mac地址：" +  NetworkUtils.getWifiMacAddress());
        Log.e("数据","AndroidId：" +  DeviceUtils.getAndroidId(this));
        Log.e("数据","BuildId：" +  DeviceUtils.getBuildId());
        Log.e("数据","BuildId：" +  DeviceUtils.getModel());
        Log.e("数据","BuildId：" +  DeviceUtils.getVendor());
        Log.e("数据","BuildId：" +  NetworkUtils.getDomainAddress("192.168.1.1"));


        OkGo.<String>get("https://m.iqiyi.com/dianshiju/")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Log.e("数据", response.body());

                        String xpath= "//*[@id=\"app\"]/div[2]/div/div/div[2]/div[3]/section/ul/li[1]/div[2]/div";
                        String doc = response.body();
                        Log.e("数据", doc);
                        JXDocument jxDocument = JXDocument.create(doc);
                        List<Object> rs = jxDocument.sel(xpath);
                        Log.e("数据", "长度" + rs.size() + "");
                        for (Object o:rs){
                            if (o instanceof Element){
                                int index = ((Element) o).siblingIndex();
                                System.out.println(index);
                                Log.e("数据", "百度：" + index);
                            }

                            Log.e("数据", "百度：" + o.toString());
                            System.out.println(o.toString());
                        }
                    }
                });


//        Observable.interval(1, TimeUnit.SECONDS)
//                //.compose(bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(along -> {
//                    tv.setText("sb");
//                    Log.e("数据", "定还是");
//                }, throwable -> {
//                    Log.e("数据", throwable.getMessage());
//                });

//        showTips("你好啊你好啊你好啊你好啊你好啊你");
//        showLoading();
//
//        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //
//    @Override
//    public LayoutCreator onCreateHeader(ViewGroup container) {
//        return LayoutCreator.create(R.layout.bottom, container, false);
//    }
//
//    @Override
//    public LayoutCreator onCreateFooter(ViewGroup container) {
//        return LayoutCreator.create(R.layout.bottom, container, false);
//    }
}
