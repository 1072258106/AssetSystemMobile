package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.capitalcode.assetsystemmobile.model.TypeStatisticsInfoModelSecond;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsInfoModelThird;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.tree_view.ComplexSelectActivity;

public class TypeStatistics3Activity extends BaseActivity {
	
	ListView listDept;
	SimpleSelectAdapter adapter;
	private List<Map<String,String>> listchoose = new ArrayList<Map<String,String>>();
	
	String DeptId;
	String CategoryId;
	
	
	
	
	
	
	

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
		setContentView(R.layout.activity_typestatistics3);

		listDept = (ListView)this.findViewById(R.id.departmentlist);
		
		Button btn = (Button)this.findViewById(R.id.btn_choose);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TypeStatistics3Activity.this, ComplexSelectActivity.class);
				intent.putExtra("title", "规格型号");
				intent.putExtra("searchid", "Standard");
				intent.putExtra("isMultChoose", true);
				
				ComplexSelectActivity.listchoose = TypeStatistics3Activity.this.listchoose;
				ComplexSelectActivity.updateadapter = adapter;
				
				context.startActivity(intent);
			}
		});
		
		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);
		
		
	    
		adapter = new SimpleSelectAdapter(this,listchoose);
		listDept.setAdapter(adapter);
		
		DeptId = this.getIntent().getStringExtra("DeptId");
		CategoryId = this.getIntent().getStringExtra("CategoryId");
		
		String DeptName = this.getIntent().getStringExtra("DeptName");
		TextView tv = (TextView)this.findViewById(R.id.codevalue);
		tv.setText(DeptName);
		
		String CategoryName = this.getIntent().getStringExtra("CategoryName");
		tv = (TextView)this.findViewById(R.id.tv_show);
		tv.setText(CategoryName);
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
				TypeStatistics3Activity.this.Prepare(param);
				
				param.put("MenuId", MenuId);
				
				
				
				Map<String, String> map = new HashMap<String, String>();

				String DeptIds="";
				for( Map<String,String> item:listchoose )
				{
					String id = item.get("value");
					if( DeptIds.length() == 0 )
					{
						DeptIds = id;
					}
					else
					{
						DeptIds = DeptIds+","+id;
					}
				}
				
				map.put("DeptIds", DeptId);
				map.put("CategoryIds", CategoryId);
				map.put("Standard", DeptIds);
				param.put("MsQuerys", map);
				
				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {
						
						m_pDialog = new ProgressDialog(TypeStatistics3Activity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();
						
					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(TypeStatistics3Activity.this).Post(
								ApiAddressHelper.getStatisticsByStandard(), param);
					}
				}, new Callback<String>() {
					public void onCallback(String res) {
						
						m_pDialog.hide();
						
						if (res.equals("")) {
							m_pDialog.hide();
							Common.ShowInfo(
									context,"网络故障");
							return;
						}
						try {
							
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
							
							
							
							RequestRetModel<TypeStatisticsInfoModelThird> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<TypeStatisticsInfoModelThird>>() {
									}.getType());
							if (model != null) {


								
								
								StatisticsResultActivity.MsAssetType_third = model.rspcontent.MsAsset;
								
								
						        Intent intent = new Intent(TypeStatistics3Activity.this, StatisticsResultActivity.class);
						        
						        intent.putExtra("MenuId", MenuId);
						        intent.putExtra("step", "third");

						        intent.putExtra("title", TypeStatistics3Activity.this.getIntent().getStringExtra("title"));
 

						        
						        TypeStatistics3Activity.this.startActivity(intent);

							} else {
								
								Common.ShowInfo(context, "网络异常");
								Log.e("fail","failfailfailfailfailfailfailfailfail");
							}
						} catch (JsonSyntaxException localJsonSyntaxException) {
							
							Log.e("iws", "Login json转换错误 e:" + localJsonSyntaxException);
							Log.e("fail","failfailfailfailfailfailfailfailfail");
						}
						catch(JSONException e)
						{
							
						}
						
						
					}
				});

			}
		});

	}

}
