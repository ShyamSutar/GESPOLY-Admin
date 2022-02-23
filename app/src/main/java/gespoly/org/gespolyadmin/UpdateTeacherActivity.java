package gespoly.org.gespolyadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.squareup.picasso.Picasso;

import gespoly.org.gespolyadmin.databinding.ActivityUpdateTeacherBinding;

public class UpdateTeacherActivity extends AppCompatActivity {

    ActivityUpdateTeacherBinding binding;
    private String name, email, post, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");


        try {
            Picasso.get().load(image).into(binding.updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.updateTeacherEmail.setText(email);
        binding.updateTeacherName1.setText(name);
        binding.updateTeacherPost.setText(post);

    }
}