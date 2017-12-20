package com.wmt_demo.utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtility {

    // Create progress dialog
    public static ProgressDialog getProgressDialog(Context context, String message, Boolean isCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    // Dismiss progress dialog
    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    // Error dialog
    public static void showErrorDialog(Context context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle(message);
        dialog.setCancelable(true);
        dialog.show();
    }
}
