package org.curinno.www.firebase;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private EditText ttitlename;
    private Button mButton;
    private ImageButton mImage;
    private DatabaseReference mDatabaes;
    private StorageReference blog;
    private Uri imageUri;
    private static final int GALLERY_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blog= FirebaseStorage.getInstance().getReference();
        mDatabaes= FirebaseDatabase.getInstance().getReference().child("Test");
        ttitlename=(EditText)findViewById(R.id.title);
        mImage=(ImageButton)findViewById(R.id.imageButton);
        mButton=(Button)findViewById(R.id.submit);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });







    }

    private void startPosting() {
        final String title=ttitlename.getText().toString().trim();
        if(!TextUtils.isEmpty(title) && imageUri!=null)
        {
            StorageReference filepath = blog.child("Circular_Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDatabaes.push();
                    newPost.child("title").setValue(title);
                    newPost.child("image").setValue(downloadUrl.toString());

                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mImage.setImageURI(imageUri);
        }
    }


}
