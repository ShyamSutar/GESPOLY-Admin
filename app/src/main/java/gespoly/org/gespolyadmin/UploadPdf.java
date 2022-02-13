package gespoly.org.gespolyadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

import gespoly.org.gespolyadmin.databinding.ActivityUploadPdfBinding;

public class UploadPdf extends AppCompatActivity {

    ActivityUploadPdfBinding binding;
    Uri pdfData;
    DatabaseReference database;
    StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;
    private String pdfName,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);


        binding.selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("pdf/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select File"),3);
            }
        });

        binding.uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = binding.PdfTitle.getText().toString();
                if (title.isEmpty()){
                    binding.PdfTitle.setError("Empty");
                    binding.PdfTitle.requestFocus();
                }else if(pdfData == null){
                    Toast.makeText(UploadPdf.this, "please select file", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdf();
                }
            }
        });

    }

    private void uploadPdf() {
        pd.setTitle("please wait...");
        pd.setMessage("uploading pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+ System.currentTimeMillis());
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete()); // ki ye kaam hone tak aage mat badho
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void uploadData(String valueOf) {

        String uniqueKey = database.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",valueOf);

        database.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UploadPdf.this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                binding.PdfTitle.setText("");
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPdf.this, "Failed to Upload Pdf", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data!=null){

            pdfData = data.getData();


            if (pdfData.toString().startsWith("content://")){

                Cursor cursor = null;
                try {
                    cursor = UploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }

            binding.pdfTextView.setText(pdfName);


        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }


}