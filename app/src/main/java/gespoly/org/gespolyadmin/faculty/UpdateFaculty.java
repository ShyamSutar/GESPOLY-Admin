package gespoly.org.gespolyadmin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gespoly.org.gespolyadmin.Adapter.TeacherAdapter;
import gespoly.org.gespolyadmin.Model.TeacherDataModel;
import gespoly.org.gespolyadmin.databinding.ActivityUpdateFacultyBinding;

public class UpdateFaculty extends AppCompatActivity {

    ActivityUpdateFacultyBinding binding;

    private List<TeacherDataModel> list1, list2, list3, list4;
    private DatabaseReference reference, db;
//    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("teachers");

        //creating diff method for ease
        csDapartment();
        meDapartment();
        elDapartment();
        ceDapartment();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));

            }
        });

    }

    private void csDapartment() {
        db = reference.child("Computer Department");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    binding.csNoData.setVisibility(View.VISIBLE);
                    binding.csDepartment.setVisibility(View.GONE);
                    Toast.makeText(UpdateFaculty.this, "No image", Toast.LENGTH_SHORT).show();
                }else{

                    binding.csNoData.setVisibility(View.GONE);
                    binding.csDepartment.setVisibility(View.VISIBLE);



                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherDataModel data = dataSnapshot.getValue(TeacherDataModel.class);
//                        data.setKey(dataSnapshot.getKey());
                        list1.add(data);
                    }

                    binding.csDepartment.setHasFixedSize(true);
                    binding.csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    TeacherAdapter adapter = new TeacherAdapter(list1,UpdateFaculty.this, "Computer Department");
                    binding.csDepartment.setAdapter(adapter);
//                  adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void meDapartment() {
        db = reference.child("Mechanical Department");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();

                if (!snapshot.exists()){
                    binding.meNoData.setVisibility(View.VISIBLE);
                    binding.meDepartment.setVisibility(View.GONE);
                }else {

                    binding.meNoData.setVisibility(View.GONE);
                    binding.meDepartment.setVisibility(View.VISIBLE);

                    binding.meDepartment.setHasFixedSize(true);
                    binding.meDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));

                    TeacherAdapter adapter = new TeacherAdapter(list2, UpdateFaculty.this, "Mechanical Department");
                    binding.meDepartment.setAdapter(adapter);

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        TeacherDataModel data = snap.getValue(TeacherDataModel.class);
                        list2.add(data);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void elDapartment() {
        db = reference.child("Electrical Department");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    binding.elNoData.setVisibility(View.VISIBLE);
                    binding.elDepartment.setVisibility(View.GONE);
                }else{

                    binding.elNoData.setVisibility(View.GONE);
                    binding.elDepartment.setVisibility(View.VISIBLE);

                    binding.elDepartment.setHasFixedSize(true);
                    binding.elDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));


                    TeacherAdapter dapter = new TeacherAdapter(list3, UpdateFaculty.this, "Electrical Department");
                    binding.elDepartment.setAdapter(dapter);

                    for (DataSnapshot snap: snapshot.getChildren()){
                        TeacherDataModel data = snap.getValue(TeacherDataModel.class);
                        list3.add(data);
                    }

                   dapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ceDapartment() {
        db = reference.child("Civil Department");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if (!snapshot.exists()){
                    binding.ceNoData.setVisibility(View.VISIBLE);
                    binding.ceDepartment.setVisibility(View.GONE);
                }else{

                    binding.ceNoData.setVisibility(View.GONE);
                    binding.ceDepartment.setVisibility(View.VISIBLE);

                    binding.ceDepartment.setHasFixedSize(true);
                    binding.ceDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));

                    TeacherAdapter adapter = new TeacherAdapter(list4, UpdateFaculty.this, "Civil Department");
                    binding.ceDepartment.setAdapter(adapter);

                    for (DataSnapshot snap: snapshot.getChildren()){
                        TeacherDataModel data = snap.getValue(TeacherDataModel.class);
                        list4.add(data);
                    }

                    adapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
