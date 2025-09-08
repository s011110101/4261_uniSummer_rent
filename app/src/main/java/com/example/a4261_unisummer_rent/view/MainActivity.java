package com.example.a4261_unisummer_rent.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.control.ListingPagerAdapter;
import com.example.a4261_unisummer_rent.model.Listing;
import com.example.a4261_unisummer_rent.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.media.Image;
import android.util.*;
import android.widget.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ListingPagerAdapter adapter;
    private final List<Listing> data = new ArrayList<>();
    private ListenerRegistration registration;
    private FloatingActionButton fabAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            }
            return false;
        });
        rv = findViewById(R.id.recyclerView);   // <-- ensure your activity_main has this id
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingPagerAdapter(data);
        rv.setAdapter(adapter);

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v -> onActionClicked());
        // Start Firestore realtime updates (or call loadOnce() if you prefer one-shot)
        subscribeToListings();
    }

    private void subscribeToListings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        registration = db.collection("listings")
                // .orderBy("createdAt", Query.Direction.DESCENDING) // if you add a timestamp later
                .orderBy("title", Query.Direction.ASCENDING) // deterministic order for now
                .addSnapshotListener(this, (snap, e) -> {
                    if (e != null) {
                        Log.e("MainActivity", "listen failed", e);
                        Toast.makeText(this, "Failed to load listings", Toast.LENGTH_SHORT).show();
                        // fallback to seed only if we have nothing to show yet
                        if (data.isEmpty()) {
                            data.addAll(seedData());
                            adapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (snap == null) return;

                    bindSnapshot(snap);
                });
    }

    private void bindSnapshot(QuerySnapshot snap) {
        data.clear();
        for (DocumentSnapshot d : snap.getDocuments()) {
            Listing l = mapDocToListing(d);
            if (l != null) data.add(l);
        }
        // Optional: if Firestore is empty, show local seed data
        if (data.isEmpty()) {
            data.addAll(seedData());
        }
        adapter.notifyDataSetChanged();
    }

    /** Map Firestore doc to your current Listing constructor:
     *  new Listing(id, title, price, description, posterId, image)
     */
    private @Nullable Listing mapDocToListing(DocumentSnapshot d) {
        try {
            String id = getStringSafe(d, "id", d.getId());
            String title = getStringSafe(d, "title", "");
            String description = getStringSafe(d, "description", "");
            String posterId = getStringSafe(d, "posterId", null);

            int price = getIntSafe(d, "price", 0);
            // WARNING: resource IDs aren't portable; seeded for demo only
            int image = getIntSafe(d, "image", R.drawable.hub); // use your own placeholder

            // If your Listing class has this exact constructor, this will compile.
            // If your parameter order differs, reorder accordingly here.
            return new Listing(id, title, price, description, posterId, image);

        } catch (Exception ex) {
            Log.w("MainActivity", "Skipping bad doc " + d.getId(), ex);
            return null;
        }
    }

    private static String getStringSafe(DocumentSnapshot d, String key, String def) {
        Object v = d.get(key);
        return (v instanceof String) ? (String) v : def;
    }

    private static int getIntSafe(DocumentSnapshot d, String key, int def) {
        Object v = d.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        if (v instanceof String) {
            try { return Integer.parseInt((String) v); } catch (Exception ignore) {}
        }
        return def;
    }

    /** Your seed data, shaped like your adapter expects (image=int, postedBy left null/unused). */
    private List<Listing> seedData() {
        List<Listing> list = new ArrayList<>();
        list.add(new Listing(
                UUID.randomUUID().toString(),
                "Sunny Studio near Campus",
                1200,
                "very good1",
                "00001",           // posterId for later
                R.drawable.hub     // local drawable
        ));
        return list;
    }
    private void onActionClicked() {
        startActivity(new Intent(this, CreatePostActivity.class));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) registration.remove();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}


