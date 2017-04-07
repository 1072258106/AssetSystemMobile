package cn.shouma.bsytest.activiy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.adapter.MyAdapter;
import cn.shouma.bsytest.adapter.MyAdapter.ViewHolder;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.Product;

public class GoodsListActivity extends Activity {
	private ListView lv;
	private MyAdapter mAdapter;
	private Button bt_download;
	private Button bt_cancel;
	private Button bt_deselectall;
	private int checkNum; // ��¼ѡ�е���Ŀ����
	private TextView tv_show;// ������ʾѡ�е���Ŀ����
	private SQLiteDatabase database;
	private List<Product> data = new ArrayList<Product>();
	private List<Order> orderData;
	private String pCode_Checked;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodslist_activity);

		/* ʵ���������ؼ� */
		lv = (ListView) findViewById(R.id.lvList);

		bt_download = (Button) findViewById(R.id.btn_commit);
		bt_cancel = (Button) findViewById(R.id.btn_cancel);
		bt_deselectall = (Button) findViewById(R.id.btn_selectAll);
		tv_show = (TextView) findViewById(R.id.tvSelectedItem);

		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		getData();

		mAdapter = new MyAdapter(data, this);
		// ��Adapter
		lv.setAdapter(mAdapter);
		listener();

	}

	private void listener() {
		// ��ѡ��ť�Ļص��ӿ�
		bt_deselectall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����list�ĳ��ȣ���MyAdapter�е�mapֵȫ����Ϊtrue
				for (int i = 0; i < data.size(); i++) {
					MyAdapter.getIsSelected().put(i, true);
				}
				// ������Ϊlist�ĳ���
				checkNum = data.size();
				// ˢ��listview��TextView����ʾ
				dataChanged();
			}
		});

		// ȡ����ť�Ļص��ӿ�
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����list�ĳ��ȣ�����ѡ�İ�ť��Ϊδѡ
				for (int i = 0; i < data.size(); i++) {
					if (MyAdapter.getIsSelected().get(i)) {
						MyAdapter.getIsSelected().put(i, false);
						checkNum--;// ������1
					}
				}
				// ˢ��listview��TextView����ʾ
				dataChanged();
			}
		});

		bt_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < data.size(); i++) {
					if (MyAdapter.getIsSelected().get(i)) {
						TextView tv_Child_pCode = (TextView) lv.getChildAt(i)
								.findViewById(R.id.tv_clientName);
						pCode_Checked = tv_Child_pCode.getText().toString();
						Log.i("grq", "����ֵΪ��"+selectData());
						//����
						if(selectData()==0){
							orderData = getSendOrderData();
							for (int a = 0; a < orderData.size(); a++) {
								Order downloaderOrder = new Order();							
								downloaderOrder = orderData.get(a);
								Log.i("gra", downloaderOrder.toString());
								// ����ContentValues���� //key:������value:������ֵ
								ContentValues cv = new ContentValues();
								// ��ContentValues���������ݣ���-ֵ��ģʽ
								cv.put("sendcode",
										"" + downloaderOrder.getSendcode());
								cv.put("customer", downloaderOrder.getCustomer());
								cv.put("pCode", downloaderOrder.getpCode());
								cv.put("jxsName", downloaderOrder.getJxsName());
								cv.put("outType", downloaderOrder.getOutType());
								cv.put("boxNum", downloaderOrder.getBoxNum());
								cv.put("orderstate",
										"" + downloaderOrder.getOrderstate());
								// ����insert�����������ݲ������ݿ�
								database.insert("sendorder", null, cv);
								bt_download.setEnabled(false);								
							}							
						}else{
							Toast.makeText(GoodsListActivity.this, "�ظ����أ����Զ����Σ�", Toast.LENGTH_SHORT).show();
						}
						
					}

				}
				
			}
		});

		// ��listView�ļ�����
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				ViewHolder holder = (ViewHolder) arg1.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				MyAdapter.getIsSelected().put(arg2, holder.cb.isChecked());
				// ����ѡ����Ŀ
				if (holder.cb.isChecked() == true) {
					checkNum++;
				} else {
					checkNum--;
				}
				Log.i("grq", "�б������");
				// ֪ͨlistViewˢ��
				mAdapter.notifyDataSetChanged();
				// ��TextView��ʾ
				tv_show.setText("��ѡ��" + checkNum + "��");
			}
		});
	}

	protected int selectData() {
		Cursor cur=database.query("sendorder", new String[]{"pcode"}, "pcode like ?", new String[]{""+pCode_Checked}, null, null, null);
		Log.d("grq", "cur="+cur.getCount());
		if(cur.getCount()!=0){
			return 1;
		}		
		return 0;
	}

	private void getData() {

		Cursor cur = database.query("product", null, null, null, null, null,
				null);
		if (cur != null) {
			while (cur.moveToNext()) {
				String pName = cur.getString(cur.getColumnIndex("pName"));
				int pCode = cur.getInt(cur.getColumnIndex("pCode"));
				String pGZ = cur.getString(cur.getColumnIndex("pGZ"));
				Product product = new Product();
				product.setpCode(pCode);
				product.setpGZ(pGZ);
				product.setpName(pName);
				data.add(product);
			}
		}
		cur.close();

	}

	private List<Order> getSendOrderData() {
		List<Order> orderData = new ArrayList<Order>();
		Cursor cur = database.query("sendorder_copy", null, null, null, null,
				null, null);
		
		if (cur != null) {
			while (cur.moveToNext()) {
				int sendcode = cur.getInt(cur.getColumnIndex("sendcode"));
				String customer = cur.getString(cur.getColumnIndex("customer"));
				String pCode = cur.getString(cur.getColumnIndex("pcode"));
				String jxsName = cur.getString(cur.getColumnIndex("jxsName"));
				String outType = cur.getString(cur.getColumnIndex("outType"));
				int boxNum = cur.getInt(cur.getColumnIndex("boxNum"));
				int orderstate = cur.getInt(cur.getColumnIndex("orderstate"));
				if (pCode.equals(pCode_Checked)) {
					Order orderDetail = new Order();

					orderDetail.setSendcode(sendcode);
					orderDetail.setCustomer(customer);
					orderDetail.setpCode(pCode);
					orderDetail.setJxsName(jxsName);
					orderDetail.setOutType(outType);
					orderDetail.setBoxNum(boxNum);
					orderDetail.setOrderstate(orderstate);
					orderData.add(orderDetail);
				}
			}
		}
		cur.close();
		return orderData;
	}

	private List<Order> getDownloadData() {
		List<Order> orderData = new ArrayList<Order>();
		Cursor cur = database.query("sendorder", null, null, null, null, null,
				null);
		if (cur != null) {
			while (cur.moveToNext()) {
				int sendcode = cur.getInt(cur.getColumnIndex("sendcode"));
				Order order = new Order();
				order.setSendcode(sendcode);
				orderData.add(order);
			}
		}
		cur.close();
		return orderData;
	}

	// ˢ��listview��TextView����ʾ
	private void dataChanged() {
		// ֪ͨlistViewˢ��
		mAdapter.notifyDataSetChanged();
		// TextView��ʾ���µ�ѡ����Ŀ
		tv_show.setText("��ѡ��" + checkNum + "��");
	}

	@Override
	protected void onDestroy() {
		database.close();
		super.onDestroy();
	}
}
