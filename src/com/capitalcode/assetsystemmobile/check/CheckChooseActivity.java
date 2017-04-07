package com.capitalcode.assetsystemmobile.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.view.Gravity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.capitalcode.assetsystemmobile.BaseActivity;
import com.capitalcode.assetsystemmobile.R;
import com.capitalcode.assetsystemmobile.adapter.PeopleCheckAdapter;
import com.capitalcode.assetsystemmobile.async.CallEarliest;
import com.capitalcode.assetsystemmobile.async.Callable;
import com.capitalcode.assetsystemmobile.async.Callback;
import com.capitalcode.assetsystemmobile.model.DetailCheckModel;
import com.capitalcode.assetsystemmobile.model.Getui;
import com.capitalcode.assetsystemmobile.model.ListMsStockModel;
import com.capitalcode.assetsystemmobile.model.MsAssetModel;
import com.capitalcode.assetsystemmobile.model.MsStockModel;
import com.capitalcode.assetsystemmobile.model.PeopleCheck;
import com.capitalcode.assetsystemmobile.model.RequestRetModel;
import com.capitalcode.assetsystemmobile.model.SaveCheckDataModel;
import com.capitalcode.assetsystemmobile.model.StockRightCodeModel;
import com.capitalcode.assetsystemmobile.utils.ApiAddressHelper;
import com.capitalcode.assetsystemmobile.utils.Common;
import com.capitalcode.assetsystemmobile.utils.HttpHelper;
import com.capitalcode.assetsystemmobile.utils.StaticUtil;
import com.capitalcode.assetsystemmobile.utils.ToastUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

//控制盘点下载，明细，上传，盘点等功能界面
public class CheckChooseActivity extends BaseActivity {

	private SQLiteDatabase batchSqlData;
	public static ListMsStockModel listBatch;
	private Spinner mSpinner;
	private List<Getui> list;
	private Spinner spinner;
	private String strbatch;
	private String peoplebatchNumble;
	private ListView listView;
	private String[] mItems;
	private String[] BatchmItems;
	private String zzlb;
	private String zzbh;
	//	private List<Map<String, Object>> peoplecheckbatchs = new ArrayList<Map<String,Object>>();
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
		setContentView(R.layout.activity_check_choose);

		openSQL();
		creatTable();
		batchSqlData.close();

		mItems = new String[2];
		mItems[0] = "部门盘点";
		mItems[1] = "个人核查";

