package cn.shouma.bsytest.activiy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;

public class MainActivity extends Activity {
	public DBManager dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void doClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_baseData:
			intent = new Intent(this, BaseDataActivity.class);
			this.startActivity(intent);
			break;
		case R.id.btn_ShouYeDownload:
			intent = new Intent(this, GoodsListActivity.class);
			this.startActivity(intent);
			break;
		case R.id.btn_ShouYeCollect:
			intent = new Intent(this, GoodsCollectActivity.class);
			this.startActivity(intent);
			break;
		}
	}
}