//public class MainActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//    private ViewPager2 pager;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String listingDBId = db.collection("listings").document().getId();
//        Listing demo = new Listing(
//                "00001",
//                "Sunny Studio near Campus",
//                1200,
//                "Walk to GT, utilities included.",
//                "00001",
//                R.drawable.hub
//        );
//        db.collection("listings").document(listingDBId).set(demo);
//
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.fabAdd).setOnClickListener(v -> {
//            startActivity(new android.content.Intent(this, CreatePostActivity.class));
//        });
//        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
//        setSupportActionBar(topAppBar);
//        pager = findViewById(R.id.pager);
//        pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//
//        int pageMarginPx = getResources().getDimensionPixelSize(android.R.dimen.app_icon_size) / 4;
//        pager.setPageTransformer((page, position) -> {
//            page.setTranslationY(position * pageMarginPx);
//            page.setAlpha(1 - Math.abs(position) * 0.15f);
//        });
//
//        ListingPagerAdapter adapter = new ListingPagerAdapter(seedData());
//        pager.setAdapter(adapter);
//    }

//    private List<Listing> seedData() {
//        User jane = new User(UUID.randomUUID().toString(), "jane_doe", "Jane Doe");
//        User alex = new User(UUID.randomUUID().toString(), "alex_gt", "Alex G.");
//        User kim  = new User(UUID.randomUUID().toString(), "kim_atl", "Kim A.");
//
//
//        List<Listing> list = new ArrayList<>();
//        list.add(new Listing(
//                UUID.randomUUID().toString(),
//                "Sunny Studio near Campus",
//                1200,
//                "very good1",
//                "00001",
//                R.drawable.hub
//
//        ));
//        list.add(new Listing(
//                UUID.randomUUID().toString(),
//                "1BR with Balcony + Parking",
//                1500,
//                "very good2",
//                "00001",
//                R.drawable.hub
//        ));
//        list.add(new Listing(
//                UUID.randomUUID().toString(),
//                "Shared 2BR — Huge Living Room",
//                900,
//                "very good3",
//                "00001",
//                R.drawable.hue
//        ));
//        return list;
//    }
//    private void upsertUser(String uid) {
//        User user = new User(uid, "Anonymous Renter", null);
//        user.setCreatedAt(Timestamp.now());
//        user.setLastLoginAt(Timestamp.now());
//
//        db.collection("users").document(uid).set(user)
//                .addOnSuccessListener(unused -> Log.d(TAG, "User upserted"))
//                .addOnFailureListener(e -> Log.e(TAG, "User upsert failed", e));
//    }
//
//    private void createSampleListing(String ownerId) {
//        Listing l = new Listing(ownerId, "Sunny Studio Near Campus", 1200.0);
//        l.setDescription("Furnished studio, walk to GT. Utilities included.");
//        l.setCity("Atlanta");
//        l.setState("GA");
//        l.setCreatedAt(Timestamp.now());
//        l.setUpdatedAt(Timestamp.now());
//
//        db.collection("listings")
//                .add(l)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference docRef) {
//                        String id = docRef.getId();
//                        // optionally write back id field
//                        Map<String, Object> patch = new HashMap<>();
//                        patch.put("id", id);
//                        docRef.update(patch);
//                        Log.d(TAG, "Listing created with ID: " + id);
//                    }
//                })
//                .addOnFailureListener(e -> Log.e(TAG, "Create listing failed", e));
//    }
//
//    private void fetchActiveListings() {
//        db.collection("listings")
//                .whereEqualTo("active", true)              // if you named it isActive, use "isActive"
//                .orderBy("createdAt", Query.Direction.DESCENDING)
//                .limit(20)
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    Log.d(TAG, "Fetched " + querySnapshot.size() + " listings");
//                    for (var doc : querySnapshot.getDocuments()) {
//                        Log.d(TAG, doc.getId() + " => " + doc.getData());
//                    }
//                })
//                .addOnFailureListener(e -> Log.e(TAG, "Fetch listings failed", e));
//    }
//}
//}
