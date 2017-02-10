package com.shayke.galleryfrgament;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookFrag extends Fragment {


    public FacebookFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.logMethodCalled();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_facebook, container, false);
    }

}
