package com.forbitbd.myfin.zoom_image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.forbitbd.androidutils.customView.ZoomableImageView;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;



public class ZoomImageScene extends Fragment {


    private ZoomableImageView zoomableImageView;

    private Communicator communicator;
    private ProgressBar progressBar;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            progressBar.setVisibility(View.GONE);
            zoomableImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    public ZoomImageScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = (Communicator) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoom_image_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        zoomableImageView = view.findViewById(R.id.zoomImage);

        progressBar = view.findViewById(R.id.recycler_view);
        progressBar.setVisibility(View.VISIBLE);

        Picasso.get().load(communicator.getZoomImagePath()).into(target);
    }

    @Override
    public void onDestroy() {
        Picasso.get().cancelRequest(target);
        super.onDestroy();
    }
}