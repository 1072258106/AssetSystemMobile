package com.capitalcode.assetsystemmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.*;
import com.capitalcode.assetsystemmobile.adapter.GridViewAdapter;
import com.capitalcode.assetsystemmobile.business.AssetAddActivity;
import com.capitalcode.assetsystemmobile.business.RegisterActivity;
import com.capitalcode.assetsystemmobile.business.ReturnRegisterActivity;
import com.capitalcode.assetsystemmobile.business.StateStatisticsActivity;
import com.capitalcode.assetsystemmobile.business.TrendStatisticsActivity;
import com.capitalcode.assetsystemmobile.business.TypeStatisticsActivity;
import com.capitalcode.assetsystemmobile.model.MenuModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends BaseActivity implements View.OnClickListener{

	static public List<MenuModel> listmenu;

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
		Log.e("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeee","DestroyDestroyDestroyDestroy");
	}

	class  ItemClickListener implements OnItemClickListener  
	{  
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
				View arg1,//The view within the AdapterView that was clicked  
				int arg2,//The position of the view in the adapter  
				long arg3//The row id of the item that was clicked  
				) 
		{  

			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
			String MenuId = (String)item.get("menuid");
			if( MenuId != null && MenuId.equals("310") )
			{
				Intent intent = new Intent(MenuActivity.this, AssetAddActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}
			else if( MenuId != null && ( MenuId.equals("19") || MenuId.equals("207") || MenuId.equals("206") || MenuId.equals("209") || MenuId.equals("321")
					|| MenuId.equals("211") || MenuId.equals("210") || MenuId.equals("208") ) )
			{
				Intent intent = new Intent(MenuActivity.this, SearchActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}
			else if( MenuId != null && ( MenuId.equals("29") || MenuId.equals("31") || MenuId.equals("39") || MenuId.equals("34")
					|| MenuId.equals("48") || MenuId.equals("43") ))
			{
				Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}
			else if( MenuId != null && MenuId.equals("36") )
			{
				Intent intent = new Intent(MenuActivity.this, ReturnRegisterActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}
			else if( MenuId != null && MenuId.equals("280") )
			{
				Intent intent = new Intent(MenuActivity.this, StateStatisticsActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}
			else if( MenuId != null && MenuId.equals("420") )
			{
				Intent intent = new Intent(MenuActivity.this, TrendStatisticsActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}		    
			else if( MenuId != null && MenuId.equals("421") )
			{
				Intent intent = new Intent(MenuActivity.this, TypeStatisticsActivity.class);

				intent.putExtra("title", (String)item.get("ItemText"));
				intent.putExtra("MenuId", MenuId);

				MenuActivity.this.startActivity(intent);
			}		    

		}  
	}


	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_menu);


		Button btn = (Button)this.findViewById(R.id.btn_title_left);
		btn.setVisibility(View.GONE);

		btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		LinearLayout ll = (LinearLayout)this.findViewById(R.id.tv_back);
		ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/*ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});*/

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText(this.getIntent().getStringExtra("title"));


		GridView gv = (GridView) this.findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();


		for (MenuModel model : listmenu) {

			String MenuId = model.MenuId;
			String MenuName = model.MenuName;
			
			Log.i("gggggg", MenuId);
			Log.i("gggggg", MenuName);
			
			if(MenuId.equals(""+419)){
				continue;
			}
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", home.get(MenuId).get("up"));
			map.put("ItemImageSelect", home.get(MenuId).get("down"));
			map.put("ItemText", MenuName);
			map.put("submenu", model.SubMenu);
			map.put("menuid", model.MenuId);

			lstImageItem.add(map);
		}


		/*SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.main_item,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemText" },

				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemImage, R.id.ItemText });*/

		GridViewAdapter adapter = new GridViewAdapter(this,lstImageItem);
		// 添加并且显示
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new ItemClickListener());




	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void Init(Bundle paramBundle) {
		// TODO Auto-generated method stub


	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.tv_back:
			finish();
			break;
		}
	}
}
