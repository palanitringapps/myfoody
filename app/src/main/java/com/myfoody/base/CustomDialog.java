package com.myfoody.base;


import android.os.Bundle;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myfoody.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * Created by tringapps-admin on 3/3/17.
 */

public class CustomDialog extends DialogFragment {

    private String title;
    private String message;
    private String defaultValue;
    private String positiveButtonName, negativeButtonName;
    private View.OnClickListener okClickListener;
    private View.OnClickListener negativeButtonListener;
    private DialogListener mListener;
    private int inputType;

    public CustomDialog() {
        // Empty constructor required for DialogFragment
    }

    public void setContent(String title) {
        this.title = title;
    }

    public void setContent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public void setContent(int inputType,
                           DialogListener dialogListener, String... content) {
        this.title = content[0];
        this.message = content[1];
        this.mListener = dialogListener;
        this.defaultValue = content[2];
        this.inputType = inputType;
    }

    public void setContent(String title, String message, View.OnClickListener okClickListener) {
        this.title = title;
        this.message = message;
        this.okClickListener = okClickListener;
    }

    public void setContent(View.OnClickListener pListener,
                           View.OnClickListener nListener, String... content) {
        this.title = content[0];
        this.message = content[1];
        this.positiveButtonName = content[2];
        this.negativeButtonName = content[3];
        this.okClickListener = pListener;
        this.negativeButtonListener = nListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        if (!TextUtils.isEmpty(title)) {
            TextView titleTv = (TextView) v.findViewById(R.id.dialog_title);
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView messageTv = (TextView) v.findViewById(R.id.dialog_message);
            messageTv.setText(message);
        }
        final EditText inputMessage = (EditText) v.findViewById(R.id.input_dialog);
        inputMessage.setVisibility(mListener != null ? View.VISIBLE : View.GONE);
        inputMessage.setText(defaultValue);
        inputMessage.setInputType(inputType);

        if (!TextUtils.isEmpty(positiveButtonName)) {
            ((Button) v.findViewById(R.id.dialog_ok_button)).setText(positiveButtonName);
        }

        if (!TextUtils.isEmpty(negativeButtonName)) {
            v.findViewById(R.id.dialog_cancel_button).setVisibility(View.VISIBLE);
            v.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            ((Button) v.findViewById(R.id.dialog_cancel_button)).setText(negativeButtonName);
        } else {
            v.findViewById(R.id.dialog_cancel_button).setVisibility(View.GONE);
            v.findViewById(R.id.divider).setVisibility(View.GONE);
        }


        inputMessage.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if (okClickListener == null) {
            v.findViewById(R.id.dialog_ok_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onSubmit(inputMessage.getText().toString());
                    }
                    dismiss();
                }
            });
        } else {
            v.findViewById(R.id.dialog_ok_button).setOnClickListener(okClickListener);
        }

        if (negativeButtonListener == null) {
            v.findViewById(R.id.dialog_cancel_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else{
            v.findViewById(R.id.dialog_cancel_button).setOnClickListener(negativeButtonListener);
        }

        return v;
    }

    public void showDialog(FragmentManager fragmentManager) {
        show(fragmentManager, "dialog");
    }
}
