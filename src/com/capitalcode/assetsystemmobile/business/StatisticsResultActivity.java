package com.capitalcode.assetsystemmobile.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.ChartActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.ResultActivity;
import com.capitalcode.assetsystemmobile.WebActivity;
import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.model.AssetInfoModel;
import com.capitalcode.assetsystemmobile.model.AssetListInfoModel;
import com.capitalcode.assetsystemmobile.model.StateStatisticsModel;
import com.capitalcode.assetsystemmobile.model.TrendStatisticsModel;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsModel;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsModelSecond;
import com.capitalcode.assetsystemmobile.model.TypeStatisticsModelThird;
import com.capitalcode.assetsystemmobile.utils.SerializableList;

public class StatisticsResultActivity extends BaseActivity {

	private ListView mListView;

	ScrollAdapter adapter;
	static public List<StateStatisticsModel> MsAsset;
	static public List<TrendStatisticsModel> MsAssetTrend;
	static public List<TypeStatisticsModel> MsAssetType_first;
	static public List<TypeStatisticsModelSecond> MsAssetType_second;
	static public List<TypeStatisticsModelThird> MsAssetType_third;
	
	List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
	
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
		setContentView(R.layout.activity_statisticsresult);
		
		mListView = (ListView) findViewById(R.id.scroll_list);
		

		
		if( MenuId.equals("280"))
		{
			Map<String, String> data = null;
			TextView tv = (TextView)findViewById(R.id.title1);
			tv.setText("资产状态");
			for( StateStatisticsModel model : MsAsset ) {
				data = new HashMap<String, String>();
				
				data.put("data_" + 0, model.Status );
				data.put("data_" + 1, model.Quantity );
				data.put("data_" + 2, model.AssetOriWorth );
				datas.add(data);
			}
		
			
			adapter = new ScrollAdapter(this, datas, R.layout.item
					, new String[] { "data_0", "data_1", "data_2",  }
					, new int[] { R.id.item_data0 
								, R.id.item_data1
								, R.id.item_data2
								
			});
		}
		else if( MenuId.equals("420"))
		{
			Map<String, String> data = null;
			TextView tv = (TextView)findViewById(R.id.title1);
			tv.setText("统计时间");
			for( TrendStatisticsModel model : MsAssetTrend ) {
				data = new HashMap<String, String>();
				
				data.put("data_" + 0, model.StaticTime );
				data.put("data_" + 1, model.Quantity );
				data.put("data_" + 2, model.AssetOriWorth );
				datas.add(data);
			}
			
			
			adapter = new ScrollAdapter(this, datas, R.layout.item
					, new String[] { "data_0", "data_1", "data_2",  }
					, new int[] { R.id.item_data0 
								, R.id.item_data1
								, R.id.item_data2
								
			});
			
		}
		else if( MenuId.equals("421"))
		{
			String step = getIntent().getStringExtra("step");
			
			if( step.equals("first") )
			{
				TextView tv = (TextView)findViewById(R.id.title1);
				tv.setText("使用部门");
				
				tv = (TextView)findViewById(R.id.title2);
				tv.setText("库存数量");
				
				tv = (TextView)findViewById(R.id.title3);
				tv.setText("在用数量");
				
				tv = (TextView)findViewById(R.id.title4);
				tv.setText("库存率");
				tv.setVisibility(View.VISIBLE);
				
				tv = (TextView)findViewById(R.id.title5);
				tv.setText("合计原值");
				tv.setVisibility(View.VISIBLE);
				
				
				Map<String, String> data = null;
				for( TypeStatisticsModel model : MsAssetType_first ) {
					data = new HashMap<String, String>();
					
					data.put("data_" + 0, model.DeptName );
					data.put("data_" + 1, model.StockQuantity );
					data.put("data_" + 2, model.UseQuantity );
					data.put("data_" + 3, model.StockRate );
					data.put("data_" + 4, model.TotalOriWorth );
					datas.add(data);
				}
				
				
				
				mListView.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						TypeStatisticsModel model = MsAssetType_first.get(arg2);
						String DeptName = model.DeptName;
						String DeptId = model.DeptId;
						
				        Intent intent = new Intent(StatisticsResultActivity.this, TypeStatistics2Activity.class);
				        
				        intent.putExtra("MenuId", MenuId);
				        intent.putExtra("title", StatisticsResultActivity.this.getIntent().getStringExtra("title"));
				        intent.putExtra("DeptName", DeptName);
				        intent.putExtra("DeptId", DeptId);

				        
				        StatisticsResultActivity.this.startActivity(intent);
					}
					
				});
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			else if( step.equals("second"))
			{
				TextView tv = (TextView)findViewById(R.id.title1);
				tv.setText("资产分类");
				
				tv = (TextView)findViewById(R.id.title2);
				tv.setText("库存数量");
				
				tv = (TextView)findViewById(R.id.title3);
				tv.setText("在用数量");
				
				tv = (TextView)findViewById(R.id.title4);
				tv.setText("库存率");
				tv.setVisibility(View.VISIBLE);
				
				tv = (TextView)findViewById(R.id.title5);
				tv.setText("合计原值");
				tv.setVisibility(View.VISIBLE);
				
				Map<String, String> data = null;
				for( TypeStatisticsModelSecond model : MsAssetType_second ) {
					data = new HashMap<String, String>();
					
					data.put("data_" + 0, model.CategoryName );
					data.put("data_" + 1, model.StockQuantity );
					data.put("data_" + 2, model.UseQuantity );
					data.put("data_" + 3, model.StockRate );
					data.put("data_" + 4, model.TotalOriWorth );
					datas.add(data);
				}
				
				mListView.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						TypeStatisticsModelSecond model = MsAssetType_second.get(arg2);
						
						
						
						
				        Intent intent = new Intent(StatisticsResultActivity.this, TypeStatistics3Activity.class);
				        
				        intent.putExtra("MenuId", MenuId);
				        intent.putExtra("title", StatisticsResultActivity.this.getIntent().getStringExtra("title"));
				        intent.putExtra("DeptName", StatisticsResultActivity.this.getIntent().getStringExtra("DeptName"));
				        intent.putExtra("CategoryName", model.CategoryName);
				        intent.putExtra("DeptId", StatisticsResultActivity.this.getIntent().getStringExtra("DeptId"));
				        intent.putExtra("CategoryId", model.CategoryId);
				        
				        StatisticsResultActivity.this.startActivity(intent);
						
						
						
					}
					
				});
				
				
			}
			else
			{
				TextView tv = (TextView)findViewById(R.id.title1);
				tv.setText("规格型号");
				
				tv = (TextView)findViewById(R.id.title2);
				tv.setText("库存数量");
				
				tv = (TextView)findViewById(R.id.title3);
				tv.setText("在用数量");
				
				tv = (TextView)findViewById(R.id.title4);
				tv.setText("库存率");
				tv.setVisibility(View.VISIBLE);
				
				tv = (TextView)findViewById(R.id.title5);
				tv.setText("合计原值");
				tv.setVisibility(View.VISIBLE);
				
				Map<String, String> data = null;
				for( TypeStatisticsModelThird model : MsAssetType_third ) {
					data = new HashMap<String, String>();
					
					data.put("data_" + 0, model.Standard );
					data.put("data_" + 1, model.StockQuantity );
					data.put("data_" + 2, model.UseQuantity );
					data.put("data_" + 3, model.StockRate );
					data.put("data_" + 4, model.TotalOriWorth );
					datas.add(data);
				}
			}
			
			

			
			
			adapter = new ScrollAdapter(this, datas, R.layout.item
					, new String[] { "data_0", "data_1", "data_2", "data_3", "data_4", }
					, new int[] { R.id.item_data0 
								, R.id.item_data1
								, R.id.item_data2
								, R.id.item_data3
								, R.id.item_data4
								
			});
			
		}

		mListView.setAdapter(adapter);

	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		if( MenuId.equals("421") == false )
		{
			Button btn = (Button) this.findViewById(R.id.btn_title_right);
			btn.setText("图形报表");
			
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if( MenuId.equals("280"))
					{
						/*
						Bundle bundle = new Bundle();
						bundle.putString("title1", "资产状态数量统计图");
						bundle.putString("title2", "资产状态原值统计图");
						
						bundle.putString("primary", "资产状态");
						bundle.putString("attach1", "数量");
						bundle.putString("attach2", "原值");
						
						List<Object> primarys = new ArrayList<Object>();
						List<Object> attach1s = new ArrayList<Object>();
						List<Object> attach2s = new ArrayList<Object>();
						
						for( StateStatisticsModel item:MsAsset)
						{
							primarys.add(item.Status);
							attach1s.add(item.Quantity);
							attach2s.add(item.AssetOriWorth);
						}

						SerializableList listprimary = new SerializableList();
						listprimary.setList(primarys);
						SerializableList listattach1 = new SerializableList();
						listattach1.setList(attach1s);
						SerializableList listattach2 = new SerializableList();
						listattach2.setList(attach2s);
						
						bundle.putSerializable("listprimary", listprimary);
						bundle.putSerializable("listattach1", listattach1);
						bundle.putSerializable("listattach2", listattach2);
						
						Intent intent = new Intent(
								StatisticsResultActivity.this,
								ChartActivity.class);
						intent.putExtras(bundle);

						StatisticsResultActivity.this.startActivity(intent);
						*/

						

						
						String url = "http://58.134.112.6:80/reportcharts.aspx?data="+"{\"MsAsset\":" + gson.toJson(MsAsset) + "}";
						Log.e("graphurl",url);
						
						Intent intent = new Intent(
								StatisticsResultActivity.this,
								WebActivity.class);
						intent.putExtra("url", url);

						StatisticsResultActivity.this.startActivity(intent);
					}
					else
					{
						/*
						Bundle bundle = new Bundle();
						bundle.putString("title1", "资产走势数量统计图");
						bundle.putString("title2", "资产走势原值统计图");
						
						bundle.putString("primary", "统计时间");
						bundle.putString("attach1", "数量");
						bundle.putString("attach2", "原值");
						
						List<Object> primarys = new ArrayList<Object>();
						List<Object> attach1s = new ArrayList<Object>();
						List<Object> attach2s = new ArrayList<Object>();
						
						for( TrendStatisticsModel item:MsAssetTrend)
						{
							primarys.add(item.StaticTime);
							attach1s.add(item.Quantity);
							attach2s.add(item.AssetOriWorth);
						}

						SerializableList listprimary = new SerializableList();
						listprimary.setList(primarys);
						SerializableList listattach1 = new SerializableList();
						listattach1.setList(attach1s);
						SerializableList listattach2 = new SerializableList();
						listattach2.setList(attach2s);
						
						bundle.putSerializable("listprimary", listprimary);
						bundle.putSerializable("listattach1", listattach1);
						bundle.putSerializable("listattach2", listattach2);
						
						Intent intent = new Intent(
								StatisticsResultActivity.this,
								ChartActivity.class);
						intent.putExtras(bundle);

						StatisticsResultActivity.this.startActivity(intent);
						*/
						String url = "http://58.134.112.6:80/reportcharts.aspx?data="+"{\"MsAsset\":" + gson.toJson(MsAssetTrend) + "}";
						Log.e("graphurl",url);
						
						Intent intent = new Intent(
								StatisticsResultActivity.this,
								WebActivity.class);
						intent.putExtra("url", url);

						StatisticsResultActivity.this.startActivity(intent);
					}
					
					
				}
			});
			
			
		}
		else
		{
			Button btn = (Button) this.findViewById(R.id.btn_title_right);
			btn.setVisibility(View.GONE);
		}
		

	}

}
