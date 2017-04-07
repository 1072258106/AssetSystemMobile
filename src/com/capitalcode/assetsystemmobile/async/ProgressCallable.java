package com.capitalcode.assetsystemmobile.async;

public abstract interface ProgressCallable<T>
{
  public abstract T call(IProgressListener paramIProgressListener)
    throws Exception;
}
