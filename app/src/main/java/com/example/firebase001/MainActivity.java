package com.example.firebase001;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private Button add;
    private EditText name;
    private ListView listView;

    private Uri imageUrl;

    private static final int IMAGE_REQUEST=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        add=findViewById(R.id.add);
        name=findViewById(R.id.name);
        listView=findViewById(R.id.Listview);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name=name.getText().toString();
                openImage();
            }
        });




//        final ArrayList<String> list = new ArrayList<>();
//        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
//        listView.setAdapter(adapter);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Information");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list.clear();
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    Information info = snapshot.getValue(Information.class);
//                    String txt = info.getName() + " : " + info.getEmail();
//                    list.add(txt);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



//        FirebaseFirestore.getInstance().collection("Cities").whereEqualTo("capital", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot doc : task.getResult()){
//                        Log.d("Document", doc.getId()+ "=>"+ doc.getData());
//                    }
//                }
//            }
//        });


//        DocumentReference ref = FirebaseFirestore.getInstance().collection("Cities").document("BJ");
//        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()){
//                        Log.d("Document", doc.getData().toString());
//                    }else {
//                        Log.d("Document", "No Data");
//                    }
//                }
//            }
//        });





//        DocumentReference ref = FirebaseFirestore.getInstance().collection("Cities").document("JSR");
//        ref.update("capital", true);


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "Tokyo");
//        data.put("Capital", "Japan");
//
//        db.collection("Cities").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Values Added", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



//        Map<String, Object> data = new HashMap<>();
//        data.put("capital", false);
//
//        db.collection("Cities").document("JSR").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Merg Succesfull", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });




//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "Kandy");
//        city.put("Province", "Central");
//        city.put("Country", "Sri Lanka");
//
//        db.collection("Cities").document("JSR").set(city).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(MainActivity.this, "Values Added", Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (imageUrl!= null){
            final StorageReference fileref = FirebaseStorage.getInstance().getReference().child("Uploads").child(System.currentTimeMillis()+"."+ getFileExtension(imageUrl));

            fileref.putFile(imageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("Download Url", url);
                            pd.dismiss();
                            Toast.makeText(MainActivity.this, "Upload Succesful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imageUrl = data.getData();
            uploadImage();
        }
    }
}
