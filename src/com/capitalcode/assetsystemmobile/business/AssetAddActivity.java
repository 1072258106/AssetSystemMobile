package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetListInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetSaveRetModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.utils.Address;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.ImageFactory;
import com.capitalcode.assetsystemmobile.utils.SerializableList;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;
import com.capitalcode.assetsystemmobile.utils.StaticUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;
import com.zhy.tree_view.ComplexSelectActivity;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AssetAddActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

	private List<Map<String, Object>> listBase = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listFix = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newlistFix = listFix;
	public Map<String, String> pageLabel = this.basedataModel.PageLabel;
	ContentAdapter baseAdapter;
	ContentAdapter fixAdapter;

	private Map<String, Object> addmapMsAsset = new HashMap<String, Object>();;
	
	String assetId;
	boolean isBatch;
	static public AssetListInfoModel assetmodel;
	int indexBatch;

	static public List<Object> listImgGuid = new ArrayList<Object>();

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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onPause();
		if(StaticUtil.forAssetAddActivity==1){
			AssetsInformation();
		}
		StaticUtil.forAssetAddActivity=1;
	}

	void updatedata(Map<String, Object> assetmap, List<Object> listImgGuid) {
		if (assetmap != null) {
			assetId = (String) assetmap.get("assetId");
			for (Map<String, Object> map : listBase) {
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

			for (Map<String, Object> map : listFix) {
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

		if (listImgGuid != null) {
			for (Object base64 : listImgGuid) {

				Bitmap bmp = decodeImg((String) base64);
				if (bmp != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", "pic");
					map.put("image", bmp);
					map.put("data", (String) base64);
					listFix.add(map);
				}
			}
		}
	}

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_assetadd);

		isBatch = this.getIntent().getBooleanExtra("isBatch", false);

		Button btn = (Button) this.findViewById(R.id.btn_base);
		btn.setOnClickListener(this);
		btn.setOnTouchListener(this);

		btn = (Button) this.findViewById(R.id.btn_fix);
		btn.setOnClickListener(this);
		btn.setOnTouchListener(this);

		if (this.MenuId.equals("310")) {

			btn = (Button) this.findViewById(R.id.btn_save);
			btn.setOnClickListener(this);

			btn = (Button) this.findViewById(R.id.btn_scan_code);
			btn.setOnClickListener(this);

			btn = (Button) this.findViewById(R.id.btn_scan_sn);
			btn.setOnClickListener(this);

			btn = (Button) this.findViewById(R.id.btn_save_copy);
			btn.setOnClickListener(this);
			btn.setEnabled(false);

			btn = (Button) this.findViewById(R.id.btn_clear);
			btn.setOnClickListener(this);

		} else {
			btn = (Button) this.findViewById(R.id.btn_save);
			btn.setVisibility(View.INVISIBLE);

			btn = (Button) this.findViewById(R.id.btn_scan_code);
			// btn.setEnabled(false);
			btn.setVisibility(View.GONE);

			TextView tv = (TextView) this.findViewById(R.id.bottom_left_text);
			tv.setVisibility(View.GONE);

			btn = (Button) this.findViewById(R.id.btn_scan_sn);
			btn.setOnClickListener(this);

			btn = (Button) this.findViewById(R.id.btn_save_copy);
			btn.setVisibility(View.INVISIBLE);

			btn = (Button) this.findViewById(R.id.btn_clear);
			btn.setVisibility(View.INVISIBLE);

			btn = (Button) this.findViewById(R.id.btn_edit_save);
			btn.setVisibility(View.VISIBLE);
			btn.setOnClickListener(this);
			if (isBatch == true) {
				btn.setText("保存并修改下一条");
			} else {
				btn.setText("保存");
			}
		}

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText(this.getIntent().getStringExtra("title"));

		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		this.setupbaselist();
		this.setupfixlist();

		ListView lv = (ListView) this.findViewById(R.id.baselist);
		baseAdapter = new ContentAdapter(this, listBase, this.basedataModel.PageLabel);
		lv.setAdapter(baseAdapter);
		lv.setVisibility(View.VISIBLE);
		// 过滤没用的信息
		for (int i = 0; i < newlistFix.size(); i++) {
			HashMap<String, Object> map = (HashMap<String, Object>) newlistFix.get(i);
			String name = (String) map.get("name");
			String type = (String) map.get("type");
			Log.v("grq", "type的值为" + type);
			String id = null;
			boolean flag = false;
			if (pageLabel != null && name == null) {
				id = (String) map.get("id");
				Iterator iter = pageLabel.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					if (id.equals(key.toString())) {
						Object val = entry.getValue();

						Log.i("grq", "key=" + key + "   ,id=" + id + "   ,val=" + val + "    ,name" + name);
						flag = true;

					}
				}

				if (flag == false) {

					Log.i("grq", "异类id：" + id + "   下标的值" + i);
					newlistFix.remove(i);
					Log.d("grq",
							"异类map" + map.toString() + "   下标的值" + i + "  删除掉的项=" + newlistFix.remove(i).toString());
				}
			}

		}
		newlistFix.remove(11);
		newlistFix.remove(2);
		newlistFix.remove(1);
		lv = (ListView) this.findViewById(R.id.fixlist);
		fixAdapter = new ContentAdapter(this, listFix, this.basedataModel.PageLabel);
		lv.setAdapter(fixAdapter);
		lv.setVisibility(View.INVISIBLE);
		ComplexSelectActivity.specialupdateadapter = fixAdapter;

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && isBatch == false) {
			SerializableMap serializableMap = (SerializableMap) bundle.get("param");
			SerializableList serializableList = (SerializableList) bundle.get("ImgGuid");

			if (serializableMap != null) {

				this.updatedata(serializableMap.getMap(), listImgGuid);

			}

		} else if (isBatch == true) {
			this.indexBatch = 0;
			AssetInfoModel model = assetmodel.MsAsset.get(indexBatch++);
			this.getAssetInfo(model);
		}

		baseAdapter.notifyDataSetChanged();
		fixAdapter.notifyDataSetChanged();

		setDataforList();
	}

	void getAssetInfo(AssetInfoModel model) {
		Prepare(param);
		param.put("MenuId", "19");

		Map<String, String> mapMsAsset = new HashMap<String, String>();
		mapMsAsset.put("AssetCode", model.AssetCode);
		mapMsAsset.put("AssetName", model.AssetName);
		mapMsAsset.put("Standard", model.Standard);
		mapMsAsset.put("SerialNumber", model.SerialNumber);

		param.put("MsAsset", mapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(AssetAddActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.getResultUrl("19"), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {

				if (res.equals("")) {
					m_pDialog.hide();
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
							if (ImgGuid != null) {
								for (int i = 0; i < ImgGuid.length(); i++) {
									JSONObject item = ImgGuid.getJSONObject(i);
									String base64 = item.get("ImageData").toString();
									Log.e("base64", base64);

									listImgGuid.add(base64);

								}
							}

						}

					}

					Log.e("pagepage", String.valueOf(listImgGuid.size()));

					AssetAddActivity.this.updatedata(assetmap, listImgGuid);

					Log.e("baseAdapter.notifyDataSetChanged();", "baseAdapter.notifyDataSetChanged();");

					baseAdapter.notifyDataSetChanged();
					fixAdapter.notifyDataSetChanged();

					m_pDialog.hide();

					Button btn = (Button) AssetAddActivity.this.findViewById(R.id.btn_save_copy);
					if (btn.getVisibility() == View.VISIBLE) {
						btn.setEnabled(true);
						Button btnsave = (Button) AssetAddActivity.this.findViewById(R.id.btn_save);
						btnsave.setEnabled(false);
					}

				} catch (JSONException localJsonSyntaxException) {
					m_pDialog.hide();

					Log.e("222222222222222222222222222222222222", localJsonSyntaxException.getMessage());

				}
			}
		});

	}

	//添加数据，设置表头
	void setupbaselist() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "assetCode");
		map.put("value", "");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str9");
		map.put("value", "");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetOriCode");
		map.put("value", "");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetName");
		map.put("value", "");
		map.put("searchid", "AssetName");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "insDt");
		map.put("value", "");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "assetType");
		map.put("value", "");
		map.put("searchid", "AssetType");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "category");
		map.put("value", "");
		map.put("searchid", "category");// ////////////////////////////////special
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "standard");
		map.put("value", "");
		map.put("searchid", "Standard");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "brand");
		map.put("value", "");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetOriWorth");
		map.put("value", "");
		map.put("inputtype", String.valueOf(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER));
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "addMode");
		map.put("value", "");
		map.put("searchid", "AddMode");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "quantity");
		map.put("value", "");
		map.put("inputtype", String.valueOf(InputType.TYPE_CLASS_NUMBER));
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "measureUnit");
		map.put("value", "");
		map.put("searchid", "MeasureUnit");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "useUserId");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "useDeptId");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "address");
		map.put("id", "str8");
		map.put("value", "");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "status");
		map.put("value", "");
		//		map.put("searchid", "Status");
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "memo");
		map.put("value", "");
		listBase.add(map);

	}

	void setupfixlist() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date7");
		map.put("value", "");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date8");
		map.put("value", "");
		map.put("searchid", "datepicker");// /////////////////////////////special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "num7");
		map.put("value", "");
		map.put("searchid", "useUser");// ///////////////////////////////////////////////special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date9");
		map.put("value", "");
		map.put("searchid", "datepicker");// ///////////////////////////////////////////special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str12");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str17");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "str18");
		map.put("value", "");
		map.put("searchid", "EquipmentSource");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "str19");
		map.put("value", "");
		map.put("searchid", "UseType");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "str22");
		map.put("value", "");
		map.put("searchid", "RepairLevel");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "purchaseDt");
		map.put("value", "");
		map.put("inputtype", String.valueOf(InputType.TYPE_CLASS_NUMBER));
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "adminDeptId");
		map.put("value", "");
		map.put("searchid", "useDept");// ///////////////////////////////////////////////special
		listFix.add(map);
		ComplexSelectActivity.specialitem = map;

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetPurpose");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "providerName");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "factoryName");
		map.put("value", "");
		map.put("searchid", "EquipmentFactory");
		listFix.add(map);

		// 新添加的尺寸
		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str13");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str28");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str29");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str16");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str25");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str26");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str27");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "str15");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "addpic");
		map.put("id", "imgGuid");
		map.put("value", "");
		map.put("name", "资产图片");
		listFix.add(map);
	}

	@Override
	protected void ViewListen() {

		Button btn = (Button) this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(this);

		btn = (Button) this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(this);

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.tv_back);
		ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_scan_code: {
			Intent it = new Intent(AssetAddActivity.this, CaptureActivity.class);
			startActivityForResult(it, 1);

		}

		break;

		case R.id.btn_scan_sn: {
			Intent it = new Intent(AssetAddActivity.this, CaptureActivity.class);
			startActivityForResult(it, 2);

		}
		break;

		case R.id.btn_edit_save: {
			Button btn = (Button) arg0;
			if (isBatch == true && btn.equals("完成")) {
				finish();
				return;
			}

			String[] warn = { "assetName", "assetType", "category", "assetOriWorth", "addMode" };
			for (String warnid : warn) {
				for (Map<String, Object> map : listBase) {
					String id = (String) map.get("id");
					if (id.equals(warnid)) {

						String realvalue = (String) map.get("realvalue");

						if (realvalue == null || realvalue.length() == 0) {
							String warnname = basedataModel.PageLabel.get(id);
							new AlertDialog.Builder(AssetAddActivity.this).setTitle("提示")// 设置对话框标题
							.setMessage(warnname + "不能为空!")// 设置显示的内容
							.setPositiveButton("是", new DialogInterface.OnClickListener() {// 添加确定按钮
								@Override
								public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件

								}
							}).show();// 在按键响应事件中显示此对话框
							return;
						}
					}
				}
			}

			AssetAddActivity.this.Prepare(param);

			for (Map<String, Object> map : listBase) {
				if (((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
						&& map.get("realvalue").equals("") == false
						&& ((String) map.get("id")).equals("assetCode") == false) {
					addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
				}
				String id = (String) map.get("id");
				if(id.equals("quantity")){
					String usevalue = (String) map.get("value");
					if(usevalue.equals("")||usevalue.equals(null)){
						addmapMsAsset.put("quantity", ""+1);
					}
				}
			}
			//遍历定制的属性
			for (Map<String, Object> map : listFix) {
				if (map.get("id") != null && ((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
						&& map.get("realvalue").equals("") == false && map.get("id").equals("imgGuid") == false) {
					addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
				}
			}

			//判断资产状态
			String usevalue = null;
			String deptvalue = null;
			for (Map<String, Object> map : listBase) {
				String id = (String) map.get("id");
				if (id.equals("useUserId")) {
					usevalue = (String) map.get("value");

				}
				if(id.equals("useDeptId")){
					deptvalue = (String) map.get("value");

				}
				Log.i("usevalue", "usevalue="+usevalue);
				Log.i("deptvalue", "deptvalue="+deptvalue);
				try {
					if((usevalue==null && deptvalue==null )||(usevalue.equals("") && deptvalue.equals(""))||
							(usevalue.equals("0") && deptvalue.equals("0"))){
						addmapMsAsset.put("status", "001001");
					}else {
						addmapMsAsset.put("status", "001002");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			addmapMsAsset.put("assetId", assetId);

			param.put("MsAsset", addmapMsAsset);

			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {

					m_pDialog = new ProgressDialog(AssetAddActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在保存数据...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();

				}
				//发送请求菜单的地方
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSaveAssetUrl(MenuId), param);
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

							Log.e("success", "successsuccesssuccesssuccesssuccess");
							SharedPreferences preferences = AssetAddActivity.this.getSharedPreferences("AssetAdd", Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.clear();
							editor.commit();
							StaticUtil.forAssetAddActivity=2;
							m_pDialog.hide();

							String ErrorMsg = model.response.ErrorMsg;
							Common.ShowInfo(context, ErrorMsg);

							if (model.response.ErrorCode.equals("S00000")) {
								List<Map<String, String>> listImgGuid = new ArrayList<Map<String, String>>();
								for (Map<String, Object> map : listFix) {
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

								AssetAddActivity.this.Prepare(param);

								Map<String, Object> MsAttch = new HashMap<String, Object>();
								MsAttch.put("OriderId", model.rspcontent.OrderId);
								MsAttch.put("AttchType", "006013");

								param.put("MsAttch", MsAttch);
								if (listImgGuid.size() > 0) {
									param.put("ImgGuid", listImgGuid);
								}
								doAsync(new CallEarliest<String>() {
									public void onCallEarliest() throws Exception {

										m_pDialog = new ProgressDialog(AssetAddActivity.this);
										m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										m_pDialog.setMessage("正在保存图片数据...");
										m_pDialog.setCancelable(false);
										m_pDialog.show();

									}
								}, new Callable<String>() {
									public String call() throws Exception {
										return HttpHelper.getInstance(context)
												.Post(ApiAddressHelper.getSavePicUrl(MenuId), param);
									}
								}, new Callback<String>() {
									public void onCallback(String res) {

										if (res.equals("")) {
											m_pDialog.hide();
											Common.ShowInfo(context, "网络故障");
											return;
										}

										try {
											RequestRetModel<String> model = gson.fromJson(res,
													new TypeToken<RequestRetModel<String>>() {
											}.getType());
											if (model != null) {

												Log.e("success", "successsuccesssuccesssuccesssuccess");

												m_pDialog.hide();

												String ErrorMsg = model.response.ErrorMsg;
												if (ErrorMsg != null && ErrorMsg.length() > 0) {
													Common.ShowInfo(context, ErrorMsg);
												}

												if (isBatch == true && indexBatch < assetmodel.MsAsset.size()) {

													AssetInfoModel itemmodel = assetmodel.MsAsset.get(indexBatch++);
													getAssetInfo(itemmodel);
												} else if (isBatch == true) {
													Button btn = (Button) findViewById(R.id.btn_edit_save);
													btn.setText("完成");
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
		break;

		case R.id.btn_save: {
			saveAsset();
		}
		break;

		case R.id.btn_save_copy: {

			listBase.get(0).put("value", "");// assetcode
			listBase.get(0).put("realvalue", "");// assetcode
			listBase.get(1).put("value", "");// assetcode
			listBase.get(1).put("realvalue", "");// assetcode
			baseAdapter.notifyDataSetChanged();
			saveAsset();

		}
		break;

		case R.id.btn_clear: {

			for (Map<String, Object> map : listBase) {

				map.put("value", "");
				map.put("realvalue", "");
			}

			for (Map<String, Object> map : listFix) {
				if (map.get("id") != null && map.get("id").equals("imgGuid") == false
						&& ((String) map.get("type")).equals("pic") == false) {
					map.put("value", "");
					map.put("realvalue", "");
				} else if (((String) map.get("type")).equals("pic") == true) {
					// listFix.remove(map);

				}

			}
			/*
			 * for( int i : indexs ) { if( indexs[i] != 0 ) {
			 * listFix.remove(indexs[i]); } }
			 */
			while (listFix.size() > 22) {
				listFix.remove(listFix.size() - 1);
			}

			baseAdapter.notifyDataSetChanged();
			fixAdapter.notifyDataSetChanged();

		}
		break;

		}

	}

	//新增时保存按钮执行的方法
	void saveAsset() {
		String[] warn = { "assetName", "assetType", "category", "assetOriWorth", "addMode" };
		//判断是否存在空值
		for (String warnid : warn) {
			for (Map<String, Object> map : listBase) {
				String id = (String) map.get("id");
				Log.d("grq1234", "listBase的id的值："+id);
				if (id.equals(warnid)) {

					String realvalue = (String) map.get("realvalue");

					if (realvalue == null || realvalue.length() == 0) {
						String warnname = basedataModel.PageLabel.get(id);
						new AlertDialog.Builder(AssetAddActivity.this).setTitle("提示")// 设置对话框标题
						.setMessage(warnname + "不能为空!")// 设置显示的内容
						.setPositiveButton("是", new DialogInterface.OnClickListener() {// 添加确定按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件

							}
						}).show();// 在按键响应事件中显示此对话框
						return;

					}

				}

			}

		}

		AssetAddActivity.this.Prepare(param);

		for (Map<String, Object> map : listBase) {
			if (((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false
					&& ((String) map.get("id")).equals("assetCode") == false) {
				addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
			String id = (String) map.get("id");
			if(id.equals("quantity")){
				String usevalue = (String) map.get("value");
				if(usevalue.equals("")||usevalue.equals(null)){
					addmapMsAsset.put("quantity", ""+1);
				}
			}
		}
		//遍历定制的属性
		for (Map<String, Object> map : listFix) {
			if (map.get("id") != null && ((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false && map.get("id").equals("imgGuid") == false) {
				addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
		}

		//判断资产状态
		String usevalue = null;
		String deptvalue = null;
		for (Map<String, Object> map : listBase) {
			String id = (String) map.get("id");
			if (id.equals("useUserId")) {
				usevalue = (String) map.get("value");

			}
			if(id.equals("useDeptId")){
				deptvalue = (String) map.get("value");

			}
			Log.i("usevalue", "usevalue="+usevalue);
			Log.i("deptvalue", "deptvalue="+deptvalue);
			try {
				if((usevalue==null && deptvalue==null )||(usevalue.equals("") && deptvalue.equals(""))||
						(usevalue.equals("0") && deptvalue.equals("0"))){
					addmapMsAsset.put("status", "001001");
				}else {
					addmapMsAsset.put("status", "001002");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		addmapMsAsset.put("assetId", assetId);

		param.put("MsAsset", addmapMsAsset);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(AssetAddActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在保存数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(ApiAddressHelper.getSaveAssetUrl(MenuId), param);
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

						Log.e("success", "successsuccesssuccesssuccesssuccess");
						
						SharedPreferences preferences = AssetAddActivity.this.getSharedPreferences("AssetAdd", Context.MODE_PRIVATE);
						Editor editor = preferences.edit();
						editor.clear();
						editor.commit();
						StaticUtil.forAssetAddActivity=2;
						
						m_pDialog.hide();

						String ErrorMsg = model.response.ErrorMsg;
						Common.ShowInfo(context, ErrorMsg);

						if (model.response.ErrorCode.equals("S00000")) {
							Button btn = (Button) AssetAddActivity.this.findViewById(R.id.btn_save);
							btn.setEnabled(false);

							btn = (Button) AssetAddActivity.this.findViewById(R.id.btn_save_copy);
							btn.setEnabled(true);

							listBase.get(0).put("value", "");// assetcode
							listBase.get(0).put("realvalue", "");// assetcode
							baseAdapter.notifyDataSetChanged();

							List<Map<String, String>> listImgGuid = new ArrayList<Map<String, String>>();
							for (Map<String, Object> map : listFix) {
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

							AssetAddActivity.this.Prepare(param);

							Map<String, Object> MsAttch = new HashMap<String, Object>();
							MsAttch.put("OriderId", model.rspcontent.OrderId);
							MsAttch.put("AttchType", "006013");

							param.put("MsAttch", MsAttch);
							param.put("ImgGuid", listImgGuid);

							doAsync(new CallEarliest<String>() {
								public void onCallEarliest() throws Exception {

									m_pDialog = new ProgressDialog(AssetAddActivity.this);
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
									Log.i("res", res.toString());
									if (res.equals("")) {
										m_pDialog.hide();
										Common.ShowInfo(context, "网络故障");
										return;
									}

									try {
										RequestRetModel<AssetSaveRetModel> model = gson.fromJson(res,
												new TypeToken<RequestRetModel<AssetSaveRetModel>>() {
										}.getType());
										Log.i("model", model.toString());
										if (model != null) {

											Log.e("success", "successsuccesssuccesssuccesssuccess");

											m_pDialog.hide();

											String ErrorMsg = model.response.ErrorMsg;
											if (ErrorMsg != null && ErrorMsg.length() > 0) {
												Common.ShowInfo(context, ErrorMsg);
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 1: {
				Log.e("camera", "recv");
				/*
				 * String str = data.getStringExtra("result"); Map<String,
				 * Object> map = listBase.get(0); map.put("value", str);
				 * 
				 * baseAdapter.notifyDataSetChanged();
				 */
				AssetInfoModel model = new AssetInfoModel();
				model.AssetCode = data.getStringExtra("result");

				getAssetInfo(model);

			}
			break;
			case 2: {
				Log.e("camera", "recv");
				/*
				 * String str = data.getStringExtra("result"); Map<String,
				 * Object> map = listBase.get(1); map.put("value", str);
				 * 
				 * Log.e("snsnsnssnssnsnsns",str);
				 * 
				 * baseAdapter.notifyDataSetChanged();
				 */

				if (MenuId != null && MenuId.equals("310")) {
					Map<String, Object> map = this.listBase.get(1);
					map.put("value", data.getStringExtra("result"));
					map.put("realvalue", data.getStringExtra("result"));

					baseAdapter.notifyDataSetChanged();

					AssetInfoModel model = new AssetInfoModel();
					model.SerialNumber = data.getStringExtra("result");

					getAssetInfo(model);
				} else {
					Map<String, Object> map = this.listBase.get(1);
					map.put("value", data.getStringExtra("result"));
					map.put("realvalue", data.getStringExtra("result"));

					baseAdapter.notifyDataSetChanged();

				}
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
				listFix.add(map);

				fixAdapter.notifyDataSetChanged();

				ListView lv = (ListView) this.findViewById(R.id.fixlist);

				lv.post(new Runnable() {
					@Override
					public void run() {
						// Select the last row so it will scroll into view...
						ListView lv = (ListView) AssetAddActivity.this.findViewById(R.id.fixlist);
						lv.setSelection(fixAdapter.getCount() - 1);
					}
				});
				/*
				 * if(!bmp.isRecycled() ) { bmp.recycle(); //回收图片所占的内存
				 * System.gc(); //提醒系统及时回收 }
				 */

			}
			break;

			case 5 :{
				String result = data.getExtras().getString("result");
				if(result!=null){
					Address.setAddress(result);
					baseAdapter.notifyDataSetChanged();
				}
				Log.i("result", result);
			}
			break;

			default:
				break;
			}

		}
	}

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig);
	 * 
	 * }
	 * 
	 * @Override public void onSaveInstanceState(Bundle savedInstanceState) {
	 * Log.e("camera", "killkillkill");
	 * super.onSaveInstanceState(savedInstanceState); // 实现父类方法 放在最后 //
	 * 防止拍照后无法返回当前activity }
	 */
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.btn_base && arg1.getAction() == MotionEvent.ACTION_DOWN) {
			Button btn = (Button) this.findViewById(R.id.btn_base);
			btn.setBackgroundResource(R.drawable.tablebar_bg_down);

			btn = (Button) this.findViewById(R.id.btn_fix);
			btn.setBackgroundResource(R.drawable.tablebar_bg);

			ListView lv = (ListView) this.findViewById(R.id.baselist);
			lv.setVisibility(View.VISIBLE);
			lv = (ListView) this.findViewById(R.id.fixlist);
			lv.setVisibility(View.INVISIBLE);

		} else if (arg0.getId() == R.id.btn_fix && arg1.getAction() == MotionEvent.ACTION_DOWN) {
			Button btn = (Button) this.findViewById(R.id.btn_base);
			btn.setBackgroundResource(R.drawable.tablebar_bg);

			btn = (Button) this.findViewById(R.id.btn_fix);
			btn.setBackgroundResource(R.drawable.tablebar_bg_down);

			ListView lv = (ListView) this.findViewById(R.id.baselist);
			lv.setVisibility(View.INVISIBLE);
			lv = (ListView) this.findViewById(R.id.fixlist);
			lv.setVisibility(View.VISIBLE);
		}

		return false;
	}

	//资产信息，用于未保存前退出当前界面时，缓存已添加的部分信息
	private void AssetsInformation(){
		for (Map<String, Object> map : listBase) {
			if (((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false
					&& ((String) map.get("id")).equals("assetCode") == false) {
				addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
		}
		//遍历定制的属性
		for (Map<String, Object> map : listFix) {
			if (map.get("id") != null && ((String) map.get("id")).contains("_") == false && map.get("realvalue") != null
					&& map.get("realvalue").equals("") == false && map.get("id").equals("imgGuid") == false) {
				addmapMsAsset.put((String) map.get("id"), map.get("realvalue"));
			}
		}

		String string = gson.toJson(addmapMsAsset);
		SharedPreferences preferences = AssetAddActivity.this.getSharedPreferences("AssetAdd", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("mapMsAsset", string);
		editor.commit();
		
	}

	//为界面设置值
	private void setDataforList(){
		SharedPreferences preferences = AssetAddActivity.this.getSharedPreferences("AssetAdd", Context.MODE_PRIVATE);
		String string = preferences.getString("mapMsAsset", null);
		if (string != null) {
			addmapMsAsset = gson.fromJson(string, new TypeToken<HashMap<String, Object>>() {}.getType());
			Map<String, Object> assetmap = addmapMsAsset;
			for (Map<String, Object> map : listBase) {
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

			for (Map<String, Object> map : listFix) {
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
}
