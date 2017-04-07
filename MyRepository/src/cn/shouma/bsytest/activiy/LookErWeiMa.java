package cn.shouma.bsytest.activiy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.adapter.MyLookAdapter;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.ScanNumber;

public class LookErWeiMa extends Activity {
	private ListView lv;
	private SQLiteDatabase database;
	private List<ScanNumber> list;
	private MyLookAdapter adapter;
	private Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_activity);

		initialize();
		// 获得对象
		Intent intent = this.getIntent();
		order = (Order) intent.getSerializableExtra("order");
		Log.i("grq", order.toString());
		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		list = getData();

		adapter = new MyLookAdapter(list, this);
		lv.setAdapter(adapter);
	}

	private List<ScanNumber> getData() {
		List<ScanNumber> list = new ArrayList<ScanNumber>();
		Cursor cur = database.query("barcode", null, null, null, null, null,
				null);
		if (cur != null) {
			while (cur.moveToNext()) {
				int sendorderId = cur.getInt(cur.getColumnIndex("sendorderId"));
				String barcode = cur.getString(cur.getColumnIndex("barcode"));
				if (Integer.parseInt(order.getpCode()) == sendorderId) {
					ScanNumber scanNumber = new ScanNumber();
					scanNumber.setSendorderId(sendorderId);
					scanNumber.setBarcode(barcode);
					list.add(scanNumber);
				}
			}
		}
		cur.close();
		return list;
	}

	private void initialize() {
		lv = (ListView) findViewById(R.id.lv_content);
	}
}
