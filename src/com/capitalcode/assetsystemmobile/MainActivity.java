package com.capitalcode.assetsystemmobile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.capitalcode.assetsystemmobile.adapter.GridViewAdapter;
import org.apache.http.ParseException;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.business.AssetBrowseActivity;
import com.capitalcode.assetsystemmobile.check.CheckMenuActivity;
import com.capitalcode.assetsystemmobile.model.BaseDataModel;
import com.capitalcode.assetsystemmobile.model.CategoryModel;
import com.capitalcode.assetsystemmobile.model.AssetHistoryModel;
import com.capitalcode.assetsystemmobile.model.LoginDataModel;
import com.capitalcode.assetsystemmobile.model.MenuModel;
import com.capitalcode.assetsystemmobile.model.SupplierModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.SerializableList;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;
import com.capitalcode.assetsystemmobile.utils.StaticUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;

import com.wyy.twodimcode.CaptureActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	//	private RoundImageView img;
	private Button scan;

	//设置全屏显示
	private void full(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	void getBaseDada(String strBase) {
		try {
			basedataModel = new BaseDataModel();

			JSONObject respJson = new JSONObject(strBase);

			JSONObject basedata = respJson.getJSONObject("BaseData");

			JSONObject PageLabel = basedata.getJSONObject("PageLabel");
			{
				Iterator keys = PageLabel.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = PageLabel.get(key).toString();

					basedataModel.PageLabel.put(key, value);
				}
			}

			basedataModel.AssetName = basedata.getString("AssetName");

			basedataModel.Standard = basedata.getString("Standard");

			JSONArray Category = basedata.getJSONArray("Category");
			String strCategory = Category.toString();

			basedataModel.Category = gson.fromJson(strCategory,
					new TypeToken<List<CategoryModel>>() {
			}.getType());

			JSONArray AssetType = basedata.getJSONArray("AssetType");
			Map<String, String> itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("AssetType", itemmap);

			for (int i = 0; i < AssetType.length(); i++) {
				JSONObject item = AssetType.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray AddMode = basedata.getJSONArray("AddMode");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("AddMode", itemmap);
			for (int i = 0; i < AddMode.length(); i++) {
				JSONObject item = AddMode.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray MeasureUnit = basedata.getJSONArray("MeasureUnit");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("MeasureUnit", itemmap);
			for (int i = 0; i < MeasureUnit.length(); i++) {
				JSONObject item = MeasureUnit.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray Status = basedata.getJSONArray("Status");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("Status", itemmap);
			for (int i = 0; i < Status.length(); i++) {
				JSONObject item = Status.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray EquipmentSource = basedata
					.getJSONArray("EquipmentSource");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("EquipmentSource", itemmap);
			for (int i = 0; i < EquipmentSource.length(); i++) {
				JSONObject item = EquipmentSource.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray UseType = basedata.getJSONArray("UseType");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("UseType", itemmap);
			for (int i = 0; i < UseType.length(); i++) {
				JSONObject item = UseType.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray RepairLevel = basedata.getJSONArray("RepairLevel");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("RepairLevel", itemmap);
			for (int i = 0; i < RepairLevel.length(); i++) {
				JSONObject item = RepairLevel.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray Supplier = basedata.getJSONArray("Supplier");
			String strSupplier = Supplier.toString();
			basedataModel.mapList.put("Supplier", (List<SupplierModel>) gson
					.fromJson(strSupplier,
							new TypeToken<List<SupplierModel>>() {
					}.getType()));
			/*
			 * basedataModel.Supplier = gson.fromJson(strSupplier, new
			 * TypeToken<List<SupplierModel>>() { }.getType());
			 */

			JSONArray EquipmentFactory = basedata
					.getJSONArray("EquipmentFactory");
			String strEquipmentFactory = EquipmentFactory.toString();
			basedataModel.mapList.put("EquipmentFactory",
					(List<SupplierModel>) gson.fromJson(strEquipmentFactory,
							new TypeToken<List<SupplierModel>>() {
					}.getType()));
			/*
			 * basedataModel.EquipmentFactory =
			 * gson.fromJson(strEquipmentFactory, new
			 * TypeToken<List<SupplierModel>>() { }.getType());
			 */

			JSONArray RepairFactory = basedata.getJSONArray("RepairFactory");
			String strRepairFactory = RepairFactory.toString();
			basedataModel.mapList.put("RepairFactory",
					(List<SupplierModel>) gson.fromJson(strRepairFactory,
							new TypeToken<List<SupplierModel>>() {
					}.getType()));
			/*
			 * basedataModel.RepairFactory = gson.fromJson(strRepairFactory, new
			 * TypeToken<List<SupplierModel>>() { }.getType());
			 */

			JSONArray ApplyState = basedata.getJSONArray("ApplyState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("ApplyState", itemmap);
			for (int i = 0; i < ApplyState.length(); i++) {
				JSONObject item = ApplyState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray RevertState = basedata.getJSONArray("RevertState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("RevertState", itemmap);
			for (int i = 0; i < RevertState.length(); i++) {
				JSONObject item = RevertState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray MoveState = basedata.getJSONArray("MoveState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("MoveState", itemmap);
			for (int i = 0; i < MoveState.length(); i++) {
				JSONObject item = MoveState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray BorrowState = basedata.getJSONArray("BorrowState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("BorrowState", itemmap);
			for (int i = 0; i < BorrowState.length(); i++) {
				JSONObject item = BorrowState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray RepairState = basedata.getJSONArray("RepairState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("RepairState", itemmap);
			for (int i = 0; i < RepairState.length(); i++) {
				JSONObject item = RepairState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray ScrapState = basedata.getJSONArray("ScrapState");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("ScrapState", itemmap);
			for (int i = 0; i < ScrapState.length(); i++) {
				JSONObject item = ScrapState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONArray ScrapType = basedata.getJSONArray("ScrapType");
			itemmap = new HashMap<String, String>();
			basedataModel.mapType.put("ScrapType", itemmap);
			for (int i = 0; i < ScrapType.length(); i++) {
				JSONObject item = ScrapType.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					itemmap.put(key, value);
				}
			}

			JSONObject StockLabel = basedata.getJSONObject("StockLabel");
			{
				Iterator keys = StockLabel.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = StockLabel.get(key).toString();

					basedataModel.StockLabel.put(key, value);
				}
			}

			JSONArray AssetStockState = basedata
					.getJSONArray("AssetStockState");
			for (int i = 0; i < AssetStockState.length(); i++) {
				JSONObject item = AssetStockState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					basedataModel.AssetStockState.put(key, value);
				}
			}

			JSONArray StockState = basedata.getJSONArray("StockState");
			for (int i = 0; i < StockState.length(); i++) {
				JSONObject item = StockState.getJSONObject(i);

				Iterator keys = item.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = item.get(key).toString();

					basedataModel.StockState.put(key, value);
				}
			}

			/*
			 * JSONArray TimeType = basedata.getJSONArray("TimeType"); if(
			 * TimeType != null ) { for (int i = 0; i < TimeType.length(); i++)
			 * { JSONObject item = TimeType.getJSONObject(i);
			 * 
			 * Iterator keys = item.keys(); while (keys.hasNext()) { String key
			 * = (String) keys.next(); String value = item.get(key).toString();
			 * 
			 * basedataModel.TimeType.put(key, value); } } }
			 */

		} catch (ParseException e) {
			// m_pDialog.hide();
			Log.e("parse", "CcMsgSender 2 ParseException: " + e.toString());
		} catch (JSONException e) {
			// m_pDialog.hide();
			Log.e("parse", "CcMsgSender 2 JSONException: " + e.toString());
		}

		if (m_pDialog != null) {
			m_pDialog.hide();
		}
		updateMain();
	}

	@Override
	protected void AppInit() {
		// TODO Auto-generated method stub

		SharedPreferences sp = this.getSharedPreferences("logindata",
				Context.MODE_PRIVATE);

		String saveDir = Environment.getExternalStorageDirectory() + "/webapi";
		File dir = new File(saveDir);
		if (dir.exists()) {
			String fileName = saveDir + "/" + "webapi.txt";

			try {
				File file = new File(fileName);
				FileInputStream inputFile = new FileInputStream(file);
				byte[] buffer = new byte[inputFile.available()];
				inputFile.read(buffer);

				if (buffer.length > 0) {
					ApiAddressHelper.SERVERHOST = EncodingUtils.getString(
							buffer, "UTF-8").trim();

					SharedPreferences.Editor editor = sp.edit();
					editor.putString( "webapi", ApiAddressHelper.SERVERHOST);
					editor.commit();
					Log.e("fileapi", ApiAddressHelper.SERVERHOST);
				}
				else
				{
					ApiAddressHelper.SERVERHOST = sp.getString("webapi", "http://58.134.112.6");
				}

				inputFile.close();

			} catch (Exception e) {

				ApiAddressHelper.SERVERHOST = sp.getString("webapi", "http://58.134.112.6");

			}

		}
		else
		{
			ApiAddressHelper.SERVERHOST = sp.getString("webapi", "http://58.134.112.6");
		}


		mobile = sp.getString("mobile", "");
		pwd = sp.getString("pwd", "");
		StaticUtil.People = mobile;
		if (mobile.length() > 0 && pwd.length() > 0) {
			String strlogin = sp.getString("logindata" + mobile, "");
			if (strlogin.length() > 0) {
				try {
					loginModel = gson.fromJson(strlogin,
							new TypeToken<LoginDataModel>() {
					}.getType());

				} catch (JsonSyntaxException localJsonSyntaxException) {

				}
			}
		}

		if (loginModel == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_1);//
		map.put("down", R.drawable.menu_2);//
		home.put("18", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_1_1_1);//
		map.put("down", R.drawable.menu_1_1_0);//
		home.put("310", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_1_2_1);//
		map.put("down", R.drawable.menu_1_2_0);//
		home.put("19", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_3);//
		map.put("down", R.drawable.menu_4);//
		home.put("21", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_5);//
		map.put("down", R.drawable.menu_6);//
		home.put("26", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_3_1_1);//
		map.put("down", R.drawable.menu_3_1_0);//
		home.put("29", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_3_2_1);//
		map.put("down", R.drawable.menu_3_2_0);//
		home.put("207", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_7);//
		map.put("down", R.drawable.menu_8);//
		home.put("299", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_4_1_1);//
		map.put("down", R.drawable.menu_4_1_0);//
		home.put("31", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_4_2_1);//
		map.put("down", R.drawable.menu_4_2_0);//
		home.put("208", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_9);//
		map.put("down", R.drawable.menu_10);//
		home.put("38", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_5_1_1);//
		map.put("down", R.drawable.menu_5_1_0);//
		home.put("39", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_5_2_1);//
		map.put("down", R.drawable.menu_5_2_0);//
		home.put("206", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_11);//
		map.put("down", R.drawable.menu_12);//
		home.put("33", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_6_1_1);//
		map.put("down", R.drawable.menu_6_1_0);//
		home.put("34", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_6_2_1);//
		map.put("down", R.drawable.menu_6_2_0);//
		home.put("209", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_6_3_1);//
		map.put("down", R.drawable.menu_6_3_0);//
		home.put("36", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_6_4_1);//
		map.put("down", R.drawable.menu_6_4_0);//
		home.put("321", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_13);//
		map.put("down", R.drawable.menu_14);//
		home.put("47", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_7_1_1);//
		map.put("down", R.drawable.menu_7_1_0);//
		home.put("48", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_7_2_1);//
		map.put("down", R.drawable.menu_7_2_0);//
		home.put("211", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_15);//
		map.put("down", R.drawable.menu_16);//
		home.put("42", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_8_1_1);//
		map.put("down", R.drawable.menu_8_1_0);//
		home.put("43", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_8_2_1);//
		map.put("down", R.drawable.menu_8_2_0);//
		home.put("210", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_9_1_1);//
		map.put("down", R.drawable.menu_9_1_0);//
		home.put("55", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_9_5_1);//
		map.put("down", R.drawable.menu_9_5_0);//
		home.put("417", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_21);//
		map.put("down", R.drawable.menu_22);//
		home.put("78", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_21_3);//
		map.put("down", R.drawable.menu_21_4);//
		home.put("280", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_21_3);//
		map.put("down", R.drawable.menu_21_4);//
		home.put("420", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_21_1);//
		map.put("down", R.drawable.menu_21_2);//
		home.put("421", map);

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_19);//
		map.put("down", R.drawable.menu_20);//
		home.put("local_personalcenter", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.people_check);//
		map.put("down", R.drawable.people_check);//
		home.put("people_check", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.people_upload);//
		map.put("down", R.drawable.people_upload);//
		home.put("people_upload", map);
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.check_download);//
		map.put("down", R.drawable.check_download);//
		home.put("offline_download", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.lixian_check);//
		map.put("down", R.drawable.lixian_check);//
		home.put("offline_check", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.check_statistics);//
		map.put("down", R.drawable.check_statistics);//
		home.put("offline_statistics", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.check_upload);//
		map.put("down", R.drawable.check_upload);//
		home.put("offline_upload", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_9_5_1);//
		map.put("down", R.drawable.menu_9_5_0);//
		home.put("online_check", map);

		map = new HashMap<String, Object>();
		map.put("up", R.drawable.menu_9_2_1);//
		map.put("down", R.drawable.menu_9_2_0);//
		home.put("online_statistics", map);

		mapModule.put("29", "104001");
		mapModule.put("31", "105001");
		mapModule.put("39", "106001");
		mapModule.put("34", "107001");
		mapModule.put("36", "108001");
		mapModule.put("43", "109001");
		mapModule.put("48", "089001");

		mapAttchType.put("29", "006006");
		mapAttchType.put("48", "006007");
		mapAttchType.put("39", "006009");
		mapAttchType.put("34", "006011");
		mapAttchType.put("43", "006010");
		mapAttchType.put("31", "006021");

		mapSearchToReg.put("207", "29");
		mapSearchToReg.put("208", "31");
		mapSearchToReg.put("206", "39");
		mapSearchToReg.put("209", "34");
		mapSearchToReg.put("211", "48");
		mapSearchToReg.put("210", "43");
	}

	@Override
	protected void DataInit() {
		// TODO Auto-generated method stub
		if (loginModel == null) {
			return;
		}

		if (mobile != null && mobile.length() > 0) {
			SharedPreferences sp = this.getSharedPreferences("basedata",
					Context.MODE_PRIVATE);

			String strbase = sp.getString("basedata" + mobile, "");

			if (strbase.length() > 0) {
				this.getBaseDada(strbase);
				return;
			}
		}

		if (basedataModel == null) {
			Log.e("enterenterenterenter", "enterenterenterenterenter");

			this.param.clear();
			// this.param.put("maccode", "aaaaaa");//352315052327497

			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(MainActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在获取基础数据...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(
							ApiAddressHelper.getBaseDataUrl(), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					Log.e("basedata", res);
					if (res.equals("")) {
						m_pDialog.hide();
						Common.ShowInfo(context, "网络故障");
						return;
					}

					SharedPreferences sp = MainActivity.this
							.getSharedPreferences("basedata",
									Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("basedata" + mobile, res);
					editor.commit();

					MainActivity.this.getBaseDada(res);
				}
			});

		}

	}

	@Override
	protected void Destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		Getui();
	}
	
	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		Log.e("ViewInitViewInitViewInitViewInit",
				"ViewInitViewInitViewInitViewInit");
		
		full(false);
		setContentView(R.layout.activity_main);

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.tv_back);
		if (ll != null) {
			ll.setVisibility(View.GONE);
		}
		Button btn = (Button) this.findViewById(R.id.btn_title_left);
		btn.setVisibility(View.VISIBLE);

		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setText("注销");

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText("资产管理系统");

		if(StaticUtil.flag==2){
			login();
			StaticUtil.flag=1;
		}
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		//		img = (RoundImageView) this.findViewById(R.id.btn_scan_code);
		scan = (Button) findViewById(R.id.btn_scan_code);
		scan.setOnClickListener(this);

		Button btn = (Button) this.findViewById(R.id.btn_title_left);
		btn.setOnClickListener(this);

		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setOnClickListener(this);

	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
				// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
				) {

			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			String MenuId = (String) item.get("menuid");

			Log.e("MenuId", MenuId);

			if (MenuId.equals("21") || MenuId.equals("55")
					|| MenuId.equals("417")
					|| MenuId.equals("local_personalcenter")) {
				if (MenuId.equals("21")) {
					Intent intent = new Intent(MainActivity.this,
							SearchActivity.class);

					intent.putExtra("title", (String) item.get("ItemText"));
					intent.putExtra("MenuId", MenuId);

					MainActivity.this.startActivity(intent);
				} else if (MenuId.equals("55")) // 离线盘点
				{
					CheckMenuActivity.listmenu.clear();

					MenuModel model = new MenuModel();
					model.MenuId = "offline_download";
					model.MenuName = "盘点下载";
					CheckMenuActivity.listmenu.add(model);

					model = new MenuModel();
					model.MenuId = "offline_check";
					model.MenuName = "离线盘点";
					CheckMenuActivity.listmenu.add(model);

					model = new MenuModel();
					model.MenuId = "offline_statistics";
					model.MenuName = "盘点统计";
					CheckMenuActivity.listmenu.add(model);

					model = new MenuModel();
					model.MenuId = "offline_upload";
					model.MenuName = "盘点上传";
					CheckMenuActivity.listmenu.add(model);

					Intent intent = new Intent(MainActivity.this,
							CheckMenuActivity.class);

					intent.putExtra("title", (String) item.get("ItemText"));
					intent.putExtra("MenuId", MenuId);

					MainActivity.this.startActivity(intent);

				} else if (MenuId.equals("417")) // 在线盘点
				{
					CheckMenuActivity.listmenu.clear();

					MenuModel model = new MenuModel();
					model.MenuId = "online_check";
					model.MenuName = "在线盘点";
					CheckMenuActivity.listmenu.add(model);

					model = new MenuModel();
					model.MenuId = "online_statistics";
					model.MenuName = "在线统计";
					CheckMenuActivity.listmenu.add(model);

					Intent intent = new Intent(MainActivity.this,
							CheckMenuActivity.class);

					intent.putExtra("title", (String) item.get("ItemText"));
					intent.putExtra("MenuId", MenuId);

					MainActivity.this.startActivity(intent);
				} else if (MenuId.equals("local_personalcenter")) {

					Intent intent = new Intent(MainActivity.this,
							PersonalCenterActivity.class);

					MainActivity.this.startActivity(intent);

				}

			} else {
				Intent intent = new Intent(MainActivity.this,
						MenuActivity.class);

				intent.putExtra("title", (String) item.get("ItemText"));
				MenuActivity.listmenu = (List<MenuModel>) item.get("submenu");

				MainActivity.this.startActivity(intent);
			}

		}
	}

	void createIdToName(MenuModel model) {
		if (model != null) {
			mapIdToName.put(model.MenuId, model.MenuName);

			if (model.SubMenu != null) {
				for (MenuModel submodel : model.SubMenu) {
					createIdToName(submodel);
				}
			}

		}
	}

	void updateMain() {
		GridView gv = (GridView) this.findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

		List<MenuModel> lstMenu = loginModel.lstMenu;
		for (MenuModel model : lstMenu) {

			createIdToName(model);

			String MenuId = model.MenuId;
			String MenuName = model.MenuName;

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", home.get(MenuId).get("up"));
			map.put("ItemImageSelect", home.get(MenuId).get("down"));
			map.put("ItemText", MenuName);
			map.put("submenu", model.SubMenu);
			map.put("menuid", model.MenuId);

			lstImageItem.add(map);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.menu_19);// 添加图像资源的ID
		map.put("ItemImageSelect", R.drawable.menu_20);
		map.put("ItemText", "个人中心");// 按序号做ItemText
		map.put("menuid", "local_personalcenter");// 按序号做ItemText
		lstImageItem.add(map);

		/*
		 * SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
		 * lstImageItem,// 数据来源 R.layout.main_item,// night_item的XML实现
		 * 
		 * // 动态数组与ImageItem对应的子项 new String[] { "ItemImage", "ItemText" },
		 * 
		 * // ImageItem的XML文件里面的一个ImageView,两个TextView ID new int[] {
		 * R.id.ItemImage, R.id.ItemText }); // 添加并且显示
		 * gv.setAdapter(saImageItems); gv.setOnItemClickListener(new
		 * ItemClickListener());
		 */

		GridViewAdapter adapter = new GridViewAdapter(this, lstImageItem);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new ItemClickListener());

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_scan_code: {
			Intent it = new Intent(MainActivity.this, CaptureActivity.class);
			startActivityForResult(it, 1);
		}

		break;

		case R.id.btn_title_left: {
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("提示")
			// 设置对话框标题
			.setMessage("确认要刷新基础数据吗？")
			// 设置显示的内容
			.setPositiveButton("是",
					new DialogInterface.OnClickListener() {// 添加确定按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 确定按钮的响应事件
					login();
				}
			})
			.setNegativeButton("否",
					new DialogInterface.OnClickListener() {// 添加返回按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 响应事件

				}
			}).show();// 在按键响应事件中显示此对话框
		}
		break;

		case R.id.btn_title_right: {
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("提示")
			// 设置对话框标题
			.setMessage("确认要注销吗？")
			// 设置显示的内容
			.setPositiveButton("是",
					new DialogInterface.OnClickListener() {// 添加确定按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 确定按钮的响应事件
					cancel();
					return;
				}
			})
			.setNegativeButton("否",
					new DialogInterface.OnClickListener() {// 添加返回按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 响应事件

				}
			}).show();// 在按键响应事件中显示此对话框
		}
		break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("提示")
			// 设置对话框标题
			.setMessage("确认要注销吗？")
			// 设置显示的内容
			.setPositiveButton("是",
					new DialogInterface.OnClickListener() {// 添加确定按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 确定按钮的响应事件
					cancel();
					return;
				}
			})
			.setNegativeButton("否",
					new DialogInterface.OnClickListener() {// 添加返回按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 响应事件

				}
			}).show();// 在按键响应事件中显示此对话框
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	//查询数据返回到资产浏览页
	void scantofind(String name,  String result) {
		Log.e("camera", result);
		if (result != null) {
			Prepare(param);
			param.put("MenuId", "19");

			Map<String, String> mapMsAsset = new HashMap<String, String>();
			mapMsAsset.put(name, result);

			param.put("MsAsset", mapMsAsset);

			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(MainActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在查询...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(
							ApiAddressHelper.getResultUrl("21"), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {

					if (res.equals("")) {
						m_pDialog.hide();
						Common.ShowInfo(context, "网络故障");
						return;
					}

					try {

						JSONObject respJson = new JSONObject(res);

						JSONObject response = respJson
								.getJSONObject("response");

						String ErrorCode = response.getString("ErrorCode");
						if (ErrorCode.equals("S00000") == false) {
							m_pDialog.hide();
							String ErrorMsg = response.getString("ErrorMsg");
							Common.ShowInfo(context, ErrorMsg);
							return;
						}

						HashMap<String, Object> assetmap = new HashMap<String, Object>();
						List<Object> listImgGuid = new ArrayList<Object>();
						assetmap.put("ImgGuid", listImgGuid);

						JSONObject rspcontent = respJson
								.getJSONObject("rspcontent");

						JSONObject MsAsset = rspcontent
								.getJSONObject("MsAsset");
						if (MsAsset != null) {
							Iterator keys = MsAsset.keys();
							while (keys.hasNext()) {
								String key = (String) keys.next();
								if (key.equals("ImgGuid") == false) {

									if (MsAsset.get(key) != null) {
										String value = MsAsset.get(key)
												.toString();
										assetmap.put(key, value);
									} else {
										assetmap.put(key, "");
									}

								} else {
									JSONArray ImgGuid = MsAsset
											.getJSONArray("ImgGuid");
									if (ImgGuid != null) {
										for (int i = 0; i < ImgGuid.length(); i++) {
											JSONObject item = ImgGuid
													.getJSONObject(i);
											String base64 = item
													.getString("ImageData");
											listImgGuid.add(base64);
										}
									}
								}
							}
						}

						JSONArray History = rspcontent
								.getJSONArray("MsAssetHistory");
						if (History != null) {
							String strHistory = History.toString();
							AssetBrowseActivity.historymodel = gson.fromJson(
									strHistory,
									new TypeToken<List<AssetHistoryModel>>() {
									}.getType());
						}

						m_pDialog.hide();

						Intent intent = new Intent(MainActivity.this,
								AssetBrowseActivity.class);

						intent.putExtra("MenuId", MenuId);
						intent.putExtra("title", "资产浏览");

						SerializableList tmplist = new SerializableList();
						tmplist.setList(listImgGuid);

						SerializableMap tmpmap = new SerializableMap();
						tmpmap.setMap(assetmap);
						Bundle bundle = new Bundle();
						bundle.putSerializable("param", tmpmap);
						bundle.putSerializable("ImgGuid", tmplist);

						intent.putExtras(bundle);

						MainActivity.this.startActivity(intent);

					} catch (JSONException localJsonSyntaxException) {

						Log.e("erroe", "sdkjcnskcndkscndskcdc");
						m_pDialog.hide();

						try {
							JSONObject respJson = new JSONObject(res);

							String rspcontent = respJson
									.getString("rspcontent");

							Common.ShowInfo(context, rspcontent);

						} catch (JSONException localJsonSyntaxException2) {

						}
					}
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 1:
			if (data != null) {
				String result = data.getStringExtra("result");
				scantofind("AssetCode", result);
			}
			break;

		case 2:
			if (data != null) {
				String result = data.getStringExtra("result");
				scantofind("AssetCode",result);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void Init(Bundle paramBundle) {
		// TODO Auto-generated method stub

	}

	//初始化个推信息
	private void Getui(){
		Context context = this.getApplicationContext();
		SDKInitializer.initialize(getApplicationContext());
		PushManager pushManager = PushManager.getInstance();
		pushManager.initialize(context);
		pushManager.bindAlias(context,mobile);
	}
	
	//注销方法
	private void cancel(){
		SharedPreferences sp = MainActivity.this.getSharedPreferences("logindata", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString( "logindata"+mobile, null);
		editor.commit();
		
		StaticUtil.flag=2;
		
		Intent intent = new Intent(
				MainActivity.this,
				LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	//刷新方法
	private void login(){
		MainActivity.this.param.clear();
		MainActivity.this.param.put("loginname",
				mobile);
		MainActivity.this.param.put("pwd", pwd);
		MainActivity.this.param.put("maccode", /* "aaaaaa" */
				deviceId);// 352315052327497

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest()
					throws Exception {

				m_pDialog = new ProgressDialog(
						MainActivity.this);
				m_pDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取登录数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(
						context).Post(
								ApiAddressHelper
								.getLoginUrl(),
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
					loginModel = gson
							.fromJson(
									res,
									new TypeToken<LoginDataModel>() {
									}.getType());
					if (loginModel != null) {

						SharedPreferences sp = MainActivity.this
								.getSharedPreferences(
										"logindata",
										Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp
								.edit();

						editor.putString("mobile",
								mobile);
						editor.putString("pwd", pwd);
						editor.putString(
								"logindata"
										+ mobile,
										res);
						editor.commit();

						doAsync(new CallEarliest<String>() {
							public void onCallEarliest()
									throws Exception {
								m_pDialog
								.setMessage("正在获取基础数据...");
							}
						}, new Callable<String>() {
							public String call()
									throws Exception {
								return HttpHelper
										.getInstance(
												context)
												.Post(ApiAddressHelper
														.getBaseDataUrl(),
														param);
							}
						}, new Callback<String>() {
							public void onCallback(
									String res) {
								Log.e("basedata",
										res);
								if (res.equals("")) {
									m_pDialog
									.hide();
									Common.ShowInfo(
											context,
											"网络故障");
									return;
								}

								SharedPreferences sp = MainActivity.this
										.getSharedPreferences(
												"basedata",
												Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp
										.edit();
								editor.putString(
										"basedata"
												+ mobile,
												res);
								editor.commit();

								MainActivity.this
								.getBaseDada(res);

							}
						});

					} else {
						m_pDialog.hide();
						Common.ShowInfo(context,
								"登陆异常");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {
					m_pDialog.hide();
					Log.e("iws",
							"Login json转换错误 e:"
									+ localJsonSyntaxException);
				}
			}
		});
	}
}
