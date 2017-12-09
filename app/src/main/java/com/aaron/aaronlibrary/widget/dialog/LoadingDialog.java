package com.aaron.aaronlibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.xhy.zhanhui.R;

/**
 * 加载框
 *
 * @author Aaron
 */
public class LoadingDialog extends Dialog {
	private Context context = null;
	private static LoadingDialog customProgressDialog = null;
	static Animation hyperspaceJumpAnimation = null;

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static LoadingDialog getInstance(Context context) {
		if (null == customProgressDialog) {
			customProgressDialog = createDialog(context);
		}
		return customProgressDialog;
	}

	/**
	 * 
	 * @param context
	 * 
	 */
	public static void showDialog(Context context) {
		if (null == customProgressDialog)
			;
		customProgressDialog = getInstance(context);
		if (!customProgressDialog.isShowing())
			customProgressDialog.show();
	}

	public static void DissmissDia() {
		if (null != customProgressDialog && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
	}

	public static void CancelDia() {
		if (null != customProgressDialog && customProgressDialog.isShowing())
			customProgressDialog.cancel();
	}

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingDialog createDialog(final Context context) {
		customProgressDialog = new LoadingDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.setOnKeyListener(keylistener);
		customProgressDialog.setCancelable(false);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//		ImageView imageView = (ImageView) customProgressDialog
//				.findViewById(R.id.iv_dialog_loading);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,
				R.anim.load_animation);
//		imageView.startAnimation(hyperspaceJumpAnimation);
		return customProgressDialog;
	}
	
	public static LoadingDialog createDialog(final Context context, boolean cancle) {
		customProgressDialog = new LoadingDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.setOnKeyListener(keylistener);
		customProgressDialog.setCancelable(cancle);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//		ImageView imageView = (ImageView) customProgressDialog
//				.findViewById(R.id.iv_dialog_loading);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,
				R.anim.load_animation);
//		imageView.startAnimation(hyperspaceJumpAnimation);
		return customProgressDialog;
	}

	// public void onWindowFocusChanged(boolean hasFocus) {
	// if (customProgressDialog == null) {
	// return;
	// }
	// Tools.log("--onWindowFocusChanged--");
	// ImageView imageView = (ImageView) customProgressDialog
	// .findViewById(R.id.iv_dialog_loading);
	// imageView.setBackgroundResource(R.drawable.loading_quan);
	// imageView.startAnimation(hyperspaceJumpAnimation);
	// }

	/**
	 * 
	 * [Summary] setTitile
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	public void showTimeOut() {
		Toast.makeText(context, R.string.request_timeout, Toast.LENGTH_SHORT)
				.show();
	}

	static OnKeyListener keylistener = new OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				customProgressDialog.cancel();
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}