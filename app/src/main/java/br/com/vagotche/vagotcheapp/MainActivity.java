package br.com.vagotche.vagotcheapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    EditText editEmail,editPassword;
    Button btnEntrar;
    TextView txtCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        txtCadastrar = (TextView)findViewById(R.id.txtCadastrar);

        SignInButton signInButton = (SignInButton) findViewById(R.id.ggLogin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]

        // Views
        //mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        btnEntrar.setOnClickListener(this);
        txtCadastrar.setOnClickListener(this);
        findViewById(R.id.ggLogin).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]


    }

    //@Override
    //public void onStart() {
    //    super.onStart();
//
    //    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    //    if (opr.isDone()) {
    //        // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
    //        // and the GoogleSignInResult will be available instantly.
    //        Log.d(TAG, "Got cached sign-in");
    //        GoogleSignInResult result = opr.get();
    //        handleSignInResult(result);
    //    } else {
    //        // If the user has not previously signed in on this device or the sign-in has expired,
    //        // this asynchronous branch will attempt to sign in the user silently.  Cross-device
    //        // single sign-on will occur in this branch.
    //        showProgressDialog();
    //        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
    //            @Override
    //            public void onResult(GoogleSignInResult googleSignInResult) {
    //                hideProgressDialog();
    //                handleSignInResult(googleSignInResult);
    //            }
    //        });
    //    }
    //}

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String img_url = acct.getPhotoUrl().toString();
            //Glide.with(this).load(img_url).into(ProgressDialog)

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void iniciar() {
        //TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        //TextView txtPassword = (TextView) findViewById(R.id.txtPassword);
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.equals("guizymmer@gmail.com") && password.equals("123")) {
            alert("Login realizado com sucesso");

            Intent it = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(it);
            }
        else {

            alert("Login ou senha incorretos");
        }

    }

    // [END signIn]

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



    // [START cadastro]
    private void cadastrar() {
        Intent it = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(it);

    }
    // [END signIn]

    // [START google signIn]
    private void iniciarComGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END google signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            //mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.ggLogin).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.ggLogin).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEntrar:
                iniciar();
                break;
            case R.id.txtCadastrar:
                cadastrar();
                break;
            case R.id.ggLogin:
                iniciarComGoogle();
                break;
            //case R.id.sign_out_button:
            //    signOut();
            //    break;
            //case R.id.disconnect_button:
            //    revokeAccess();
            //    break;
        }
    }
}
