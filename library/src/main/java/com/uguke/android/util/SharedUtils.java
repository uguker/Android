package com.uguke.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * SharedPreferences工具类
 * @author LeiJue
 */
public final class SharedUtils {

    public static SharedUtils create(Context context) {
        return new SharedUtils(context);
    }

    public static SharedUtils create(Context context, String name) {
        return new SharedUtils(context, name);
    }

    public static SharedUtils create(Context context, String name, int mode) {
        return new SharedUtils(context, name, mode);
    }

	private SharedPreferences shared;

	private SharedUtils(Context context) {
	    this(context, context.getPackageName());
    }

    private SharedUtils(Context context, String name) {
        this(context, name, Context.MODE_PRIVATE);
    }

    private SharedUtils(Context context, String name, int mode) {
        this.shared = context.getSharedPreferences(name, mode);
    }

    public SharedUtils put(String key, boolean value) {
        shared.edit().putBoolean(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, int value) {
        shared.edit().putInt(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, long value) {
        shared.edit().putLong(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, float value) {
        shared.edit().putFloat(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, String value) {
        shared.edit().putString(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, Set<String> value) {
        shared.edit().putStringSet(key, value).apply();
        return this;
    }

    public SharedUtils put(String key, Serializable value) {
        ByteArrayOutputStream aOs = new ByteArrayOutputStream();
        ObjectOutputStream bOs;
        try {
            bOs = new ObjectOutputStream(aOs);
            bOs.writeObject(value);
            String objectVal = new String(Base64.encode(
                    aOs.toByteArray(), Base64.NO_WRAP));
            shared.edit().putString(key, objectVal).apply();
            bOs.close();
            aOs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean getBoolean(String key) {
        return shared.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return shared.getBoolean(key, defValue);
    }

	public int getInt(String key) {
        return shared.getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return shared.getInt(key, defValue);
    }

    public long getLong(String key) {
        return shared.getLong(key, 0);
    }

    public long getLong(String key, long defValue) {
	    return shared.getLong(key, defValue);
    }

    public Float getFloat(String key) {
        return shared.getFloat(key, 0);
    }

    public Float getFloat(String key, float defValue) {
	    return shared.getFloat(key, defValue);
    }

    public String getString(String key) {
        return shared.getString(key, "");
    }

    public String getString(String key, String defValue) {
	    return shared.getString(key, defValue);
    }

    public Set<String> getStringSet(String key) {
        return shared.getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return shared.getStringSet(key, defValue);
    }

    public Serializable getSerializable(String key) {
        Serializable obj = null;
        try {
            String temp = shared.getString(key, "");
            byte[] buffer = Base64.decode(temp, Base64.NO_WRAP);
            ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);

            ObjectInputStream oIn = null;
            if (bIn.available() != 0) {
                oIn = new ObjectInputStream(bIn);
                obj = (Serializable) oIn.readObject();
            }
            if (oIn != null) {
                oIn.close();
            }
            bIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void remove(String key) {
        shared.edit().remove(key).apply();
    }

    public void removeAll() {
        shared.edit().clear().apply();
    }

}
