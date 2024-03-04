package com.pedro.streamer.utils;

import android.app.Dialog;
import android.view.ViewGroup;
import android.view.Window;

import com.pedro.streamer.R;


public class AppProgressDialog {

    public static void show(Dialog mProgressDialog) {
        try {
            if (mProgressDialog.isShowing())
                return;
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setContentView(R.layout.layout_progress_bar);
            // ((TextView) mProgressDialog.findViewById(R.id.title)).setText(msg);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mProgressDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hide(Dialog mProgressDialog) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            //stopAnim(mProgressDialog);
            mProgressDialog.dismiss();
        }
    }

  /*  private static void startAnim(Dialog mProgressDialog) {
        ((AVLoadingIndicatorView) mProgressDialog.findViewById(R.id.avi)).smoothToShow();//show();
        // or avi.smoothToShow();
    }

    private static void stopAnim(Dialog mProgressDialog) {
        ((AVLoadingIndicatorView) mProgressDialog.findViewById(R.id.avi)).smoothToHide();
        // or avi.smoothToHide();
    }*/


}