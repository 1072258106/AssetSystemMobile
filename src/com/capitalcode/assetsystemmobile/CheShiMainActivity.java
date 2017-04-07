package com.capitalcode.assetsystemmobile;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CheShiMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_che_shi_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.che_shi_main, menu);
		return true;
	}

}
