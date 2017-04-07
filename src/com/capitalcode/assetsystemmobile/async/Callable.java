package com.capitalcode.assetsystemmobile.async;
public abstract interface Callable<T>
{
  public abstract T call()
    throws Exception;
}
