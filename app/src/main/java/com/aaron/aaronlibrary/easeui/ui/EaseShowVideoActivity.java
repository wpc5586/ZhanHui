package com.aaron.aaronlibrary.easeui.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aaron.aaronlibrary.easeui.base.EaseBaseActivity;
import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.util.EMLog;

import java.io.File;

/**
 * show the video
 *
 */
public class EaseShowVideoActivity extends EaseBaseActivity {
	private static final String TAG = "ShowVideoActivity";

	private RelativeLayout loadingLayout;
	private ProgressBar progressBar;
	private String localFilePath;

	@Override
	protected int getContentLayoutId() {
		return R.layout.ease_showvideo_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setActionbarVisibility(true);
		setActionbarTitle("播放视频");
		loadingLayout = findViewById(R.id.loading_layout);
		progressBar = findViewById(R.id.progressBar);

		final EMMessage message = getIntent().getParcelableExtra("msg");
		if (!(message.getBody() instanceof EMVideoMessageBody)) {
			Toast.makeText(EaseShowVideoActivity.this, "Unsupported message body", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		EMVideoMessageBody messageBody = (EMVideoMessageBody)message.getBody();

		localFilePath = messageBody.getLocalUrl();

		if (localFilePath != null && new File(localFilePath).exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri videoUri;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				//添加这一句表示对目标应用临时授权该Uri所代表的文件
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				//通过FileProvider创建一个content类型的Uri
				videoUri = FileProvider.getUriForFile(mContext, Constants.FILE_CONTENT_FILEPROVIDER, new File(localFilePath));
			} else {
				videoUri = Uri.fromFile(new File(localFilePath));
			}
			intent.setDataAndType(videoUri,
					"video/mp4");
			startActivity(intent);
			finish();
		} else {
			EMLog.d(TAG, "download remote video file");
			downloadVideo(message);
		}
	}

	/**
	 * show local video
	 * @param localPath -- local path of the video file
	 */
	private void showLocalVideo(String localPath){
		File file = new File(localPath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri videoUri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			//通过FileProvider创建一个content类型的Uri
			videoUri = FileProvider.getUriForFile(mContext, Constants.FILE_CONTENT_FILEPROVIDER, new File(localPath));
		} else {
			videoUri = Uri.fromFile(new File(localPath));
		}
		intent.setDataAndType(videoUri, "video/mp4");
		startActivity(intent);
		finish();
	}

	/**
	 * download video file
	 */
	private void downloadVideo(EMMessage message) {
		message.setMessageStatusCallback(new EMCallBack() {
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadingLayout.setVisibility(View.GONE);
						progressBar.setProgress(0);
						showLocalVideo(localFilePath);
					}
				});
			}

			@Override
			public void onProgress(final int progress,String status) {
				Log.d("ease", "video progress:" + progress);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressBar.setProgress(progress);
					}
				});

			}

			@Override
			public void onError(int error, String msg) {
				Log.e("###", "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
		});
		EMClient.getInstance().chatManager().downloadAttachment(message);
	}

	@Override
	public void onBackPressed() {
		finish();
	}


}
