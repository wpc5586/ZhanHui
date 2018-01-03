/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aaron.aaronlibrary.easeui.ui;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.aaron.aaronlibrary.easeui.Constant;
import com.aaron.aaronlibrary.easeui.base.EaseBaseActivity;
import com.xhy.zhanhui.R;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.chat.EMMessage;

public class ContextMenuActivity extends EaseBaseActivity {
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_RECALL = 4;

	@Override
	protected int getContentLayoutId() {
		EMMessage message = getIntent().getParcelableExtra("message");
		int type = message.getType().ordinal();
		if (type == EMMessage.Type.TXT.ordinal()) {
			if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)
					|| message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)
					//red packet code : 屏蔽红包消息的转发功能
					|| message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)){
				//end of red packet code
				return R.layout.em_context_menu_for_location;
			}else if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
				return R.layout.em_context_menu_for_image;
			}else{
				return R.layout.em_context_menu_for_text;
			}
		} else if (type == EMMessage.Type.LOCATION.ordinal()) {
			return R.layout.em_context_menu_for_location;
		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
			return R.layout.em_context_menu_for_image;
		} else if (type == EMMessage.Type.VOICE.ordinal()) {
			return R.layout.em_context_menu_for_voice;
		} else if (type == EMMessage.Type.VIDEO.ordinal()) {
			return R.layout.em_context_menu_for_video;
		} else if (type == EMMessage.Type.FILE.ordinal()) {
			return R.layout.em_context_menu_for_location;
		}
		return 0;
	}

	@Override
	protected void init() {
		super.init();
		setActionbarTitle("消息操作");
		boolean isChatroom = getIntent().getBooleanExtra("ischatroom", false);
		EMMessage message = getIntent().getParcelableExtra("message");
		if (isChatroom
				//red packet code : 屏蔽红包消息的撤回功能
				|| message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
			//end of red packet code
			View v = (View) findViewById(R.id.forward);
			if (v != null) {
				v.setVisibility(View.GONE);
			}
		}
		if(message.direct() == EMMessage.Direct.RECEIVE )
		{
			View recall = (View) findViewById(R.id.recall);
			recall.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void copy(View view){
		setResult(RESULT_CODE_COPY);
		finish();
	}
	public void delete(View view){
		setResult(RESULT_CODE_DELETE);
		finish();
	}
	public void forward(View view){
		setResult(RESULT_CODE_FORWARD);
		finish();
	}
	public void recall(View view){
		setResult(RESULT_CODE_RECALL);
		finish();
	}
	
}
