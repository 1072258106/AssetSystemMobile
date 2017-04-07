package com.capitalcode.assetsystemmobile.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.SearchActivity;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.check.CheckActivity;
import com.capitalcode.assetsystemmobile.check.CheckMenuActivity;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.CheckModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.RegisterRetModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.RetAssetInfo;
import com.capitalcode.assetsystemmobile.model.RetAssetInfoModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.CurrentTime;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;

public class ReturnRegisterActivity extends BaseActivity {

	private List<Map<String,Object>> listRegister = new ArrayList<Map<String,Object>>();
	ContentAdapter registerAdapter;	

	ScrollAdapter adapter;
	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

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

	}

	@Override
	protected void Destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_return_register);

		Button btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", "choose");
		map.put("id", "RetUserId");
		map.put("name", "归还人");
		map.put("value", "");
		map.put("searchid", "useUser");/////////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String,Object>();
		map.put("type", "choose");
		map.put("id", "RetDt");
		map.put("value", "");
		map.put("name", "归还时间");
		map.put("searchid", "datepicker");///////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String,Object>();
		map.put("type", "edit");
		map.put("id", "RetMemo");
		map.put("name", "归还说明");
		map.put("value", "");
		listRegister.add(map);

		ListView lv = (ListView)this.findViewById(R.id.searchlist);
		registerAdapter = new ContentAdapter(this,listRegister);
		lv.setAdapter(registerAdapter);

		ListView mListView = (ListView) findViewById(R.id.scroll_list);
		adapter = new ScrollAdapter(this, datas, R.layout.item, new String[] {
				"data_0", "data_1", "data_2", "data_3", }, new int[] {
				R.id.item_data0, R.id.item_data1, R.id.item_data2,
				R.id.item_data3 });
		mListView.setAdapter(adapter);



	}


	void getcanretassetinfo(String AssetCode,String SerialNumber)
	{
		ReturnRegisterActivity.this.Prepare(param);




		Map<String,String> map = new HashMap<String,String>();
		map.put("AssetCode", AssetCode);
		map.put("SerialNumber", SerialNumber);
		map.put("Module", mapModule.get(MenuId));
		param.put("MsAsset", map);



		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(ReturnRegisterActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getcanretassetinfo(), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				m_pDialog.hide();
				if (res.equals("")) {

					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {
					RequestRetModel<RetAssetInfoModel> model = gson
							.fromJson(
									res,
									new TypeToken<RequestRetModel<RetAssetInfoModel>>() {
									}.getType());
					if (model != null) 
					{
						if( model.response.ErrorCode.equals("S00000") == false || model.rspcontent.MsAsset == null )
						{
							Common.ShowInfo(context, model.response.ErrorMsg);
							return;
						}

						for (RetAssetInfo item : model.rspcontent.MsAsset) 
						{

							boolean find = false;
							for( Map<String,String> one : datas )
							{
								String AssetId = one.get("AssetId");
								if( AssetId.equals(item.AssetId))
								{
									find = true;
									break;
								}
							}

							if(find == true)
							{
								continue;
							}






							Map<String, String> data = null;

							data = new HashMap<String, String>();

							data.put("AssetId", item.AssetId);
							data.put("BorRecId", item.BorRecId);
							data.put("data_" + 0, item.AssetCode);
							data.put("data_" + 1, item.AssetName);
							data.put("data_" + 2, item.Standard);
							data.put("data_" + 3, item.SerialNumber);

							datas.add(data);

						}

						adapter.notifyDataSetChanged();




					} 
					else 
					{

						Common.ShowInfo(context, "网络异常");
						Log.e("fail",
								"failfailfailfailfailfailfailfailfail");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {

					RequestRetModel<String> model = gson
							.fromJson(
									res,
									new TypeToken<RequestRetModel<String>>() {
									}.getType());


					Common.ShowInfo(context, model.rspcontent);



					Log.e("fail",
							localJsonSyntaxException.getMessage());
				}
			}
		});



	}





















	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		EditText edit = (EditText)this.findViewById(R.id.valuecode);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{  
					EditText edit = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuecode);
					String result = edit.getText().toString();
					edit.setText(result);
					edit.selectAll();


					if( result.length() > 0 )
					{
						EditText editsn = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuesn);
						editsn.setText("");

						getcanretassetinfo(result,null);

					} 
					//edit.clearFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(edit.getWindowToken(),0);   


					return true;  
				}  
				return false;  
			}


		});


		edit = (EditText)this.findViewById(R.id.valuesn);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{  
					EditText edit = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuesn);
					String result = edit.getText().toString();
					edit.setText(result);
					edit.selectAll();


					if( result.length() > 0 )
					{
						EditText editcode = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuecode);
						editcode.setText("");


						getcanretassetinfo(null,result);
					} 

					//edit.clearFocus(); 
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(edit.getWindowToken(),0); 
					return true;  
				}  
				return false;  
			}


		}); 




		Button btn = (Button) this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ReturnRegisterActivity.this,
						CaptureActivity.class);
				startActivityForResult(it, 1);
			}
		});

		btn = (Button) this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ReturnRegisterActivity.this,
						CaptureActivity.class);
				startActivityForResult(it, 2);
			}
		});

		//		btn = (Button) this.findViewById(R.id.btn_search);
		//		btn.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Intent intent = new Intent(ReturnRegisterActivity.this,
		//						SearchActivity.class);
		//
		//				intent.putExtra("title", "资产查询");
		//				intent.putExtra("MenuId", "21");
		//				intent.putExtra("Module", mapModule.get(MenuId));
		//				intent.putExtra("RealMenuId", MenuId);
		//
		//				ReturnRegisterActivity.this.startActivityForResult(intent, 4);
		//
		//			}
		//		});

		btn = (Button) this.findViewById(R.id.btn_save);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				commitForm("0");

			}

		});


	}

	void commitForm(String IsCheck)
	{
		ReturnRegisterActivity.this.Prepare(param);
		Map<String, Object> mapMsAsset = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
		String time = formatter.format(curDate);

		for (Map<String, Object> map : listRegister)
		{
			String id = (String) map.get("id");
			String realvalue;
			if( id.equals("RetUserId"))
			{
				realvalue = (String) map.get("realvalue");
				if( realvalue == null || realvalue.length() == 0 )
				{
					new AlertDialog.Builder(ReturnRegisterActivity.this)
					.setTitle("提示")// 设置对话框标题
					.setMessage("归还人" + "不能为空!")// 设置显示的内容
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {// 添加确定按钮
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {// 确定按钮的响应事件

						}
					}).show();// 在按键响应事件中显示此对话框
				}
			}

			mapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			
			if(id.equals("RetDt")){
				realvalue = (String) map.get("realvalue");
				Log.i("realvalue", "realvalue="+realvalue);
				if( realvalue == null || realvalue.length() == 0 ){
					realvalue = time;
					Log.i("time", "time="+time);
					mapMsAsset.put((String) map.get("id"), time);
				}
			}

			

		}

		if(datas.size() == 0 )
		{
			new AlertDialog.Builder(ReturnRegisterActivity.this)
			.setTitle("提示")// 设置对话框标题
			.setMessage("资产信息不能为空!")// 设置显示的内容
			.setPositiveButton("是",
					new DialogInterface.OnClickListener() {// 添加确定按钮
				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {// 确定按钮的响应事件

				}
			}).show();// 在按键响应事件中显示此对话框


			return;
		}


		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		mapMsAsset.put("BorRetRel", list);
		for (Map<String, String> item : datas) {
			Map<String, String> MsBorrowRel = new HashMap<String, String>();
			String AssetId = item.get("AssetId");
			String BorRecId = item.get("BorRecId");

			MsBorrowRel.put("AssetId", AssetId);
			MsBorrowRel.put("BorRecId", BorRecId);

			list.add(MsBorrowRel);
		}

		mapMsAsset.put("IsCheck", IsCheck);
		param.put("MsRet", mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(ReturnRegisterActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在保存数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getSaveAssetUrl(MenuId), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				m_pDialog.hide();
				if (res.equals("")) {

					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {
					JSONObject respJson = new JSONObject(res);

					JSONObject response = respJson
							.getJSONObject("response");

					String ErrorCode = response.getString("ErrorCode");
					if( response.getString("ErrorMsg") != null )
					{
						Common.ShowInfo(context, response.getString("ErrorMsg"));
					}
					if( ErrorCode.equals("S00000") == true )
					{
						Button btn = (Button)findViewById(R.id.btn_save);
						btn.setEnabled(false);

					}






				} catch (JSONException e) {


				}
			}
		});





	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 1: {
				EditText editsn = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuesn);
				editsn.setText("");

				EditText editcode = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuecode);
				editcode.setText(data.getStringExtra("result"));

				
				getcanretassetinfo(data.getStringExtra("result"),null);

			}
			break;
			case 2: {
				EditText editcode = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuecode);
				editcode.setText("");

				EditText editsn = (EditText)ReturnRegisterActivity.this.findViewById(R.id.valuesn);
				editsn.setText(data.getStringExtra("result"));

				
				getcanretassetinfo(null,data.getStringExtra("result"));

			}
			break;
			}
		}
	}




























}
