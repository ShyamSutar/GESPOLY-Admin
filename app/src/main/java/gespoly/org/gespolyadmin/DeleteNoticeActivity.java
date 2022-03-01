package gespoly.org.gespolyadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import gespoly.org.gespolyadmin.databinding.ActivityDeleteNoticeBinding;

public class DeleteNoticeActivity extends AppCompatActivity {

    ActivityDeleteNoticeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}