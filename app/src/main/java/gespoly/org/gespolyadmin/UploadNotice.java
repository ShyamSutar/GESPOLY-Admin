package gespoly.org.gespolyadmin;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageRegistrar;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import gespoly.org.gespolyadmin.Model.NoticeModel;
import gespoly.org.gespolyadmin.databinding.ActivityUploadNoticeBinding;

public class UploadNotice extends AppCompatActivity {

    ActivityUploadNoticeBinding binding;
//    Bitmap bitmap;
    Uri imageUri;
    DatabaseReference reference;
    StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);




        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        binding.uploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.noticeTitle.getText().toString().isEmpty()){
//                    Toast.makeText(UploadNotice.this, "Please Enter Notice Title", Toast.LENGTH_SHORT).show();
                    binding.noticeTitle.setError("Empty");
                    binding.noticeTitle.requestFocus();
                }else if(imageUri == null){
//                    if image not given
                    uploadData();
                }else{
//                    if image given
                    uploadImage();
                }
            }
        });


    }

    private void uploadData() {

        reference = reference.child("Notice");
        final String uniqueKey = reference.push().getKey();


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NoticeModel noticeModel = new NoticeModel(binding.noticeTitle.toString(),downloadUrl,date,time,uniqueKey);

        reference.child(uniqueKey).setValue(noticeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadImage() {

        pd.setMessage("Uploading...");
        pd.show();

        //to compress and upload
        //here to compressing image OK
        final StorageReference ref;
//        final String randomKey = UUID.randomUUID().toString();
        ref = storageReference.child("Notice");

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadNotice.this, "Cover photo saved", Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = String.valueOf(uri);
                        uploadData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadNotice.this, "Error...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

            binding.noticeImageView.setImageURI(imageUri);

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