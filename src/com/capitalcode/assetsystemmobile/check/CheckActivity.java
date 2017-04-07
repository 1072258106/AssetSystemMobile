package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.ElectronicSignatureActivity;
import com.capitalcode.assetsystemmobile.MainActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.business.AssetAddActivity;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetSaveRetModel;
import com.capitalcode.assetsystemmobile.model.CheckModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.MsStockModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckModel;
import com.capitalcode.assetsystemmobile.model.StockRightCodeModel;
import com.capitalcode.assetsystemmobile.utils.Address;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;

//盘点界面
public class CheckActivity extends BaseActivity implements View.OnClickListener{

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
		if( MenuId.equals("offline_check"))
		{
			SharedPreferences sp = CheckActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
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


				HaveStock = 0;
				NoStock = 0;
				OverStock = 0;

				for( MsAssetModel model : MsAsset )
				{
					if( model.inventoryStateName.equals("已盘"))
					{
						HaveStock++;
					}
					else if( model.inventoryStateName.equals("盘亏"))
					{
						NoStock++;
					}
					else
					{
						OverStock++;
					}
				}

				String show ="已盘:"+HaveStock+" 盘亏:"+NoStock+" 盘盈:"+OverStock;
				TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
				tv.setText(show);
				tv.setVisibility(View.VISIBLE);

			}


		}

	}

	@Override
	protected void Destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_check);

		Button btn = (Button)this.findViewById(R.id.btn_title_left);

		btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText("资产盘点");

		setuplist(new MsAssetModel());
		ListView lv = (ListView) this.findViewById(R.id.listinfo);
		checkAdapter = new ContentAdapter(this, listCheck,
				this.basedataModel.StockLabel);
		lv.setAdapter(checkAdapter);

		btn = (Button)this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(this);

		btn = (Button)this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(this);

		btn = (Button)this.findViewById(R.id.btn_clear);
		btn.setOnClickListener(this);

		btn = (Button)this.findViewById(R.id.btn_check);
		btn.setOnClickListener(this);
		btn.setEnabled(false);

		BatchId = this.getIntent().getStringExtra("BatchId");
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		EditText edit = (EditText)this.findViewById(R.id.codevalue);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED )
				{  
					EditText edit = (EditText)CheckActivity.this.findViewById(R.id.codevalue);
					String result = edit.getText().toString();

					if( result.length() > 0 )
					{

						getStockAssetObj("AssetCode", result);

					} 

					//edit.clearFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(edit.getWindowToken(),0);   


					return true;  
				}  
				return false;  
			}


		});


		edit = (EditText)this.findViewById(R.id.snvalue);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED )
				{  
					EditText edit = (EditText)CheckActivity.this.findViewById(R.id.snvalue);
					String result = edit.getText().toString();

					if( result.length() > 0 )
					{

						getStockAssetObj("SerialNumber", result);

					} 

					//edit.clearFocus(); 
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(edit.getWindowToken(),0); 
					return true;  
				}  
				return false;  
			}


		}); 



	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.btn_scan_code:
		{
			Intent it = new Intent(this, CaptureActivity.class);
			startActivityForResult(it, 1);

		}
		break;

		case R.id.btn_scan_sn:
		{
			Intent it = new Intent(this, CaptureActivity.class);
			startActivityForResult(it, 2);

		}
		break;

		case R.id.btn_clear:
		{
			EditText edit = (EditText)this.findViewById(R.id.codevalue);
			edit.setText("");

			edit = (EditText)this.findViewById(R.id.snvalue);
			edit.setText("");

			edit = (EditText)this.findViewById(R.id.remarkvalue);
			edit.setText("");


			setuplist(new MsAssetModel());
			checkAdapter.notifyDataSetChanged();

			TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvresult);
			tv.setText("");
			tv.setVisibility(View.GONE);

			if( MenuId.equals("online_check"))
			{
				tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
				tv.setText("");
				tv.setVisibility(View.GONE);
			}

			Button btn = (Button)CheckActivity.this.findViewById(R.id.btn_check);
			btn.setEnabled(false);
		}
		break;

		//盘点资产
		case R.id.btn_check:
		{
			//			saveImage();
			Map<String,Object> map = listCheck.get(0);
			String AssetCode = (String)map.get("value");

			map = listCheck.get(listCheck.size()-1);
			String SerialNumber = (String)map.get("value");

			if( AssetCode.length()>0 || SerialNumber.length() > 0 )
			{
				EditText edit = (EditText)this.findViewById(R.id.remarkvalue);
				String StockMemo = edit.getText().toString();

				if( MenuId.equals("online_check"))
				{

					CheckActivity.this.Prepare(param);
					if( MenuId.equals("online_check")  )
					{
						param.put("MenuId", "419");
					}
					else
					{
						param.put("MenuId", "104");
					}

					Map<String,String> item = new HashMap<String,String>();
					item.put("BatchId", BatchId);
					item.put("AssetCode", AssetCode);
					item.put("SerialNumber", SerialNumber);
					item.put("StockMemo", StockMemo);				

					param.put("MsStock", item);
					param.put("StockRightCode", CheckMenuActivity.StockRightCode);


					doAsync(new CallEarliest<String>() {
						public void onCallEarliest() throws Exception {

							m_pDialog = new ProgressDialog(CheckActivity.this);
							m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							m_pDialog.setMessage("正在提交数据...");
							m_pDialog.setCancelable(false);
							m_pDialog.show();

						}
					}, new Callable<String>() {
						public String call() throws Exception {
							return HttpHelper.getInstance(context).Post(
									ApiAddressHelper.getSaveStockAssetObj(), param);
						}
					}, new Callback<String>() {
						public void onCallback(String res) {
							m_pDialog.hide();
							if (res.equals("")) {

								Common.ShowInfo(context, "网络故障");
								return;
							}

							try
							{
								JSONObject respJson = new JSONObject(res);

								JSONObject response = respJson
										.getJSONObject("response");

								String ErrorCode = response.getString("ErrorCode");
								if( ErrorCode.equals("S00000") == false )
								{
									if( response.getString("ErrorMsg") != null )
									{
										Common.ShowInfo(context, response.getString("ErrorMsg"));
									}

									return;
								}

								RequestRetModel<SaveCheckModel> model = gson
										.fromJson(
												res,
												new TypeToken<RequestRetModel<SaveCheckModel>>() {
												}.getType());

								if( model.response.ErrorMsg != null )
								{
									Common.ShowInfo(context, model.response.ErrorMsg);
								}
								else if( model.response.ErrorCode.equals("S00000") )
								{
									Common.ShowInfo(context, "方法执行成功");
								}

								if (model != null) 
								{
									TextView tvresult = (TextView)CheckActivity.this.findViewById(R.id.tvresult);
									tvresult.setText(model.rspcontent.StockResult);
									tvresult.setVisibility(View.VISIBLE);

									if( model.rspcontent.MsStock != null  )
									{
										String show ="已盘:"+model.rspcontent.MsStock.HaveStock+" 盘亏:"+model.rspcontent.MsStock.NoStock+" 盘盈:"+model.rspcontent.MsStock.OverStock;
										TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
										tv.setText(show);
										tv.setVisibility(View.VISIBLE);
									}

								} 
								else 
								{

									Common.ShowInfo(context, "网络异常");
									Log.e("fail",
											"failfailfailfailfailfailfailfailfail");
								}
							} catch (JsonSyntaxException localJsonSyntaxException) {

								Log.e("fail",
										localJsonSyntaxException.getMessage());
							}
							catch (JSONException localJsonSyntaxException) {

								Log.e("fail",
										localJsonSyntaxException.getMessage());
							}
						}
					});

				}
				else if( MenuId.equals("offline_check"))
				{

					if( assetId != null )
					{
						for( MsAssetModel model : MsAsset )
						{
							if( model.assetId.equals(assetId))
							{
								if( model.inventoryStateName.equals("盘亏"))
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


									model.inventoryStateName = "已盘";
									model.StockMemo = StockMemo;
									String saveStr = gson.toJson(listSave);

									SharedPreferences sp = CheckActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sp.edit();

									Log.e("gson2",saveStr);

									editor.putString("check"+mobile, saveStr);

									editor.commit();

									Common.ShowInfo(context, "盘点成功!");

									HaveStock++;
									NoStock--;



									String show ="已盘:"+HaveStock+" 盘亏:"+NoStock+" 盘盈:"+OverStock;
									TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
									tv.setText(show);
									tv.setVisibility(View.VISIBLE);	
								}
								else
								{
									new AlertDialog.Builder(CheckActivity.this).setTitle("提示")//设置对话框标题  		  
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
					else
					{

						MsAssetModel model = new MsAssetModel();
						model.assetCode = AssetCode;
						model.str9 = SerialNumber;
						model.inventoryStateName = "盘盈";
						model.StockMemo = StockMemo;

						MsAsset.add(model);

						String saveStr = gson.toJson(listSave);

						SharedPreferences sp = CheckActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();

						Log.e("gson2",saveStr);

						editor.putString("check"+mobile, saveStr);

						editor.commit();

						Common.ShowInfo(context, "盘点成功!");

						OverStock++;

						String show ="已盘:"+HaveStock+" 盘亏:"+NoStock+" 盘盈:"+OverStock;
						TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
						tv.setText(show);
						tv.setVisibility(View.VISIBLE);	
					}


				}


			}

			Button btn = (Button)CheckActivity.this.findViewById(R.id.btn_check);
			btn.setEnabled(false);

		}
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

		//		if (listImgGuid != null) {
		//			for (Object base64 : listImgGuid) {
		//
		//				Bitmap bmp = decodeImg((String) base64);
		//				if (bmp != null) {
		//					Map<String, Object> map = new HashMap<String, Object>();
		//					map.put("type", "pic");
		//					map.put("image", bmp);
		//					map.put("data", (String) base64);
		//					listFix.add(map);
		//				}
		//			}
		//		}
	}


	void getStockAssetObj(String type,String result)
	{
		TextView tvresult = (TextView)CheckActivity.this.findViewById(R.id.tvresult);
		tvresult.setText("");
		tvresult.setVisibility(View.GONE);

		if( type.equals("AssetCode"))
		{
			EditText edit = (EditText)CheckActivity.this.findViewById(R.id.snvalue);
			edit.setText("");

			edit = (EditText)CheckActivity.this.findViewById(R.id.codevalue);
			edit.setText(result);
			edit.selectAll();
		}
		else
		{
			EditText edit = (EditText)CheckActivity.this.findViewById(R.id.codevalue);
			edit.setText("");

			edit = (EditText)CheckActivity.this.findViewById(R.id.snvalue);
			edit.setText(result);
			edit.selectAll();
		}

		if( MenuId.equals("online_check") )
		{

			CheckActivity.this.Prepare(param);
			if( MenuId.equals("online_check")  )
			{
				param.put("MenuId", "419");
			}
			else
			{
				param.put("MenuId", "104");
			}

			Map<String,String> map = new HashMap<String,String>();
			map.put("BatchId", BatchId);
			map.put(type, result);

			param.put("MsStock", map);
			param.put("StockRightCode", CheckMenuActivity.StockRightCode);


			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(CheckActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在获取数据...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(
							ApiAddressHelper.getStockAssetObj(), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}

					try {
						RequestRetModel<CheckModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<CheckModel>>() {
										}.getType());
						if (model != null) 
						{
							if( model.rspcontent.MsAsset != null && model.rspcontent.MsAsset.assetId != null && model.rspcontent.MsAsset.assetId.length()>0 )
							{
								assetId = model.rspcontent.MsAsset.assetId;
								setuplist(model.rspcontent.MsAsset);
								checkAdapter.notifyDataSetChanged();
							}
							else
							{
								assetId = null;
								MsAssetModel blankmodel = new MsAssetModel();
								EditText edit = (EditText)CheckActivity.this.findViewById(R.id.codevalue);
								blankmodel.assetCode = edit.getText().toString();
								edit = (EditText)CheckActivity.this.findViewById(R.id.snvalue);
								blankmodel.str9 = edit.getText().toString();

								setuplist(blankmodel);
								checkAdapter.notifyDataSetChanged();


								Common.ShowInfo(context, "未查到资产");
							}

							if( model.rspcontent.MsStock != null  )
							{
								String show ="已盘:"+model.rspcontent.MsStock.HaveStock+" 盘亏:"+model.rspcontent.MsStock.NoStock+" 盘盈:"+model.rspcontent.MsStock.OverStock;
								TextView tv = (TextView)CheckActivity.this.findViewById(R.id.tvstate);
								tv.setText(show);
								tv.setVisibility(View.VISIBLE);

							}

						} 
						else 
						{

							Common.ShowInfo(context, "网络异常");
							Log.e("fail",
									"failfailfailfailfailfailfailfailfail");
						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail",
								localJsonSyntaxException.getMessage());
					}
				}
			});

		}
		else
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

			if( assetId == null )
			{
				MsAssetModel blankmodel = new MsAssetModel();
				EditText edit = (EditText)CheckActivity.this.findViewById(R.id.codevalue);
				blankmodel.assetCode = edit.getText().toString();
				edit = (EditText)CheckActivity.this.findViewById(R.id.snvalue);
				blankmodel.str9 = edit.getText().toString();

				setuplist(blankmodel);
				checkAdapter.notifyDataSetChanged();


				Common.ShowInfo(context, "未查到资产");
			}



		}

		Button btn = (Button)CheckActivity.this.findViewById(R.id.btn_check);
		btn.setEnabled(true);


	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		switch (requestCode)
		{
		case 1:
			if (data != null)
			{
				String result = data.getStringExtra("result");
				getStockAssetObj("AssetCode", result);
			}
			break;

		case 2:
			if (data != null)
			{
				String result = data.getStringExtra("result");
				getStockAssetObj("SerialNumber", result);
			}
			break;

		case 3: {
			Log.e("camera", "recv");
			// Bitmap bmp = (Bitmap) data.getExtras().get("data");
			// ;

			Bitmap bmp = getBitmap();
			String base64 = getBase64();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", "pic");
			map.put("image", bmp);
			map.put("data", base64);
			listCheck.add(map);

			checkAdapter.notifyDataSetChanged();

			ListView lv = (ListView) this.findViewById(R.id.listinfo);

			lv.post(new Runnable() {
				@Override
				public void run() {
					// Select the last row so it will scroll into view...
					ListView lv = (ListView) CheckActivity.this.findViewById(R.id.listinfo);
					lv.setSelection(checkAdapter.getCount() - 1);
				}
			});
		}
		break;
		default:
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
		CheckActivity.this.Prepare(param);
		param.put("MenuId", ""+0);
		if (listImgGuid.size() > 0) {
			param.put("ImgGuid", listImgGuid);
		}
		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(CheckActivity.this);
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
