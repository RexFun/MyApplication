package com.rex.paperdiy.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;

public class ImageFullscreenPagerActivityFragment extends Fragment {
    @InjectUtil.InjectView(id=R.id.img)
    PhotoView mImageView;

    private OnClickListener mListener;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public ImageFullscreenPagerActivityFragment() {
        // Required empty public constructor
    }

    public static ImageFullscreenPagerActivityFragment newInstance(String param1) {
        ImageFullscreenPagerActivityFragment fragment = new ImageFullscreenPagerActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_image_fullscreen_paper_fragment, container, false);
        InjectUtil.injectView(this, rootView);
        mImageView.enable();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v);
            }
        });
        MainActivity.mImageLoader.displayImage(
                getString(R.string.app_path) + "/client/paperimage/getPaperImageById.action?id=" + mParam1,
                mImageView,
                MainActivity.mDisplayImageOptions);
        return rootView;
    }

    public interface OnClickListener {
        public void onClick(View v);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
