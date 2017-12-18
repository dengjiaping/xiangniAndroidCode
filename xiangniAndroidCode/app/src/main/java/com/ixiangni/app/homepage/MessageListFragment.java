package com.ixiangni.app.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment2;
import com.ixiangni.app.R;
import com.ixiangni.app.contactlist.SearchContactsActivity;

import butterknife.Bind;

/**
 * 想你消息列表
 * @ClassName:MessageListFragment

 * @PackageName:com.ixiangni.app.homepage

 * @Create On 2017/7/3 0003   16:22

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/7/3 0003 handongkeji All rights reserved.
 */

public class MessageListFragment extends EaseConversationListFragment2 {


    @Bind(R.id.iv_search)
    ImageView ivSearch;
    public static MessageListFragment instance;

//    private String [] textArrays = new String[]{"热烈庆祝融信普惠迁移北海市成功","分享助人，共享始终","信心源泉"};
//    private MarqueeTextView marqueeTv;

    public MessageListFragment() {
        // Required empty public constructor
    }



    public static MessageListFragment newInstance(String param1, String param2) {

        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance=null;

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.ease_fragment_conversation_list2,null);
//        marqueeTv= (MarqueeTextView) view.findViewById(R.id.marqueeTv);
//        marqueeTv.setTextArraysAndClickListener(textArrays, new MarqueeTextViewClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getActivity(), "123456", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), ThingActivity.class));
//            }
//        });
//
//        return view;
//    }

    @Override
    protected void initView() {
        super.initView();
        titleBar.setRightLayoutClickListener(v -> {

            startActivity(new Intent(getActivity(),SearchContactsActivity.class));
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {

                String userName = conversation.conversationId();

                EMConversation.EMConversationType chatType = conversation.getType();
                int ct =1;
                if(chatType== EMConversation.EMConversationType.Chat){
                    ct= EaseConstant.CHATTYPE_SINGLE;
                }else if(chatType== EMConversation.EMConversationType.GroupChat){
                    ct=EaseConstant.CHATTYPE_GROUP;
                }else if(chatType== EMConversation.EMConversationType.ChatRoom){
                    ct=EaseConstant.CHATTYPE_CHATROOM;
                }
                ChatActivity.startChat(getContext(),userName,ct);

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
