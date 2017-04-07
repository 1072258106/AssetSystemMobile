package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.ResultActivity;
import com.capitalcode.assetsystemmobile.SearchActivity;
import com.capitalcode.assetsystemmobile.adapter.ContentAdapter;
import com.capitalcode.assetsystemmobile.adapter.SimpleSelectAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.check.CheckChooseActivity;
import com.capitalcode.assetsystemmobile.model.AssetListInfoModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.StateStatisticsInfoModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.SerializableMap;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.tree_view.ComplexSelectActivity;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StateStatisticsActivity extends BaseActivity {

	ListView listDept;
	
	
	SimpleSelectAdapter adapter;
	private List<Map<String,String>> listchoose = new ArrayList<Map<String,String>>();
	
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

	@Override
	protected void ViewInit() {
		setContentView(R.layout.activity_statestatistics);
		
		listDept = (ListView)this.findViewById(R.id.departmentlist);
		
		
		
		Button btn = (Button)this.findViewById(R.id.btn_choose);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StateStatisticsActivity.this, ComplexSelectActivity.class);
				intent.putExtra("title", "使用部门");
				intent.putExtra("searchid", "useDept");
				intent.putExtra("isMultChoose", true);
				
				ComplexSelectActivity.listchoose = StateStatisticsActivity.this.listchoose;
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
		
		TextView tv = (TextView)this.findViewById(R.id.dateup);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Calendar cal = Calendar.getInstance();

				
				final DatePickerDialog mDialog = new DatePickerDialog(StateStatisticsActivity.this, null,  
			                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));  
			  
				mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {  
		            @SuppressLint("NewApi")
					@Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息  
		                DatePicker datePicker = mDialog.getDatePicker();  
		                int year = datePicker.getYear();  
		                int month = datePicker.getMonth()+1;  
		                int day = datePicker.getDayOfMonth(); 
		                
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
		                
		                TextView tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.dateup);
		                tv.setText(year + "-" + strMonth + "-" + strDay);
		            }  
		        }); 
				
				
				
				
				mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {  
				                
				            }  
				        });
				
				mDialog.show();

			}
		});
		Button btn = (Button)this.findViewById(R.id.btn_clear);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.dateup);
				tv.setText("");
			}
		});
						
		
		
		
		
		
		
		
		tv = (TextView)this.findViewById(R.id.datedown);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Calendar cal = Calendar.getInstance();

				
				final DatePickerDialog mDialog = new DatePickerDialog(StateStatisticsActivity.this, null,  
			                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));  
			  
				mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息  
		                DatePicker datePicker = mDialog.getDatePicker();  
		                int year = datePicker.getYear();  
		                int month = datePicker.getMonth()+1;  
		                int day = datePicker.getDayOfMonth(); 
		                
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
		                
		                TextView tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.datedown);
		                tv.setText(year + "-" + strMonth + "-" + strDay);
		            }  
		        }); 
				
				
				
				
				mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {  
				                
				            }  
				        });
				
				mDialog.show();
				
				
				
				
			}
		});
		
		
		btn = (Button)this.findViewById(R.id.btn_clear_down);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.datedown);
				tv.setText("");
			}
		});
		
		
		btn = (Button)this.findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				
				param.clear();
				StateStatisticsActivity.this.Prepare(param);
				
				param.put("MenuId", MenuId);
				
				
				
				Map<String, String> map = new HashMap<String, String>();
				TextView tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.dateup);
				map.put("StaticTimeStart", tv.getText().toString());
				tv = (TextView)StateStatisticsActivity.this.findViewById(R.id.datedown);
				map.put("StaticTimeEnd", tv.getText().toString());
				
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
				param.put("MsQuery", map);
				
				doAsync(new CallEarliest<String>() {
					public void onCallEarliest() throws Exception {
						
						m_pDialog = new ProgressDialog(StateStatisticsActivity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();
						
					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(StateStatisticsActivity.this).Post(
								ApiAddressHelper.getStatisticsByStatus(), param);
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
							RequestRetModel<StateStatisticsInfoModel> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<StateStatisticsInfoModel>>() {
									}.getType());
							if (model != null) {


								m_pDialog.hide();
								
								StatisticsResultActivity.MsAsset = model.rspcontent.MsAsset;
								
								
						        Intent intent = new Intent(StateStatisticsActivity.this, StatisticsResultActivity.class);
						        intent.putExtra("MenuId", MenuId);
						        intent.putExtra("title", StateStatisticsActivity.this.getIntent().getStringExtra("title"));
 

						        
						        StateStatisticsActivity.this.startActivity(intent);

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
