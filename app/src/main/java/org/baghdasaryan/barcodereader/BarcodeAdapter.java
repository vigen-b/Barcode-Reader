package org.baghdasaryan.barcodereader;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.util.ArrayList;
import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder>  {

    private List<FirebaseVisionBarcode> barcodes;

    BarcodeAdapter() {
        this(new ArrayList<FirebaseVisionBarcode>());
    }

    BarcodeAdapter(List<FirebaseVisionBarcode> barcodes) {
        this.barcodes = barcodes;
    }

    void updateBarcodes(List<FirebaseVisionBarcode> barcodes) {
        this.barcodes = barcodes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_barcode, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FirebaseVisionBarcode barcode = barcodes.get(i);
        viewHolder.mTextView.setText(barcode.getDisplayValue());
    }

    @Override
    public int getItemCount() {
        return barcodes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.barcode_view);
        }
    }
}
