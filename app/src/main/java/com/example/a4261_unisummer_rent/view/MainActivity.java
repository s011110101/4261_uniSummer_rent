package com.example.a4261_unisummer_rent.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import android.media.Image;
import android.util.*;
import android.widget.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ViewPager2 pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
        String listingDBId = db.collection("listings").document().getId();
        Listing demo = new Listing(
                "00001",
                "Sunny Studio near Campus",
                1200,
                "Walk to GT, utilities included.",
                "00001",
                R.drawable.hub
        );
        db.collection("listings").document(listingDBId).set(demo);

        setContentView(R.layout.activity_main);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        pager = findViewById(R.id.pager);
        pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        int pageMarginPx = getResources().getDimensionPixelSize(android.R.dimen.app_icon_size) / 4;
        pager.setPageTransformer((page, position) -> {
            page.setTranslationY(position * pageMarginPx);
            page.setAlpha(1 - Math.abs(position) * 0.15f);
        });

        ListingPagerAdapter adapter = new ListingPagerAdapter(seedData());
        pager.setAdapter(adapter);
    }

    private List<Listing> seedData() {
        User jane = new User(UUID.randomUUID().toString(), "jane_doe", "Jane Doe");
        User alex = new User(UUID.randomUUID().toString(), "alex_gt", "Alex G.");
        User kim  = new User(UUID.randomUUID().toString(), "kim_atl", "Kim A.");


        List<Listing> list = new ArrayList<>();
        list.add(new Listing(
                UUID.randomUUID().toString(),
                "Sunny Studio near Campus",
                1200,
                "very good1",
                "00001",
                R.drawable.hub

        ));
        list.add(new Listing(
                UUID.randomUUID().toString(),
                "1BR with Balcony + Parking",
                1500,
                "very good2",
                "00001",
                R.drawable.hub
        ));
        list.add(new Listing(
                UUID.randomUUID().toString(),
                "Shared 2BR — Huge Living Room",
                900,
                "very good3",
                "00001",
                R.drawable.hue
        ));
        return list;
    }
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
}
