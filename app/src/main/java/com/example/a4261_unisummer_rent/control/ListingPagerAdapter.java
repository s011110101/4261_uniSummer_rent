package com.example.a4261_unisummer_rent.control;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.model.Listing;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class ListingPagerAdapter extends RecyclerView.Adapter<ListingPagerAdapter.ListingViewHolder> {

    private final List<Listing> data;
    private final Map<String, String> usernameCache = new HashMap<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListingPagerAdapter(List<Listing> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder h, int position) {
        Listing item = data.get(position);

        h.title.setText(item.getTitle());
        h.price.setText("$"+item.getPrice()+"/month");
        db.collection("listings").limit(1).get()
                .addOnSuccessListener(qs -> {
                    for (DocumentSnapshot d : qs) {
                        Log.d("DBG", "doc=" + d.getId() + " data=" + d.getData());
                    }
                });
        h.description.setText(item.getDescription());
        String uid = item.getPostedBy();
        h.boundPosterId = uid;
        if (uid == null || uid.isEmpty()) {
            h.postedBy.setText("Posted by: unknown"+uid);
            return;
        }
        String cached = usernameCache.get(uid);
        if (cached != null) {
            h.postedBy.setText("Posted by: @" + cached);
            return;
        }
        db.collection("users").document(uid).get()
                .addOnSuccessListener((DocumentSnapshot doc) -> {
                    String uname = doc != null ? doc.getString("username") : null;
                    if (uname == null || uname.trim().isEmpty()) uname = "unknown";
                    usernameCache.put(uid, uname);
                    if (uid.equals(h.boundPosterId)) {
                        h.postedBy.setText("Posted by: @3" + uname);
                    }
                })
                .addOnFailureListener(e -> {
                    usernameCache.put(uid, "unknown");
                    if (uid.equals(h.boundPosterId)) {
                        h.postedBy.setText("Posted by: unknown2");
                    }
                });
        h.image.setImageResource(R.drawable.hub);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price, postedBy, description;
        String boundPosterId;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            postedBy = itemView.findViewById(R.id.postedBy);
            description = itemView.findViewById(R.id.description);
        }
    }


}
