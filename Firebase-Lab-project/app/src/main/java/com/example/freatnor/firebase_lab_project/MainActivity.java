package com.example.freatnor.firebase_lab_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.freatnor.firebase_lab_project.models.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DatabaseReference mDBRef;

    private ArrayList<ChatMessage> mMessages;
    private ListView mListView;
    private BaseAdapter mAdapter;

    private String mUsername;

    private TextView mUsernameDisplay;
    private EditText mMessageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference to the root object
        mDBRef = FirebaseDatabase.getInstance().getReference();

        mUsername = new StringBuilder().append("User-").append((int) (Math.random() * 4000)).toString();

        mUsernameDisplay = (TextView) findViewById(R.id.username_display);
        mUsernameDisplay.setText(getString(R.string.username_prefix) + mUsername);
        mMessageInput = (EditText) findViewById(R.id.message_input);

        mMessages = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mMessages.size();
            }

            @Override
            public Object getItem(int i) {
                return mMessages.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, viewGroup, false);
                }
                TextView user = (TextView) view.findViewById(android.R.id.text2);
                TextView message = (TextView) view.findViewById(android.R.id.text1);

                message.setText(mMessages.get(i).getMessageText());
                user.setText(mMessages.get(i).getUser());

                return view;
            }
        };
        mListView.setAdapter(mAdapter);


        mDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: data returned - " + dataSnapshot.toString());
                Log.d(TAG, "onChildAdded: s - " + s);
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                Log.d(TAG, "onChildAdded: received child with message - " + chatMessage.getMessageText());
                mMessages.add(chatMessage);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void sendMessage(View view) {
        ChatMessage message = new ChatMessage();
        message.setUser(mUsername);
        message.setMessageText(mMessageInput.getText().toString());

        mDBRef.push().setValue(message);
    }

}
