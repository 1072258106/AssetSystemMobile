package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.SearchActivity;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.AssetHistoryModel;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetModel;
import com.capitalcode.assetsystemmobile.model.RegisterRetModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.utils.Address;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.SerializableList;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;
import com.capitalcode.assetsystemmobile.utils.StaticUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;

public class RegisterActivity extends BaseActivity {

	private List<Map<String, Object>> listRegister = new ArrayList<Map<String, Object>>();
	ContentAdapter registerAdapter;

	ListView mListView;
	ScrollAdapter adapter;
	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

	String OrderId;
	String OrderCode;

	static public List<Object> listImgGuid=new ArrayList<Object>();

	@Override
	protected void Init(Bundle paramBundle) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void AppInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(StaticUtil.forRegisterActivity==1){
			saveData();
		}
		StaticUtil.forRegisterActivity=1;
	}

	@Override
	protected void DataInit() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null ) 
		{


			SerializableMap serializableMap = (SerializableMap) bundle
					.get("param");
			SerializableList imgList = (SerializableList) bundle
					.get("ImgGuid");
			SerializableList assetList = (SerializableList) bundle
					.get("asset");
			String statename = bundle.getString("statename");
			if( statename != null && statename.equals("完成"))
			{
				Button btn = (Button) this.findViewById(R.id.btn_save);
				btn.setEnabled(false);

				btn = (Button) this.findViewById(R.id.btn_confirm);
				btn.setEnabled(false);
			}
			else
			{

			}

			if( serializableMap != null /*&& imgList != null*/ && assetList != null )
			{
				Button btn = (Button) this.findViewById(R.id.btn_confirm);
				btn.setVisibility(View.VISIBLE);


				Map<String, Object> mapForm = serializableMap.getMap();
				//List<Object> listImgGuid = imgList.getList();
				List<Object> listAsset = assetList.getList();

				if (MenuId.equals("29")) {

					OrderId = (String)mapForm.get("DrawRecId");
					OrderCode = (String)mapForm.get("DrawCode");

				} else if (MenuId.equals("39")) {

					OrderId = (String)mapForm.get("FlitRecId");
					OrderCode = (String)mapForm.get("FlitCode");

				} else if (MenuId.equals("34")) {

					OrderId = (String)mapForm.get("BorRecId");
					OrderCode = (String)mapForm.get("BorrowCode");
				} else if (MenuId.equals("48")) {

					OrderId = (String)mapForm.get("RegRecId");
					OrderCode = (String)mapForm.get("RegCode");
				} else if (MenuId.equals("43")) {

					OrderId = (String)mapForm.get("ScrapRecId");
					OrderCode = (String)mapForm.get("ScrapCode");
				} else if (MenuId.equals("31")) {

					OrderId = (String)mapForm.get("RetRecId");
					OrderCode = (String)mapForm.get("RetRecCode");
				}


				for (Map<String, Object> map : listRegister) 
				{
					if( statename != null && statename.equals("完成"))
					{
						map.put("readonly", "true");
					}


					String id = (String) map.get("id");

					if (mapForm.get(id) != null) 
					{
						if(id.equals("Int130"))		
						{
							map.put("value", (String) mapForm.get("RetHandleDeptName"));
							map.put("realvalue", (String) mapForm.get(id));

							continue;
						}
						else if(id.equals("Int190"))		
						{
							map.put("value", (String) mapForm.get("HandlingPersonName"));
							map.put("realvalue", (String) mapForm.get(id));

							continue;
						}
						else if(id.equals("Int200"))		
						{
							map.put("value", (String) mapForm.get("HandlingDeptName"));
							map.put("realvalue", (String) mapForm.get(id));

							continue;
						}					



						String name = id + "Name";
						String name2 = id.replace("Id", "Name");
						String name3 = id.replace("Id", "");

						if (mapForm.get(name) != null) 
						{
							map.put("value", (String) mapForm.get(name));
							map.put("realvalue", (String) mapForm.get(id));
						} 
						else if (mapForm.get(name2) != null) 
						{
							map.put("value", (String) mapForm.get(name2));
							map.put("realvalue", (String) mapForm.get(id));
						} 
						else if (mapForm.get(name3) != null) 
						{
							map.put("value", (String) mapForm.get(name3));
							map.put("realvalue", (String) mapForm.get(id));
						} 					
						else 
						{
							map.put("value", (String) mapForm.get(id));
							map.put("realvalue", (String) mapForm.get(id));
						}

					}
				}

				if (listImgGuid != null) {
					for (Object base64 : listImgGuid) {
						Log.e("base64data",(String) base64);
						Bitmap bmp = /*base64ToBitmap*/decodeImg((String) base64);
						if (bmp != null) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("type", "pic");
							map.put("image", bmp);
							map.put("data", (String) base64);
							listRegister.add(map);
						}

					}
				}

				registerAdapter.notifyDataSetChanged();

				datas.clear();
				if( listAsset != null )
				{
					for( Object item : listAsset )
					{
						Map<String, Object> map = (Map<String, Object>)item;

						Map<String, String> data = new HashMap<String, String>();

						data.put("AssetId", (String)map.get("AssetId"));
						data.put("data_" + 0, (String)map.get("AssetCode"));
						data.put("data_" + 1, (String)map.get("AssetName"));
						data.put("data_" + 2, (String)map.get("Standard"));
						data.put("data_" + 3, (String)map.get("SerialNumber"));

						datas.add(data);
					}
				}
				adapter.notifyDataSetChanged();

			}
		}


	}

	@Override
	protected void Destroy() {
		// TODO Auto-generated method stub

	}

	int delitem;
	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);

		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		if (MenuId.equals("29")) {
			setupUse();
		} else if (MenuId.equals("39")) {
			setupAllocate();
		} else if (MenuId.equals("34")) {
			setupBorrow();
		} else if (MenuId.equals("48")) {
			setupRepair();
		} else if (MenuId.equals("43")) {
			setupHandle();
		} else if (MenuId.equals("31")) {
			setupCancel();
			Log.e("cancel","cancel");
		}

		ListView lv = (ListView) this.findViewById(R.id.searchlist);
		//TODO
		registerAdapter = new ContentAdapter(this, listRegister);
		lv.setAdapter(registerAdapter);

		mListView = (ListView) findViewById(R.id.scroll_list);
		adapter = new ScrollAdapter(this, datas, R.layout.item, new String[] {
				"data_0", "data_1", "data_2", "data_3", }, new int[] {
				R.id.item_data0, R.id.item_data1, R.id.item_data2,
				R.id.item_data3 });
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Prepare(param);
				param.put("MenuId", "19");

				Map<String, String> model = datas
						.get((int) arg3);

				Map<String, String> mapMsAsset = new HashMap<String, String>();
				mapMsAsset.put("AssetCode", model.get("data_0"));
				mapMsAsset.put("AssetName", model.get("data_1"));
				mapMsAsset.put("Standard", model.get("data_2"));
				mapMsAsset.put("SerialNumber", model.get("data_3"));

				param.put("MsAsset", mapMsAsset);

				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {

						m_pDialog = new ProgressDialog(
								RegisterActivity.this);
						m_pDialog
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();

					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(context).Post(
								ApiAddressHelper.getResultUrl("21"),
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

							JSONObject respJson = new JSONObject(res);

							JSONObject response = respJson
									.getJSONObject("response");

							String ErrorCode = response
									.getString("ErrorCode");
							if (ErrorCode.equals("S00000") == false) {
								m_pDialog.hide();
								String ErrorMsg = response
										.getString("ErrorMsg");
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
											String value = MsAsset.get(
													key).toString();
											assetmap.put(key, value);
										} else {
											assetmap.put(key, "");
										}

									} else {
										JSONArray ImgGuid = MsAsset
												.getJSONArray("ImgGuid");
										if (ImgGuid != null) {
											for (int i = 0; i < ImgGuid
													.length(); i++) {
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
								AssetBrowseActivity.historymodel = gson
										.fromJson(
												strHistory,
												new TypeToken<List<AssetHistoryModel>>() {
												}.getType());
							}

							m_pDialog.hide();

							Intent intent = new Intent(
									RegisterActivity.this,
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

							RegisterActivity.this.startActivity(intent);

						} catch (JSONException localJsonSyntaxException) {
							m_pDialog.hide();

						}
					}
				});



			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				delitem = (int) arg3;
				new AlertDialog.Builder(RegisterActivity.this)
				.setTitle("操作")
				.setItems(new String[] { "删除" },
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int which) {


						datas.remove(delitem);
						adapter.notifyDataSetChanged();


					}
				})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub

					}
				}).show();
				return true;
			}});
		setDataforRegister();
	}

	void setupCancel() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RetDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Int130");
		map.put("name", "退库接收部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "RetMemo");
		map.put("name", "退库备注");
		map.put("value", "");
		listRegister.add(map);

		//		map = new HashMap<String, Object>();
		//		map.put("type", "addpic");
		//		map.put("id", "imgGuid");
		//		map.put("value", "");
		//		map.put("name", "登记图片");
		//		listRegister.add(map);



	}

	void setupHandle() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ScrapType");
		map.put("name", "处置方式");
		map.put("value", "");
		map.put("searchid", "ScrapType");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "ApplyFileName");
		map.put("name", "批准人员");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ApplyFileCode");
		map.put("value", "");
		map.put("name", "批准日期");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "ScrapReason");
		map.put("name", "处置原因");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "ScrapMemo");
		map.put("name", "处置备注");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "RecordFileName");
		map.put("name", "处置结果");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "登记图片");
		listRegister.add(map);
	}

	void setupRepair() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegUserId");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegDeptId");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "RegDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "RegCharge");
		map.put("name", "维修费用");
		map.put("inputtype", String.valueOf(InputType.TYPE_CLASS_NUMBER));
		map.put("value", "");
		listRegister.add(map);

		//		map = new HashMap<String, Object>();
		//		map.put("type", "choose");
		//		map.put("id", "Int230");
		//		map.put("name", "维修商");
		//		map.put("value", "");
		//		map.put("searchid", "RepairFactory");// ///////////////////////////////////////////////special
		//		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "CompanyName");
		map.put("name", "送修单位");
		map.put("value", "");
		listRegister.add(map);
		//TODO
		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "CompUserName");
		map.put("name", "单位联系人");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "CompContactWay");
		map.put("name", "单位联系方式");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "CompAddress");
		map.put("name", "单位地址");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String190");
		map.put("name", "维修方法");
		map.put("value", "");
		listRegister.add(map);



		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String210");
		map.put("name", "更换配件");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "RegDesc");
		map.put("name", "维修备注");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "登记图片");
		listRegister.add(map);
	}

	void setupBorrow() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorUserId");
		map.put("name", "借用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDeptId");
		map.put("name", "借用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Int190");
		map.put("name", "经办人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Int200");
		map.put("name", "经办部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "BorDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "PredictReturnDt");
		map.put("value", "");
		map.put("name", "预计归还时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "BorReason");
		map.put("name", "借用原因");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "BorMemo");
		map.put("name", "借用备注");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "登记图片");
		listRegister.add(map);

	}

	void setupAllocate() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ReceUserId");
		map.put("name", "接收人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ReceDeptId");
		map.put("name", "接收部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "FlitDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "address");
		map.put("id", "String130");
		map.put("name", "存放地点");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "ManageDeptId");
		map.put("name", "管理部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String110");
		map.put("name", "批准人员");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "String120");
		map.put("value", "");
		map.put("name", "批准日期");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "FlitMemo");
		map.put("name", "调拨备注");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "登记图片");
		listRegister.add(map);
	}


	String getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String strMonth = null;
		if( month < 10 )
		{
			strMonth = "0"+month;

		}
		else
		{
			strMonth = month +"";
		}

		String strDay = null;
		if( day < 10 )
		{
			strDay = "0"+day;

		}
		else
		{
			strDay = day +"";
		}

		return year + "-" + strMonth + "-" + strDay;	
	}

	void setupUse() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawUserId");
		map.put("name", "领用人员");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawDeptId");
		map.put("name", "领用部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "DrawDt");
		map.put("value", getCurrentDate());
		map.put("realvalue", getCurrentDate());
		map.put("name", "登记时间");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String130");
		map.put("value", "");
		map.put("name", "资产用途");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "address");
		map.put("id", "SaveAddressName");
		map.put("name", "存放地点");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "Int150");
		map.put("name", "管理部门");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listRegister.add(map);

		//		map = new HashMap<String, Object>();
		//		map.put("type", "choose");
		//		map.put("id", "Int240");
		//		map.put("name", "设备保管人");
		//		map.put("value", "");
		//		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		//		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "String90");
		map.put("name", "批准人员");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "String120");
		map.put("value", "");
		map.put("name", "批准日期");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listRegister.add(map);

		//		map = new HashMap<String, Object>();
		//		map.put("type", "edit");
		//		map.put("id", "String270");
		//		map.put("value", "");
		//		map.put("name", "设备IP地址");
		//		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "DrawMemo");
		map.put("name", "领用备注");
		map.put("value", "");
		listRegister.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "登记图片");
		listRegister.add(map);
	}

	boolean warncannotempty(String[] warn) {
		for (String warnid : warn) {
			for (Map<String, Object> map : listRegister) {
				String id = (String) map.get("id");
				if (id != null && id.equals(warnid)) {

					String realvalue = (String) map.get("realvalue");

					if (realvalue == null || realvalue.length() == 0) {
						String warnname = null;

						if (id.equals("DrawUserId")) {
							warnname = "领用人员";
						} else if (id.equals("DrawDeptId")) {
							warnname = "领用部门";
						} else if (id.equals("Int240")) {
							warnname = "设备保管人";
						} else if (id.equals("ReceUserId")) {
							warnname = "接收人员";
						} else if (id.equals("ReceDeptId")) {
							warnname = "接收部门";
						} else if (id.equals("BorUserId")) {
							warnname = "借用人员";
						} else if (id.equals("BorDeptId")) {
							warnname = "借用部门";
						} else if (id.equals("Int190")) {
							warnname = "经办人员";
						} else if (id.equals("Int200")) {
							warnname = "经办部门";
						} else if (id.equals("RegUserId")) {
							warnname = "经办人员";
						} else if (id.equals("RegDeptId")) {
							warnname = "经办部门";
						} else if (id.equals("ScrapUserId")) {
							warnname = "经办人员";
						} else if (id.equals("ScrapDeptId")) {
							warnname = "经办部门";
						} else if (id.equals("ScrapType")) {
							warnname = "处置方式";
						} else if (id.equals("RetDeptId")) {
							warnname = "经办部门";
						} else if (id.equals("Int130")) {
							warnname = "退库接收部门";
						} else if (id.equals("RetUserId")) {
							warnname = "经办人员";
						}

						new AlertDialog.Builder(RegisterActivity.this)
						.setTitle("提示")// 设置对话框标题
						.setMessage(warnname + "不能为空!")// 设置显示的内容
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {// 添加确定按钮
							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {// 确定按钮的响应事件

							}
						}).show();// 在按键响应事件中显示此对话框
						return false;

					}

				}

			}

		}

		return true;
	}

	void commitForm(String IsCheck) {
		if (MenuId.equals("29")) {
			String[] warn = { "DrawUserId", "DrawDeptId", "Int240" };
			if (warncannotempty(warn) == false) {
				return;
			}
		} else if (MenuId.equals("39")) {
			String[] warn = { "ReceUserId", "ReceDeptId" };
			if (warncannotempty(warn) == false) {
				return;
			}
		} else if (MenuId.equals("34")) {
			String[] warn = { "BorUserId", "BorDeptId", "Int190", "Int200" };
			if (warncannotempty(warn) == false) {
				return;
			}
		} else if (MenuId.equals("48")) {
			String[] warn = { "RegUserId", "RegDeptId" };
			if (warncannotempty(warn) == false) {
				return;
			}
		} else if (MenuId.equals("43")) {
			String[] warn = { "ScrapUserId", "ScrapDeptId", "ScrapType" };
			if (warncannotempty(warn) == false) {
				return;
			}
		} else if (MenuId.equals("31")) {
			String[] warn = { "RetDeptId", "Int130", "RetUserId" };
			if (warncannotempty(warn) == false) {
				return;
			}
		}

		if (datas.size() == 0) {
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("提示")
			// 设置对话框标题
			.setMessage("资产信息" + "不能为空!")
			// 设置显示的内容
			.setPositiveButton("是",
					new DialogInterface.OnClickListener() {// 添加确定按钮
				@Override
				public void onClick(DialogInterface dialog,
						int which) {// 确定按钮的响应事件

				}
			}).show();// 在按键响应事件中显示此对话框
			return;
		}

		RegisterActivity.this.Prepare(param);

		Map<String, Object> mapMsAsset = new HashMap<String, Object>();
		for (Map<String, Object> map : listRegister) {
			if (map.get("id") != null && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false
					&& map.get("id").equals("imgGuid") == false) {
				mapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
		}

		String AssetIds = null;
		for (Map<String, String> item : datas) {
			String AssetId = item.get("AssetId");
			if (AssetIds == null) {
				AssetIds = AssetId;
			} else {
				AssetIds = AssetIds + "," + AssetId;
			}
		}

		mapMsAsset.put("AssetIds", AssetIds);
		mapMsAsset.put("IsCheck", IsCheck);

		if (MenuId.equals("29")) {
			mapMsAsset.put("DrawRecId", OrderId);
			mapMsAsset.put("DrawCode", OrderCode);
			param.put("MsDraw", mapMsAsset);
		} else if (MenuId.equals("39")) {
			mapMsAsset.put("FlitRecId", OrderId);
			mapMsAsset.put("FlitCode", OrderCode);
			param.put("MsMove", mapMsAsset);
		} else if (MenuId.equals("34")) {
			mapMsAsset.put("BorRecId", OrderId);
			mapMsAsset.put("BorrowCode", OrderCode);
			param.put("MsBorrow", mapMsAsset);
		} else if (MenuId.equals("48")) {
			mapMsAsset.put("RegRecId", OrderId);
			mapMsAsset.put("RegCode", OrderCode);
			param.put("MsRepairReg", mapMsAsset);
		} else if (MenuId.equals("43")) {
			mapMsAsset.put("ScrapRecId", OrderId);
			mapMsAsset.put("ScrapCode", OrderCode);
			param.put("MsScrap", mapMsAsset);
		} else if (MenuId.equals("31")) {
			mapMsAsset.put("RetRecId", OrderId);
			mapMsAsset.put("RetRecCode", OrderCode);
			param.put("MsRevert", mapMsAsset);
		}

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(RegisterActivity.this);
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

				if (res.equals("")) {
					m_pDialog.hide();
					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {
					RequestRetModel<RegisterRetModel> model = gson.fromJson(
							res,
							new TypeToken<RequestRetModel<RegisterRetModel>>() {
							}.getType());
					if (model != null) {

						Log.e("success", "successsuccesssuccesssuccesssuccess");

						m_pDialog.hide();

						String ErrorMsg = model.response.ErrorMsg;
						Common.ShowInfo(context, ErrorMsg);

						if (model.response.ErrorCode.equals("S00000") == false) {
							return;
						}

						OrderId = model.rspcontent.OrderId;
						OrderCode = model.rspcontent.OrderCode;

						if( model.rspcontent.IsCheck.equals("2") )
						{
							Button btn = (Button)findViewById(R.id.btn_save);
							btn.setEnabled(false);

							btn = (Button)findViewById(R.id.btn_confirm);
							btn.setEnabled(false);
						}

						if (model.response.ErrorCode.equals("S00000")) {
							List<Map<String, String>> listImgGuid = new ArrayList<Map<String, String>>();
							for (Map<String, Object> map : listRegister) {
								String type = (String) map.get("type");

								if (type.equals("pic")) {

									Bitmap bmp = (Bitmap) map.get("image");
									if (bmp != null) {
										String base64 = bitmapToBase64(bmp,70);
										Map<String, String> item = new HashMap<String, String>();
										item.put("ImageData", base64);
										listImgGuid.add(item);

									}

								}
							}

							RegisterActivity.this.Prepare(param);

							Map<String, Object> MsAttch = new HashMap<String, Object>();
							MsAttch.put("OriderId", model.rspcontent.OrderId);
							MsAttch.put("AttchType", mapAttchType.get(MenuId));

							param.put("MsAttch", MsAttch);
							// if( listImgGuid.size() > 0 )
							{
								param.put("ImgGuid", listImgGuid);
							}
							doAsync(new CallEarliest<String>() {
								public void onCallEarliest() throws Exception {

									m_pDialog = new ProgressDialog(
											RegisterActivity.this);
									m_pDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									m_pDialog.setMessage("正在保存图片数据...");
									m_pDialog.setCancelable(false);
									m_pDialog.show();

								}
							}, new Callable<String>() {
								public String call() throws Exception {
									return HttpHelper.getInstance(context)
											.Post(ApiAddressHelper
													.getSavePicUrl(MenuId),
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
										RequestRetModel<String> model = gson
												.fromJson(
														res,
														new TypeToken<RequestRetModel<String>>() {
														}.getType());
										if (model != null) {

											Log.e("success",
													"successsuccesssuccesssuccesssuccess");
											SharedPreferences preferences = SP(); 
											Editor editor = preferences.edit();
											editor.clear();
											editor.commit();
											StaticUtil.forRegisterActivity=2;

											m_pDialog.hide();

											String ErrorMsg = model.response.ErrorMsg;
											if( ErrorMsg != null && ErrorMsg.length() > 0 )
											{
												Common.ShowInfo(context, ErrorMsg);
											}

										} else {
											m_pDialog.hide();
											Common.ShowInfo(context, "网络异常");
											Log.e("fail",
													"failfailfailfailfailfailfailfailfail");
										}
									} catch (JsonSyntaxException localJsonSyntaxException) {
										m_pDialog.hide();

									}
								}
							});
						}
					} else {
						m_pDialog.hide();
						Common.ShowInfo(context, "网络异常");
						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {
					m_pDialog.hide();

				}
			}
		});

	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button) this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(RegisterActivity.this,
						CaptureActivity.class);
				startActivityForResult(it, 1);
			}
		});

		btn = (Button) this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(RegisterActivity.this,
						CaptureActivity.class);
				startActivityForResult(it, 2);
			}
		});

		btn = (Button) this.findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,
						SearchActivity.class);

				intent.putExtra("title", "资产查询");
				intent.putExtra("MenuId", "21");
				intent.putExtra("Module", mapModule.get(MenuId));
				intent.putExtra("RealMenuId", MenuId);

				RegisterActivity.this.startActivityForResult(intent, 4);

			}
		});

		btn = (Button) this.findViewById(R.id.btn_save);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				commitForm("0");

			}

		});

		btn = (Button) this.findViewById(R.id.btn_confirm);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				commitForm("2");

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 1: {

				AssetInfoModel model = new AssetInfoModel();
				model.AssetCode = data.getStringExtra("result");

				getAssetInfo(model);

			}
			break;
			case 2: {

				AssetInfoModel model = new AssetInfoModel();
				model.SerialNumber = data.getStringExtra("result");

				getAssetInfo(model);

			}
			break;

			case 3: {
				Log.e("camera", "recv");
				Bitmap bmp = getBitmap();
				String base64 = getBase64();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", "pic");
				map.put("image", bmp);
				map.put("data", base64);
				listRegister.add(map);

				registerAdapter.notifyDataSetChanged();

				ListView lv = (ListView) this.findViewById(R.id.searchlist);

				lv.post(new Runnable() {
					@Override
					public void run() {
						// Select the last row so it will scroll into view...
						ListView lv = (ListView) RegisterActivity.this
								.findViewById(R.id.searchlist);
						lv.setSelection(registerAdapter.getCount() - 1);
					}
				});

			}
			break;

			case 4: {

				Map<String, String> item = null;

				item = new HashMap<String, String>();

				item.put("AssetId", data.getStringExtra("AssetId"));
				item.put("data_" + 0, data.getStringExtra("AssetCode"));
				item.put("data_" + 1, data.getStringExtra("AssetName"));
				item.put("data_" + 2, data.getStringExtra("Standard"));
				item.put("data_" + 3, data.getStringExtra("SerialNumber"));

				datas.add(item);

				adapter.notifyDataSetChanged();

			}
			break;

			case 5 :{
				String result = data.getExtras().getString("result");
				if(result!=null){
					Address.setAddress(result);
					//					adapter.notifyDataSetChanged();
					registerAdapter.notifyDataSetChanged();
				}
				Log.i("result", result);
			}
			break;
			default:
				break;
			}

		}
	}

	void getAssetInfo(AssetInfoModel model) {
		Prepare(param);
		param.put("MenuId", MenuId);

		Map<String, String> mapMsAsset = new HashMap<String, String>();
		mapMsAsset.put("AssetCode", model.AssetCode);
		mapMsAsset.put("AssetName", model.AssetName);
		mapMsAsset.put("Standard", model.Standard);
		mapMsAsset.put("SerialNumber", model.SerialNumber);
		mapMsAsset.put("Module", mapModule.get(MenuId));

		param.put("MsAsset", mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(RegisterActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在查询数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getAssetformodel(), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				m_pDialog.hide();
				if (res.equals("")) {

					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {

					Log.e("baseAdapter.notifyDataSetChanged();1111111111111111111111111",
							"baseAdapter.notifyDataSetChanged();");

					JSONObject respJson = new JSONObject(res);

					JSONObject response = respJson.getJSONObject("response");

					String ErrorCode = response.getString("ErrorCode");
					if (ErrorCode.equals("S00000") == false) {

						String ErrorMsg = response.getString("ErrorMsg");
						Common.ShowInfo(context, ErrorMsg);
						return;
					}

					RequestRetModel<AssetModel> getmodel = gson.fromJson(res,
							new TypeToken<RequestRetModel<AssetModel>>() {
					}.getType());
					if (getmodel != null) {

						for (AssetInfoModel item : getmodel.rspcontent.MsAsset) 
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
							data.put("data_" + 0, item.AssetCode);
							data.put("data_" + 1, item.AssetName);
							data.put("data_" + 2, item.Standard);
							data.put("data_" + 3, item.SerialNumber);

							datas.add(data);

						}

						adapter.notifyDataSetChanged();

					} else {

						Common.ShowInfo(context, "网络异常");
						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}

				} catch (JSONException localJsonSyntaxException) {

					Log.e("222222222222222222222222222222222222",
							localJsonSyntaxException.getMessage());

				} catch (JsonSyntaxException localJsonSyntaxException) {

					RequestRetModel<String> model = gson.fromJson(res,
							new TypeToken<RequestRetModel<String>>() {
					}.getType());
					if (model != null) {

						Common.ShowInfo(context, model.rspcontent);

					}

				}

			}
		});

	}

	//缓存界面的值
	private void saveData(){
		Map<String, Object> mapMsAsset = new HashMap<String, Object>();
		for (Map<String, Object> map : listRegister) {
			if (map.get("id") != null && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false
					&& map.get("id").equals("imgGuid") == false) {
				mapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
		}

		String string = gson.toJson(mapMsAsset);
		SharedPreferences preferences = SP(); 
		Editor editor = preferences.edit();
		editor.putString("information", string);
		editor.commit();
	}

	//为界面赋值缓存的值
	private void setDataforRegister(){
		SharedPreferences preferences = SP(); 
		String string = preferences.getString("information", null);
		Map<String, Object> mapMsAsset = new HashMap<String, Object>();
		if (string != null) {
			mapMsAsset = gson.fromJson(string, new TypeToken<HashMap<String, Object>>() {}.getType());
			Map<String, Object> assetmap = mapMsAsset;
			for (Map<String, Object> map : listRegister) {
				String id = (String) map.get("id");
				if (assetmap.get(id) != null) {
					String name = id + "Name";
					String name2 = id.replace("Id", "Name");
					if (assetmap.get(name) != null) {
						map.put("value", (String) assetmap.get(name));
						map.put("realvalue", (String) assetmap.get(id));
					} else if (assetmap.get(name2) != null) {
						map.put("value", (String) assetmap.get(name2));
						map.put("realvalue", (String) assetmap.get(id));
					} else {
						map.put("value", (String) assetmap.get(id));
						map.put("realvalue", (String) assetmap.get(id));
					}
				}
			}
		}
	}

	private SharedPreferences SP(){
		SharedPreferences preferences = null;
		if (MenuId.equals("29")) {
			preferences = RegisterActivity.this.getSharedPreferences("29", Context.MODE_PRIVATE);
		} else if (MenuId.equals("39")) {
			preferences = RegisterActivity.this.getSharedPreferences("39", Context.MODE_PRIVATE);
		} else if (MenuId.equals("34")) {
			preferences = RegisterActivity.this.getSharedPreferences("34", Context.MODE_PRIVATE);
		} else if (MenuId.equals("48")) {
			preferences = RegisterActivity.this.getSharedPreferences("48", Context.MODE_PRIVATE);
		} else if (MenuId.equals("43")) {
			preferences = RegisterActivity.this.getSharedPreferences("43", Context.MODE_PRIVATE);
		} else if (MenuId.equals("31")) {
			preferences = RegisterActivity.this.getSharedPreferences("32", Context.MODE_PRIVATE);
		}
		return preferences;
	}
}
