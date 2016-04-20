package com.lst.lstjx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lst.yuewo.R;

/**
 * 发表更多
 * author describe parameter return
 */
public class MoreActivity extends Activity implements OnClickListener {
	private LinearLayout buyprods, sellprods, deliver_goods, freight;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		FrameActivity.getInstance().addActivity(this);
		initView();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		System.out
				.println("________________________MoreActivity_______onDetachedFromWindow");
	}

	private void initView() {
		// TODO Auto-generated method stub
		buyprods = (LinearLayout) findViewById(R.id.buyprods);
		sellprods = (LinearLayout) findViewById(R.id.sellprods);
		deliver_goods = (LinearLayout) findViewById(R.id.deliver_goods);
		freight = (LinearLayout) findViewById(R.id.freight);
		cancel = (Button) findViewById(R.id.cancel);

		buyprods.setOnClickListener(this);
		sellprods.setOnClickListener(this);
		deliver_goods.setOnClickListener(this);
		freight.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//买货
		case R.id.buyprods:
			Intent intent = new Intent(MoreActivity.this,
					MoreBuyActivity.class);
			startActivity(intent);

			break;
		//卖货
		case R.id.sellprods:
			Intent intent1 = new Intent(MoreActivity.this,
					MoreSellActivity.class);
			startActivity(intent1);

			break;
//		发货
		case R.id.deliver_goods:
			Intent intent2 = new Intent(MoreActivity.this,
					MoreDeliverActivity.class);
			startActivity(intent2);
			break;
//		运货
		case R.id.freight:
			Intent intent3 = new Intent(MoreActivity.this,
					MoreFreightActivity.class);
			startActivity(intent3);
			break;
		case R.id.cancel:
			Intent it = new Intent();		
			MoreActivity.this.setResult(1, it);
		finish();
			break;

		default:
			break;
		}
	}

}
