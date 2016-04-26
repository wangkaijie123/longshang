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
public class FriendWlAddFragment extends Fragment {
	private View Dynamic;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_friend_wl_add,
					container, false);
		}
	
		return Dynamic;
	}	

}
