package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.ScrollAdapter;
import com.capitalcode.assetsystemmobile.model.DetailCheckModel;
import com.capitalcode.assetsystemmobile.model.ListMsStockModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.PeopleCheck;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.utils.StaticUtil;
import com.google.gson.reflect.TypeToken;
import com.wyy.twodimcode.CaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PeopleCheckActivity extends BaseActivity implements OnClickListener {
	private String BatchId;
	private ScrollAdapter adapter;
	public static ListMsStockModel listBatch;
	private List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
	private List<PeopleCheck> checks;
	private Spinner spinner;

	int pagecount = 0;
	int currentpage = 1;
	private List<MsAssetModel> AllFindMsAsset=new ArrayList<MsAssetModel>();

	private List<SaveCheckDataModel> listSave;
	private List<MsAssetModel> MsAsset;

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

	//Activity创建或者从被覆盖、后台重新回到前台时被调用  
	@Override  
	protected void onResume() {  
		super.onResume(); 
		if(StaticUtil.forPeopleCheckActivity!=2){
			setPeopleData();
		}
		StaticUtil.forPeopleCheckActivity=1;
	} 

	private EditText code;
	private EditText sn;
	
	private TextView oldCheck;
	private TextView newCheck;
	private SQLiteDatabase batchSqlData;
	@Override
	protected void ViewInit() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_people_check);
		spinner = (Spinner) this.findViewById(R.id.people_spinner_state);
		query();
		Log.e("erwefefdfdgfdggytr",MenuId);

		Button btn = (Button)this.findViewById(R.id.btn_title_left);

		btn = (Button)this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);

		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText("批次明细");

		code = (EditText)PeopleCheckActivity.this.findViewById(R.id.codevalue);
		sn = (EditText)PeopleCheckActivity.this.findViewById(R.id.snvalue);
		
		oldCheck = (TextView) this.findViewById(R.id.oldcheck_people_check);
		newCheck = (TextView) this.findViewById(R.id.newcheck_people_check);

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

		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				code.setText("");
				sn.setText("");
				
				String Assetscode = datas.get(position).get("data_0");
				Log.i("资产编号", Assetscode);
				Intent intent = new Intent(PeopleCheckActivity.this,PeopleUpdataActivity.class);
				intent.putExtra("assetscode", Assetscode);
				intent.putExtra("BatchId", BatchId);
				startActivity(intent);
			}

		});

	}

	private void setupData(DetailCheckModel model)
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

			datas.add(data);
		}
	}

	private void getList()
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

		TextView tv = (TextView)PeopleCheckActivity.this.findViewById(R.id.page);
		tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
	}


	private void getList(String AssetCode,String SerialNumber)
	{
		AllFindMsAsset.clear();
		for( MsAssetModel model : MsAsset )
		{
			if( (model.assetCode.equals(AssetCode) && AssetCode.length()>0) || (model.str9.equals(SerialNumber) && SerialNumber.length()>0) )
			{
				AllFindMsAsset.add(model);
				break;
			}
			else if(((AssetCode!=null && SerialNumber!=null &&
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

		TextView tv = (TextView)PeopleCheckActivity.this.findViewById(R.id.page);
		tv.setText("第"+currentpage+"页/共"+pagecount+"页" );
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub
		Button btn = (Button)this.findViewById(R.id.btn_scan_code);
		btn.setOnClickListener(this);

		btn = (Button)this.findViewById(R.id.btn_scan_sn);
		btn.setOnClickListener(this);

		code.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{  
					String result = code.getText().toString();

					if( result.length() > 0 )
					{
						Log.i("查询", result);
						getList(result, null);
					} 
					//edit.clearFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(code.getWindowToken(),0);   
					return true;  
				}  
				return false;  
			}
		});


		sn.setOnEditorActionListener(new TextView.OnEditorActionListener() {  

			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
				{  
					String result = sn.getText().toString();

					if( result.length() > 0 )
					{
						getList(null, result);
					} 
					//edit.clearFocus(); 
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(sn.getWindowToken(),0); 
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
				if( PeopleCheckActivity.this.currentpage > 1 )
				{
					PeopleCheckActivity.this.currentpage--;
				}
				if( PeopleCheckActivity.this.currentpage == 1 )
				{
					btn.setEnabled(false);
				}
				if( PeopleCheckActivity.this.currentpage < PeopleCheckActivity.this.pagecount )
				{
					Button btn_next = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_next);
					btn_next.setEnabled(true);
				}
				getList();
			}
		});

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
				if( PeopleCheckActivity.this.currentpage < PeopleCheckActivity.this.pagecount )
				{
					PeopleCheckActivity.this.currentpage++;
				}

				if( PeopleCheckActivity.this.currentpage == PeopleCheckActivity.this.pagecount )
				{
					btn.setEnabled(false);
				}

				if( PeopleCheckActivity.this.currentpage > 1 )
				{
					Button btn_pre = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_pre);
					btn_pre.setEnabled(true);
				}
				getList();
			}
		});	

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
							Button btn_pre = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_pre);
							btn_pre.setEnabled(false);
						}
						if( currentpage > 1 )
						{
							Button btn_pre = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_pre);
							btn_pre.setEnabled(true);
						}

						if( currentpage < pagecount )
						{
							Button btn_next = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_next);
							btn_next.setEnabled(true);
						}

						if( currentpage == pagecount )
						{
							Button btn_next = (Button)PeopleCheckActivity.this.findViewById(R.id.btn_next);
							btn_next.setEnabled(false);
						}
						getList();
					}
				}
				et.setText("");
			}
		});       
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent it;
		switch(arg0.getId()){

		case R.id.btn_scan_code:
			StaticUtil.forPeopleCheckActivity=2;
			it = new Intent(PeopleCheckActivity.this, CaptureActivity.class);
			startActivityForResult(it, 1);
			break;

		case R.id.btn_scan_sn:
			StaticUtil.forPeopleCheckActivity=2;
			it = new Intent(PeopleCheckActivity.this, CaptureActivity.class);
			startActivityForResult(it, 2);
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
				code.setText(result);
				getList(result, null);
			}
			break;

		case 2:
			if (data != null)
			{
				String result = data.getStringExtra("result");
				sn.setText(result);
				getList(null, result);
			}
			break;    

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setDataForSpinner(){

		List<String> mItems = new ArrayList<String>();
		for (int i = 0; i < checks.size(); i++) {
			mItems.add(checks.get(i).getBatchNumble());
		}
		Log.i("ss", mItems.toString());


		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);

		spinner.setAdapter(_Adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String str = parent.getItemAtPosition(position).toString();
				Log.i("str", str);
				for (int i = 0; i < checks.size(); i++) {
					if(checks.get(i).getBatchNumble().equals(str)){
						BatchId = checks.get(i).getBatch();
						Log.i("当前批次id", BatchId);
						setPeopleData();
						break;
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setPeopleData(){
		SharedPreferences sp = PeopleCheckActivity.this.getSharedPreferences("peoplecheckdata", Context.MODE_PRIVATE);
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
				Log.i("你粗来", model.MsStock.BatchId);
				if(model.MsStock.BatchId.equals(BatchId))
				{
					MsAsset = model.MsAsset;			
					Log.e("sizesize",MsAsset.size()+"");

					break;
				}
			}

			int old=0;
			int xin=0;
			if(MsAsset!=null){
				for (int i = 0; i < MsAsset.size(); i++) {
					if(MsAsset.get(i).inventoryStateName.equals("已核查")){
						old++;
					}
					if(MsAsset.get(i).inventoryStateName.equals("未核查")){
						xin++;
					}
				}
				getList(null,null);
			}
			oldCheck.setText("已核查："+old);
			newCheck.setText("未核查："+xin);
		}
	}

	private void openSQL(){
		batchSqlData = this.openOrCreateDatabase("batchSqlData", MODE_PRIVATE, null);
	}

	private void query(){
		openSQL();
		checks = new ArrayList<PeopleCheck>();
		Cursor cursor = batchSqlData.query("BatchTable", 
				new String[]{
				"batchid",
				"batchnumble"
		}, "people ='"+mobile+"'", null, null, null, null);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				PeopleCheck check = new PeopleCheck();
				check.setBatch(cursor.getString(0));
				check.setBatchNumble(cursor.getString(1));
				checks.add(check);
			}
			cursor.close();
		}
		batchSqlData.close();
		if(checks!=null){
			setDataForSpinner();
		}
	}
	
	private void selectData(){
		String string = code.toString();
		String string2 = sn.toString();
		if(string!=null && string.equals("")==false){
			getList(string, null);
		}
		if(string2!=null && string2.equals("")==false){
			getList(null, string2);
		}
	}
}
