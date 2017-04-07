package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;

import com.capitalcode.assetsystemmobile.adapter.GridViewAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.ListMsStockModel;
import com.capitalcode.assetsystemmobile.model.MenuModel;
import com.capitalcode.assetsystemmobile.model.MsStockModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

//盘点功能菜单界面
public class CheckMenuActivity extends BaseActivity {

	static public List<MenuModel> listmenu = new ArrayList<MenuModel>();
	static public String StockRightCode="";

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


	String title;
	void getBatch(String MenuId)
	{
		CheckMenuActivity.this.Prepare(param);
		if( MenuId.equals("online_check") || MenuId.equals("online_statistics") )
		{
			param.put("MenuId", "419");
		}
		else
		{
			param.put("MenuId", "104");
		}
		param.put("StockRightCode", StockRightCode);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(CheckMenuActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getOnLineBatch(), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				m_pDialog.hide();
				if (res.equals("")) {

					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {
					RequestRetModel<ListMsStockModel> model = gson
							.fromJson(
									res,
									new TypeToken<RequestRetModel<ListMsStockModel>>() {
									}.getType());
					if (model != null) 
					{
						CheckChooseActivity.listBatch = model.rspcontent;

						Intent intent = new Intent(CheckMenuActivity.this, CheckChooseActivity.class);


						intent.putExtra("MenuId", CheckMenuActivity.this.MenuId);
						intent.putExtra("title", title);
						CheckMenuActivity.this.startActivity(intent);

					} 
					else
					{

						Common.ShowInfo(context, "网络异常");
						Log.e("fail",
								"failfailfailfailfailfailfailfailfail");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {
				}
			}
		});



	}


	void getOnLineBatch()
	{
		CheckMenuActivity.this.Prepare(param);
		param.put("MenuId", "419");
		param.put("StockRightCode", StockRightCode);

		doAsync(new CallEarliest<String>() {
			public void onCallEarliest() throws Exception {

				m_pDialog = new ProgressDialog(CheckMenuActivity.this);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_pDialog.setMessage("正在获取数据...");
				m_pDialog.setCancelable(false);
				m_pDialog.show();

			}
		}, new Callable<String>() {
			public String call() throws Exception {
				return HttpHelper.getInstance(context).Post(
						ApiAddressHelper.getOnLineBatch(), param);
			}
		}, new Callback<String>() {
			public void onCallback(String res) {
				m_pDialog.hide();
				if (res.equals("")) {

					Common.ShowInfo(context, "网络故障");
					return;
				}

				try {
					RequestRetModel<ListMsStockModel> model = gson
							.fromJson(
									res,
									new TypeToken<RequestRetModel<ListMsStockModel>>() {
									}.getType());
					if (model != null) 
					{
						CheckChooseActivity.listBatch = model.rspcontent;

						Intent intent = new Intent(CheckMenuActivity.this, CheckChooseActivity.class);

						intent.putExtra("MenuId", MenuId);
						intent.putExtra("title", title);
						CheckMenuActivity.this.startActivity(intent);

					} 
					else 
					{

						Common.ShowInfo(context, "网络异常");
						Log.e("fail",
								"failfailfailfailfailfailfailfailfail");
					}
				} catch (JsonSyntaxException localJsonSyntaxException) {


				}
			}
		});
	}


	class  ItemClickListener implements OnItemClickListener  
	{  
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
				View arg1,//The view within the AdapterView that was clicked  
				int arg2,//The position of the view in the adapter  
				long arg3//The row id of the item that was clicked  
				) 
		{  
			EditText et = (EditText)CheckMenuActivity.this.findViewById(R.id.codevalue);
			StockRightCode = et.getText().toString();

			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
			String MenuId = (String)item.get("menuid");
			title = (String)item.get("ItemText");
			CheckMenuActivity.this.MenuId = MenuId;
			if(  MenuId.equals("offline_download") )
			{
				getBatch(MenuId);
			}
			else if(  MenuId.equals("offline_check") || MenuId.equals("offline_statistics"))
			{
				SharedPreferences sp = CheckMenuActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
				String strlist = sp.getString("check"+mobile, null);

				if( strlist != null )
				{
					List<SaveCheckDataModel> listSave = gson
							.fromJson(
									strlist,
									new TypeToken<List<SaveCheckDataModel>>() {
									}.getType());

					if( CheckChooseActivity.listBatch == null )
					{
						CheckChooseActivity.listBatch = new ListMsStockModel();
					}


					if( CheckChooseActivity.listBatch.MsStock != null )
					{
						CheckChooseActivity.listBatch.MsStock.clear();
					}
					else
					{
						CheckChooseActivity.listBatch.MsStock = new ArrayList<MsStockModel>();
					}

					for( SaveCheckDataModel model : listSave )
					{
						CheckChooseActivity.listBatch.MsStock.add(model.MsStock);
					}

					Intent intent = new Intent(CheckMenuActivity.this, CheckChooseActivity.class);
					intent.putExtra("MenuId", MenuId);
					intent.putExtra("title", title);
					CheckMenuActivity.this.startActivity(intent);	
				}
			}
			else if( MenuId.equals("offline_upload")){
				SharedPreferences sp = CheckMenuActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
				String strlist = sp.getString("check"+mobile, null);

				SharedPreferences sp2 = CheckMenuActivity.this.getSharedPreferences("peoplecheckdata", Context.MODE_PRIVATE);
				String strlist2 = sp2.getString("check"+mobile, null);
				if( strlist != null || strlist2 !=null)
				{
					List<SaveCheckDataModel> listSave = gson
							.fromJson(
									strlist,
									new TypeToken<List<SaveCheckDataModel>>() {
									}.getType());

					List<SaveCheckDataModel> listSave2 = gson
							.fromJson(
									strlist2,
									new TypeToken<List<SaveCheckDataModel>>() {
									}.getType());

					if( CheckChooseActivity.listBatch == null )
					{
						CheckChooseActivity.listBatch = new ListMsStockModel();
					}


					if( CheckChooseActivity.listBatch.MsStock != null )
					{
						CheckChooseActivity.listBatch.MsStock.clear();
					}
					else
					{
						CheckChooseActivity.listBatch.MsStock = new ArrayList<MsStockModel>();
					}
					if( strlist != null){
						for( SaveCheckDataModel model : listSave )
						{
							CheckChooseActivity.listBatch.MsStock.add(model.MsStock);
						}
					}
					if(strlist2 != null){
						for( SaveCheckDataModel model : listSave2 )
						{
							CheckChooseActivity.listBatch.MsStock.add(model.MsStock);
						}
					}

					Intent intent = new Intent(CheckMenuActivity.this, CheckChooseActivity.class);
					intent.putExtra("MenuId", MenuId);
					intent.putExtra("title", title);
					CheckMenuActivity.this.startActivity(intent);	
				}
			}
			else if( MenuId.equals("people_check")){
				SharedPreferences sp = CheckMenuActivity.this.getSharedPreferences("peoplecheckdata", Context.MODE_PRIVATE);
				String strlist = sp.getString("check"+mobile, null);
				if(strlist!=null){
					Intent intent = new Intent(CheckMenuActivity.this, PeopleCheckActivity.class);
					intent.putExtra("MenuId", MenuId);
					intent.putExtra("title", title);

					CheckMenuActivity.this.startActivity(intent);
				}
			}
			else if(MenuId.equals("getui")){
				Intent intent = new Intent(CheckMenuActivity.this, GetuiActivity.class);
				intent.putExtra("MenuId", MenuId);
				intent.putExtra("title", title);
				CheckMenuActivity.this.startActivity(intent);
			}
			else if(  MenuId.equals("online_check")  )
			{
				getBatch(MenuId);
			}
			else if(  MenuId.equals("online_statistics")  )
			{
				getBatch(MenuId);
			}			    
		}  
	}

	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_menu);

		RelativeLayout rl = (RelativeLayout)this.findViewById(R.id.rlcode);
		rl.setVisibility(View.VISIBLE);

		Button btn = (Button)this.findViewById(R.id.btn_title_left);


		btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText(this.getIntent().getStringExtra("title"));

		GridView gv = (GridView) this.findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

		for (MenuModel model : listmenu) {

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
		map.put("ItemImage", R.drawable.people_check);// 添加图像资源的ID
		map.put("ItemImageSelect", R.drawable.people_check);
		map.put("ItemText", "个人核查");// 按序号做ItemText
		map.put("menuid", "people_check");// 按序号做ItemText
		lstImageItem.add(map);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.people_upload);// 添加图像资源的ID
		map2.put("ItemImageSelect", R.drawable.people_upload);
		map2.put("ItemText", "推送消息");// 按序号做ItemText
		map2.put("menuid", "getui");// 按序号做ItemText
		lstImageItem.add(map2);
		
		GridViewAdapter adapter = new GridViewAdapter(this,lstImageItem);
		// 添加并且显示
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new ItemClickListener());
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

}
