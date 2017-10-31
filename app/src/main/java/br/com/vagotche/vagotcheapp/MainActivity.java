package br.com.vagotche.vagotcheapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.vagotche.vagotcheapp.Validações.ValidaCPF;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    EditText editCPF1,editPassword1;
    Button btnEntrar;
    TextView txtCadastrar;

    String url = "";
    String parametros = "";

    //FB
    LoginButton fbLogin;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    //Default alert
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        editCPF1 = (EditText)findViewById(R.id.editCPF1);
        editPassword1 = (EditText)findViewById(R.id.editPassword1);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        txtCadastrar = (TextView)findViewById(R.id.txtCadastrar);

        //Facebook Buttons
        //lb.setPublishPermissions(Arrays.asList("email","public_profile","user_friends"));

        //Google Buttons
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

    private void iniciarComFacebook(){

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        fbLogin = (LoginButton) findViewById(R.id.fbLogin);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                alert("Loggin in...");

            }

            @Override
            public void onCancel() {
                alert("Login Cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                alert("Login com erro: " + error.getMessage());
            }
        };
        fbLogin.setReadPermissions("user_friends");
        fbLogin.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
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
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }
    // [END onActivityResult]

    //FB
    private void nextActivity(Profile profile){
        if (profile != null){
            Intent main = new Intent(MainActivity.this, MenuActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(100,100).toString());

        }
    }

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

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Pattern passwordPat = Pattern.compile("(?=^.{6,32}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

        if (networkInfo != null && networkInfo.isConnected()){

            String cpf = editCPF1.getText().toString();
            String senha = editPassword1.getText().toString();

        if(cpf.isEmpty() || senha.isEmpty()){
            alert("Nenhum campo pode estar vazio");

//        } else if (!passwordPat.matcher(senha).matches()){
//            alert("A senha deve conter entre 6~32 caracteres e ao menos um caracter especial ou numérico.");

        } else if (!ValidaCPF.isCPF(cpf) == true) {
            alert("O CPF digitado está incorreto");

        } else {
                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Login/ValidarLogin";

                parametros = "cpf=" + cpf + "&senha=" + senha;

                new SolicitaDados().execute(url);
            }

        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }

    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            //teste
            //editCPF1.setText(resultado);

            if (resultado.contains("login_ok")) {

                alert("Login realizado com sucesso");

                String[] dados = resultado.split(",");
                int cdUsuario = Integer.parseInt(dados[1]);

                //alert("id= " + cdUsuario);

                Intent it = new Intent(MainActivity.this, MenuActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("id_usuario", cdUsuario);
                it.putExtra("nome_usuario", dados[2]);
                it.putExtra("email_usuario", dados[3]);
                it.putExtra("Token", FirebaseInstanceId.getInstance().getToken());
                startActivity(it);
            } else if (resultado.contains("cpf_invalido")){
                alert("CPF inválido");

            } else if (resultado.contains("cpf_nao_cadastrado_ou_senha_invalida")){
                alert("CPF não cadastrado ou senha incorreta");

            }

        }
    }

    // [END signIn]

    // [START cadastro]
    private void cadastrar() {
        Intent it = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(it);

    }
    // [END cadastro]

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
            case R.id.fbLogin:
                iniciarComFacebook();
                break;
            //case R.id.disconnect_button:
            //    revokeAccess();
            //    break;
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();
//    }

}
