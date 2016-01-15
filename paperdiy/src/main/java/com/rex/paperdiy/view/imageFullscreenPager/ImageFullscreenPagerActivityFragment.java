package com.rex.paperdiy.view.imageFullscreenPager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rex.paperdiy.R;
import com.rex.paperdiy.view.main.MainActivity;
import com.rexfun.androidlibrarytool.InjectUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullscreenPagerActivityFragment extends Fragment {
    @InjectUtil.InjectView(id = R.id.iv_paper_image_bmp) ImageView mTvPaperImageBmp;

    private PhotoViewAttacher mAttacher;
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
        MainActivity.mImageLoader.displayImage(
                getString(R.string.app_path) + "/client/paperimage/getPaperImageById.action?id=" + mParam1,
                mTvPaperImageBmp,
                MainActivity.mDisplayImageOptions);
        mAttacher = new PhotoViewAttacher(mTvPaperImageBmp);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View v, float x, float y) {
                mListener.onClick(v);
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }
}
