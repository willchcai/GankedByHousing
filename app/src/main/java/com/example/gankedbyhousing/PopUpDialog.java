package com.example.gankedbyhousing;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.common.base.FinalizableWeakReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopUpDialog extends AppCompatDialogFragment {

    private TextView newPass;
    private TextView confirmPass;
    private PopUpDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.open_dialog, null);

        builder.setView(view)
                .setTitle("Change Password")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String updatedPass = newPass.getText().toString();
                        String mConfirmPass = confirmPass.getText().toString();
                        listener.applyText(mConfirmPass, updatedPass);
                    }
                });

        newPass = view.findViewById(R.id.newPass);
        confirmPass = view.findViewById(R.id.confirmPass);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = ((PopUpDialogListener)context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PopUpDialogListener");
        }
    }

    public interface PopUpDialogListener{
        void applyText(String newPass, String confirmPass);
    }
}

