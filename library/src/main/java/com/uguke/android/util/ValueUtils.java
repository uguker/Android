package com.uguke.android.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;

/**
 * 常用工具
 * @author LeiJue
 */
public class ValueUtils {

    private ValueUtils() {
        throw new UnsupportedOperationException("can't instantiate me...");
    }

    public static <T> List<T> toList(T [] array) {
        List<T> list = new ArrayList<>();
        if (array != null) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public static <T> List<T> toList(Map<?, T> map) {
        List<T> list = new ArrayList<>();
        for (Object key : map.keySet()) {
            list.add(map.get(key));
        }
        return list;
    }

    public static <T> Map<Integer, T> toMap(T [] array) {
        if (array == null) {
            return new HashMap<>(1);
        }
        int length = array.length;
        Map<Integer, T> map = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            map.put(i, array[i]);
        }
        return map;
    }

    public static <T> Map<Integer, T> toMap(Collection<T> collection) {
        if (CheckUtils.isEmpty(collection)) {
            return new HashMap<>(1);
        }
        int length = collection.size();
        Map<Integer, T> map = new HashMap<>(length);
        int index = 0;
        for (T t : collection) {
            map.put(index, t);
            index ++;
        }
        return map;
    }

    public static <T> Collection<T> toCollection(T [] array) {
        return toMap(array).values();
    }

    public static <T> Collection<T> toCollection(Map<?, T> map) {
        if (map == null) {
            return new HashMap<Object, T>(1).values();
        }
        return map.values();
    }

    @SuppressWarnings("unchecked")
    public static <T> T [] toArray(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        int index = 0;
        Class clazz = collection.iterator().next().getClass();
        try {
            T [] array = (T[]) Array.newInstance(clazz, collection.size());
            for (T t : collection) {
                array[index] = t;
                index++;
            }
            return array;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static <T> T [] toArray(Map<?, T> map) {
        if (map == null) {
            return null;
        }
        return toArray(map.values());
    }

    public static void print(Object value) {
        print(ValueUtils.class.getSimpleName(), value);
    }

    public static void print(String tag, Object value) {
        if (CheckUtils.isEmpty(value)) {
            log(tag, "empty value.");
        } else if (value instanceof CharSequence) {
            log(tag, (String) value);
        } else if (value instanceof Collection) {
            Object [] array = ((Collection) value).toArray();
            for (int i = 0; i < array.length; i++) {
                log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
            }
        } else if (value instanceof Map) {
            Map map = (Map) value;
            for (Object key : map.keySet()) {
                log(tag, String.format(Locale.getDefault(), "key: %s | value: %s", String.valueOf(key), String.valueOf(map.get(key))));
            }
        } else if (value.getClass().isArray()) {
            Class clazz = value.getClass();
            if (clazz.equals(boolean [].class)) {
                boolean [] array = (boolean []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(char [].class)) {
                char [] array = (char []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(byte [].class)) {
                byte [] array = (byte []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(short [].class)) {
                int [] array = (int[]) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(int [].class)) {
                int [] array = (int[]) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(long [].class)) {
                long [] array = (long []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(float [].class)) {
                float [] array = (float []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else if (clazz.equals(double [].class)) {
                double [] array = (double []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            } else {
                Object [] array = (Object []) value;
                for (int i = 0; i < array.length; i++) {
                    log(tag, String.format(Locale.getDefault(), "index: %d | value: %s", i, String.valueOf(array[i])));
                }
            }
        } else {
            log(tag, String.valueOf(value));
        }
    }

    private static void log(String tag, String message) {
        Logger.getLogger(tag).info(message);
    }
}
