package com.project.athath.ui.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.project.athath.R;

public class DialogUtils {
    private static Dialog loadingDialog;

    // ✅ Show Loading Dialog
    public static void showLoadingDialog(Context context, String message) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return; // Prevent duplicate dialogs
        }

        // Inflate custom layout
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView messageTextView = view.findViewById(R.id.loadingMessage);
        messageTextView.setText(message);

        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    // ✅ Hide Loading Dialog
    public static void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public static void showConfirmationDialog(Context context, String title, String message,
                                              String positiveButtonText, String negativeButtonText,
                                              DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveClickListener);
        builder.setNegativeButton(negativeButtonText, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
    public static void showConfirmationDialog(Context context, String title, String message,
                                              String positiveButtonText, String negativeButtonText,
                                              DialogInterface.OnClickListener positiveClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveClickListener);

        // Use the passed negativeClickListener, ensuring it isn't null
        if (negativeClickListener != null) {
            builder.setNegativeButton(negativeButtonText, negativeClickListener);
        } else {
            builder.setNegativeButton(negativeButtonText, (dialog, which) -> dialog.dismiss());
        }

        builder.create().show();
    }
    //success dialog
    public static void showCustomDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
    public static void showCustomDialog(Context context, String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    if (onConfirm != null) onConfirm.run();  // Executes callback after dialog dismissal
                })
                .setCancelable(false)
                .show();
    }
}