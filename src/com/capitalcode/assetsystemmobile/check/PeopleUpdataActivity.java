package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.R.layout;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.AssetSaveRetModel;
import com.capitalcode.assetsystemmobile.model.CheckModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PeopleUpdataActivity extends BaseActivity implements OnClickListener {


	private List<Map<String, Object>> listCheck = new ArrayList<Map<String, Object>>();
	ContentAdapter checkAdapter;
	String BatchId;
	String assetId;

	int HaveStock = 0;
	int NoStock = 0;
	int OverStock = 0;


	List<SaveCheckDataModel> listSave;
	List<MsAssetModel> MsAsset;

	@Override
	protected void Init(Bundle paramBundle) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void AppInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DataInit() {
		// TODO Auto-generated method stub

		Intent intent = getIntent();
		String result = intent.getStringExtra("assetscode");
		BatchId = intent.getStringExtra("BatchId");

		SharedPreferences sp = PeopleUpdataActivity.this.getSharedPreferences("peoplecheckdata", Context.MODE_PRIVATE);
		String strlist = sp.getString("check"+mobile, null);
		if( strlist != null )
		{
			listSave = gson
					.fromJson(
							strlist,
							new TypeToken<List<SaveCheckDataModel>>() {
							}.getType());

			for( SaveCheckDataModel model :  listSave )
			{
				if(model.MsStock.BatchId.equals(BatchId))
				{
					MsAsset = model.MsAsset;
					Log.i("MsAsset", "MsAsset="+MsAsset.toString());
					break;
				}
			}
		}
		getStockAssetObj("AssetCode", result);	
	}

	@Override
	protected void Destroy() {

	}

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(layout.activity_people_updata);

		Button btn = (Button)this.findViewById(R.id.btn_title_left);

		btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText("个人核查");

		setuplist(new MsAssetModel());
		ListView lv = (ListView) this.findViewById(R.id.people_listinfo);
		checkAdapter = new ContentAdapter(this, listCheck,
				this.basedataModel.StockLabel);
		lv.setAdapter(checkAdapter);

		btn = (Button)this.findViewById(R.id.people_btn_clear);
		btn.setOnClickListener(this);

		btn = (Button)this.findViewById(R.id.people_btn_check);
		btn.setOnClickListener(this);
		btn.setEnabled(false);

		BatchId = this.getIntent().getStringExtra("BatchId");
	}

	@Override
	protected void ViewListen() {

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{

		case R.id.people_btn_clear:
		{
			EditText edit = (EditText)this.findViewById(R.id.people_remarkvalue);
			edit.setText("");

			setuplist(new MsAssetModel());
			checkAdapter.notifyDataSetChanged();

			TextView tv = (TextView)PeopleUpdataActivity.this.findViewById(R.id.people_tvresult);
			tv.setText("");
			tv.setVisibility(View.GONE);
		}
		break;

		//盘点资产
		case R.id.people_btn_check:
		{
			//				saveImage();
			Map<String,Object> map = listCheck.get(0);
			String AssetCode = (String)map.get("value");

			map = listCheck.get(listCheck.size()-1);
			String SerialNumber = (String)map.get("value");

			if( AssetCode.length()>0 || SerialNumber.length() > 0 )
			{
				EditText edit = (EditText)this.findViewById(R.id.people_remarkvalue);
				String StockMemo = edit.getText().toString();
				if( assetId != null )
				{
					for( MsAssetModel model : MsAsset )
					{
						if( model.assetId.equals(assetId))
						{
							if( model.inventoryStateName.equals("未核查"))
							{
								//<---->
								//获取图片数据
								List<Map<String, String>> listImgGuid = new ArrayList<Map<String, String>>();
								for (Map<String, Object> map2 : listCheck) {
									String type = (String) map2.get("type");

									if (type.equals("pic")) {

										Bitmap bmp = (Bitmap) map2.get("image");
										if (bmp != null) {
											String base64 = bitmapToBase64(bmp, 70);
											Map<String, String> item = new HashMap<String, String>();
											item.put("ImageData", base64);
											listImgGuid.add(item);
										}
									}
								}
								Log.i("sss", ""+listImgGuid.size());
								model.ImgGuid = listImgGuid;
								//<---->
								model.inventoryStateName = "已核查";
								model.StockMemo = StockMemo;
								String saveStr = gson.toJson(listSave);

								SharedPreferences sp = PeopleUpdataActivity.this.getSharedPreferences("peoplecheckdata", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp.edit();

								Log.e("gson2",saveStr);

								editor.putString("check"+mobile, saveStr);

								editor.commit();

								Common.ShowInfo(context, "盘点成功!");
							}
							else
							{
								new AlertDialog.Builder(PeopleUpdataActivity.this).setTitle("提示")//设置对话框标题  		  
								.setMessage("该资产已盘点!")//设置显示的内容  
								.setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
									@Override  
									public void onClick(DialogInterface dialog, int which) 
									{//确定按钮的响应事件  

									}  
								}).show();//在按键响应事件中显示此对话框
							}
						}
					}
				}
			}
		}

		Button btn = (Button)PeopleUpdataActivity.this.findViewById(R.id.people_btn_check);
		btn.setEnabled(false);

		break;
		}
	}

	void setuplist(MsAssetModel model) {
		listCheck.clear();
		Log.e("enterenter","8888888888888888888888888888888888888");
		/*
		 * 
		 * 	
		 * 	public String assetId;
				public String assetCode;
				public String assetName;
				public String useDeptName;
				public String useUserName;
				public String stockStateName;
				public String str9;
		 * 
		 * 
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "assetCode");
		map.put("value", model.assetCode);
		map.put("readonly", "true");
		map.put("name", "资产编号");
		listCheck.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "assetName");
		map.put("value", model.assetName);
		map.put("readonly", "true");
		map.put("name", "资产名称");
		listCheck.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "useDeptName");
		map.put("value", model.useDeptName);	
		map.put("readonly", "true");
		map.put("name", "使用部门");
		listCheck.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "useUserName");
		map.put("value", model.useUserName);
		map.put("readonly", "true");
		map.put("name", "使用人员");
		listCheck.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "str9");
		map.put("value", model.str9);
		map.put("readonly", "true");
		map.put("name", "序列号");
		listCheck.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "资产图片");
		listCheck.add(map);

	}


	void getStockAssetObj(String type,String result)
	{
		assetId = null;
		if( "AssetCode".equals(type) )
		{
			for( MsAssetModel model : MsAsset )
			{
				if( model.assetCode.equals(result))
				{
					assetId = model.assetId;
					setuplist(model);
					checkAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
		else
		{
			for( MsAssetModel model : MsAsset )
			{
				if( model.str9.equals(result))
				{
					assetId = model.assetId;
					setuplist(model);
					checkAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
		Button btn = (Button)PeopleUpdataActivity.this.findViewById(R.id.people_btn_check);
		btn.setEnabled(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		switch (requestCode)
		{
		case 3: {
			Log.e("camera", "recv");

			Bitmap bmp = getBitmap();
			String base64 = getBase64();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", "pic");
			map.put("image", bmp);
			map.put("data", base64);
			listCheck.add(map);

			checkAdapter.notifyDataSetChanged();

			ListView lv = (ListView) this.findViewById(R.id.people_listinfo);

			lv.post(new Runnable() {
				@Override
				public void run() {
					// Select the last row so it will scroll into view...
					ListView lv = (ListView) PeopleUpdataActivity.this.findViewById(R.id.people_listinfo);
					lv.setSelection(checkAdapter.getCount() - 1);
				}
			});
		}
		break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	//保存图片
	private void saveImage(){

		List<Map<String, String>> listImgGuid = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map : listCheck) {
			String type = (String) map.get("type");

			if (type.equals("pic")) {

				Bitmap bmp = (Bitmap) map.get("image");
				if (bmp != null) {
					String base64 = bitmapToBase64(bmp, 70);
					Map<String, String> item = new HashMap<String, String>();
					item.put("ImageData", base64);
					listImgGuid.add(item);
				}
			}
		}

		//初始化param
		PeopleUpdataActivity.this.Prepare(param);
		param.put("MenuId", ""+0);
		if (listImgGuid.size() > 0) {
			param.put("ImgGuid", listImgGuid);
		}
		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(PeopleUpdataActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在保存图片数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSavePicUrl(MenuId),
						param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				if (res.equals("")) {

					m_pDialog.hide();
					Common.ShowInfo(context, "网络故障");
					return;
				}
				try {
					RequestRetModel<AssetSaveRetModel> model = gson.fromJson(res,
							new TypeToken<RequestRetModel<AssetSaveRetModel>>() {
					}.getType());
					if (model != null) {
						m_pDialog.hide();

						String ErrorMsg = model.response.ErrorMsg;
						if (ErrorMsg != null && ErrorMsg.length() > 0) {
							Common.ShowInfo(context, ErrorMsg);
						}

					} else {
						m_pDialog.hide();
						Common.ShowInfo(context, "网络异常");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {
					m_pDialog.hide();

				}
			}
		});
	}
}
