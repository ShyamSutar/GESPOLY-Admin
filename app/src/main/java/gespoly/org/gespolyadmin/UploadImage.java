package gespoly.org.gespolyadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.net.URI;

import gespoly.org.gespolyadmin.databinding.ActivityUploadImageBinding;

public class UploadImage extends AppCompatActivity {

    ActivityUploadImageBinding binding;
    private String category;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] items = new String[]{"Select Category", "Computer Department","Mechanical Department","Electrical Department", "Civil Department"};
        binding.imageCatergory1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));


        binding.imageCatergory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = binding.imageCatergory1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

            binding.galleryImageView.setImageURI(imageUri);

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