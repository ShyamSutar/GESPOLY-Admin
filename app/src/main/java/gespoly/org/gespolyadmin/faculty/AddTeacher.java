package gespoly.org.gespolyadmin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import gespoly.org.gespolyadmin.MainActivity;
import gespoly.org.gespolyadmin.Model.NoticeModel;
import gespoly.org.gespolyadmin.Model.TeacherDataModel;
import gespoly.org.gespolyadmin.UploadNotice;
import gespoly.org.gespolyadmin.databinding.ActivityAddTeacherBinding;

public class AddTeacher extends AppCompatActivity {

    ActivityAddTeacherBinding binding;
    private String category;
    Uri imageUri;
    ProgressDialog pd;
    DatabaseReference reference, db;
    StorageReference storageReference;
    private String name, email, post;
    private String downloadUrl = "";
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("teachers");
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] items = new String[]{"Select Category", "Computer Department","Mechanical Department","Electrical Department", "Civil Department"};
        binding.addTeacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));


        binding.addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = binding.addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,4);
            }
        });

        binding.addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkValidation();

            }
        });


    }

    private void checkValidation() {
        name = binding.teacherName.getText().toString();
        email = binding.teacherEmail.getText().toString();
        post = binding.teacherPost.getText().toString();

        if(name.isEmpty()){
            binding.teacherName.setError("Empty");
            binding.teacherName.requestFocus();
        }else if(email.isEmpty()){
            binding.teacherEmail.setError("Empty");
            binding.teacherEmail.requestFocus();
        }else if(post.isEmpty()){
            binding.teacherPost.setError("Empty");
            binding.teacherPost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please provide teacher category", Toast.LENGTH_SHORT).show();
        }else if(imageUri == null){
            Toast.makeText(this, "select Image", Toast.LENGTH_SHORT).show();
            insertData();
        }else{
            insertImage();
        }
    }

    private void insertImage() {
        pd.setMessage("Uploading...");
        pd.show();

        //to compress and upload
        //here to compressing image OK

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();


        final StorageReference filePath;
        filePath = storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();
                                }
                            });
                        }
                    });
                }else{
                        pd.dismiss();
                    Toast.makeText(AddTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void insertData() {
        db = reference.child(category);
        final String uniqueKey = db.push().getKey();



        TeacherDataModel tdModel = new TeacherDataModel(binding.teacherName.getText().toString(),binding.teacherEmail.getText().toString(),binding.teacherPost.getText().toString(),downloadUrl,uniqueKey);

        db.child(uniqueKey).setValue(tdModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTeacher.this, UpdateFaculty.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

//            Picasso.get().load(imageUri).into(binding.addTeacherImage);
//            binding.addTeacherImage.setImageURI(imageUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

            } catch (IOException e) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
            binding.addTeacherImage.setImageBitmap(bitmap);

        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}