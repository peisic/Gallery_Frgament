package com.shayke.galleryfrgament;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.service.carrier.CarrierMessagingService;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.FileDescriptor;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFrag extends Fragment {

    ImageView mImageView;
    //2.0
    PinchZoomImageView mPinchZoomImageView;

    private static final int REQUEST_OPEN_CODE = 0;



    public GalleryFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.frag_gallery, container, false);

        //2.1
        mPinchZoomImageView = (PinchZoomImageView) v.findViewById(R.id.pinchZoomImageView);

        mImageView = (ImageView) v.findViewById(R.id.imageView);

        //note for ver4 to check the test



        Intent intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_OPEN_CODE);

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == REQUEST_OPEN_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                /*
                Bitmap bitmap = null;
                try {
                    bitmap = getBitmapFromUri(uri);
                    mImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
               */
                /*
                Glide.with(this)
                        .load(uri)
                        .into(mImageView);
                        */

                //2.3
                Glide.with(this)
                        .load(uri)
                        .into(mPinchZoomImageView);
            }
        }
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;

    }

}


