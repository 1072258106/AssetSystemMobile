package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;

import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.model.AssetHistoryModel;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.CategoryModel;
import com.capitalcode.assetsystemmobile.utils.Address;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;

public class AssetBrowseActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

	private List<Map<String, Object>> listBase = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listFix = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newlistFix = listFix;
	public Map<String, String> pageLabel = this.basedataModel.PageLabel;

	ContentAdapter baseAdapter;
	ContentAdapter fixAdapter;

	static public List<AssetHistoryModel> historymodel;

	static public List<String> listImgGuid = new ArrayList<String>();

	@Override
	protected void Init(Bundle paramBundle) {

	}

	@Override
	protected void AppInit() {

	}

	@Override
	protected void DataInit() {

	}

	@Override
	protected void Destroy() {

	}

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
		map.put("searchid", "datepicker");/////////////////////////////// special
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
		map.put("searchid", "category");////////////////////////////////// special
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
		map.put("inputtype", String.valueOf(InputType.TYPE_NUMBER_FLAG_DECIMAL));
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
		map.put("searchid", "useUser");///////////////////////////////////////////////// special
		listBase.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "useDeptId");
		map.put("value", "");
		map.put("searchid", "useDept");///////////////////////////////////////////////// special
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
		map.put("searchid", "datepicker");/////////////////////////////// special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date8");
		map.put("value", "");
		map.put("searchid", "datepicker");/////////////////////////////// special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "num7");
		map.put("value", "");
		map.put("searchid", "useUser");///////////////////////////////////////////////// special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "choose");
		map.put("id", "date9");
		map.put("value", "");
		map.put("searchid", "datepicker");///////////////////////////////////////////// special
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
		map.put("searchid", "useDept");///////////////////////////////////////////////// special
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "assetPurpose");
		map.put("value", "");
		listFix.add(map);

		map = new HashMap<String, Object>();
		map.put("type", "edit");
		map.put("id", "providerName");
		map.put("value", "");
		//		map.put("searchid", "Supplier");
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

	@SuppressWarnings("static-access")
	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_assetbrowse);

		Button btn = (Button) this.findViewById(R.id.btn_base);
		btn.setOnClickListener(this);
		btn.setOnTouchListener(this);

		btn = (Button) this.findViewById(R.id.btn_fix);
		btn.setOnClickListener(this);
		btn.setOnTouchListener(this);

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText("资产浏览");

		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		this.setupbaselist();
		this.setupfixlist();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			SerializableMap serializableMap = (SerializableMap) bundle.get("param");
			if (serializableMap != null) {
				Map<String, Object> assetmap = serializableMap.getMap();
				for (Map<String, Object> map : listBase) {
					map.put("readonly", "true");
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
					map.put("readonly", "true");
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

				// List<String> listImgGuid =
				// (List<String>)assetmap.get("ImgGuid");
				if (listImgGuid != null) {
					for (String base64 : listImgGuid) {
						Bitmap bmp = decodeImg(base64);
						if (bmp != null) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("type", "pic");
							map.put("image", bmp);
							map.put("data", (String) base64);
							map.put("readonly", "true");
							listFix.add(map);
						}

					}
				}

			}

		}

		if (historymodel != null) {
			setupData();
		}

		ListView mListView = (ListView) findViewById(R.id.scroll_list);

		ScrollAdapter adapter = new ScrollAdapter(this, datas, R.layout.item,
				new String[] { "data_0", "data_1", "data_2", "data_3", "data_4", "data_5", },
				new int[] { R.id.item_data0, R.id.item_data1, R.id.item_data2, R.id.item_data3, R.id.item_data4,
				R.id.item_data5 });
		mListView.setAdapter(adapter);

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

				Log.i("grq", "id的值为" + id);
				if (id != null) {
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
						Log.d("grq", "异类map" + map.toString() + "   下标的值" + i + "  删除掉的项="
								+ newlistFix.remove(i).toString());
					}
				}
			}

		}
		newlistFix.remove(11);
		newlistFix.remove(2);
		newlistFix.remove(1);

		Log.d("grq", this.basedataModel.PageLabel.toString());
		lv = (ListView) this.findViewById(R.id.fixlist);
		fixAdapter = new ContentAdapter(this, newlistFix, this.basedataModel.PageLabel);
		lv.setAdapter(fixAdapter);

		lv.setVisibility(View.INVISIBLE);

	}

	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

	void setupData() {

		datas.clear();

		Map<String, String> data = null;
		for (AssetHistoryModel model : historymodel) {
			data = new HashMap<String, String>();

			data.put("data_" + 0, model.OperatorType);
			data.put("data_" + 1, model.OperatorTime);
			data.put("data_" + 2, model.BeforDept);
			data.put("data_" + 3, model.BeforUser);
			data.put("data_" + 4, model.AfterDept);
			data.put("data_" + 5, model.AfterUser);

			datas.add(data);
		}

	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) { 
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
}
