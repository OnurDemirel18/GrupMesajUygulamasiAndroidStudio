package com.example.groupmessageapp.createmessage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupmessageapp.MessageModel;
import com.example.groupmessageapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateMessageFragment extends Fragment {

    EditText message, messageName;
    Button createMessage;
    RecyclerView messageRecyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ArrayList<MessageModel> messageModelArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

            message = view.findViewById(R.id.editText_message);
            messageName = view.findViewById(R.id.editText_messageName);
            createMessage = view.findViewById(R.id.btn_createMessage);
            messageRecyclerView = view.findViewById(R.id.recyclerView_message);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();

            messageModelArrayList = new ArrayList<>();

            createMessage.setOnClickListener(v -> {
                String messageText = message.getText().toString();
                String messageNameText = messageName.getText().toString();

                if(messageText.isEmpty()){
                    Toast.makeText(getContext(), "Mesaj Boş Olamaz.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(messageNameText.isEmpty()){
                    Toast.makeText(getContext(), "Mesaj Adı Boş Olamaz.", Toast.LENGTH_SHORT).show();
                    return;
                }
                createMessage(messageText, messageNameText);

            });
        listMessage();
        return view;
    }

    private void createMessage(String messageText, String messageNameText) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("/users/" + userId + "/messages").add(new HashMap<String, String>(){{
            put("message", messageText);
            put("messageName", messageNameText);
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Mesaj Oluşturuldu.", Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                MessageModel messageModel = new MessageModel(messageNameText, messageText, documentSnapshot.getId());
                messageModelArrayList.add(messageModel);
                messageRecyclerView.getAdapter().notifyItemInserted(messageModelArrayList.size() - 1);
            });

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Mesaj Oluşturulamadı.", Toast.LENGTH_SHORT).show();
        });

    }

    private void listMessage(){
        String userId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("/users/" + userId + "/messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            messageModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                MessageModel messageModel = new MessageModel(documentSnapshot.getId(), documentSnapshot.getString("messageName"), documentSnapshot.getString("message"));
                messageModelArrayList.add(messageModel);
            }
            messageRecyclerView.setAdapter(new MessageAdapter(messageModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            messageRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}