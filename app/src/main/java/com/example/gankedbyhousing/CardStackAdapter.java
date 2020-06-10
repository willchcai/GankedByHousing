package com.example.gankedbyhousing;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<Listing> items;

    public CardStackAdapter(List<Listing> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, price, location, email, phoneNumber;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_age);
            location = itemView.findViewById(R.id.item_city);
            email = itemView.findViewById(R.id.item_email);
            phoneNumber = itemView.findViewById(R.id.item_number);
        }

        void setData(Listing data) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userID = mAuth.getCurrentUser().getUid();

            StorageReference profRef = mStorageRef.child("image"+data.getListingID());
            Log.d("CardStackAdapter", "value of userID" + data.getListingID());

            profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("CardStackAdapter", "onSuccessListener triggered");

                    Picasso.get().load(uri).fit().centerCrop().into(image);
                }
            });
/*            Picasso.get()
                    .load(data.getImage())
                    .fit()
                    .centerCrop()
                    .into(image);*/
            title.setText(data.getTitle());
            price.setText("Price: "+data.getPrice());
            location.setText(data.getLocation());
            email.setText(data.getEmail());
            phoneNumber.setText(data.getPhoneNumber());
        }
    }

    public List<Listing> getItems() {
        return items;
    }

    public void setItems(List<Listing> items) {
        this.items = items;
    }
}
