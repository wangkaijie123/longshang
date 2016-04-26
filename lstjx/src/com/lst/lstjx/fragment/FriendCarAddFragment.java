package com.lst.lstjx.fragment;

import com.lst.yuewo.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author describe parameter return
 */
public class FriendCarAddFragment extends Fragment {
	private View Dynamic;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_friend_car_add,
					container, false);
		}
		return Dynamic;
	}


}
