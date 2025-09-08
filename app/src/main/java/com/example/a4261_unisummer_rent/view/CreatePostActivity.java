package com.example.a4261_unisummer_rent.view;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.model.Listing;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etDescription, etPrice;
    private ImageView ivPreview;
    private MaterialButton btnPickImage, btnPost;

    private Uri pickedImageUri = null;
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    pickedImageUri = uri;
                    ivPreview.setImageURI(uri);
                }
            });

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        ivPreview = findViewById(R.id.ivPreview);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnPost = findViewById(R.id.btnPost);

        btnPickImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        btnPost.setOnClickListener(v -> saveListing());
    }

    private void saveListing() {
        String title = textOf(etTitle);
        String description = textOf(etDescription);
        String priceStr = textOf(etPrice);

        if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            toast("Please fill in title, description, and price.");
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            toast("Price must be a number.");
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid == null) {
            toast("You must be logged in.");
            return;
        }

        // Pre-create a doc id so we can use it for Storage path
        DocumentReference docRef = db.collection("listings").document();
        String listingId = docRef.getId();

        btnPost.setEnabled(false);

        if (pickedImageUri != null) {
            uploadImageThenSaveDoc(uid, listingId, title, description, price, docRef);
        } else {
            // No image chosen; save with empty imageUrl
            Listing listing = new Listing(listingId, title, price, description,
                    uid, 0);
            docRef.set(listing)
                    .addOnSuccessListener(v -> {
                        toast("Listing posted!");
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        btnPost.setEnabled(true);
                        toast("Failed to post: " + e.getMessage());
                    });
        }
    }

    private void uploadImageThenSaveDoc(String uid, String listingId,
                                        String title, String description, int price,
                                        DocumentReference docRef) {
        // Storage path: listing_images/{uid}/{listingId}-{random}.jpg
        String fileName = listingId + "-" + UUID.randomUUID() + ".jpg";
        StorageReference ref = storage.getReference()
                .child("listing_images")
                .child(uid)
                .child(fileName);

        ref.putFile(pickedImageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) throw task.getException();
                    return ref.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Listing listing = new Listing(listingId, title, price, description,
                            uid, R.drawable.hub);
                    docRef.set(listing)
                            .addOnSuccessListener(v -> {
                                toast("Listing posted!");
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                btnPost.setEnabled(true);
                                toast("Failed to save listing: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    btnPost.setEnabled(true);
                    toast("Image upload failed: " + e.getMessage());
                });
    }

    @NonNull
    private String textOf(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
