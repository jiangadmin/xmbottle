package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.ChatActivity;

/**
 * Created by wangtao on 2017/11/5.
 */

public class MessageListFragment extends EaseConversationListFragment {
    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView =  View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText =  errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.hyphenate.easeui.R.layout.ease_fragment_conversation_list, container, false);
        view.findViewById(R.id.title_bar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_search).setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        registerForContextMenu(conversationListView);

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            intent.putExtra("chatType", EMMessage.ChatType.ChatRoom);
                        } else {
                            intent.putExtra("chatType", EMMessage.ChatType.GroupChat);
                        }
                    }
                    intent.putExtra("userId", username);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }


}
