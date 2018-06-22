package com.jinbolx.ioc;

public interface ViewInjector<T> {
    void inject(T target,Object source);
}
