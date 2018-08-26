package org.baghdasaryan.barcodereader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 101;
    private static final String TAG = "MainActivity";

    private BarcodeAdapter barcodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        Button selectImageButton = findViewById(R.id.select_image_btn);
        selectImageButton.setOnClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.barcode_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        barcodeAdapter = new BarcodeAdapter();
        recyclerView.setAdapter(barcodeAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        removeBarcodes();
        if (PICK_IMAGE == requestCode) {
            handleImageChooserResult(resultCode, data);
        }
    }

    private void handleImageChooserResult(int resultCode, Intent data) {
        if (RESULT_OK == resultCode && null != data) {
            tryReadBarcode(data.getData());
        }
    }

    private void tryReadBarcode(Uri uri) {
        try {
            readBarcode(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readBarcode(Uri uri) throws IOException {
        FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this, uri);
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();
        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        showBarcodes(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void removeBarcodes() {
        barcodeAdapter.updateBarcodes(new ArrayList<FirebaseVisionBarcode>());
    }

    private void showBarcodes(List<FirebaseVisionBarcode> barcodes) {
        barcodeAdapter.updateBarcodes(barcodes);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.select_image_btn) {
            openImageChooser();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
}
