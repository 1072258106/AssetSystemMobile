package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.LoginActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.SimpleSelectAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;

import com.capitalcode.assetsystemmobile.model.TrendStatisticsInfoModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.tree_view.ComplexSelectActivity;

public class TrendStatisticsActivity extends BaseActivity {

	ListView listDept;
	int beginyear;
	int endyear;
	
	
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
		setContentView(R.layout.activity_trendstatistics);
		
		listDept = (ListView)this.findViewById(R.id.departmentlist);
		
		
		
		Button btn = (Button)this.findViewById(R.id.btn_choose);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TrendStatisticsActivity.this, ComplexSelectActivity.class);
				intent.putExtra("title", "使用部门");
				intent.putExtra("searchid", "useDept");
				intent.putExtra("isMultChoose", true);
				
				ComplexSelectActivity.listchoose = TrendStatisticsActivity.this.listchoose;
				ComplexSelectActivity.updateadapter = adapter;
				
				startActivity(intent);
			}
		});
		
		btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);
		
		
	    
		adapter = new SimpleSelectAdapter(this,listchoose);
		listDept.setAdapter(adapter);
	}

	RadioGroup rg;
	
	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		
		rg = (RadioGroup)this.findViewById(R.id.timetype);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				
				
				
				
				
			}
		});
		
		RadioButton rb = (RadioButton)this.findViewById(R.id.year);
		rb.setChecked(true);
		
		
		
		TextView tv = (TextView)this.findViewById(R.id.dateup);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Calendar cal = Calendar.getInstance();

				
				final DatePickerDialog mDialog = new DatePickerDialog(TrendStatisticsActivity.this, null,  
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
		                
		                beginyear = year;
		                
		                TextView tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.dateup);
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
				TextView tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.dateup);
				tv.setText("");
			}
		});
		
		
		
		
		
		
		
		tv = (TextView)this.findViewById(R.id.datedown);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Calendar cal = Calendar.getInstance();

				
				final DatePickerDialog mDialog = new DatePickerDialog(TrendStatisticsActivity.this, null,  
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
		                
		                endyear = year;
		                
		                TextView tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.datedown);
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
				TextView tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.datedown);
				tv.setText("");
			}
		});
		
		btn = (Button)this.findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				int checkid = TrendStatisticsActivity.this.rg.getCheckedRadioButtonId();
				
				param.clear();
				TrendStatisticsActivity.this.Prepare(param);
				
				param.put("MenuId", MenuId);


				Map<String, String> map = new HashMap<String, String>();
				TextView tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.dateup);
				if(tv.getText().toString().length() == 0 )
				{
					new AlertDialog.Builder(TrendStatisticsActivity.this).setTitle("提示")//设置对话框标题  		  
				     .setMessage("请完整填写购置起止时间")//设置显示的内容  
				     .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) 
				         {//确定按钮的响应事件  
				        	
					        return;
				         }  
				     }).show();//在按键响应事件中显示此对话框
					return;
				}
				
				
				map.put("StaticTimeStart", tv.getText().toString());
				tv = (TextView)TrendStatisticsActivity.this.findViewById(R.id.datedown);
				if(tv.getText().toString().length() == 0 )
				{
					new AlertDialog.Builder(TrendStatisticsActivity.this).setTitle("提示")//设置对话框标题  		  
				     .setMessage("请完整填写购置起止时间")//设置显示的内容  
				     .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) 
				         {//确定按钮的响应事件  
				        	
					        return;
				         }  
				     }).show();//在按键响应事件中显示此对话框
					return;
				}
				map.put("StaticTimeEnd", tv.getText().toString());
				if( checkid == R.id.year )
				{
					map.put("TimeType", "1");
				}
				else if( checkid == R.id.month )
				{
					map.put("TimeType", "2");
				}
				else
				{
					map.put("TimeType", "3");
				}
				
				if( checkid != R.id.year && beginyear != endyear)
				{
					if( checkid == R.id.month )
					{
						new AlertDialog.Builder(TrendStatisticsActivity.this).setTitle("提示")//设置对话框标题  		  
					     .setMessage("按月查询不能跨年")//设置显示的内容  
					     .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
					         @Override  
					         public void onClick(DialogInterface dialog, int which) 
					         {//确定按钮的响应事件  
					        	
						        return;
					         }  
					     }).show();//在按键响应事件中显示此对话框
					}
					else
					{
						new AlertDialog.Builder(TrendStatisticsActivity.this).setTitle("提示")//设置对话框标题  		  
					     .setMessage("按周查询不能跨年")//设置显示的内容  
					     .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
					         @Override  
					         public void onClick(DialogInterface dialog, int which) 
					         {//确定按钮的响应事件  
					        	
						        return;
					         }  
					     }).show();//在按键响应事件中显示此对话框
					}
					
					
					
					return;
				}
				
				
				
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
						
						m_pDialog = new ProgressDialog(TrendStatisticsActivity.this);
						m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						m_pDialog.setMessage("正在获取数据...");
						m_pDialog.setCancelable(false);
						m_pDialog.show();
						
					}
				}, new Callable<String>() {
					public String call() throws Exception {
						return HttpHelper.getInstance(context).Post(
								ApiAddressHelper.getStatisticsTrend(), param);
					}
				}, new Callback<String>() {
					public void onCallback(String res) throws JSONException {
						m_pDialog.hide();
						if (res.equals("")) {
							
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

							RequestRetModel<TrendStatisticsInfoModel> model = gson.fromJson(res,
									new TypeToken<RequestRetModel<TrendStatisticsInfoModel>>() {
									}.getType());
							if (model != null) {


								
								
								StatisticsResultActivity.MsAssetTrend = model.rspcontent.MsAsset;
								
								
						        Intent intent = new Intent(TrendStatisticsActivity.this, StatisticsResultActivity.class);
						        
						        intent.putExtra("MenuId", MenuId);
						        intent.putExtra("title", TrendStatisticsActivity.this.getIntent().getStringExtra("title"));
 

						        
						        TrendStatisticsActivity.this.startActivity(intent);

							} else {
								
								Common.ShowInfo(context, "网络异常");
								Log.e("fail","failfailfailfailfailfailfailfailfail");
							}
						} catch (JsonSyntaxException localJsonSyntaxException) {
							
							Log.e("iws", "Login json转换错误 e:" + localJsonSyntaxException);
							Log.e("fail","failfailfailfailfailfailfailfailfail");
						}
					}
				});

			}
		});
		
		
		
		
		
		
		
		
		

	}

}
