package gespoly.org.gespolyadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gespoly.org.gespolyadmin.Model.TeacherDataModel;
import gespoly.org.gespolyadmin.R;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter> {

    private ArrayList<TeacherDataModel> list;
    private Context context;

    public ArrayList<TeacherDataModel> getList() {
        return list;
    }

    public void setList(ArrayList<TeacherDataModel> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TeacherAdapter(ArrayList<TeacherDataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAdapter holder, int position) {
        TeacherDataModel item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());

        try {
            Picasso.get().load(item.getImage()).placeholder(R.drawable.profile).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Update Teacher", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, post;
        private Button update;
        private ImageView imageView;

        public TeacherViewAdapter(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.teachername);
            email = itemView.findViewById(R.id.teacheremail);
            post = itemView.findViewById(R.id.teacherpost);
            update = itemView.findViewById(R.id.teacherUpdateBtn);
            imageView = itemView.findViewById(R.id.teacherImage);



        }
    }

}
