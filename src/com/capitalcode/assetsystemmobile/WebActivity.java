package com.capitalcode.assetsystemmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class WebActivity extends BaseActivity {

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
		
		setContentView(R.layout.activity_webview);
		
		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText("图形报表");
		
		Button btn = (Button) this.findViewById(R.id.btn_title_right);
		btn.setVisibility(View.GONE);
		
		String url = this.getIntent().getStringExtra("url");
		
		WebView wv = (WebView)this.findViewById(R.id.webView);
		wv.getSettings().setJavaScriptEnabled(true);  
		
		wv.getSettings().setDefaultTextEncodingName("UTF-8");
		

		wv.setWebViewClient(new WebViewClient() { 


    public boolean shouldOverrideUrlLoading(WebView view, String url) 


      { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边 


             view.loadUrl(url); 


             return true; 


      } 


 }); 
		
		
		String url2 = "http://58.134.112.6:80/reportcharts.aspx?data={\"MsAsset\":[{\"Status\":\"借用\",\"Quantity\":\"6\",\"AssetOriWorth\":\"2187.00\"},{\"Status\":\"库存\",\"Quantity\":\"30\",\"AssetOriWorth\":\"67048.00\"},{\"Status\":\"维修\",\"Quantity\":\"6\",\"AssetOriWorth\":\"11185.00\"}]}";
		Log.e("1",url);
		Log.e("2",url2);
		
		wv.loadUrl(url);


	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

}
