package xyz.fmsoft.collegepa.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.R;

/**
 * Created by frederic on 12/18/16.
 */

public class DeleteDialog extends AppCompatDialogFragment {

    @Bind(R.id.delete_dialog_question)TextView _question;
    @Bind(R.id.delete_dialog_yes)TextView _yes;
    @Bind(R.id.delete_dialog_no)TextView _no;

    private static final String TAG = "DeleteDialog";
    private OnDeleteListener listener;

    public DeleteDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_dialog, container);
        ButterKnife.bind(this,view);
        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(true);
            }
        });
        _no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(false);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnDeleteListener) activity;
        }
        catch (final ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement OnDeleteListener");
        }
    }

    public void delete(boolean delete){
        listener.shouldDelete(delete);
        Log.d(TAG, "Should delete: "+delete);
        dismiss();
    }

    public interface OnDeleteListener{
        void shouldDelete(boolean dekete);
    }


}

