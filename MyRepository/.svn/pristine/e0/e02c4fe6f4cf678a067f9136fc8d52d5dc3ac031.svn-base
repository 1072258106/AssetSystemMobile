package cn.shouma.bsytest.activiy;

import java.util.ArrayList;
import java.util.List;

import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.adapter.BaseDataAdapter;
import cn.shouma.bsytest.adapter.MyAdapter;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.Product;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BaseDataActivity extends Activity {
	private List<Product> data = new ArrayList<Product>();
	private BaseDataAdapter adapter;
	private TextView tv_download;
	private ListView lv;
	private SQLiteDatabase database;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.basedatalist_activity);
		initialize();
		tv_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}

		});
		

	}

	

	private void refresh() {
		connectSqlite();
		getProductData();
		adapter = new BaseDataAdapter(data, BaseDataActivity.this);
		// °ó¶¨Adapter
		lv.setAdapter(adapter);
		database.close();
	}

	private void connectSqlite() {
		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
	}

	private List<Product> getProductData() {
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

	private void initialize() {
		adapter = new BaseDataAdapter(data, this);
		tv_download = (TextView) findViewById(R.id.tv_baseData_download);
		lv = (ListView) findViewById(R.id.lv_baseData_List);
	}
}
