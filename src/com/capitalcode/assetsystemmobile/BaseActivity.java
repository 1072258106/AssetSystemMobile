package com.capitalcode.assetsystemmobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.async.AsyncTaskUtils;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.async.ProgressCallable;
import com.capitalcode.assetsystemmobile.model.BaseDataModel;
import com.capitalcode.assetsystemmobile.model.LoginDataModel;
//import com.fz.ilucky.view.DialogView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class BaseActivity extends Activity{
	public static Context context;

	//protected DialogView dialogView;
	protected Gson gson;
	protected Map<String, Object> param;
	//protected ProgressDialog pgView;
	protected ProgressDialog m_pDialog;
	protected String MenuId;

	public void onSearch()
	{

	}

	protected abstract void Init(Bundle paramBundle);

	protected abstract void AppInit();

	protected abstract void DataInit();

	protected abstract void Destroy();

	protected abstract void ViewInit();

	protected abstract void ViewListen();

	static String deviceId;
	static public HashMap<String, HashMap<String, Object>> home = new HashMap<String, HashMap<String, Object>>();

	static public LoginDataModel loginModel = null;
	static public BaseDataModel basedataModel = null;
	static public String mobile, pwd;

	static public Map<String,String> mapModule = new HashMap<String, String>();
	static public Map<String,String> mapAttchType = new HashMap<String, String>();
	static public Map<String,String> mapSearchToReg = new HashMap<String, String>();
	static public Map<String,String> mapIdToName = new HashMap<String, String>();

	public <T> void doAsync(CallEarliest<T> paramCallEarliest, Callable<T> paramCallable, Callback<T> paramCallback)
	{
		AsyncTaskUtils.doAsync(paramCallEarliest, paramCallable, paramCallback);
	}

	public <T> void doAsyncProgress(Context pContext, int styleID, String pTitle, String pMessage, CallEarliest<String> pCallEarliest, ProgressCallable<String> progressCallable, Callback<String> pCallback)
	{
		AsyncTaskUtils.doProgressAsync(pContext, styleID, pTitle, pMessage, pCallEarliest, progressCallable, pCallback);
	}

	protected void Prepare(Map<String, Object> param)
	{
		param.clear();
		param.put("MenuId", MenuId);
		param.put("UserId", loginModel.LoginUser.UserId);
		param.put("DeptId", loginModel.LoginUser.DeptId);
		param.put("PosId", loginModel.LoginUser.PosId);
		param.put("UserType", loginModel.LoginUser.UserType);


		Log.e("deviceId",deviceId);
		param.put("MacCode", /*"352315052327497"*/deviceId);
	}



	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		this.param = new HashMap<String,Object>();
		this.gson = new GsonBuilder().disableHtmlEscaping().create();
		MenuId = this.getIntent().getStringExtra("MenuId");

		if( deviceId == null )
		{
			deviceId = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}

//		Getui();
		Init(paramBundle);
		AppInit();
		ViewInit();

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		if( tv != null && this.getIntent().getStringExtra("title") != null )
		{
			tv.setText(this.getIntent().getStringExtra("title"));
		}
		ViewListen();
		DataInit();

		LinearLayout ll = (LinearLayout)this.findViewById(R.id.tv_back);
		if( ll != null )
		{
			ll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}

	}




	protected void onDestroy()
	{
		super.onDestroy();
		Destroy();
	}

	protected void onPause()
	{
		super.onPause();
	}

	protected void onRestart()
	{
		super.onRestart();
	}

	protected void onResume()
	{
		super.onResume();
	}

	protected void onStart()
	{
		super.onStart();
	}

	protected void onStop()
	{
		super.onStop();
	}




	/**
	 * 02. * bitmap转为base64 03. * @param bitmap 04. * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap,int rate) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, /*30*/rate, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}



	public static Bitmap decodeImg(String picStrInMsg) 
	{
		Bitmap bitmap = null;

		byte[] imgByte = null;
		InputStream input = null;
		try
		{
			imgByte = Base64.decode(picStrInMsg, Base64.DEFAULT);
			input = new ByteArrayInputStream(imgByte);


			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, options);

			// 计算 inSampleSize 的值
			options.inSampleSize = calculateInSampleSize(options, 100, 100);
			Log.e("inSampleSizeinSampleSizeinSampleSizeinSampleSize",options.inSampleSize+"");
			options.inJustDecodeBounds = false;
			SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(new ByteArrayInputStream(imgByte), null, options));
			bitmap = (Bitmap)softRef.get();








			/*		   
		       imgByte = Base64.decode(picStrInMsg, Base64.DEFAULT);
		       BitmapFactory.Options options=new BitmapFactory.Options();
		       options.inSampleSize = 10;
		       input = new ByteArrayInputStream(imgByte);
		       SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
		       bitmap = (Bitmap)softRef.get();
			 */	       
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(imgByte!=null)
			{
				imgByte = null;
			}

			if(input!=null)
			{
				try 
				{
					input.close();
				} 

				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return bitmap;

	}





	protected String getFileName() {
		String saveDir = Environment.getExternalStorageDirectory() + "/myPic";
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdir(); // 创建文件夹
		}

		String fileName = saveDir + "/"  + "temp.jpg";

		return fileName;
	}


	protected String getBase64() 
	{
		try
		{
			File  file = new File(getFileName());
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
			inputFile.close();
			return Base64.encodeToString(buffer,Base64.DEFAULT);
		}
		catch(Exception e)
		{
			return null;
		}
	}


	protected Bitmap getBitmap()
	{
		Bitmap bmp = null; 

		String filePath = getFileName();
		File file = new File(filePath);

		try
		{
			/*
				FileInputStream fis = new FileInputStream(file);
				BitmapFactory.Options options=new BitmapFactory.Options();
			    options.inJustDecodeBounds = false;
			    options.inSampleSize = 10;   //width，hight设为原来的十分一
			    bmp =BitmapFactory.decodeStream(fis,null,options);
			 */

			FileInputStream input = new FileInputStream(file);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, options);

			// 计算 inSampleSize 的值
			options.inSampleSize = calculateInSampleSize(options, 100, 100);
			Log.e("inSampleSizeinSampleSizeinSampleSizeinSampleSize",options.inSampleSize+"");
			options.inJustDecodeBounds = false;
			SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(new FileInputStream(file), null, options));
			bmp = (Bitmap)softRef.get();
		}
		catch(Exception e)
		{

		}

		return bmp;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
		// 原始图片的宽高
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
