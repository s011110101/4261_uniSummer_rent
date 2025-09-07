package com.example.a4261_unisummer_rent.control;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.model.Listing;

import java.util.List;

public class ListingPagerAdapter extends RecyclerView.Adapter<ListingPagerAdapter.ListingViewHolder> {

    private final List<Listing> data;

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
        String posted = item.getPostedBy() != null
                ? "Posted by: @" + "aaa"//item.getPostedBy().getUsername() hold for later database
                : "Posted by: unknown";
        h.postedBy.setText(posted);
        h.description.setText(item.getDescription());

        h.image.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price, postedBy, description;

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
