package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;

import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.DetailCheckModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;


public class CheckDetailActivity extends BaseActivity implements View.OnClickListener{
	private String BatchId;
	private ScrollAdapter adapter;
	private List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
	
	private Spinner mSpinner;
	int pagecount = 0;
	int currentpage = 1;
	private List<MsAssetModel> AllFindMsAsset=new ArrayList<MsAssetModel>();
	
	private List<SaveCheckDataModel> listSave;
	private List<MsAssetModel> MsAsset;
	private String[] values;
	
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
		setContentView(R.layout.activity_check_detail);
		
		BatchId = this.getIntent().getStringExtra("BatchId");
		
		Log.e("erwefefdfdgfdggytr",MenuId);
		if( MenuId.equals("offline_statistics") )
		{
            SharedPreferences sp = CheckDetailActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
            String strlist = sp.getString("check"+mobile, null);
			if( strlist != null )
			{
				listSave = gson
						.fromJson(
								strlist,
								new TypeToken<List<SaveCheckDataModel>>() {
								}.getType());

				
				for( SaveCheckDataModel model :  listSave )
				{
					if(model.MsStock.BatchId.equals(BatchId))
					{
						MsAsset = model.MsAsset;			
						Log.e("sizesize",MsAsset.size()+"");
						break;
					}
				}
				
			}

		}
		
		
        Button btn = (Button)this.findViewById(R.id.btn_title_left);

        btn = (Button)this.findViewById(R.id.btn_title_right);
        btn.setVisibility(View.GONE);
        
        TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
        tv.setText("批次明细");
        
        mSpinner = (Spinner) findViewById(R.id.spinner_state);
        String[] mItems = new String[basedataModel.AssetStockState.size()+1];
        values = new String[basedataModel.AssetStockState.size()+1];
        int index = 0;
        mItems[index++] = "全部";
        
        for (String key : basedataModel.AssetStockState.keySet()) 
        {
        	mItems[index] =  basedataModel.AssetStockState.get(key); 	  
        	values[index] = key;
        	index++;
        }  

        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
 
        mSpinner.setAdapter(_Adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) 
            {
            	EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.codevalue);
            	edit.setText("");
            	
            	edit = (EditText)CheckDetailActivity.this.findViewById(R.id.snvalue);
            	edit.setText("");
            	
            	String StockState = null ;
            	if( position > 0 )
            	{
            		StockState = values[position];
            	}
            	
            	currentpage = 1;
            	getList(null,null,StockState);
            	
				// 设置选中的字体颜色 大小 位置
				TextView tvSelect = (TextView)view;
				tvSelect.setTextColor(Color.parseColor("#464d4c"));
				tvSelect.setTextSize(18.0f);    //设置大小
				tvSelect.setGravity(Gravity.CENTER_VERTICAL);   //设置居中
            	
                
                
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        
        
        
        
