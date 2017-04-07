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
import com.capitalcode.assetsystemmobile.model.TypeStatisticsInfoModel;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsInfoModelSecond;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.tree_view.ComplexSelectActivity;

public class TypeStatistics2Activity extends BaseActivity {

	ListView listDept;
	SimpleSelectAdapter adapter;
	private List<Map<String,String>> listchoose = new ArrayList<Map<String,String>>();
	
	String DeptId;
	String DeptName;
	
	
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
		setContentView(R.layout.activity_typestatistics2);

		listDept = (ListView)this.findViewById(R.id.departmentlist);
		
		Button btn = (Button)this.findViewById(R.id.btn_choose);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TypeStatistics2Activity.this, ComplexSelectActivity.class);
				intent.putExtra("title", "资产分类");
				intent.putExtra("searchid", "category");
				intent.putExtra("isMultChoose", true);
				
				ComplexSelectActivity.listchoose = TypeStatistics2Activity.this.listchoose;
				ComplexSelectActivity.updateadapter = adapter;
				
				context.startActivity(intent);
			}
		});
		
		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);
		
		
	    
		adapter = new SimpleSelectAdapter(this,listchoose);
		listDept.setAdapter(adapter);
		
		DeptId = this.getIntent().getStringExtra("DeptId");
		
		DeptName = this.getIntent().getStringExtra("DeptName");
		TextView tv = (TextView)this.findViewById(R.id.codevalue);
		tv.setText(DeptName);
		
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
				TypeStatistics2Activity.this.Prepare(param);
				
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
				
				map.put("DeptIds", DeptId);
				map.put("CategoryIds", DeptIds);
				param.put("MsQuerys", map);
				
				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {
						
						m_pDialog = new ProgressDialog(TypeStatistics2Activity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();
						
					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(TypeStatistics2Activity.this).Post(
								ApiAddressHelper.getStatisticsByCategory(), param);
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
							
							
							
							RequestRetModel<TypeStatisticsInfoModelSecond> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<TypeStatisticsInfoModelSecond>>() {
									}.getType());
							if (model != null) {


								m_pDialog.hide();
								
								StatisticsResultActivity.MsAssetType_second = model.rspcontent.MsAsset;
								
								
						        Intent intent = new Intent(TypeStatistics2Activity.this, StatisticsResultActivity.class);
						        
						        intent.putExtra("MenuId", MenuId);
						        intent.putExtra("step", "second");
						        intent.putExtra("DeptId", DeptId);
						        intent.putExtra("DeptName", DeptName);
						        intent.putExtra("title", TypeStatistics2Activity.this.getIntent().getStringExtra("title"));
 

						        
						        TypeStatistics2Activity.this.startActivity(intent);

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
						catch(JSONException e)
						{
							
						}
						
						
					}
				});

			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
