package com.barbos.sergey.weatherapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.barbos.sergey.weatherapp.R;

/**
 * Created by Sergey on 13.09.2016.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.dialog_positiveButtonText, null);

        AlertDialog dialog = builder.create();

        return dialog;
    }
}
