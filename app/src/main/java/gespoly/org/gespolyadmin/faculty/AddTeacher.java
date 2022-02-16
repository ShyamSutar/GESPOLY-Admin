package gespoly.org.gespolyadmin.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import gespoly.org.gespolyadmin.databinding.ActivityAddTeacherBinding;

public class AddTeacher extends AppCompatActivity {

    ActivityAddTeacherBinding binding;
    private String category;
    Uri imageUri;
    ProgressDialog pd;
    DatabaseReference reference;
    StorageReference storageReference;
    private String name, email, post, downloadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("gallery");

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
            binding.teacherEmail.setError("Empty");
            binding.teacherEmail.requestFocus();
        }else if(email.isEmpty()){
            binding.teacherEmail.setError("Empty");
            binding.teacherEmail.requestFocus();
        }else if(post.isEmpty()){
            binding.teacherPost.setError("Empty");
            binding.teacherPost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please provide teacher category", Toast.LENGTH_SHORT).show();
        }else if(imageUri == null){
            insertData();
        }else{
            insertImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

            binding.addTeacherImage.setImageURI(imageUri);

//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
//
//            } catch (IOException e) {
//                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//            }
//            binding.noticeImageView.setImageBitmap(bitmap);

        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}