		TextView tv = (TextView) this.findViewById(R.id.tv_title_name);
		tv.setText(this.getIntent().getStringExtra("title"));

		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		if (MenuId.equals("online_check") || MenuId.equals("online_statistics")
				|| MenuId.equals("offline_download")) {
			btn.setVisibility(View.VISIBLE);
			btn.setText("获取授权码");

			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					CheckChooseActivity.this.Prepare(param);
					if (MenuId.equals("online_check")
							|| MenuId.equals("online_statistics")) {
						param.put("MenuId", "419");
					} else {
						param.put("MenuId", "104");
					}

					int position = mSpinner.getSelectedItemPosition();
					MsStockModel model = listBatch.MsStock.get(position);

					Map<String, String> map = new HashMap<String, String>();
					map.put("BatchId", model.BatchId);

					param.put("MsStock", map);

					doAsync(new CallEarliest<String>() {
						public void onCallEarliest() throws Exception {

							m_pDialog = new ProgressDialog(
									CheckChooseActivity.this);
							m_pDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							m_pDialog.setMessage("正在获取数据...");
							m_pDialog.setCancelable(false);
							m_pDialog.show();

						}
					}, new Callable<String>() {
						public String call() throws Exception {
							return HttpHelper.getInstance(context)
									.Post(ApiAddressHelper.getStockRightCode(),
											param);
						}
					}, new Callback<String>() {
						public void onCallback(String res) {
							m_pDialog.hide();
							if (res.equals("")) {

								Common.ShowInfo(context, "网络故障");
								return;
							}

							try {
								RequestRetModel<StockRightCodeModel> model = gson
										.fromJson(
												res,
												new TypeToken<RequestRetModel<StockRightCodeModel>>() {
												}.getType());
								if (model != null) {
									String StockRightCode = model.rspcontent.StockRightCode;
									RelativeLayout rl = (RelativeLayout) CheckChooseActivity.this
											.findViewById(R.id.author);
									rl.setVisibility(View.VISIBLE);

									TextView tv = (TextView) CheckChooseActivity.this
											.findViewById(R.id.tv_author);
									tv.setText(StockRightCode);
								} else {

									Common.ShowInfo(context, "网络异常");
									Log.e("fail",
											"failfailfailfailfailfailfailfailfail");
								}
							} catch (JsonSyntaxException localJsonSyntaxException) {

							}
						}
					});
				}
			});
		} else {
			btn.setVisibility(View.GONE);
		}

		mSpinner = (Spinner) findViewById(R.id.spinner_choose);
		//去除listBatch.MsStock里面的重复元素
		if(!listBatch.MsStock.isEmpty()){
			for(int i=0;i<listBatch.MsStock.size();i++){
				for(int j=listBatch.MsStock.size()-1;j>i;j--){
					if(listBatch.MsStock.get(i).BatchNumer.equals(listBatch.MsStock.get(j).BatchNumer)){
						listBatch.MsStock.remove(i);
					}
				}
			}
		}

		//批次编号数组
		BatchmItems = new String[listBatch.MsStock.size()];
		int n = 0;
		for (MsStockModel model : listBatch.MsStock) {
			BatchmItems[n++] = model.BatchNumer;
			Log.i("BatchNumer", "BatchNumer="+model.BatchNumer.toString());
			Log.i("BatchId", "BatchId="+model.BatchId.toString());
		}
		setDataForBatchNumber(BatchmItems);

		if (MenuId.equals("online_check") || MenuId.equals("online_statistics")) {
			btn = (Button) this.findViewById(R.id.btn_left);
			btn.setVisibility(View.GONE);

			btn = (Button) this.findViewById(R.id.btn_right);
			btn.setVisibility(View.GONE);

			btn = (Button) this.findViewById(R.id.btn_single);
			btn.setVisibility(View.VISIBLE);
			if (MenuId.equals("online_check")) {
				btn.setText("盘点");
			} else {
				btn.setText("明细");
			}

			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int position = mSpinner.getSelectedItemPosition();
					MsStockModel model = listBatch.MsStock.get(position);

					if (MenuId.equals("online_check")) {
						Intent intent = new Intent(CheckChooseActivity.this,
								CheckActivity.class);


						intent.putExtra("MenuId", MenuId);
						intent.putExtra("BatchId", model.BatchId);

						CheckChooseActivity.this.startActivity(intent);
					} else if (MenuId.equals("online_statistics")) {
						Intent intent = new Intent(CheckChooseActivity.this,
								CheckDetailActivity.class);


						intent.putExtra("MenuId", MenuId);
						intent.putExtra("BatchId", model.BatchId);

						CheckChooseActivity.this.startActivity(intent);
					}

				}
			});

		} else {

			btn = (Button) this.findViewById(R.id.btn_left);
			btn.setVisibility(View.VISIBLE);
			if (MenuId.equals("offline_download")) {
				listView = (ListView) this.findViewById(R.id.lv_getui_check);
				listView.setVisibility(View.VISIBLE);
				setDataForSpinner(mItems);//设置部门盘点或者个人核查
				setDataForList();
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//设置个人盘点或者部门盘点
						String[] CheckmItems = new String[1];
//						String[] CheckNumble = new String[1];
						zzlb = list.get(position).getZzlb();
						CheckmItems[0] = zzlb;
						setDataForSpinner(CheckmItems);
						//设置盘点批次号
						zzbh = list.get(position).getZzbh();
						Log.i("资产编号", zzbh);
						int n=0;
						for (MsStockModel model : listBatch.MsStock) {
							if(zzbh.equals(model.BatchNumer)){
								mSpinner.setSelection(n, true);
								MsStockModel msStockModel = listBatch.MsStock.get(n);
								setDataforDown(zzbh,msStockModel);
								Log.i("自动选择", msStockModel.toString());
								Log.i("第几次", ""+n);
								break;
							}
							n++;
						}
					}
				});

				btn.setText("下载");
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						mSpinner = (Spinner) findViewById(R.id.spinner_choose);

						CheckChooseActivity.this.Prepare(param);
						param.put("MenuId", "104");
						param.put("StockRightCode",
								CheckMenuActivity.StockRightCode);

						int position = mSpinner.getSelectedItemPosition();
						MsStockModel model = listBatch.MsStock.get(position);

						Map<String, String> item = new HashMap<String, String>();
						item.put("BatchId", model.BatchId);
						strbatch = model.BatchId;
						param.put("MsStock", item);

						doAsync(new CallEarliest<String>() {
							public void onCallEarliest() throws Exception {

								m_pDialog = new ProgressDialog(
										CheckChooseActivity.this);
								m_pDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								m_pDialog.setMessage("正在下载盘点信息...");
								m_pDialog.setCancelable(false);
								m_pDialog.show();

							}
						}, new Callable<String>() {
							public String call() throws Exception {
								if(StaticUtil.getSpinner().equals("个人核查")){
									return HttpHelper.getInstance(context).Post(
											ApiAddressHelper.downloadpeopleBatchDetail(),
											param);
								}
								return HttpHelper.getInstance(context).Post(
										ApiAddressHelper.downloadBatchDetail(),
										param);
							}
						}, new Callback<String>() {
							public void onCallback(String res) {
								m_pDialog.hide();
								if (res.equals("")) {

									Common.ShowInfo(context, "网络故障");
									return;
								}

								try {

									JSONObject respJson = new JSONObject(res);

									//
									Log.i("res", "res="+res.toString());

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
									} else {
										if (response.getString("ErrorMsg") != null) {
											Common.ShowInfo(context, response
													.getString("ErrorMsg"));
										}
									}

									RequestRetModel<DetailCheckModel> model = gson
											.fromJson(
													res,
													new TypeToken<RequestRetModel<DetailCheckModel>>() {
													}.getType());
									if (model != null) {
										int position = mSpinner
												.getSelectedItemPosition();
										MsStockModel MsStock = listBatch.MsStock
												.get(position);
										List<MsAssetModel> MsAsset = model.rspcontent.MsAsset;

										SaveCheckDataModel addobject = new SaveCheckDataModel();
										addobject.MsAsset = MsAsset;
										addobject.MsStock = MsStock;

										SharedPreferences sp;
										if(StaticUtil.getSpinner().equals("个人核查")){
											openSQL();
											ContentValues values = new ContentValues();
											values.put("people", mobile);
											values.put("batchid", strbatch);
											values.put("batchnumble", peoplebatchNumble);
											batchSqlData.insert("BatchTable", null, values);
											batchSqlData.close();

											sp = CheckChooseActivity.this.getSharedPreferences("peoplecheckdata",Context.MODE_PRIVATE);

										}else{
											sp = CheckChooseActivity.this
													.getSharedPreferences(
															"checkdata",
															Context.MODE_PRIVATE);
										}
										SharedPreferences.Editor editor = sp
												.edit();

										List<SaveCheckDataModel> listSave;
										String strList = sp.getString("check"
												+ mobile, null);

										if (strList == null) {
											listSave = new ArrayList<SaveCheckDataModel>();
										} else {
											listSave = gson
													.fromJson(
															strList,
															new TypeToken<List<SaveCheckDataModel>>() {
															}.getType());
											Log.i("strList", "strList="+strList.toString());
										}
										listSave.add(addobject);
										String saveStr = gson.toJson(listSave);
										//
										Log.i("saveStr", "saveStr="+saveStr.toString());

										Log.e("gson", saveStr);

										editor.putString("check" + mobile,
												saveStr);
										editor.commit();

										updatafordata(zzlb,zzbh);
										setDataForList();
									} else {

										Common.ShowInfo(context, "网络异常");
										Log.e("fail",
												"failfailfailfailfailfailfailfailfail");
									}
								} catch (JsonSyntaxException localJsonSyntaxException) {

								} catch (JSONException e) {

								}

							}
							
						});
					}
				});


			} else if (MenuId.equals("offline_check")) {
				btn.setText("盘点");
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int position = mSpinner.getSelectedItemPosition();
						MsStockModel model = listBatch.MsStock.get(position);

						Intent intent = new Intent(CheckChooseActivity.this,
								CheckActivity.class);


						intent.putExtra("MenuId", MenuId);
						intent.putExtra("BatchId", model.BatchId);

						CheckChooseActivity.this.startActivity(intent);
					}
				});

			} else if (MenuId.equals("offline_statistics")) {
				btn.setText("删除");
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int position = mSpinner.getSelectedItemPosition();
						MsStockModel model = listBatch.MsStock.get(position);

						SharedPreferences sp = CheckChooseActivity.this
								.getSharedPreferences("checkdata",
										Context.MODE_PRIVATE);
						String strlist = sp.getString("check" + mobile, null);
						SharedPreferences.Editor editor = sp.edit();

						List<SaveCheckDataModel> listSave = gson.fromJson(
								strlist,
								new TypeToken<List<SaveCheckDataModel>>() {
								}.getType());

						for (SaveCheckDataModel item : listSave) {
							if (item.MsStock.BatchId.equals(model.BatchId)) {
								List<MsAssetModel> delitems = new ArrayList<MsAssetModel>();
								for (MsAssetModel t : item.MsAsset) {
									if (t.inventoryStateName.equals("已盘")) {
										t.inventoryStateName = "盘亏";
									} else if (t.inventoryStateName.equals("盘盈")) {
										delitems.add(t);
									}
								}

								item.MsAsset.removeAll(delitems);
								break;
							}
						}

						String saveStr = gson.toJson(listSave);

						Log.e("gson3", saveStr);

						editor.putString("check" + mobile, saveStr);

						editor.commit();

						Common.ShowInfo(context, "删除成功!");
					}
				});

			} else if (MenuId.equals("offline_upload")) {
				btn.setText("上传");
				setDataForSpinner(mItems);//设置部门盘点或者个人核查
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int position = mSpinner.getSelectedItemPosition();
						MsStockModel model = listBatch.MsStock.get(position);
						SharedPreferences sp;
						String strflag = "盘点";
						if(StaticUtil.getSpinner().equals("个人核查")){
							strflag = "核查";
							sp = CheckChooseActivity.this
									.getSharedPreferences("peoplecheckdata",
											Context.MODE_PRIVATE);
						}else {
							sp = CheckChooseActivity.this
									.getSharedPreferences("checkdata",
											Context.MODE_PRIVATE);
						}
						String strlist = sp.getString("check" + mobile, null);
						if(strlist!=null){
							SharedPreferences.Editor editor = sp.edit();

							List<SaveCheckDataModel> listSave = gson.fromJson(
									strlist,
									new TypeToken<List<SaveCheckDataModel>>() {
									}.getType());

							for (SaveCheckDataModel item : listSave) 
							{
								if (item.MsStock.BatchId.equals(model.BatchId)) 
								{


									List<Map<String,Object>> MsAsset = new ArrayList<Map<String,Object>>();
									for (MsAssetModel t : item.MsAsset) 
									{
										if( t.inventoryStateName.equals("盘亏") == false && t.inventoryStateName.equals("未核查") == false )
										{
											Map<String,Object> map = new HashMap<String,Object>();
											map.put("AssetCode", t.assetCode);
											map.put("SerialNumber", t.str9);
											map.put("StockMemo", t.StockMemo);
											//<--->
											map.put("AssetId", t.assetId);
											map.put("ImgGuid",t.ImgGuid);
											//<--->
											for (String key : basedataModel.AssetStockState.keySet()) 
											{
												if( basedataModel.AssetStockState.get(key).equals(t.inventoryStateName))
												{
													map.put("StockState", key);
													break;
												}
											}  
											MsAsset.add(map);
										}
									}

									if( MsAsset.size() == 0 )
									{
										new AlertDialog.Builder(CheckChooseActivity.this).setTitle("提示")//设置对话框标题  		  
										.setMessage("该批次未"+strflag+"过，不需要上传!")//设置显示的内容  
										.setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
											@Override  
											public void onClick(DialogInterface dialog, int which) 
											{//确定按钮的响应事件  

											}  
										}).show();//在按键响应事件中显示此对话框
										break;

									}

									Map<String,Object> MsStock = new HashMap<String,Object>();
									CheckChooseActivity.this.Prepare(param);
									param.put("MenuId", "104");
									param.put("StockRightCode",
											CheckMenuActivity.StockRightCode);
									MsStock.put("BatchId", model.BatchId);
									MsStock.put("MsAsset", MsAsset);

									param.put("MsStock", MsStock);

									doAsync(new CallEarliest<String>() {
										public void onCallEarliest() throws Exception {
											String strflag = "盘点";
											if(StaticUtil.getSpinner().equals("个人核查")){
												strflag = "核查";
												openSQL();
												batchSqlData.delete("BatchTable", "batchid=?", new String[]{""+strbatch});
											}
											m_pDialog = new ProgressDialog(
													CheckChooseActivity.this);
											m_pDialog
											.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											m_pDialog.setMessage("正在上传"+strflag+"信息...");
											m_pDialog.setCancelable(false);
											m_pDialog.show();

										}
									}, new Callable<String>() {
										public String call() throws Exception {
											if(StaticUtil.getSpinner().equals("个人核查")){
												return HttpHelper.getInstance(context).Post(
														ApiAddressHelper.uppeopleloadBatchDetail(),
														param);
											}
											return HttpHelper.getInstance(context).Post(
													ApiAddressHelper.uploadBatchDetail(),
													param);
										}
									}, new Callback<String>() {
										public void onCallback(String res) {
											m_pDialog.hide();
											if (res.equals("")) {

												Common.ShowInfo(context, "网络故障");
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
												} else {
													if (response.getString("ErrorMsg") != null) {
														Common.ShowInfo(context, response
																.getString("ErrorMsg"));
													}
												}



											} catch (JsonSyntaxException localJsonSyntaxException) {

											} catch (JSONException e) {

											}

										}
									});


									break;
								}
							}
						}
					}
				});
			}

			btn = (Button) this.findViewById(R.id.btn_right);
			btn.setVisibility(View.VISIBLE);
			/*
			 * if( MenuId.equals("offline_download") ) {
			 * 
			 * } else if( MenuId.equals("offline_check") ) {
			 * 
			 * } else if( MenuId.equals("offline_statistics") ) {
			 * 
			 * } else if( MenuId.equals("offline_upload") ) {
			 * 
			 * }
			 */
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int position = mSpinner.getSelectedItemPosition();
					MsStockModel model = listBatch.MsStock.get(position);



					SharedPreferences sp = CheckChooseActivity.this.getSharedPreferences("checkdata", Context.MODE_PRIVATE);
					String strlist = sp.getString("check"+mobile, null);
					if( strlist != null )
					{
						List<SaveCheckDataModel> listSave = gson
								.fromJson(
										strlist,
										new TypeToken<List<SaveCheckDataModel>>() {
										}.getType());


						for( SaveCheckDataModel item :  listSave )
						{
							if(item.MsStock.BatchId.equals(model.BatchId))
							{
								Intent intent = new Intent(CheckChooseActivity.this,
										CheckDetailActivity.class);

								intent.putExtra("MenuId", "offline_statistics");
								intent.putExtra("BatchId", model.BatchId);

								CheckChooseActivity.this.startActivity(intent);
								break;
							}
						}
					}
				}
			});

			btn = (Button) this.findViewById(R.id.btn_single);
			btn.setVisibility(View.GONE);

		}

	}

	private void setDataForList() {
		list = new ArrayList<Getui>();
		String userName = loginModel.LoginUser.UserName;
		DatabaseHelper helper = new DatabaseHelper(CheckChooseActivity.this, "Message.db");
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query("MessageTable", new String[]{
				"message",
				"zzlb",
				"zzbh"
		},"zzflag=? and user=?", new String[]{"未读",userName}, null, null, null);
		if(cursor!=null){
			while(cursor.moveToNext()){
				Getui getui = new Getui();
				getui.setContents(cursor.getString(0));
				getui.setZzlb(cursor.getString(1));
				getui.setZzbh(cursor.getString(2));
				getui.setReadflag("未读");
				list.add(getui);
			}
			cursor.close();
		}
		if(list!=null){
			PeopleCheckAdapter adapter = new PeopleCheckAdapter(CheckChooseActivity.this, list);
			listView.setAdapter(adapter);
		}
	}

	private void updatafordata(String zzlb,String zzbh) {
		DatabaseHelper helper = new DatabaseHelper(CheckChooseActivity.this, "Message.db");
		SQLiteDatabase database = helper.getWritableDatabase();
		 ContentValues values = new ContentValues();  
         values.put("zzflag", "已读"); 
         database.update("MessageTable", values, "zzlb=? and zzbh=?", new String[] { zzlb,zzbh });  
	}
	
	private void setDataForSpinner(String[] mItems){
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.check_lei);
		layout.setVisibility(View.VISIBLE);

		spinner = (Spinner) this.findViewById(R.id.spinner_chech_lei);
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);

		spinner.setAdapter(_Adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String str = parent.getItemAtPosition(position).toString();
				if(str.equals("个人核查")){
					StaticUtil.setSpinner("个人核查");
				}else if(str.equals("部门盘点")){
					StaticUtil.setSpinner("部门盘点");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setDataForBatchNumber(String[] Items){
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Items);

		mSpinner.setAdapter(_Adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String str = parent.getItemAtPosition(position).toString();
				MsStockModel model = listBatch.MsStock.get(position);
				Log.i("批次号position", ""+position);
				Log.i("手动选择", model.toString());
				TextView tvSelect = (TextView)view;
				// 设置选中的字体颜色 大小 位置
				tvSelect.setTextColor(Color.parseColor("#464d4c"));
				tvSelect.setTextSize(18.0f);    //设置大小
				tvSelect.setGravity(Gravity.CENTER_VERTICAL);   //设置居中
				
				setDataforDown(str,model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

	private void openSQL(){
		batchSqlData = this.openOrCreateDatabase("batchSqlData", MODE_PRIVATE, null);
	}

	private void creatTable(){
		String CREATE_TABLE = "create table if not exists BatchTable (" +
				"batchnumble TEXT PRIMARY KEY," +	//批次编号
				"batchid TEXT ," +		//批次id
				"people TEXT"+	//操作人
				");"; 
		batchSqlData.execSQL(CREATE_TABLE);
	}

	private void setDataforDown(String str,MsStockModel model) {
		// TODO Auto-generated method stub
		peoplebatchNumble = str;
		Log.i("批次编号", str);
		TextView tv = (TextView) CheckChooseActivity.this
				.findViewById(R.id.tv_show);
		tv.setText(model.BatchMemo);

		tv = (TextView) CheckChooseActivity.this
				.findViewById(R.id.tv_state);
		tv.setText(model.BatchState);

		tv = (TextView) CheckChooseActivity.this
				.findViewById(R.id.tv_time);
		tv.setText(model.StockTimeStart);
	}
}
