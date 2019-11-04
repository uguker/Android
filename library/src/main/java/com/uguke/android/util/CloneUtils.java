package com.uguke.android.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 克隆工具
 * @author LeiJue
 */
public class CloneUtils {


    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(Serializable obj) {
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    public static <T extends Parcelable> T clone(Parcelable obj) {
        Parcel parcel;
        try {
            parcel = Parcel.obtain();
            parcel.writeParcelable(obj, 0);
            parcel.setDataPosition(0);
            T t = parcel.readParcelable(obj.getClass().getClassLoader());
            parcel.recycle();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
