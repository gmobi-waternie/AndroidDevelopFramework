package com.charon.framework.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.charon.framework.R;


/**
 * 吐司提示的工具类，能够控制吐司的显示和隐藏
 */
@SuppressLint("HandlerLeak")
public class ToastUtil {
	public static final int LENGTH_SHORT = 0;
	public static final int LENGTH_LONG = 1;
	private static View mToastView;
	private WindowManager mWindowManager;
	private static int mDuration;

	private final int WHAT = 100;
	private static View oldView;

	/**
	 * Android原生Toast，使用此对象来获取当前Toast提示的位置
	 */
	private static Toast mToast;
	private static TextView mTextView;
	private WindowManager.LayoutParams mLayoutParams;

	private static ToastUtil instance = null;

	private Handler toastHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			cancelOldAlert();
			int id = msg.what;
			if (WHAT == id) {
				cancelCurrentAlert();
			}

		}

	};

	@SuppressLint("ShowToast")
	private ToastUtil(Context context) {
		mWindowManager = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

		mToastView = LayoutInflater.from(context).inflate(R.layout.toast_view,
				null);
		mTextView = (TextView) mToastView.findViewById(R.id.toast_text);

		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.windowAnimations = android.R.style.Animation_Toast;
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mLayoutParams.setTitle("Toast");
		mLayoutParams.gravity = mToast.getGravity();
	}

	private static ToastUtil getInstance(Context context) {
		if (instance == null) {
			synchronized (ToastUtil.class) {
				if (instance == null) {
					instance = new ToastUtil(context);
				}
			}
		}
		return instance;
	}

	public static ToastUtil makeText(Context context, CharSequence text,
			int duration) {
		ToastUtil util = getInstance(context);
		mDuration = duration;
		mToast.setText(text);
		mTextView.setText(text);
		return util;
	}

	public static ToastUtil makeText(Context context, int resId, int duration) {
		ToastUtil util = getInstance(context);
		mDuration = duration;
		mToast.setText(resId);
		mTextView.setText(context.getResources().getString(resId));
		return util;
	}

	/**
	 * 进行Toast显示，在显示之前会取消当前已经存在的Toast
	 */
	public void show() {
		long time = 0;
		switch (mDuration) {
		case LENGTH_SHORT:
			time = 2000;
			break;
		case LENGTH_LONG:
			time = 3500;
			break;
		default:
			time = 2000;
			break;
		}

		// 在显示一个Toast之前，先取消上一个Toast的显示
		cancelOldAlert();
		toastHandler.removeMessages(WHAT);
		mLayoutParams.y = mToast.getYOffset();
		mWindowManager.addView(mToastView, mLayoutParams);

		oldView = mToastView;
		toastHandler.sendEmptyMessageDelayed(WHAT, time);
	}

	private void cancelOldAlert() {
		if (oldView != null && oldView.getParent() != null) {
			mWindowManager.removeView(oldView);
		}
	}

	public void cancelCurrentAlert() {
		if (mToastView != null && mToastView.getParent() != null) {
			mWindowManager.removeView(mToastView);
		}
	}
}
