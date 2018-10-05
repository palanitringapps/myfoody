package com.myfoody.base;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected void showProgressDialog() {
        if (getBaseActivity() != null && !getBaseActivity().isFinishing()) {
            getBaseActivity().showProgressDialog();
        }
    }

    protected void hideProgressDialog() {
        if (getBaseActivity() != null && !getBaseActivity().isFinishing()) {
            getBaseActivity().hideProgressDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public BaseActivity getBaseActivity() {
        return ((BaseActivity) getActivity());
    }

    public void replaceFragment(Fragment toFragment, boolean isAddToBackStack, String tagName) {
        getBaseActivity().replaceFragment(toFragment, isAddToBackStack, tagName);
    }

    public void addFragment(Fragment toFragment, boolean showAnimation, String tagName) {
        getBaseActivity().addFragment(toFragment, showAnimation, tagName);
    }

    public void showShortToast(String msg) {
        getBaseActivity().showShortToast(msg);
    }

    public void showDialog(String title, String content) {
        getBaseActivity().showDialog(title, content);
    }

    public void showDialog(View.OnClickListener pListener, View.OnClickListener nListener, String... content) {
        getBaseActivity().showDialog(pListener, nListener, content);
    }

    public void showInputDialog(int inputType, DialogListener dialogListener, String... content) {
        getBaseActivity().showInputDialog(inputType, dialogListener, content);
    }

    public void showSuccessDialog(String content) {
        getBaseActivity().showSuccessDialog(content, null);
    }

    public void showSuccessDialog(String content, View.OnClickListener onClickListener) {
        getBaseActivity().showSuccessDialog(content, onClickListener);
    }
}
