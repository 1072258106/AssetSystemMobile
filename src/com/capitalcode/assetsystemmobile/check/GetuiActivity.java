package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.PeopleCheckAdapter;
import com.capitalcode.assetsystemmobile.model.Getui;
import com.igexin.sdk.PushManager;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class GetuiActivity extends BaseActivity{

	private ListView LvGetui;
	private List<Getui> getuis;
	private PeopleCheckAdapter adapter;

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
		setContentView(R.layout.activity_getui);

		setView();
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

	private void setView() {
		// TODO Auto-generated method stub
		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText("个推消息");
		Button btn = (Button) this.findViewById(R.id.btn_title_left);
		btn.setVisibility(View.GONE);

		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		LvGetui = (ListView) findViewById(R.id.lv_getui);
		getuis = new ArrayList<Getui>();
		getuis = query();
		adapter = new PeopleCheckAdapter(GetuiActivity.this, getuis);
		LvGetui.setAdapter(adapter);
		
		LvGetui.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String zzbh = getuis.get(position).getZzbh();
				final String zzlb = getuis.get(position).getZzlb();
				//对话框
				AlertDialog.Builder builder=new Builder(GetuiActivity.this);
				//2所有builder设置一些参数
				builder.setTitle("提示");
				builder.setMessage("是否删除该数据");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Delete(zzbh,zzlb);
						getuis = query();
						adapter = new PeopleCheckAdapter(GetuiActivity.this, getuis);
						LvGetui.setAdapter(adapter);
					}
				});
				builder.setNeutralButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.create().show();
			}
		});
	}

	private List<Getui> query(){
		List<Getui> list = new ArrayList<Getui>();
		String userName = loginModel.LoginUser.UserName;
		DatabaseHelper helper = new DatabaseHelper(GetuiActivity.this, "Message.db");
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query("MessageTable", new String[]{
				"message",
				"zzlb",
				"zzbh",
				"zzflag"
		},"user=?", new String[]{userName}, null, null, null);
		if(cursor!=null){
			while(cursor.moveToNext()){
				Getui getui = new Getui();

				getui.setContents(cursor.getString(0));
				getui.setZzlb(cursor.getString(1));
				getui.setZzbh(cursor.getString(2));
				getui.setReadflag(cursor.getString(3));
				list.add(getui);
			}
			cursor.close();
		}
		return list;
	}

	private void Delete(String zzbh, String zzlb) {
		DatabaseHelper helper = new DatabaseHelper(GetuiActivity.this, "Message.db");
		SQLiteDatabase database = helper.getWritableDatabase();
		database.delete("MessageTable", "zzbh=? and zzlb=?", new String[]{zzbh,zzlb});
	}

}
