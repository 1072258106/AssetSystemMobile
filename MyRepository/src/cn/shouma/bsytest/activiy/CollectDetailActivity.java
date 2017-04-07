package cn.shouma.bsytest.activiy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.shouma.bsytest.DBManager;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.Product;
import cn.shouma.bsytest.bean.ScanNumber;

public class CollectDetailActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * ��ʾɨ����
	 */
	private TextView mTextView;
	/**
	 * ��ʾɨ���ĵ�ͼƬ
	 */
	private ImageView mImageView;
	private Button mButton;
	private EditText et_FaHuoNumber;
	private EditText et_productNumber;
	private EditText et_productStandard;
	private EditText et_businessman;
	private EditText et_type;
	private EditText et_amount;
	private TextView tv_scanAmount;
	private EditText et_productName;
	private EditText et_clien;
	private Button lookButton;
	private TextView ed_barcodeContent;

	private Order order;
	private Product product;
	// ɨ������
	private int scanNumber ;
	private SQLiteDatabase database;
	// �������
	private List<ScanNumber> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectdetail_activity);
		// ȡ��������Զ�����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// �����ť��ת����ά��ɨ����棬�����õ���startActivityForResult��ת
		// ɨ������֮������ý���
		inntialize();
		
		
		// ��ȡ����
		Intent intent = this.getIntent();
		order = (Order) intent.getSerializableExtra("order");
		product=(Product) intent.getSerializableExtra("product");
		Log.d("grq", order.toString());
		// ��ʾ����
		setData();
		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		list = getData();
		//��ö�ά������ݴ�С
		scanNumber=list.size();
		Log.d("grq", "" + list.size() + "  scanNumber" + scanNumber);
		listener();

	}

	private void listener() {
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(CollectDetailActivity.this,
						MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		lookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(CollectDetailActivity.this, LookErWeiMa.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("order", order);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		ed_barcodeContent.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					}
					return true;
				}
				return false;
			}
		});
	}

	private List<ScanNumber> getData() {
		List<ScanNumber> list = new ArrayList<ScanNumber>();
		Log.i("grq", "���ĸ���="+list.size());
		Cursor cur = database.query("barcode", null, null, null, null, null,
				null);
		if (cur != null) {
			while (cur.moveToNext()) {
				int sendorderId = cur.getInt(cur.getColumnIndex("sendorderId"));
				String barcode = cur.getString(cur.getColumnIndex("barcode"));
				ScanNumber scanNumber = new ScanNumber();
				scanNumber.setSendorderId(sendorderId);
				scanNumber.setBarcode(barcode);
				list.add(scanNumber);
			}
		}
		cur.close();
		return list;
	}

	private void setData() {
		et_amount.setText("" + order.getBoxNum());
		et_businessman.setText("" + order.getJxsName());
		et_clien.setText("" + order.getCustomer());
		et_FaHuoNumber.setText("" + order.getSendcode());
		et_productName.setText(""+product.getpName());
		et_productNumber.setText("" + order.getpCode());
		et_productStandard.setText(""+product.getpGZ());
		et_type.setText("" + order.getOutType());
		tv_scanAmount.setText("" + scanNumber+ "��");
	}

	private void inntialize() {
		mButton = (Button) findViewById(R.id.btn_erWeiMaScan);
		lookButton = (Button) findViewById(R.id.btn_erWeiMaLook);
		et_amount = (EditText) findViewById(R.id.ed_amount);
		et_businessman = (EditText) findViewById(R.id.ed_businessman);
		et_clien = (EditText) findViewById(R.id.ed_clientName);
		et_FaHuoNumber = (EditText) findViewById(R.id.ed_FaHuonumber);
		et_productNumber = (EditText) findViewById(R.id.ed_productNumber);
		et_productStandard = (EditText) findViewById(R.id.ed_productStandard);
		tv_scanAmount = (TextView) findViewById(R.id.ed_scanAmount);
		et_type = (EditText) findViewById(R.id.ed_type);
		et_productName = (EditText) findViewById(R.id.ed_productName);
		ed_barcodeContent = (EditText) findViewById(R.id.tv_barcodeContent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {

				Bundle bundle = data.getExtras();
				// ��ʾɨ�赽������

				if (getData().size() <= 0) {
					scanNumber += 1;
					// �����ݷ������ݱ�
					ScanNumber sn = new ScanNumber();
					sn.setBarcode(bundle.getString("result"));
					sn.setSendorderId(Integer.parseInt(order.getpCode()));
					// ��������
					insertData(sn);
					tv_scanAmount.setText("" + scanNumber);
					ed_barcodeContent.setText(bundle.getString("result"));
					Toast.makeText(this, "�ɹ�¼��", Toast.LENGTH_SHORT).show();
				} else {
					// ����
					for (int i = 0; i < getData().size(); i++) {
						ScanNumber sn = getData().get(i);
						if (sn.getBarcode().equals(bundle.getString("result"))) {
							Toast.makeText(this, "�����ظ���������ɨ��",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
					scanNumber += 1;
					// �����ݷ������ݱ�
					ScanNumber sn = new ScanNumber();
					sn.setBarcode(bundle.getString("result"));
					sn.setSendorderId(Integer.parseInt(order.getpCode()));
					// ��������
					insertData(sn);
					tv_scanAmount.setText("" + scanNumber);
					ed_barcodeContent.setText(bundle.getString("result"));
					Toast.makeText(this, "�ɹ�¼��", Toast.LENGTH_SHORT).show();
				}
				if (scanNumber == order.getBoxNum()) {
					ContentValues values = new ContentValues();
					// ��values���������
					values.put("orderstate", "1");
					database.update("sendorder", values,
							"pcode=" + order.getpCode(), null);
					Toast.makeText(this, "¼����ɣ������ϴ���", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}
	}

	private void insertData(ScanNumber sn) {
		ContentValues cv = new ContentValues();
		cv.put("sendorderId", sn.getSendorderId());
		cv.put("barcode", sn.getBarcode());
		database.insert("barcode", null, cv);
	}
	
}
