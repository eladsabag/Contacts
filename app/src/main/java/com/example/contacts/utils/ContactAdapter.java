package com.example.contacts.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.activities.UpdateActivity;
import com.example.contacts.objects.Contact;
import com.example.contacts.objects.Result;
import com.example.contacts.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList<Contact> allContacts;
    private String currentContactGender;

    private Animation translate_anim;

    public ContactAdapter(Activity activity, Context context, ArrayList<Contact> allContacts) {
        this.activity = activity;
        this.context = context;
        this.allContacts = allContacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_contact,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(allContacts.get(position).getFirstName().length() == 0) {
            holder.contact_details.setText("      " + String.valueOf(allContacts.get(position).getMobileNumber()));
            holder.contact_details.setIconResource(R.drawable.ic_unknown);
        } else {
            holder.contact_details.setText("      " + allContacts.get(position).getFirstName() + " " + allContacts.get(position).getLastName());
            int drawable = getDrawableResource(allContacts.get(position).getFirstName());
            holder.contact_details.setIconResource(drawable);
        }

        holder.contact_details.setOnClickListener(e -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("contact_id",String.valueOf(allContacts.get(position).get_id()));
            intent.putExtra("firstName",String.valueOf(allContacts.get(position).getFirstName()));
            intent.putExtra("lastName",String.valueOf(allContacts.get(position).getLastName()));
            intent.putExtra("mobileNumber",String.valueOf(allContacts.get(position).getMobileNumber()));
            activity.startActivityForResult(intent,1);
        });
    }

    /**
     * This function sets the contact icon resource by the first character of the first name of the contact.
     * @param s - The first name of the contact.
     * @return - The chosen drawable icon resource.
     */
    private int getDrawableResource(String s) {
        int drawable = 0;
        switch (s.toLowerCase().charAt(0)) {
            case 'a':
                drawable = R.drawable.ic_a;
                break;
            case 'b':
                drawable = R.drawable.ic_b;
                break;
            case 'c':
                drawable = R.drawable.ic_c;
                break;
            case 'd':
                drawable = R.drawable.ic_d;
                break;
            case 'e':
                drawable = R.drawable.ic_e;
                break;
            case 'f':
                drawable = R.drawable.ic_f;
                break;
            case 'g':
                drawable = R.drawable.ic_g;
                break;
            case 'h':
                drawable = R.drawable.ic_h;
                break;
            case 'i':
                drawable = R.drawable.ic_i;
                break;
            case 'j':
                drawable = R.drawable.ic_j;
                break;
            case 'k':
                drawable = R.drawable.ic_k;
                break;
            case 'l':
                drawable = R.drawable.ic_l;
                break;
            case 'm':
                drawable = R.drawable.ic_m;
                break;
            case 'n':
                drawable = R.drawable.ic_n;
                break;
            case 'o':
                drawable = R.drawable.ic_o;
                break;
            case 'p':
                drawable = R.drawable.ic_p;
                break;
            case 'q':
                drawable = R.drawable.ic_q;
                break;
            case 'r':
                drawable = R.drawable.ic_r;
                break;
            case 's':
                drawable = R.drawable.ic_s;
                break;
            case 't':
                drawable = R.drawable.ic_t;
                break;
            case 'u':
                drawable = R.drawable.ic_u;
                break;
            case 'w':
                drawable = R.drawable.ic_w;
                break;
            case 'v':
                drawable = R.drawable.ic_v;
                break;
            case 'x':
                drawable = R.drawable.ic_x;
                break;
            case 'y':
                drawable = R.drawable.ic_y;
                break;
            case 'z':
                drawable = R.drawable.ic_z;
                break;
            default:
                break;
        }
        return drawable;
    }

    @Override
    public int getItemCount() {
        return allContacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MaterialButton contact_details;
        private LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_details = itemView.findViewById(R.id.contact_details);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            // Animate Recyclerview
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
