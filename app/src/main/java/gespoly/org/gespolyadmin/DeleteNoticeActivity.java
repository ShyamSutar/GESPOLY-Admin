package gespoly.org.gespolyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gespoly.org.gespolyadmin.Adapter.NoticeAdapter;
import gespoly.org.gespolyadmin.Model.NoticeModel;
import gespoly.org.gespolyadmin.databinding.ActivityDeleteNoticeBinding;

public class DeleteNoticeActivity extends AppCompatActivity {

    ActivityDeleteNoticeBinding binding;
    private ArrayList<NoticeModel> list;
    private NoticeAdapter adapter;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        binding.deleteNoticeRV.setLayoutManager(new LinearLayoutManager(this));
        binding.deleteNoticeRV.setHasFixedSize(true);

        getNotice();

    }

    private void getNotice() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    NoticeModel data = snapshot1.getValue(NoticeModel.class);
                    list.add(data);
                }

                adapter = new NoticeAdapter(DeleteNoticeActivity.this, list);
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.deleteNoticeRV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);

                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}