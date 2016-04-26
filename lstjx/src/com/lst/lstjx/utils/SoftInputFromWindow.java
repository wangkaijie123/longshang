<<<<<<< HEAD
package com.lst.lstjx.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * author describe parameter return
 */
public class SoftInputFromWindow {

	

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);

		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);

	}

	public static void setupUI(final Activity actity, View view) {

		// Set up touch listener for non-text box views to hide keyboard.

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(actity); // Main.this是我的activity名
					return false;
				}

			});

		}

		// If a layout container, iterate over children and seed recursion.

		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(actity, innerView);
			}

		}

	}
}
=======
package com.lst.lstjx.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * author describe parameter return
 */
public class SoftInputFromWindow {

	

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);

		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);

	}

	public static void setupUI(final Activity actity, View view) {

		// Set up touch listener for non-text box views to hide keyboard.

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(actity); // Main.this是我的activity名
					return false;
				}

			});

		}

		// If a layout container, iterate over children and seed recursion.

		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(actity, innerView);
			}

		}

	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
