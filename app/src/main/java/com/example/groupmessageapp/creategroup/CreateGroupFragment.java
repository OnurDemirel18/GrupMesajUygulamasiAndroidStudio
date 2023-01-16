package com.example.groupmessageapp.creategroup;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.groupmessageapp.GroupModel;
import com.example.groupmessageapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {

    EditText groupname, groupexplanation;
    RecyclerView Groups;
    Button creategroup;
    ImageView groupimage;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    Uri filepath;

    ArrayList<GroupModel> groupModelArrayList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupname = view.findViewById(R.id.editTextTextPersonName);
        groupexplanation =  view.findViewById(R.id.editTextTextMultiLine);
        Groups = view.findViewById(R.id.recyclerView_group);
        creategroup = view.findViewById(R.id.btn_createGroup);
        groupimage = view.findViewById(R.id.imageView3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        groupModelArrayList = new ArrayList<>();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        filepath = data.getData();
                        groupimage.setImageURI(filepath);
                    }
                });

        groupimage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });


        creategroup.setOnClickListener(v -> {
           String name = groupname.getText().toString();
           String explanation = groupexplanation.getText().toString();

           if(name.isEmpty()){
               Toast.makeText(getContext(), "Grup adı boş olamaz", Toast.LENGTH_SHORT).show();
               return;
           }
           if(explanation.isEmpty()){
               Toast.makeText(getContext(), "Grup açıklaması boş olamaz", Toast.LENGTH_SHORT).show();
               return;
           }
           if(filepath != null){
               StorageReference storageReference = firebaseStorage.getReference().child("GroupImages" + UUID.randomUUID().toString());
               storageReference.putFile(filepath).addOnSuccessListener(uri -> {
                  String imageurl = uri.toString();
                   Toast.makeText(getContext(), "Resim Yüklendi.", Toast.LENGTH_SHORT).show();
                   createGroup(name, explanation, imageurl);
               });


           }
           else{
               createGroup(name, explanation, null);
           }
        });

        listGroup();
        return view;





    }

    private void createGroup(String name, String explanation, String imageurl){
        String userid = firebaseAuth.getUid();

        firebaseFirestore.collection("/users/" + userid + "/groups").add(new HashMap<String , Object>() {
            {
                put("GrupAdi", name);
                put("GrupAciklamasi", explanation);
                put("GrupResmi", imageurl);
                put("UyeNumaralari", new ArrayList<String>());
            }
        }).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Grup oluşturuldu.", Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel(name, explanation, imageurl, (List<String>)documentSnapshot.get("UyeNumaralari"), documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
                Groups.getAdapter().notifyItemInserted(groupModelArrayList.size() - 1);


            });
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Grup oluşturulamadı.", Toast.LENGTH_SHORT).show();
        });



    }

    private void listGroup(){
        String userid = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("/users/" + userid + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("GrupAdi"), documentSnapshot.getString("GrupAciklamasi"), documentSnapshot.getString("GrupResmi"), (List<String>)documentSnapshot.get("UyeNumaralari"), documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
            }
            Groups.setAdapter(new GroupAdapter(groupModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            Groups.setLayoutManager(linearLayoutManager);

        });
    }
}