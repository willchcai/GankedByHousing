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
    private TextView newLocation;
    private PopUpDialogListener listener;
    private String editMode;

    public PopUpDialog(String edit) {
        this.editMode = edit;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        if(editMode.equals("changePass")) {
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
                            listener.applyPass(mConfirmPass, updatedPass);
                        }
                    });

            newPass = view.findViewById(R.id.newPass);
            confirmPass = view.findViewById(R.id.confirmPass);

        }else{
            View view = inflater.inflate(R.layout.open_dialog_loca, null);
            builder.setView(view)
                    .setTitle("Change Location")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String editLoc = newLocation.getText().toString();
                            listener.applyLocation(editLoc);
                        }
                    });


            newLocation = view.findViewById(R.id.newLocation);


        }

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
        void applyPass(String newPass, String confirmPass);
        void applyLocation(String newLoc);
    }
}

