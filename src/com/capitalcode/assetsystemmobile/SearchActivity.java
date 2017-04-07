package com.capitalcode.assetsystemmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.AssetListInfoModel;
import com.capitalcode.assetsystemmobile.model.MsBorrowModel;
import com.capitalcode.assetsystemmobile.model.MsDrawModel;
import com.capitalcode.assetsystemmobile.model.MsMoveModel;
import com.capitalcode.assetsystemmobile.model.MsRepairRegModel;
import com.capitalcode.assetsystemmobile.model.MsRetModel;
import com.capitalcode.assetsystemmobile.model.MsRevertModel;
import com.capitalcode.assetsystemmobile.model.MsScrapModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.utils.Address;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends BaseActivity implements
View.OnClickListener {

	private List<Map<String, Object>> listSearch = new ArrayList<Map<String, Object>>();
	ContentAdapter searchAdapter;

	String RealMenuId;

	public void onSearch()
	{

		Search();
	}


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

	void Search()
	{
		Prepare(param);
		if (MenuId.equals("19") || MenuId.equals("21")) {
			param.put("MenuId", "19");
		} else {
			param.put("MenuId", MenuId);
		}

		Map<String, Object> mapMsAsset = new HashMap<String, Object>();
		String AssetOriWorth_up = "";
		String AssetOriWorth_down = "";
		String InsDt_up = "";
		String InsDt_down = "";
		String date7_up = "";
		String date7_down = "";

		for (Map<String, Object> map : listSearch) {
			if (((String) map.get("id")).contains("_") == false
					&& map.get("realvalue") != null) {
				mapMsAsset.put((String) map.get("id"),
						map.get("realvalue"));
			} else if (((String) map.get("id"))
					.equals("AssetOriWorth_up"))// ||
				// ((String)map.get("id")).equals("AssetOriWorth_down")
				// )
			{
				AssetOriWorth_up = ((String) map.get("realvalue"));
				if (AssetOriWorth_up == null) {
					AssetOriWorth_up = "";
				}
			} else if (((String) map.get("id"))
					.equals("AssetOriWorth_down")) {
				AssetOriWorth_down = ((String) map.get("realvalue"));
				if (AssetOriWorth_down == null) {
					AssetOriWorth_down = "";
				}
			} else if (((String) map.get("id")).equals("InsDt_up"))// ||
				// ((String)map.get("id")).equals("AssetOriWorth_down")
				// )
			{
				InsDt_up = ((String) map.get("realvalue"));
				if (InsDt_up == null) {
					InsDt_up = "";
				}
			} else if (((String) map.get("id")).equals("InsDt_down")) {
				InsDt_down = ((String) map.get("realvalue"));
				if (InsDt_down == null) {
					InsDt_down = "";
				}
			}

			else if (((String) map.get("id")).equals("date7_up"))

			{
				date7_up = ((String) map.get("realvalue"));
				if (date7_up == null) {
					date7_up = "";
				}
			} else if (((String) map.get("id")).equals("date7_down")) {
				date7_down = ((String) map.get("realvalue"));
				if (date7_down == null) {
					date7_down = "";
				}
			}
		}

		if ((AssetOriWorth_up != null && AssetOriWorth_up.equals("") == false)
				|| (AssetOriWorth_down != null && AssetOriWorth_down
				.equals("") == false)) {
			mapMsAsset.put("AssetOriWorth", AssetOriWorth_up + "$"
					+ AssetOriWorth_down);
		}

		if ((InsDt_up != null && InsDt_up.equals("") == false)
				|| (InsDt_down != null && InsDt_down.equals("") == false)) {
			mapMsAsset.put("InsDt", InsDt_up + "$" + InsDt_down);
		}

		if ((date7_up != null && date7_up.equals("") == false)
				|| (date7_down != null && date7_down.equals("") == false)) {
			mapMsAsset.put("date7", date7_up + "$" + date7_down);
		}

		mapMsAsset.put("Module", SearchActivity.this.getIntent()
				.getStringExtra("Module"));





		String formstring = null;		


		if( MenuId.equals("19") || MenuId.equals("21") )
		{
			formstring = "MsAsset";
		}
		else if( MenuId.equals("207") )
		{

			formstring = "MsDraw";
		}
		else if( MenuId.equals("208") )
		{

			formstring = "MsRevert";
		}		
		else if( MenuId.equals("206") )
		{
			formstring = "MsMove";
		}		
		else if( MenuId.equals("209") )
		{

			formstring = "MsBorrow";
		}	
		else if( MenuId.equals("211") )
		{

			formstring = "MsRepairReg";
		}
		else if( MenuId.equals("210") )
		{
			formstring = "MsScrap";
		}			
		else if( MenuId.equals("321") )
		{
			formstring = "MsRet";
		}

		param.put(formstring, mapMsAsset);
		param.put("PageIndex", "1");
		param.put("PageSize", "10");

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(SearchActivity.this);
				m_pDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在查询数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getSearchUrl(MenuId), param);
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

					String ErrorCode = response
							.getString("ErrorCode");
					if (ErrorCode.equals("S00000") == false) {
						if (response.getString("ErrorMsg") != null) {
							Common.ShowInfo(context, response
									.getString("ErrorMsg"));
						}

						return;
					} 

				}
				catch(JSONException e)
				{

				}


				try {
					if (MenuId.equals("19") || MenuId.equals("21")) {

						RequestRetModel<AssetListInfoModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<AssetListInfoModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.assetmodel = (AssetListInfoModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);
							intent.putExtra(
									"RealMenuId",
									RealMenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							if( RealMenuId == null )
							{
								SearchActivity.this.startActivity(intent);
							}
							else
							{
								SearchActivity.this.startActivityForResult(intent, 4);
							}

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}

					} else if (MenuId.equals("207")) {
						RequestRetModel<MsDrawModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsDrawModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msdrawmodel = (MsDrawModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}

					}
					else if (MenuId.equals("206")) 
					{
						RequestRetModel<MsMoveModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsMoveModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msmovemodel = (MsMoveModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}
					else if (MenuId.equals("209")) 
					{
						RequestRetModel<MsBorrowModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsBorrowModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msborrowmodel = (MsBorrowModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}	
					else if (MenuId.equals("321")) 
					{
						RequestRetModel<MsRetModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsRetModel>>() {
										}.getType());
						if (model != null) {
							Log.e("eeeeeeeeeeeeeeeeeeeeee","enter");
							ResultActivity.msretmodel = (MsRetModel) model.rspcontent;

							Log.e("eeeeeeeeeeeeeeeeeeeeee",ResultActivity.msretmodel.PageCount+"page");

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}								
					else if (MenuId.equals("211")) 
					{
						RequestRetModel<MsRepairRegModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsRepairRegModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msrepairmodel = (MsRepairRegModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}		
					else if (MenuId.equals("210")) 
					{
						RequestRetModel<MsScrapModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsScrapModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msscrapmodel = (MsScrapModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}								
					else if (MenuId.equals("208")) 
					{
						RequestRetModel<MsRevertModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<MsRevertModel>>() {
										}.getType());
						if (model != null) {
							ResultActivity.msrevertmodel = (MsRevertModel) model.rspcontent;

							Intent intent = new Intent(
									SearchActivity.this,
									ResultActivity.class);

							intent.putExtra("MenuId", MenuId);

							SerializableMap tmpmap = new SerializableMap();
							tmpmap.setMap(param);
							Bundle bundle = new Bundle();
							bundle.putSerializable("param", tmpmap);
							intent.putExtras(bundle);

							SearchActivity.this.startActivity(intent);

							// finish();
						} else {

							Common.ShowInfo(context, "网络异常");
						}
					}							



				} catch (JsonSyntaxException localJsonSyntaxException) {

					try {
						RequestRetModel<String> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<String>>() {
										}.getType());
						if (model != null) {
							if (model.response.ErrorCode
									.equals("S00000") == false) {
								Common.ShowInfo(context,
										model.response.ErrorMsg);
							} else {
								Common.ShowInfo(context,
										model.rspcontent);
							}

						}

					} catch (JsonSyntaxException localJsonSyntaxException2) {

					}

				}
			}
		});
	}


	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_search);

		RealMenuId = SearchActivity.this.getIntent()
				.getStringExtra(
						"RealMenuId");

		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		btn = (Button) this.findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Search();

			}

		});

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText(this.getIntent().getStringExtra("title"));

		if (MenuId.equals("19") || MenuId.equals("21")) {
			setupAssetSearch();
		} else if (MenuId.equals("207")) {
			setupUseSearch();
		} else if (MenuId.equals("206")) {
			setupAllocateSearch();
		} else if (MenuId.equals("209")) {
			setupBorrowSearch();
		} else if (MenuId.equals("321")) {
			setupReturnSearch();
		} else if (MenuId.equals("211")) {
			setupRepairSearch();
		} else if (MenuId.equals("208")) {
			setupCancelSearch();
		} else if (MenuId.equals("210")) {
			setupHandleSearch();
		}

		for( Map<String,Object> map : this.listSearch )
		{
			map.put("onlyone", true);


		}


		ListView lv = (ListView) this.findViewById(R.id.searchlist);
		searchAdapter = new ContentAdapter(this, listSearch);
		lv.setAdapter(searchAdapter);
	}

	void setupHandleSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapStatus");
		map.put("name", "处置单状态");
		map.put("value", "");
		map.put("searchid", "RevertState");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapType");
		map.put("name", "处置方式");
		map.put("value", "");
		map.put("searchid", "ScrapType");
		listSearch.add(map);
	}

	void setupCancelSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetState");
		map.put("name", "退库单状态");
		map.put("value", "");
		map.put("searchid", "RevertState");
		listSearch.add(map);

	}

	void setupRepairSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegState");
		map.put("name", "维修单状态");
		map.put("value", "");
		map.put("searchid", "RepairState");
		listSearch.add(map);
	}

	void setupReturnSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDeptId");
		map.put("name", "借用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorUserId");
		map.put("name", "借用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetUserId");
		map.put("name", "归还人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDtStart");
		map.put("value", "");
		map.put("name", "归还时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDtEnd");
		map.put("value", "");
		map.put("name", "归还时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);
	}

	void setupBorrowSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDeptId");
		map.put("name", "借用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorUserId");
		map.put("name", "借用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorState");
		map.put("name", "调拔单状态");
		map.put("value", "");
		map.put("searchid", "BorrowState");
		listSearch.add(map);
	}

	void setupAllocateSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ReceDeptId");
		map.put("name", "接收部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ReceUserId");
		map.put("name", "接收人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "FlitDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "FlitDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "FlitState");
		map.put("name", "调拔单状态");
		map.put("value", "");
		map.put("searchid", "MoveState");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String260");
		map.put("value", "");
		map.put("name", "资产用途");
		listSearch.add(map);
	}

	void setupUseSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("value", "");
		map.put("name", "资产编号");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawDeptId");
		map.put("name", "使用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawUserId");
		map.put("name", "领用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawDtStart");
		map.put("value", "");
		map.put("name", "登记时间(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawDtEnd");
		map.put("value", "");
		map.put("name", "登记时间(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawState");
		map.put("name", "领用单状态");
		map.put("value", "");
		map.put("searchid", "ApplyState");
		listSearch.add(map);

	}

	void setupAssetSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetCode");
		map.put("imeOptions", EditorInfo.IME_ACTION_SEARCH);
		map.put("value", "");
		map.put("name", "资产编号");
		listSearch.add(map);
		
		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "SerialNumber");
		map.put("value", "");
		map.put("name", "序列号");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetOriWorth");
		map.put("value", "");
		map.put("name", "旧资产编号");
		map.put("inputtype", String.valueOf(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER));
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetName");
		map.put("value", "");
		map.put("name", "资产名称");
		map.put("searchid", "AssetName");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "Standard");
		map.put("value", "");
		map.put("name", "规格型号");
		map.put("searchid", "Standard");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "assetType");
		map.put("value", "");
		map.put("name", "资产类别");
		map.put("searchid", "AssetType");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Category");
		map.put("value", "");
		map.put("name", "资产分类");
		map.put("searchid", "category");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetOriWorth_up");
		map.put("value", "");
		map.put("name", "资产原值(起)");
		map.put("inputtype", String.valueOf(InputType.TYPE_NUMBER_FLAG_DECIMAL));
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "AssetOriWorth_down");
		map.put("value", "");
		map.put("name", "资产原值(止)");
		map.put("inputtype", String.valueOf(InputType.TYPE_NUMBER_FLAG_DECIMAL));
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "UseUserId");
		map.put("name", "使用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "UseDeptId");
		map.put("name", "使用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Status");
		map.put("value", "");
		map.put("name", "资产状态");
		//		map.put("searchid", "Status");
		listSearch.add(map);



		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "InsDt_up");
		map.put("value", "");
		map.put("name", "购置日期(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "InsDt_down");
		map.put("value", "");
		map.put("name", "购置日期(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date7_up");
		map.put("value", "");
		map.put("name", "入账日期(起)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date7_down");
		map.put("value", "");
		map.put("name", "入账日期(止)");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listSearch.add(map);
		//新增的栏目
		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "brand");
		map.put("value", "");
		map.put("name", "品牌");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "addMode");
		map.put("value", "");
		map.put("name", "增加方式");
		map.put("searchid", "AddMode");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "quantity");
		map.put("value", "");
		map.put("name", "数量");
		map.put("inputtype", String.valueOf(InputType.TYPE_CLASS_NUMBER));
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "measureUnit");
		map.put("value", "");
		map.put("name", "计数单位");
		map.put("searchid", "MeasureUnit");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "address");
		map.put("id", "str8");
		map.put("value", "");
		map.put("name", "存放地点");
		listSearch.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "memo");
		map.put("value", "");
		map.put("name", "备注");
		listSearch.add(map);
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button) this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(this);

		btn = (Button) this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_scan_code: {
			Intent it = new Intent(SearchActivity.this, CaptureActivity.class);
			startActivityForResult(it, 1);

		}

		break;

		case R.id.btn_scan_sn: {
			Intent it = new Intent(SearchActivity.this, CaptureActivity.class);
			startActivityForResult(it, 2);

		}
		break;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 1: {

				String str = data.getStringExtra("result");
				Map<String, Object> map = listSearch.get(0);
				map.put("value", str);
				map.put("realvalue", str);
				searchAdapter.notifyDataSetChanged();

				Search();

			}
			break;
			case 2: {

				String str = data.getStringExtra("result");
				Map<String, Object> map = listSearch.get(1);
				map.put("value", str);
				map.put("realvalue", str);

				searchAdapter.notifyDataSetChanged();

				Search();

			}
			break;

			case 4: {
				setResult(RESULT_OK, data);
				finish();

			}
			break;

			case 5 :{
				String result = data.getExtras().getString("result");
				if(result!=null){
					Address.setAddress(result);
					searchAdapter.notifyDataSetChanged();
				}
				Log.i("result", result);
			}
			break;



			default:
				break;
			}

		}
	}

}
