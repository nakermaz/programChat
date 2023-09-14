package com.example.chatprogram.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatprogram.ChatActivity;
import com.example.chatprogram.R;
import com.example.chatprogram.model.ChatroomModel;
import com.example.chatprogram.model.UserModel;
import com.example.chatprogram.utils.AndoridUtil;
import com.example.chatprogram.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import io.reactivex.rxjava3.annotations.NonNull;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@androidx.annotation.NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@androidx.annotation.NonNull ChatroomModelViewHolder holder, int position, @androidx.annotation.NonNull ChatroomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());



                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                        FirebaseUtil.getOtherProfilePicStorageRef(otherUserModel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(t -> {
                                    if(t.isSuccessful()){
                                        Uri uri = t.getResult();
                                        AndoridUtil.setProfilePic(context, uri, holder.profilePic);
                                    }
                                });


                        holder.usernameText.setText(otherUserModel.getUsername());
                        if(lastMessageSentByMe)
                            holder.lastMessageText.setText("You : " + model.getLastMessage());
                        else
                            holder.lastMessageText.setText(model.getLastMessage());

                        holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                        holder.itemView.setOnClickListener(v -> {
                            //navigation to chat activity
                            Intent intent = new Intent(context, ChatActivity.class);
                            AndoridUtil.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }
                });
    }

    @androidx.annotation.NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatroomModelViewHolder(view);

    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;

        public ChatroomModelViewHolder(@NonNull View itemView){
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}