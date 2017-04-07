package cn.shouma.bsytest.activiy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.adapter.GoodsCollectAdapter;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.Product;

public class GoodsCollectActivity extends Activity {
	private SQLiteDatabase database;
	private ListView lv;
	private GoodsCollectAdapter adapter;
	private List<Order> list;
	private List<Product> data;
	private LinearLayout ll_editor;
	private Order editorOder;
	private Button btn_shanchu;
	private Button btn_shangchuan;
	private int deletePosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodscollect_activity);
		// 初始化控件
		lv = (ListView) findViewById(R.id.lv_baseData_List);
		ll_editor = (LinearLayout) findViewById(R.id.ll_editor);
		btn_shanchu = (Button) findViewById(R.id.btnShanChu);
		btn_shangchuan = (Button) findViewById(R.id.btnShangChuan);

		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		list = getData();
		data = getProductData();
		adapter = new GoodsCollectAdapter(list, this);
		lv.setAdapter(adapter);
		listener();
	}

	private void listener() {
		lv.setOnItemClickListener(new OnItemClickListener() {
			Order order;
			Product product;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				order = list.get(position);
				product = data.get(position);
				Intent intent = new Intent();
				intent.setClass(GoodsCollectActivity.this,
						CollectDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("order", order);
				bundle.putSerializable("product", product);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Animation translateIn = new TranslateAnimation(0, 0, 100, 0);
				translateIn.setDuration(300);
				translateIn.setFillAfter(true);
				ll_editor.startAnimation(translateIn);
				editorOder = list.get(position);
				deletePosition = position;
				return true;
			}
		});
		btn_shanchu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				database.delete("sendorder", "pcode=" + editorOder.getpCode(),
						null);
				list.remove(deletePosition);
				adapter.notifyDataSetChanged();
				database.delete("barcode",
						"sendorderId=" + editorOder.getpCode(), null);
				animationMove();
			}
		});
		btn_shangchuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editorOder.getOrderstate() == -1) {
					Toast.makeText(GoodsCollectActivity.this, "请录入数据后，再上传", 1)
							.show();
					animationMove();
				} else if (getData().get(deletePosition).getOrderstate() == 1) {
					Toast.makeText(GoodsCollectActivity.this, "上传成功", 1).show();
					animationMove();
				}
			}
		});
	}

	protected void animationMove() {
		Animation translateIn = new TranslateAnimation(0, 0, 0, 100);
		translateIn.setDuration(300);
		translateIn.setFillAfter(true);
		ll_editor.startAnimation(translateIn);
	}

	private List<Order> getData() {
		List<Order> orderData = new ArrayList<Order>();
		Cursor cur = database.query("sendorder", null, null, null, null, null,
				null);
		if (cur != null) {
			while (cur.moveToNext()) {
				int sendcode = cur.getInt(cur.getColumnIndex("sendcode"));
				String customer = cur.getString(cur.getColumnIndex("customer"));
				String pCode = cur.getString(cur.getColumnIndex("pcode"));
				String jxsName = cur.getString(cur.getColumnIndex("jxsName"));
				String outType = cur.getString(cur.getColumnIndex("outType"));
				int boxNum = cur.getInt(cur.getColumnIndex("boxNum"));
				int orderstate = cur.getInt(cur.getColumnIndex("orderstate"));

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
		cur.close();
		return orderData;
	}

	private List<Product> getProductData() {
		List<Product> data = new ArrayList<Product>();
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
		return data;

	}

}
