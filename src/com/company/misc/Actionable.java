package com.company.misc;


public interface Actionable<K> {

    public void onCompleted(K k);

    public void onError(K k, Throwable e);

}
