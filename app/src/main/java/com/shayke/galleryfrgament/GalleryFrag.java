package com.shayke.galleryfrgament;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.service.carrier.CarrierMessagingService;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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

    private Uri mImageUri;
    private Animator mCurrentAnimator;
    private int mLongAnimationDuration;


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

        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Toast.makeText(getApplicationContext(), "ImageView long pressed!", Toast.LENGTH_SHORT).show();
               // zoomImageFromThumb();
                pinchZoomPan();
                return true;
            }
        });



        Intent intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_OPEN_CODE);

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == REQUEST_OPEN_CODE && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                mImageUri = resultData.getData();
                /*
                Bitmap bitmap = null;
                try {
                    bitmap = getBitmapFromUri(uri);
                    mImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
               */

                Glide.with(this)
                        .load(mImageUri)
                        .into(mImageView);



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

    // a method that is creating the animation
    private void zoomImageFromThumb(){
        //here we check if there's animation running and if yes than cancel it
    if(mCurrentAnimator !=null){
        mCurrentAnimator.cancel();
    }
// here we get the full size imageView using glide
    Glide.with(this)
            .load(mImageUri)
            .into(mPinchZoomImageView);

        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globaloffSet = new Point();
        mImageView.getGlobalVisibleRect(startBounds);
        getActivity().findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globaloffSet);
        startBounds.offset(-globaloffSet.x, -globaloffSet.y);
        finalBounds.offset(-globaloffSet.x, -globaloffSet.y);

        //Aspect Ratio

        float startScale;
        if((float) finalBounds.width() /finalBounds.height() >
                (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = (float) startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = (float) startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;

        }

        // Visibillity of full size Image

        mImageView.setAlpha(0f);
        mPinchZoomImageView.setVisibility(View.VISIBLE);


        // Set Pivot

        mPinchZoomImageView.setPivotX(0f);
        mPinchZoomImageView.setPivotY(0f);

        // Animation Curves

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mPinchZoomImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mPinchZoomImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mPinchZoomImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mPinchZoomImageView, View.SCALE_Y, startScale, 1f));

        set.setDuration(mLongAnimationDuration); //Duration For the Animation
        set.setInterpolator(new DecelerateInterpolator());// Set Accelerator

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }
    private void pinchZoomPan() {
        mPinchZoomImageView.setImageUri(mImageUri);
        mImageView.setAlpha(0.f);
        mPinchZoomImageView.setVisibility(View.VISIBLE);
    }

}