		ListView mListView = (ListView) findViewById(R.id.scroll_list);
		adapter = new ScrollAdapter(this, datas, R.layout.item
							, new String[] { "data_0", "data_1", "data_2", "data_3","data_4", "data_5", }
							, new int[] { R.id.item_data0 
										, R.id.item_data1
										, R.id.item_data2
										, R.id.item_data3
										, R.id.item_data4
										, R.id.item_data5
		});
		mListView.setAdapter(adapter);

        

	}
	
	void setupData(DetailCheckModel model)
	{
			datas.clear();
			pagecount = Integer.valueOf(model.PageCount)/10;
			
			int other = Integer.valueOf(model.PageCount)%10;
			if( other != 0 )
			{
				pagecount++;
			}
			
			Button btn = (Button)this.findViewById(R.id.btn_next);
			if( this.currentpage == this.pagecount )
			{
				btn.setEnabled(false);
			}
			

			Map<String, String> data = null;
			for( MsAssetModel item : model.MsAsset ) {
				data = new HashMap<String, String>();
				
				data.put("data_" + 0, item.assetCode );
				data.put("data_" + 1, item.assetName );
				data.put("data_" + 2, item.str9 );
				data.put("data_" + 3, item.useUserName );
				data.put("data_" + 4, item.useDeptName );
				data.put("data_" + 5, item.inventoryStateName );	
				
				
//				data.put("data_" + 5, item.stockStateName );	
//				{"assetId":"1495","assetCode":"CP160402451","assetName":"座椅",
//					"useDeptName":"首码","useUserName":"","inventoryStateName":"盘亏",
//					"str9":"20151125"}

				datas.add(data);
			}
	}
	
	
	void getList()
	{
		if( MenuId.equals("online_statistics") )
		{
		
			param.put("PageIndex", currentpage+"");
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {
					
					m_pDialog = new ProgressDialog(CheckDetailActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在获取数据...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();
					
				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(
							ApiAddressHelper.getStockAssetList(), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {
						
						Common.ShowInfo(
								context,"网络故障");
						return;
					}
	
					try 
					{				
						JSONObject respJson = new JSONObject(res);
						
						JSONObject response = respJson
								.getJSONObject("response");
						
						String ErrorCode = response.getString("ErrorCode");
						if( ErrorCode.equals("S00000") == false )
						{
							
							String ErrorMsg = response.getString("ErrorMsg");
							Common.ShowInfo(
									context,ErrorMsg);
							return;
						}
						
	
						RequestRetModel<DetailCheckModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<DetailCheckModel>>() {
										}.getType());
						
	
						
						if (model != null) 
						{
							setupData(model.rspcontent);
							adapter.notifyDataSetChanged();
							
							TextView tv = (TextView)CheckDetailActivity.this.findViewById(R.id.page);
							tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
						} 
						else 
						{
							
							Common.ShowInfo(context, "网络异常");
							Log.e("fail",
									"failfailfailfailfailfailfailfailfail");
						}

					}  
					catch (JsonSyntaxException localJsonSyntaxException) {
						
						Log.e("fail",
								localJsonSyntaxException.getMessage());
					}
					catch (JSONException localJsonSyntaxException) {

					}
				}
			});
		
		}
		else
		{
			
			DetailCheckModel detailmodel = new DetailCheckModel();
			int tail = 10*(currentpage-1)+9;
			if( tail >= AllFindMsAsset.size() )
			{
				tail = AllFindMsAsset.size() - 1;
			}
			
			
			detailmodel.MsAsset = AllFindMsAsset.subList(10*(currentpage-1), tail);
			
			
			
			detailmodel.PageCount = String.valueOf(AllFindMsAsset.size());
			
			setupData(detailmodel);
			adapter.notifyDataSetChanged();
			
			TextView tv = (TextView)CheckDetailActivity.this.findViewById(R.id.page);
			tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
	
		}
	}
	
	
	void getList(String AssetCode,String SerialNumber,String StockState)
	{
		
		if( AssetCode != null && AssetCode.length() > 0 )
		{
			EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.snvalue);
			edit.setText("");
			
			edit = (EditText)CheckDetailActivity.this.findViewById(R.id.codevalue);
			edit.selectAll();
		}
		else if( SerialNumber != null && SerialNumber.length() > 0 )
		{
			EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.codevalue);
			edit.setText("");			
			
			edit = (EditText)CheckDetailActivity.this.findViewById(R.id.snvalue);
			edit.selectAll();
		}
		
		if( MenuId.equals("online_statistics") )
		{
		
		
			Prepare(param);
			param.put("MenuId", "419");
	
			Map<String,String> MsStock = new HashMap<String,String>();
			MsStock.put("BatchId",BatchId);
			MsStock.put("AssetCode", AssetCode);
			MsStock.put("StockState", StockState);
			MsStock.put("SerialNumber", SerialNumber);
			
			param.put("MsStock", MsStock);
			param.put("PageSize", "10");
			param.put("PageIndex", currentpage+"");
			
			param.put("StockRightCode", CheckMenuActivity.StockRightCode);
	
			doAsync(new CallEarliest<String>() {
				public void onCallEarliest() throws Exception {
					
					m_pDialog = new ProgressDialog(CheckDetailActivity.this);
					m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					m_pDialog.setMessage("正在获取数据...");
					m_pDialog.setCancelable(false);
					m_pDialog.show();
					
				}
			}, new Callable<String>() {
				public String call() throws Exception {
					return HttpHelper.getInstance(context).Post(
							ApiAddressHelper.getStockAssetList(), param);
				}
			}, new Callback<String>() {
				public void onCallback(String res) {
					m_pDialog.hide();
					if (res.equals("")) {
						
						Common.ShowInfo(
								context,"网络故障");
						return;
					}
	
					try 
					{				
						JSONObject respJson = new JSONObject(res);
						
						JSONObject response = respJson
								.getJSONObject("response");
						
						String ErrorCode = response.getString("ErrorCode");
						if( ErrorCode.equals("S00000") == false )
						{
							
							String ErrorMsg = response.getString("ErrorMsg");
							Common.ShowInfo(
									context,ErrorMsg);
							
							datas.clear();
							adapter.notifyDataSetChanged();
							
							currentpage = 1;
							pagecount = 1;
							TextView tv = (TextView)CheckDetailActivity.this.findViewById(R.id.page);
							tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
							
							return;
						}
						
	
						RequestRetModel<DetailCheckModel> model = gson
								.fromJson(
										res,
										new TypeToken<RequestRetModel<DetailCheckModel>>() {
										}.getType());
						
	
						
						if (model != null) 
						{
							setupData(model.rspcontent);
							adapter.notifyDataSetChanged();
							
							TextView tv = (TextView)CheckDetailActivity.this.findViewById(R.id.page);
							tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
						} 
						else 
						{
							
							Common.ShowInfo(context, "网络异常");
							Log.e("fail",
									"failfailfailfailfailfailfailfailfail");
						}
						
						
						
						
					}  
					catch (JsonSyntaxException localJsonSyntaxException) {
						
						Log.e("fail",
								localJsonSyntaxException.getMessage());
					}
					catch (JSONException localJsonSyntaxException) {
						
						
	
	
					}
				}
			});
		
		}
		else
		{
			//List<MsAssetModel> findMsAsset = new ArrayList<MsAssetModel>();
			AllFindMsAsset.clear();
			for( MsAssetModel model : MsAsset )
			{
				
				if( (model.assetCode.equals(AssetCode) && AssetCode.length()>0) || (model.str9.equals(SerialNumber) && SerialNumber.length()>0) )
				{
					AllFindMsAsset.add(model);
					break;
				}
				else if( (StockState == null || model.inventoryStateName.equals(basedataModel.AssetStockState.get(StockState))) && ((AssetCode!=null && SerialNumber!=null &&
						AssetCode.length()==0 && SerialNumber.length()==0 ) || (AssetCode==null && SerialNumber==null)) )
				{
					AllFindMsAsset.add(model);
				}
				
			}
			
			DetailCheckModel detailmodel = new DetailCheckModel();
			if( AllFindMsAsset.size()<=10 )
			{
				detailmodel.MsAsset = AllFindMsAsset;
			}
			else
			{
				detailmodel.MsAsset = AllFindMsAsset.subList(0, 9);
			}
			
			
			detailmodel.PageCount = String.valueOf(AllFindMsAsset.size());
			
			setupData(detailmodel);
			adapter.notifyDataSetChanged();
			
			TextView tv = (TextView)CheckDetailActivity.this.findViewById(R.id.page);
			tv.setText("第"+currentpage+"页/共"+pagecount+"页" );

		}
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button)this.findViewById(R.id.btn_scan_code);
        btn.setOnClickListener(this);
        
        btn = (Button)this.findViewById(R.id.btn_scan_sn);
        btn.setOnClickListener(this);
        
		EditText edit = (EditText)this.findViewById(R.id.codevalue);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
			              
			            @Override  
			           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
			                /*判断是否是“GO”键*/  
			                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
			                {  
			                	EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.codevalue);
			                	String result = edit.getText().toString();
			                	
			                	if( result.length() > 0 )
			                	{
			                		
			                		getList(result, null, null);
			                	
			                	} 
			                	//edit.clearFocus();
			                	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
			                	imm.hideSoftInputFromWindow(edit.getWindowToken(),0);   

			                	
			                    return true;  
			                }  
			                return false;  
			            }


			        });

        
		edit = (EditText)this.findViewById(R.id.snvalue);
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
			              
			            @Override  
			           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
			                /*判断是否是“GO”键*/  
			                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
			                {  
			                	EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.snvalue);
			                	String result = edit.getText().toString();
			                	
			                	if( result.length() > 0 )
			                	{
			                		
			                		getList(null, result, null);
			                	
			                	} 
			                	
			                	//edit.clearFocus(); 
			                	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
			                	imm.hideSoftInputFromWindow(edit.getWindowToken(),0); 
			                    return true;  
			                }  
			                return false;  
			            }


			        });        
        
        
        
        
        
		btn = (Button)this.findViewById(R.id.btn_pre);
		btn.setEnabled(false);
		btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub

				  Button btn = (Button)arg0;
				  if( CheckDetailActivity.this.currentpage > 1 )
				  {
					  CheckDetailActivity.this.currentpage--;
				  }
				
				  if( CheckDetailActivity.this.currentpage == 1 )
				  {
					  btn.setEnabled(false);
				  }
				  
				  if( CheckDetailActivity.this.currentpage < CheckDetailActivity.this.pagecount )
				  {
					  Button btn_next = (Button)CheckDetailActivity.this.findViewById(R.id.btn_next);
					  btn_next.setEnabled(true);
				  }
				  getList();
			}
			
		}
				
		);
		
		
		btn = (Button)this.findViewById(R.id.btn_next);
		if( this.currentpage == this.pagecount )
		{
			btn.setEnabled(false);
		}
		btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				
				  Button btn = (Button)arg0;
				  if( CheckDetailActivity.this.currentpage < CheckDetailActivity.this.pagecount )
				  {
					  CheckDetailActivity.this.currentpage++;
				  }
				
				  if( CheckDetailActivity.this.currentpage == CheckDetailActivity.this.pagecount )
				  {
					  btn.setEnabled(false);
				  }
				
				
				  if( CheckDetailActivity.this.currentpage > 1 )
				  {
					  Button btn_pre = (Button)CheckDetailActivity.this.findViewById(R.id.btn_pre);
					  btn_pre.setEnabled(true);
				  }
				  getList();
				
			}
			
		}
				
		);	
		btn = (Button)this.findViewById(R.id.btn_jump);
		btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				  EditText et = (EditText)findViewById(R.id.edit_jump);
				  String strNum = et.getText().toString();
				  if( strNum.length()>0 )
				  {
				  int index = Integer.valueOf(strNum);
				  
				  if( index >= 1 && index <= pagecount && index != currentpage )
				  {
					  currentpage = index;
					  
					  if( currentpage == 1 )
					  {
						  Button btn_pre = (Button)CheckDetailActivity.this.findViewById(R.id.btn_pre);
						  btn_pre.setEnabled(false);
					  }
					  
					  if( currentpage > 1 )
					  {
						  Button btn_pre = (Button)CheckDetailActivity.this.findViewById(R.id.btn_pre);
						  btn_pre.setEnabled(true);
					  }
					  
					  if( currentpage < pagecount )
					  {
						  Button btn_next = (Button)CheckDetailActivity.this.findViewById(R.id.btn_next);
						  btn_next.setEnabled(true);
					  }
				  
					  if( currentpage == pagecount )
					  {
						  Button btn_next = (Button)CheckDetailActivity.this.findViewById(R.id.btn_next);
						  btn_next.setEnabled(false);
					  }
				
				
					  getList();
				  
				  }
				  
				  }
				  
				  et.setText("");
				
			}
			
		}
				
		);       
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.btn_scan_code:
		{
            Intent it = new Intent(this, CaptureActivity.class);
            startActivityForResult(it, 1);
			
		}
		break;
		
		case R.id.btn_scan_sn:
		{
            Intent it = new Intent(this, CaptureActivity.class);
            startActivityForResult(it, 2);
			
		}
		break;
		
		}
		
	}
	
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        switch (requestCode)
        {
            case 1:
                if (data != null)
                {
                    String result = data.getStringExtra("result");
                	EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.codevalue);
                	edit.setText(result);
                    getList(result, null, null);
                }
                break;
                
            case 2:
                if (data != null)
                {
                    String result = data.getStringExtra("result");
                	EditText edit = (EditText)CheckDetailActivity.this.findViewById(R.id.snvalue);
                	edit.setText(result);
                    getList(null, result, null);
                }
                break;    

            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
