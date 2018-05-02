package com.nkanev.taskmanager.topics;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.nkanev.taskmanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTopicFragment extends DialogFragment {

    private CreateTopicDialogListener listener;
    private EditText editTextNewTopic;

    public CreateTopicFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.new_topic_fragment_title);
        View view = inflater.inflate(R.layout.fragment_new_topic, null);
        editTextNewTopic = view.findViewById(R.id.editText_new_topic);
        builder.setView(view);

        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(editTextNewTopic.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //listener.onDialogNegativeClick(editTextNewTopic.getText().toString());
            }
        });

        return builder.create();
    }

    /*
        Not necessary, because onCreateDialog takes care of the layout
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_new_topic, container, false);
        }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CreateTopicDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CreateTopicDialogListener");
        }
    }

    public interface CreateTopicDialogListener {
        public void onDialogPositiveClick(String text);
    }

}