package com.cqray.demo.android;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.cqray.android.app.SupportActivity;
import com.cqray.android.widget.CommonToolbar;
import com.google.gson.reflect.TypeToken;

import org.jsoup.parser.Token;

import java.util.Date;
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
        setDefaultFragmentBackground(R.color.background);
        //setNativeContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        //loadRootFragment(R.id.content, new TestFragment());

        //start(new SSFragment());
        mToolbar.setRippleEnable(true)
                //.setBackIcon(R.drawable.def_back_material_dark)
                .setActionVisible(0, true)
                .setActionIcon(1, R.drawable.widget_ic_empty)
                .setActionText(0, "你好")
                .setActionTextBold(true)
                .setActionTextColor(Color.WHITE)
                //.setActionTextSize(18)
                .setBackText("返回")
                .setBackIconVisible(true)
                .setBackTextVisible(true)
                .setTitle("你好啊")
                .setActionVisible(1, true)
                //.setTitleSpace(16)
                //.setBackSpace(0)
                .setTitleGravity(CommonToolbar.CENTER)
                .setActionText(1, "6666");

        Log.e("数据", "" + TimeUtils.getChineseWeek(new Date()) + "|" + TimeUtils.getUSWeek(new Date()));

        Log.e("数据", "" + 0);

        //new TypeToken<List<String>>(){}.getType();

        List<SDCardUtils.SDCardInfo> list = SDCardUtils.getSDCardInfo();
        for (SDCardUtils.SDCardInfo sdi : list) {
            Log.e("数据", "" + sdi.getPath());
        }


//        showError();
//        mLoadingLayout
//                .setErrorText("错误信息")
//                .setErrorImage(R.mipmap.ic_launcher)
//                .setRetryText("点击");

//        mLoadingLayout
//                .setEmptyText("暂无充值记录")
//                .setEmptyMargin(16)
//                .showLoading();

        mLoadingLayout.showContent();

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
