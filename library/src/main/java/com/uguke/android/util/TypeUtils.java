package com.uguke.android.util;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class TypeUtils {


    public static Type create(final Class clazz, final Class ...types) {
        return new ParameterizedType() {
            @NonNull
            @Override
            public Type[] getActualTypeArguments() {
                return types;
            }

            @NonNull
            @Override
            public Type getRawType() {
                return clazz;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
