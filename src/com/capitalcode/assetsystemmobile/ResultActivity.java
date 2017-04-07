package com.capitalcode.assetsystemmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.business.AssetAddActivity;
import com.capitalcode.assetsystemmobile.business.AssetBrowseActivity;
import com.capitalcode.assetsystemmobile.business.RegisterActivity;
import com.capitalcode.assetsystemmobile.model.AssetHistoryModel;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetListInfoModel;
import com.capitalcode.assetsystemmobile.model.MsBorrow;
import com.capitalcode.assetsystemmobile.model.MsBorrowModel;
import com.capitalcode.assetsystemmobile.model.MsDraw;
import com.capitalcode.assetsystemmobile.model.MsDrawModel;
import com.capitalcode.assetsystemmobile.model.MsMove;
import com.capitalcode.assetsystemmobile.model.MsMoveModel;
import com.capitalcode.assetsystemmobile.model.MsRepairReg;
import com.capitalcode.assetsystemmobile.model.MsRepairRegModel;
import com.capitalcode.assetsystemmobile.model.MsRet;
import com.capitalcode.assetsystemmobile.model.MsRetModel;
import com.capitalcode.assetsystemmobile.model.MsRevert;
import com.capitalcode.assetsystemmobile.model.MsRevertModel;
import com.capitalcode.assetsystemmobile.model.MsScrap;
import com.capitalcode.assetsystemmobile.model.MsScrapModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.SerializableList;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends BaseActivity implements View.OnClickListener {

	private ListView mListView;

	ScrollAdapter adapter;
	int pagecount = 0;
	int currentpage = 1;

	static public AssetListInfoModel assetmodel;
	static public MsDrawModel msdrawmodel;
	static public MsMoveModel msmovemodel;
	static public MsBorrowModel msborrowmodel;
	static public MsRetModel msretmodel;
	static public MsRepairRegModel msrepairmodel;
	static public MsScrapModel msscrapmodel;
	static public MsRevertModel msrevertmodel;
	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

	String RealMenuId;

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
		Bundle bundle = getIntent().getExtras();
		SerializableMap serializableMap = (SerializableMap) bundle.get("param");
		this.param = serializableMap.getMap();

	}

	@Override
	protected void Destroy() {
		// TODO Auto-generated method stub

	}

	void setupData() {
		if (MenuId.equals("19") || MenuId.equals("21")) {
			datas.clear();
			pagecount = Integer.valueOf(assetmodel.PageCount) / 10;

			int other = Integer.valueOf(assetmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (AssetInfoModel model : assetmodel.MsAsset) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.AssetCode);
				data.put("data_" + 1, model.AssetName);
				data.put("data_" + 2, model.Standard);
				data.put("data_" + 3, model.SerialNumber);

				datas.add(data);
			}

		} else if (MenuId.equals("207")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("领用单编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("领用部门");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("领用人员");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			datas.clear();
			pagecount = Integer.valueOf(msdrawmodel.PageCount) / 10;

			int other = Integer.valueOf(msdrawmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsDraw model : msdrawmodel.MsDraw) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.DrawCode);
				data.put("data_" + 1, model.DrawDeptName);
				data.put("data_" + 2, model.DrawUserName);
				data.put("data_" + 3, model.DrawStateName);

				datas.add(data);
			}

		} else if (MenuId.equals("206")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("单据编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("接收部门");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("接收人员");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			datas.clear();
			pagecount = Integer.valueOf(msmovemodel.PageCount) / 10;

			int other = Integer.valueOf(msmovemodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsMove model : msmovemodel.MsMove) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.FlitCode);
				data.put("data_" + 1, model.ReceDeptName);
				data.put("data_" + 2, model.ReceUserName);
				data.put("data_" + 3, model.Status);

				datas.add(data);
			}
		} else if (MenuId.equals("209")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("单据编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("借用部门");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("借用人员");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			datas.clear();
			pagecount = Integer.valueOf(msborrowmodel.PageCount) / 10;

			int other = Integer.valueOf(msborrowmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsBorrow model : msborrowmodel.MsBorrow) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.BorrowCode);
				data.put("data_" + 1, model.BorDeptName);
				data.put("data_" + 2, model.BorUserName);
				data.put("data_" + 3, model.BorStateName);

				datas.add(data);
			}
		} else if (MenuId.equals("321")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("资产编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("资产名称");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("借用时间");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("归还时间");

			datas.clear();
			pagecount = Integer.valueOf(msretmodel.PageCount) / 10;

			int other = Integer.valueOf(msretmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsRet model : msretmodel.MsRet) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.AssetCode);
				data.put("data_" + 1, model.AssetName);
				data.put("data_" + 2, model.BorDt);
				data.put("data_" + 3, model.RetDt);

				datas.add(data);
			}
		} else if (MenuId.equals("211")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("资产编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("维修单编号");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("资产名称");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			tv = (TextView) this.findViewById(R.id.title5);
			tv.setText("操作");
			tv.setVisibility(View.VISIBLE);

			datas.clear();
			pagecount = Integer.valueOf(msrepairmodel.PageCount) / 10;

			int other = Integer.valueOf(msrepairmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsRepairReg model : msrepairmodel.MsRepairReg) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.AssetCode);
				data.put("data_" + 1, model.RegCode);
				data.put("data_" + 2, model.AssetName);
				data.put("data_" + 3, model.RegStateName);

				if (model.RegStateName.equals("维修中")) {
					// data.put("lastbutton", "true");
					data.put("data_" + 4, "维修完成");
				} else {
					// data.put("lastbutton", null);
					data.put("data_" + 4, "");
				}

				datas.add(data);
			}
		} else if (MenuId.equals("210")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("处置单编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("经办部门");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("经办人员");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			datas.clear();
			pagecount = Integer.valueOf(msscrapmodel.PageCount) / 10;

			int other = Integer.valueOf(msscrapmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsScrap model : msscrapmodel.MsScrap) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.ScrapCode);
				data.put("data_" + 1, model.ScrapDeptName);
				data.put("data_" + 2, model.ScrapUserName);
				data.put("data_" + 3, model.ScrapStatusName);

				datas.add(data);
			}
		} else if (MenuId.equals("208")) {
			TextView tv = (TextView) this.findViewById(R.id.title1);
			tv.setText("单据编号");

			tv = (TextView) this.findViewById(R.id.title2);
			tv.setText("经办部门");

			tv = (TextView) this.findViewById(R.id.title3);
			tv.setText("经办人员");

			tv = (TextView) this.findViewById(R.id.title4);
			tv.setText("单据状态");

			datas.clear();
			pagecount = Integer.valueOf(msrevertmodel.PageCount) / 10;

			int other = Integer.valueOf(msrevertmodel.PageCount) % 10;
			if (other != 0) {
				pagecount++;
			}

			Map<String, String> data = null;
			for (MsRevert model : msrevertmodel.MsRevert) {
				data = new HashMap<String, String>();

				data.put("data_" + 0, model.RetRecCode);
				data.put("data_" + 1, model.RetDeptName);
				data.put("data_" + 2, model.RetUserName);
				data.put("data_" + 3, model.RetStateName);

				datas.add(data);
			}
		}

	}

	void delFormInfo(int index) {
		formstring = null;
		Map<String, String> mapMsAsset = new HashMap<String, String>();

		if (MenuId.equals("207")) {
			MsDraw model = msdrawmodel.MsDraw.get(index);
			mapMsAsset.put("DrawRecId", model.DrawRecId);
			formstring = "MsDraw";
		} else if (MenuId.equals("208")) {
			MsRevert model = msrevertmodel.MsRevert.get(index);
			mapMsAsset.put("RetRecId", model.RetRecId);
			formstring = "MsRevert";
		} else if (MenuId.equals("206")) {
			MsMove model = msmovemodel.MsMove.get(index);
			mapMsAsset.put("FlitRecId", model.FlitRecId);
			formstring = "MsMove";
		} else if (MenuId.equals("209")) {
			MsBorrow model = msborrowmodel.MsBorrow.get(index);
			mapMsAsset.put("BorRecId", model.BorRecId);
			formstring = "MsBorrow";
		} else if (MenuId.equals("211")) {
			MsRepairReg model = msrepairmodel.MsRepairReg.get(index);
			mapMsAsset.put("RegRecId", model.RegRecId);
			formstring = "MsRepairReg";
		} else if (MenuId.equals("210")) {
			MsScrap model = msscrapmodel.MsScrap.get(index);
			mapMsAsset.put("ScrapRecId", model.ScrapRecId);
			formstring = "MsScrap";
		} else {
			return;
		}

		Prepare(clickparam);
		clickparam.put(formstring, mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(ResultActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.getDelUrl(MenuId), clickparam);
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

					JSONObject response = respJson.getJSONObject("response");

					String ErrorCode = response.getString("ErrorCode");
					if (ErrorCode.equals("S00000") == false) {

						String ErrorMsg = response.getString("ErrorMsg");
						Common.ShowInfo(context, ErrorMsg);
						return;

					} else {
						getList("正在刷新数据...");
					}

				} catch (JSONException localJsonSyntaxException) {

					Log.e("parse", localJsonSyntaxException.getMessage());

				}
			}
		});

	}

	String formstring = null;
	String statename = null;

	void getFormInfo(int index) {
		formstring = null;
		Map<String, String> mapMsAsset = new HashMap<String, String>();

		if (MenuId.equals("207")) {
			MsDraw model = msdrawmodel.MsDraw.get(index);
			mapMsAsset.put("DrawRecId", model.DrawRecId);
			formstring = "MsDraw";
			statename = model.DrawStateName;
		} else if (MenuId.equals("208")) {
			MsRevert model = msrevertmodel.MsRevert.get(index);
			mapMsAsset.put("RetRecId", model.RetRecId);
			formstring = "MsRevert";
			statename = model.RetStateName;
		} else if (MenuId.equals("206")) {
			MsMove model = msmovemodel.MsMove.get(index);
			mapMsAsset.put("FlitRecId", model.FlitRecId);
			formstring = "MsMove";
			statename = model.Status;
		} else if (MenuId.equals("209")) {
			MsBorrow model = msborrowmodel.MsBorrow.get(index);
			mapMsAsset.put("BorRecId", model.BorRecId);
			formstring = "MsBorrow";
			statename = model.BorStateName;
		} else if (MenuId.equals("211")) {
			MsRepairReg model = msrepairmodel.MsRepairReg.get(index);
			mapMsAsset.put("RegRecId", model.RegRecId);
			formstring = "MsRepairReg";
			statename = model.RegStateName;
		} else if (MenuId.equals("210")) {
			MsScrap model = msscrapmodel.MsScrap.get(index);
			mapMsAsset.put("ScrapRecId", model.ScrapRecId);
			formstring = "MsScrap";
			statename = model.ScrapStatusName;
		} else {
			return;
		}

		Prepare(clickparam);
		clickparam.put(formstring, mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(ResultActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.getResultUrl(MenuId), clickparam);
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

					JSONObject response = respJson.getJSONObject("response");

					String ErrorCode = response.getString("ErrorCode");
					if (ErrorCode.equals("S00000") == false) {
						m_pDialog.hide();
						String ErrorMsg = response.getString("ErrorMsg");
						Common.ShowInfo(context, ErrorMsg);
						return;
					}

					HashMap<String, Object> formmap = new HashMap<String, Object>();
					List<Object> listassetmap = new ArrayList<Object>();
					List<Object> listImgGuid = new ArrayList<Object>();

					JSONObject rspcontent = respJson.getJSONObject("rspcontent");

					JSONObject Form = rspcontent.getJSONObject(formstring);

					JSONArray MsAsset = Form.getJSONArray("MsAsset");

					for (int i = 0; i < MsAsset.length(); i++) {
						JSONObject item = MsAsset.getJSONObject(i);

						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("AssetId", item.get("AssetId").toString());
						map.put("AssetCode", item.get("AssetCode").toString());
						map.put("AssetName", item.get("AssetName").toString());
						map.put("Standard", item.get("Standard").toString());
						map.put("SerialNumber", item.get("SerialNumber").toString());

						listassetmap.add(map);

					}

					JSONArray ImgGuid = Form.getJSONArray("ImgGuid");

					RegisterActivity.listImgGuid.clear();
					if (ImgGuid != null) {
						for (int i = 0; i < ImgGuid.length(); i++) {
							JSONObject item = ImgGuid.getJSONObject(i);
							String base64 = item.get("ImageData").toString();
							Log.e("base64", base64);

							// listImgGuid.add(base64);
							RegisterActivity.listImgGuid.add(base64);

						}
					}

					Iterator keys = Form.keys();
					while (keys.hasNext()) {
						String key = (String) keys.next();
						if (key.equals("MsAsset") == false && key.equals("ImgGuid") == false) {
							String value = Form.get(key).toString();
							formmap.put(key, value);
						}
					}

					Log.e("pagepage", String.valueOf(listImgGuid.size()));

					m_pDialog.hide();

					Intent intent = new Intent(ResultActivity.this, RegisterActivity.class);

					intent.putExtra("MenuId", mapSearchToReg.get(MenuId));
					intent.putExtra("title", mapIdToName.get(mapSearchToReg.get(MenuId)));

					SerializableList tmplist = new SerializableList();
					tmplist.setList(listImgGuid);

					SerializableList tmplist2 = new SerializableList();
					tmplist2.setList(listassetmap);

					SerializableMap tmpmap = new SerializableMap();
					tmpmap.setMap(formmap);

					Bundle bundle = new Bundle();
					bundle.putSerializable("param", tmpmap);
					// bundle.putSerializable("ImgGuid", tmplist);
					bundle.putSerializable("asset", tmplist2);
					bundle.putString("statename", statename);
					intent.putExtras(bundle);

					ResultActivity.this.startActivity(intent);

				} catch (JSONException localJsonSyntaxException) {

					m_pDialog.hide();
					Log.e("parse", localJsonSyntaxException.getMessage());

				}
			}
		});

	}

	Map<String, Object> clickparam = new HashMap<String, Object>();

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_result);

		RealMenuId = this.getIntent().getStringExtra("RealMenuId");

		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		if (MenuId.equals("19")) {
			btn.setVisibility(View.VISIBLE);
			btn.setText("批量修改");
		} else {
			btn.setVisibility(View.GONE);
		}

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText("查询结果");

		setupData();

		mListView = (ListView) findViewById(R.id.scroll_list);

		if (MenuId.equals("211")) {
			adapter = new ScrollAdapter(this, datas, R.layout.item,
					new String[] { "data_0", "data_1", "data_2", "data_3", "data_4" },
					new int[] { R.id.item_data0, R.id.item_data1, R.id.item_data2, R.id.item_data3, R.id.item_data4 });
		} else {
			adapter = new ScrollAdapter(this, datas, R.layout.item,
					new String[] { "data_0", "data_1", "data_2", "data_3", },
					new int[] { R.id.item_data0, R.id.item_data1, R.id.item_data2, R.id.item_data3 });
		}
		adapter.setOnClickListener(this);

		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				if (MenuId.equals("21")) {
					if (RealMenuId == null) {
						Prepare(clickparam);
						clickparam.put("MenuId", "19");

						AssetInfoModel model = assetmodel.MsAsset.get((int) arg3);

						Map<String, String> mapMsAsset = new HashMap<String, String>();
						mapMsAsset.put("AssetCode", model.AssetCode);
						mapMsAsset.put("AssetName", model.AssetName);
						mapMsAsset.put("Standard", model.Standard);
						mapMsAsset.put("SerialNumber", model.SerialNumber);

						clickparam.put("MsAsset", mapMsAsset);

						doAsync(new CallEarliest<String>() {
							public void onCallEarliest() throws Exception {

								m_pDialog = new ProgressDialog(ResultActivity.this);
								m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								m_pDialog.setMessage("正在获取数据...");
								m_pDialog.setCancelable(false);
								m_pDialog.show();

							}
						}, new Callable<String>() {
							public String call() throws Exception {
								return HttpHelper.getInstance(context).Post(ApiAddressHelper.getResultUrl(MenuId),
										clickparam);
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

									JSONObject response = respJson.getJSONObject("response");

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

									JSONObject rspcontent = respJson.getJSONObject("rspcontent");
									JSONObject MsAsset = rspcontent.getJSONObject("MsAsset");
									if (MsAsset != null) {
										Iterator keys = MsAsset.keys();
										while (keys.hasNext()) {
											String key = (String) keys.next();
											if (key.equals("ImgGuid") == false) {

												if (MsAsset.get(key) != null) {
													String value = MsAsset.get(key).toString();
													assetmap.put(key, value);
												} else {
													assetmap.put(key, "");
												}
											} else {
												JSONArray ImgGuid = MsAsset.getJSONArray("ImgGuid");
												AssetBrowseActivity.listImgGuid.clear();
												if (ImgGuid != null) {
													for (int i = 0; i < ImgGuid.length(); i++) {
														JSONObject item = ImgGuid.getJSONObject(i);
														String base64 = item.getString("ImageData");
														// listImgGuid.add(base64);
														AssetBrowseActivity.listImgGuid.add(base64);
													}
												}
											}
										}
									}

									JSONArray History = rspcontent.getJSONArray("MsAssetHistory");
									if (History != null) {
										String strHistory = History.toString();
										AssetBrowseActivity.historymodel = gson.fromJson(strHistory,
												new TypeToken<List<AssetHistoryModel>>() {
										}.getType());
									}

									m_pDialog.hide();

									Intent intent = new Intent(ResultActivity.this, AssetBrowseActivity.class);

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

									ResultActivity.this.startActivity(intent);

								} catch (JSONException localJsonSyntaxException) {
									m_pDialog.hide();

								}
							}
						});
					} else // if( RealMenuId.equals("29") )
					{
						AssetInfoModel model = assetmodel.MsAsset.get((int) arg3);

						Intent it = new Intent();
						it.putExtra("AssetId", model.AssetId);
						it.putExtra("AssetCode", model.AssetCode);
						it.putExtra("AssetName", model.AssetName);
						it.putExtra("Standard", model.Standard);
						it.putExtra("SerialNumber", model.SerialNumber);

						setResult(RESULT_OK, it);
						finish();
					}

				} else if (MenuId.equals("19")) {

					Prepare(clickparam);

					AssetInfoModel model = assetmodel.MsAsset.get((int) arg3);

					Map<String, String> mapMsAsset = new HashMap<String, String>();
					mapMsAsset.put("AssetCode", model.AssetCode);
					mapMsAsset.put("AssetId", model.AssetId);
					mapMsAsset.put("SerialNumber", model.SerialNumber);

					clickparam.put("MsAsset", mapMsAsset);

					doAsync(new CallEarliest<String>() {
						public void onCallEarliest() throws Exception {

							m_pDialog = new ProgressDialog(ResultActivity.this);
							m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							m_pDialog.setMessage("正在获取数据...");
							m_pDialog.setCancelable(false);
							m_pDialog.show();

						}
					}, new Callable<String>() {
						public String call() throws Exception {
							return HttpHelper.getInstance(context).Post(ApiAddressHelper.getResultUrl(MenuId),
									clickparam);
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

								JSONObject response = respJson.getJSONObject("response");

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

								JSONObject rspcontent = respJson.getJSONObject("rspcontent");

								Iterator keys = rspcontent.keys();
								while (keys.hasNext()) {
									String key = (String) keys.next();
									if (key.equals("ImgGuid") == false) {
										String value = rspcontent.get(key).toString();
										assetmap.put(key, value);
									} else {
										JSONArray ImgGuid = rspcontent.getJSONArray("ImgGuid");
										AssetAddActivity.listImgGuid.clear();
										if (ImgGuid != null) {
											for (int i = 0; i < ImgGuid.length(); i++) {
												JSONObject item = ImgGuid.getJSONObject(i);
												String base64 = item.get("ImageData").toString();
												Log.e("base64", base64);

												// listImgGuid.add(base64);
												AssetAddActivity.listImgGuid.add(base64);

											}
										}

									}

								}

								Log.e("pagepage", String.valueOf(listImgGuid.size()));

								m_pDialog.hide();

								Intent intent = new Intent(ResultActivity.this, AssetAddActivity.class);

								intent.putExtra("MenuId", MenuId);
								intent.putExtra("title", "资产编辑");

								SerializableList tmplist = new SerializableList();
								tmplist.setList(listImgGuid);

								SerializableMap tmpmap = new SerializableMap();
								tmpmap.setMap(assetmap);
								Bundle bundle = new Bundle();
								bundle.putSerializable("param", tmpmap);
								bundle.putSerializable("ImgGuid", tmplist);
								intent.putExtras(bundle);

								ResultActivity.this.startActivity(intent);

							} catch (JSONException localJsonSyntaxException) {

								m_pDialog.hide();

							}
						}
					});

				} else {

					getFormInfo((int) arg3);

				}

			}

		});

		if (MenuId.equals("21") == false) {
			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					delitem = (int) arg3;
					new AlertDialog.Builder(ResultActivity.this).setTitle("操作")
							.setItems(new String[] { "删除" }, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							if (MenuId.equals("19")) {
								Prepare(clickparam);

								AssetInfoModel model = assetmodel.MsAsset.get(delitem);
								Map<String, String> item = new HashMap<String, String>();
								item.put("AssetId", model.AssetId);

								clickparam.put("MsAsset", item);

								doAsync(new CallEarliest<String>() {
									public void onCallEarliest() throws Exception {

										m_pDialog = new ProgressDialog(ResultActivity.this);
										m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										m_pDialog.setMessage("正在删除...");
										m_pDialog.setCancelable(false);
										m_pDialog.show();

									}
								}, new Callable<String>() {
									public String call() throws Exception {
										return HttpHelper.getInstance(context).Post(ApiAddressHelper.getDelUrl(MenuId),
												clickparam);
									}
								}, new Callback<String>() {
									public void onCallback(String res) {

										if (res.equals("")) {
											m_pDialog.hide();
											Common.ShowInfo(context, "网络故障");
											return;
										}

										RequestRetModel<String> model = gson.fromJson(res,
												new TypeToken<RequestRetModel<String>>() {
										}.getType());
										if (model != null) {

											Log.e("success", "successsuccesssuccesssuccesssuccess");

											m_pDialog.hide();

											if (model.response.ErrorCode.equals("S00000")) {

												getList("正在刷新数据...");

											} else {
												String ErrorMsg = model.response.ErrorMsg;
												Common.ShowInfo(context, ErrorMsg);
											}

										}

									}
								});

							} else if (MenuId.equals("21") == false) {

								delFormInfo(delitem);

							}

						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).show();
					return true;
				}

			});
		}

		tv = (TextView) this.findViewById(R.id.page);
		tv.setText("第" + this.currentpage + "页/共" + this.pagecount + "页");

		btn = (Button) this.findViewById(R.id.btn_pre);
		btn.setEnabled(false);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Button btn = (Button) arg0;
				if (ResultActivity.this.currentpage > 1) {
					ResultActivity.this.currentpage--;
				}

				if (ResultActivity.this.currentpage == 1) {
					btn.setEnabled(false);
				}

				if (ResultActivity.this.currentpage < ResultActivity.this.pagecount) {
					Button btn_next = (Button) ResultActivity.this.findViewById(R.id.btn_next);
					btn_next.setEnabled(true);
				}
				getList("正在刷新数据...");
			}

		}

		);

		btn = (Button) this.findViewById(R.id.btn_next);
		if (this.currentpage > this.pagecount) {
			this.currentpage = 0;
			this.pagecount = 0;

		}

		if (this.currentpage >= this.pagecount) {
			btn.setEnabled(false);
		}
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Button btn = (Button) arg0;
				if (ResultActivity.this.currentpage < ResultActivity.this.pagecount) {
					ResultActivity.this.currentpage++;
				}

				if (ResultActivity.this.currentpage == ResultActivity.this.pagecount) {
					btn.setEnabled(false);
				}

				if (ResultActivity.this.currentpage > 1) {
					Button btn_pre = (Button) ResultActivity.this.findViewById(R.id.btn_pre);
					btn_pre.setEnabled(true);
				}

				getList("正在刷新数据...");

			}

		}

		);

	}

	int delitem;
	String showprompt;

	void getList(String showprompt) {
		this.showprompt = showprompt;

		param.put("PageSize", "10");
		param.put("PageIndex", String.valueOf(this.currentpage));
		if (MenuId.equals("19") || MenuId.equals("21")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {

					if (res.equals("")) {
						m_pDialog.hide();
						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<AssetListInfoModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<AssetListInfoModel>>() {
						}.getType());
						if (model != null) {
							assetmodel = (AssetListInfoModel) model.rspcontent;
							Log.e("success", "successsuccesssuccesssuccesssuccess");

							m_pDialog.hide();
							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {
							m_pDialog.hide();
							Common.ShowInfo(context, "网络异常");
							Log.e("fail", "failfailfailfailfailfailfailfailfail");
						}
					} catch (JsonSyntaxException localJsonSyntaxException) {
						m_pDialog.hide();
						Log.e("iws", "Login json转换错误 e:" + localJsonSyntaxException);
						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("207")) {

			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsDrawModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsDrawModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msdrawmodel = (MsDrawModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("206")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsMoveModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsMoveModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msmovemodel = (MsMoveModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("209")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsBorrowModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsBorrowModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msborrowmodel = (MsBorrowModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("321")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsRetModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsRetModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msretmodel = (MsRetModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("211")) {

			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsRepairRegModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsRepairRegModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msrepairmodel = (MsRepairRegModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});

		} else if (MenuId.equals("210")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsScrapModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsScrapModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msscrapmodel = (MsScrapModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});
		} else if (MenuId.equals("208")) {
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(ResultActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage(ResultActivity.this.showprompt);
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {

						Common.ShowInfo(context, "网络故障");
						return;
					}
					try {
						RequestRetModel<MsRevertModel> model = gson.fromJson(res,
								new TypeToken<RequestRetModel<MsRevertModel>>() {
						}.getType());
						if (model != null) {
							ResultActivity.msrevertmodel = (MsRevertModel) model.rspcontent;

							setupData();

							adapter.notifyDataSetChanged();

							TextView tv = (TextView) ResultActivity.this.findViewById(R.id.page);
							tv.setText("第" + ResultActivity.this.currentpage + "页/共" + ResultActivity.this.pagecount
									+ "页");

						} else {

							Common.ShowInfo(context, "网络异常");

						}
					} catch (JsonSyntaxException localJsonSyntaxException) {

						Log.e("fail", "failfailfailfailfailfailfailfailfail");
					}
				}
			});
		}

	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*
				 * Intent intent = new Intent(ResultActivity.this,
				 * AssetAddActivity.class);
				 * 
				 * intent.putExtra("MenuId", MenuId); intent.putExtra("title",
				 * "资产修改");
				 * 
				 * intent.putExtra("isBatch", true); SerializableMap tmpmap=new
				 * SerializableMap(); tmpmap.setMap(param); Bundle bundle=new
				 * Bundle(); bundle.putSerializable("param", tmpmap);
				 * intent.putExtras(bundle);
				 * 
				 * 
				 * ResultActivity.this.startActivity(intent);
				 */

				param.put("PageSize", 10 * (Integer.valueOf(assetmodel.PageCount)) + "");
				param.put("PageIndex", "1");

				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {

						m_pDialog = new ProgressDialog(ResultActivity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();

					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSearchUrl(MenuId), param);
					}
				}, new Callback<String>() {
					public void onCallback(String res) {

						if (res.equals("")) {
							m_pDialog.hide();
							Common.ShowInfo(context, "网络故障");
							return;
						}
						try {
							RequestRetModel<AssetListInfoModel> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<AssetListInfoModel>>() {
							}.getType());
							if (model != null) {
								AssetAddActivity.assetmodel = (AssetListInfoModel) model.rspcontent;
								Log.e("success", "successsuccesssuccesssuccesssuccess");

								m_pDialog.hide();

								Intent intent = new Intent(ResultActivity.this, AssetAddActivity.class);

								intent.putExtra("MenuId", MenuId);
								intent.putExtra("title", "资产编辑");

								intent.putExtra("isBatch", true);
								ResultActivity.this.startActivity(intent);

							} else {
								m_pDialog.hide();
								Common.ShowInfo(context, "网络异常");
								Log.e("fail", "failfailfailfailfailfailfailfailfail");
							}
						} catch (JsonSyntaxException localJsonSyntaxException) {
							m_pDialog.hide();
							Log.e("iws", "Login json转换错误 e:" + localJsonSyntaxException);
							Log.e("fail", "failfailfailfailfailfailfailfailfail");
						}
					}
				});

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		MsRepairReg model = msrepairmodel.MsRepairReg.get(position);

		Prepare(clickparam);
		Map<String, String> mapMsAsset = new HashMap<String, String>();
		mapMsAsset.put("RegRecId", model.RegRecId);
		clickparam.put("MsRepairReg", mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(ResultActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在提交操作...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.finishrepair(), clickparam);
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

					JSONObject response = respJson.getJSONObject("response");

					String ErrorCode = response.getString("ErrorCode");
					if (ErrorCode.equals("S00000") == true) {

						getList("正在刷新数据...");

					} else {
						String ErrorMsg = response.getString("ErrorMsg");
						Common.ShowInfo(context, ErrorMsg);

					}

				} catch (JSONException e) {

				}

			}
		});

	}

}
