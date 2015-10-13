package com.cn.stepcounter;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by 科迪 on 2015/4/25.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        AVOSCloud.useAVCloudCN();
        // U need your AVOS key and so on to run the code.
        AVOSCloud.initialize(this,
                "DxyghogX5MvJgd9gneilVjLd",
                "Q9VATuKisrXjquR5O54dovVF");

      /*  AVObject myOrder = new AVObject("Order");
        myOrder.put("OrderNumber", "201505010001");
        myOrder.put("Route", AVObject.createWithoutData("Route","553cc981e4b04327dfd96c98"));
        myOrder.saveInBackground();*/


      /*  JSONArray myArray = new JSONArray();
        myArray.put("sgdghfghfhgd");
        myArray.put("sggfd2222hjhfjgh");



        AVObject myObject = new AVObject("DataTypeTest");

        myObject.put("myArray", myArray);
        myObject.saveInBackground();*/
  /*      AVObject record = new AVObject("UncheckedRoute");
        record.put("totalSeat", 1234);

        AVACL acl = new AVACL();
        acl.setReadAccess(AVUser.getCurrentUser(),true);   //此处设置的是所有人的可读权限
        acl.setWriteAccess(AVUser.getCurrentUser(), true);   //而这里设置了文件创建者的写权限

        record.setACL(acl);
        record.saveInBackground();*/


    }
}

