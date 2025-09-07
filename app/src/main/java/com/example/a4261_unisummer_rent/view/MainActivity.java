package com.example.a4261_unisummer_rent.view;

import android.os.Bundle;

import java.time.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.annotation.DrawableRes;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.control.ListingPagerAdapter;
import com.example.a4261_unisummer_rent.model.Listing;
import com.example.a4261_unisummer_rent.model.User;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.media.Image;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        pager = findViewById(R.id.pager);
        pager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);  // key for swipe down/up

        // Simple page spacing (optional, looks nice)
        int pageMarginPx = getResources().getDimensionPixelSize(android.R.dimen.app_icon_size) / 4;
        pager.setPageTransformer((page, position) -> {
            page.setTranslationY(position * pageMarginPx);
            page.setAlpha(1 - Math.abs(position) * 0.15f);
        });

        ListingPagerAdapter adapter = new ListingPagerAdapter(seedData());
        pager.setAdapter(adapter);
    }

    private List<Listing> seedData() {
        User jane = new User(UUID.randomUUID(), "jane_doe", "Jane Doe");
        User alex = new User(UUID.randomUUID(), "alex_gt", "Alex G.");
        User kim  = new User(UUID.randomUUID(), "kim_atl", "Kim A.");


        List<Listing> list = new ArrayList<>();
        list.add(new Listing(
                UUID.randomUUID(),
                "Sunny Studio near Campus",
                1200,
                "very good1",
                0,
                jane.getId(),
                R.drawable.hub

        ));
        list.add(new Listing(
                UUID.randomUUID(),
                "1BR with Balcony + Parking",
                1500,
                "very good2",
                0,
                jane.getId(),
                R.drawable.hub
        ));
        list.add(new Listing(
                UUID.randomUUID(),
                "Shared 2BR — Huge Living Room",
                900,
                "very good3",
                0,
                jane.getId(),
                R.drawable.hue
        ));
        return list;
    }
}
