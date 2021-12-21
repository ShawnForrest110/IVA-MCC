//package com.example.nasaproject;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class TelemetryAdapter extends RecyclerView.Adapter<TelemetryAdapter.TelemetryViewHolder> {
//    private ArrayList<SingleAstroFragment> mTelemetryList;
//
//    public static class TelemetryViewHolder extends RecyclerView.ViewHolder{
//        public TextView mTextView1;
//        public TextView mTextView2;
//
//        public TelemetryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mTextView1 = itemView.findViewById(R.id.telemetryRowTitle);
//            mTextView2 = itemView.findViewById(R.id.telemetryRowData);
//        }
//    }
//
//    public TelemetryAdapter(ArrayList<SingleAstroFragment> telemetryList){
//        mTelemetryList = telemetryList;
//    }
//    @NonNull
//    @Override
//    public TelemetryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.telemetry_row, parent,false);
//        TelemetryViewHolder tViewHolder = new TelemetryViewHolder(v);
//        return tViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TelemetryViewHolder holder, int position) {
//        SingleAstroFragment currentItem = mTelemetryList.get(position);
//        holder.mTextView1.setText(currentItem.getText1());
//        holder.mTextView1.setText(currentItem.getText2());
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
