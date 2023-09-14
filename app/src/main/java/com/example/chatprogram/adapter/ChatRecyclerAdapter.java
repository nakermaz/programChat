package com.example.chatprogram.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatprogram.ChatActivity;
import com.example.chatprogram.R;
import com.example.chatprogram.model.ChatMessageModel;
import com.example.chatprogram.utils.AndoridUtil;
import com.example.chatprogram.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.util.AndroidUtilsLight;

import io.reactivex.rxjava3.annotations.NonNull;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;

    public ChatRecyclerAdapter(@androidx.annotation.NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@androidx.annotation.NonNull ChatModelViewHolder holder, int position, @androidx.annotation.NonNull ChatMessageModel model) {
        if(model.getSenderId().equals(FirebaseUtil.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());
        } else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(model.getMessage());
        }
    }

    @androidx.annotation.NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);

    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;
        

        public ChatModelViewHolder(@NonNull View itemView){
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}