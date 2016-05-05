package xyz.fmsoft.collegepa;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fredericmurry on 1/28/16.
 */
public class FingerPrintDialog extends DialogFragment {

    @Bind(R.id.fingerprint_Title)TextView _title;
    @Bind(R.id.fingerprint_cancel)TextView _cancel;

    private int verify = 0;
    public FingerPrintDialog(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint, container);
        ButterKnife.bind(this, view);
        FingerprintManagerCompatApi23 fingerprintManager = new FingerprintManagerCompatApi23();

        setCancelable(false);
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });

        fingerprintManager.authenticate(this.getContext(), null, 0, null, new FingerprintManagerCompatApi23.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                Toast.makeText(getContext(), errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                Toast.makeText(getContext(), helpString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getContext(), "Authentication Succeeded", Toast.LENGTH_SHORT).show();
                Cancel();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        }, new Handler());
        return view;
    }

    public void Cancel(){
        dismiss();
    }





}
