package com.aaron.aaronlibrary.easeui.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.easeui.base.EaseBaseActivity;
import com.xhy.zhanhui.R;

public class EditActivity extends EaseBaseActivity {
	private EditText editText;

	@Override
	protected int getContentLayoutId() {
		return R.layout.em_activity_edit;
	}

	@Override
	protected void findView() {
		super.findView();
		editText = (EditText) findViewById(R.id.edittext);
	}

	@Override
	protected void init() {
		super.init();
		String title = getIntent().getStringExtra("title");
		String data = getIntent().getStringExtra("data");
		Boolean editable = getIntent().getBooleanExtra("editable", false);
		if(title != null)
			((TextView)findViewById(R.id.tv_title)).setText(title);
		if(data != null)
			editText.setText(data);
		editText.setEnabled(editable);
		editText.setSelection(editText.length());
		findViewById(R.id.btn_save).setEnabled(editable);
	}

	public void save(View view){
		setResult(RESULT_OK, new Intent().putExtra("data", editText.getText().toString()));
		finish();
	}

	public void back(View view) {
		finish();
	}
}
