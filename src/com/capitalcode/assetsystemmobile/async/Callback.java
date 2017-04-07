package com.capitalcode.assetsystemmobile.async;

import org.json.JSONException;

public abstract interface Callback<T>
{
  public abstract void onCallback(T paramT) throws JSONException;
}

