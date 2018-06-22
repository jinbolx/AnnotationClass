package com.jinbolx.ioc;

import android.app.Activity;
import android.view.View;

public class InjectUtil {

    private static final String SUFFIX = "$$ViewInjector";

    public static void inject(Activity activity) {
        try {
            doInject(activity, activity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void inject(View source) {
        try {
            doInject(source, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void inject(Object target, View source) {
        try {
            doInject(target, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void doInject(Object target, Object source)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<?> c = target.getClass();
        String name = c.getName() + SUFFIX;
        Class proxyClass = Class.forName(name);
        ViewInjector viewInjector = (ViewInjector) proxyClass.newInstance();
        if (viewInjector != null) {
            viewInjector.inject(target, source);
        }
    }

}
