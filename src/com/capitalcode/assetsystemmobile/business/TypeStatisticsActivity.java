package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.SimpleSelectAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.StateStatisticsInfoModel;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsInfoModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.tree_view.ComplexSelectActivity;

public class TypeStatisticsActivity extends BaseActivity {

	ListView listDept;
	SimpleSelectAdapter adapter;
	private List<Map<String,String>> listchoose = new ArrayList<Map<String,String>>();
	
	
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
		setContentView(R.layout.activity_typestatistics);
		
		listDept = (ListView)this.findViewById(R.id.departmentlist);
		
		Button btn = (Button)this.findViewById(R.id.btn_choose);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TypeStatisticsActivity.this, ComplexSelectActivity.class);
				intent.putExtra("title", "使用部门");
				intent.putExtra("searchid", "useDept");
				intent.putExtra("isMultChoose", true);
				
				ComplexSelectActivity.listchoose = TypeStatisticsActivity.this.listchoose;
				ComplexSelectActivity.updateadapter = adapter;
				
				context.startActivity(intent);
			}
		});
		
		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);
		
		
	    
		adapter = new SimpleSelectAdapter(this,listchoose);
		listDept.setAdapter(adapter);
		
		
		
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button)this.findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				
				param.clear();
				TypeStatisticsActivity.this.Prepare(param);
				
				param.put("MenuId", MenuId);
				
				
				
				Map<String, String> map = new HashMap<String, String>();

				String DeptIds="";
				for( Map<String,String> item:listchoose )
				{
					String id = item.get("id");
					if( DeptIds.length() == 0 )
					{
						DeptIds = id;
					}
					else
					{
						DeptIds = DeptIds+","+id;
					}
				}
				
				
				map.put("DeptIds", DeptIds);
				param.put("MsQuerys", map);
				
				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {
						
						m_pDialog = new ProgressDialog(TypeStatisticsActivity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();
						
					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(TypeStatisticsActivity.this).Post(
								ApiAddressHelper.getStatisticsByDept(), param);
					}
				}, new Callback<String>() {
					public void onCallback(String res) {
						
						if (res.equals("")) {
							m_pDialog.hide();
							Common.ShowInfo(
									context,"网络故障");
							return;
						}
						try {
							RequestRetModel<TypeStatisticsInfoModel> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<TypeStatisticsInfoModel>>() {
									}.getType());
							if (model != null) {


								m_pDialog.hide();
								
								StatisticsResultActivity.MsAssetType_first = model.rspcontent.MsAsset;
								
								
						        Intent intent = new Intent(TypeStatisticsActivity.this, StatisticsResultActivity.class);
						        
						        intent.putExtra("MenuId", MenuId);
						        intent.putExtra("step", "first");
						        intent.putExtra("title", TypeStatisticsActivity.this.getIntent().getStringExtra("title"));
 

						        
						        TypeStatisticsActivity.this.startActivity(intent);

							} else {
								m_pDialog.hide();
								Common.ShowInfo(context, "网络异常");
								Log.e("fail","failfailfailfailfailfailfailfailfail");
							}
						} catch (JsonSyntaxException localJsonSyntaxException) {
							m_pDialog.hide();
							Log.e("iws", "Login json转换错误 e:" + localJsonSyntaxException);
							Log.e("fail","failfailfailfailfailfailfailfailfail");
						}
					}
				});

			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	}

}
