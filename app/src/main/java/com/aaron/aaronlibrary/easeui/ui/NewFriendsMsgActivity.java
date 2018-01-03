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

import android.view.View;
import android.widget.ListView;

import com.aaron.aaronlibrary.easeui.base.EaseBaseActivity;
import com.aaron.aaronlibrary.easeui.db.InviteMessgeDao;
import com.aaron.aaronlibrary.easeui.domain.InviteMessage;
import com.aaron.aaronlibrary.easeui.widget.adapter.NewFriendsMsgAdapter;
import com.xhy.zhanhui.R;

import java.util.Collections;
import java.util.List;

/**
 * Application and notification
 *
 */
public class NewFriendsMsgActivity extends EaseBaseActivity {

	private ListView listView;

	@Override
	protected int getContentLayoutId() {
		return R.layout.em_activity_new_friends_msg;
	}

	@Override
	protected void findView() {
		super.findView();
		listView = (ListView) findViewById(R.id.list);
	}

	@Override
	protected void init() {
		super.init();
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		Collections.reverse(msgs);
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
	}

	public void back(View view) {
		finish();
	}
}
