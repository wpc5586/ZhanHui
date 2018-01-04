package com.aaron.aaronlibrary.easeui.ui;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.aaronlibrary.easeui.Constant;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.db.InviteMessgeDao;
import com.aaron.aaronlibrary.easeui.model.EaseAtMessageHelper;
import com.aaron.aaronlibrary.easeui.widget.EaseConversationList;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.domain.StartActivityUtils;

public class ConversationListFragment extends EaseConversationListFragment{

    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView = View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = errorView.findViewById(R.id.tv_connect_errormsg);
        errorView.setOnClickListener(this);
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    StartActivityUtils.startChat(mContext, username);
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    if(conversation.isGroup()){
//                        if(conversation.getType() == EMConversationType.ChatRoom){
//                            // it's group chat
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
//                        }else{
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
//                        }
//
//                    }
//                    // it's single chat
//                    intent.putExtra(Constant.EXTRA_USER_ID, username);
//                    intent.putExtra("nickName", DemoHelper.getInstance().getContactList().get(username).getNickname());
//                    startActivity(intent);
                }
            }
        });
        //red packet code : 红包回执消息在会话列表最后一条消息的展示
        conversationListView.setConversationListHelper(new EaseConversationList.EaseConversationListHelper() {
            @Override
            public String onSetItemSecondaryText(EMMessage lastMessage) {
                if (lastMessage.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
                    String sendNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
                    String receiveNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
                    String msg;
                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
                    } else {
                        if (sendNick.equals(receiveNick)) {
                            msg = getResources().getString(R.string.msg_take_red_packet);
                        } else {
                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
                        }
                    }
                    return msg;
                }
                return null;
            }
        });
        super.setUpView();
        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("~!~ delete 1 = " + item.getTitle());
        if (item.getItemId() == R.id.delete_message) {
            return deleteConversation(true, ((AdapterContextMenuInfo) item.getMenuInfo()).position);
        } else if (item.getItemId() == R.id.delete_conversation) {
            return deleteConversation(false, ((AdapterContextMenuInfo) item.getMenuInfo()).position);
        }
        return false;
    }

    /**
     * 删除消息
     * @param deleteMessage 是否删除本地记录
     */
    public boolean deleteConversation(boolean deleteMessage, int position) {
        EMConversation tobeDeleteCons = conversationListView.getItem(position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
        return true;
        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.errorView:
                ZhanHuiApplication.getInstance().loginEmchat();
                break;
        }
    }
}
