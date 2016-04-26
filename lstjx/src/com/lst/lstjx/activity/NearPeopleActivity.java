package com.lst.lstjx.activity;

import com.lst.lstjx.adapter.NearPeopleAdapter;
import com.lst.yuewo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class NearPeopleActivity extends Activity {
	private ListView mListView;
	private ImageView goback_wd;
	private NearPeopleAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearpeople);
		initView();
	}
	private void initView() {
		
		mListView = (ListView) findViewById(R.id.listView_nearPeople);
		goback_wd = (ImageView) findViewById(R.id.goback_wd);
		mAdapter = new NearPeopleAdapter(this);
		mListView.setAdapter(mAdapter);
		
		goback_wd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NearPeopleActivity.this.finish();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(NearPeopleActivity.this, "哎呀！你点人家干吗？", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(NearPeopleActivity.this, NearPeopleOneActivity.class);
				startActivity(intent);
			}
		});
	}
}
