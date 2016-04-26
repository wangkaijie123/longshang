<<<<<<< HEAD
package com.lst.lstjx.fragment;

import com.lst.lstjx.activity.MoreActivity;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * author describe parameter return
 */
public class MyDealAddFragment extends Fragment {
	private View Dynamic;
	private Button tv_mm_push;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_my_deal_add,
					container, false);
		}
		initView(Dynamic);
		return Dynamic;
	}

	private void initView(View v) {

		tv_mm_push = (Button) v.findViewById(R.id.tv_mm_push);
		tv_mm_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MoreActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

}
=======
package com.lst.lstjx.fragment;

import com.lst.lstjx.activity.MoreActivity;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * author describe parameter return
 */
public class MyDealAddFragment extends Fragment {
	private View Dynamic;
	private Button tv_mm_push;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_my_deal_add,
					container, false);
		}
		initView(Dynamic);
		return Dynamic;
	}

	private void initView(View v) {

		tv_mm_push = (Button) v.findViewById(R.id.tv_mm_push);
		tv_mm_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MoreActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
