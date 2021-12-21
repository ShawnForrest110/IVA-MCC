package com.example.nasaproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;

public class RoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RoverFragment() {
        // Required empty public constructor
    }

    public static RoverFragment newInstance(String param1, String param2) {
        RoverFragment fragment = new RoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rover, container, false);

        PDFView pdfView = rootView.findViewById(R.id.pdfView);

        pdfView.fromAsset("roverManual.pdf")
//            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
//            .enableSwipe(true) // allows to block changing pages using swipe
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .defaultPage(0)
//            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//            .password(null)
//            .scrollHandle(null)
//            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//            // spacing between pages in dp. To define spacing color, set view background
//            .spacing(0)
//            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//            .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//            .pageSnap(false) // snap pages to screen boundaries
//            .pageFling(false) // make a fling change only a single page like ViewPager
//            .nightMode(false) // toggle night mode
            .load();
        return rootView;
    }




}
