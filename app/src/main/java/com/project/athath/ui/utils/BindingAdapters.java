package com.project.athath.ui.utils;


import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.databinding.BindingAdapter;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter(value = {"app:showIfEmpty", "app:hideWhenEmptyQuery"})
    public static <T> void setPlaceHolder(View view, List<T> list, String query) {
        if (list == null || query.isEmpty()) view.setVisibility(View.INVISIBLE);
        else if (list.isEmpty()) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);
    }

    @BindingAdapter(value = {"app:hideWhenSearch", "app:hideWhenNotFound"})
    public static <T> void hideWhenSearch(View view, String query, List<T> list) {
        if (query == null || query.isEmpty() || list == null) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);
    }

    @BindingAdapter(value = {"app:showWhenEmptyList"})
    public static <T> void showWhenEmptyList(View view, List<T> list) {
        if (list == null || list.isEmpty()) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @BindingAdapter(value = {"app:hideWhenEmptyList"})
    public static void hideWhenEmptyList(View view, int listSize) {
        if (listSize == 0) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }

    // Binding Adapters for UI
    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    @BindingAdapter("isLoading")
    public static void showLoading(ProgressBar progressBar, Boolean isLoading) {
        if (isLoading != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("isButtonEnabled")
    public static void enableButton(Button button, Boolean isLoading) {
        if (isLoading != null) {
            button.setEnabled(!isLoading);
        }
    }

/*    @BindingAdapter("userResult")
    public static void bindUserResult(TextView textView, UserResult<Integer> userResult) {
        if (userResult instanceof UserResult.Success) {
            textView.setText(String.valueOf(((UserResult.Success<Integer>) userResult).getUser()));
        } else if (userResult instanceof UserResult.Error) {
            textView.setText("0");
        } else if (userResult instanceof UserResult.Loading) {
            textView.setText("تحميل...");
        }
    }*/

}