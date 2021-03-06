package br.com.vagotche.vagotcheapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.messaging.RemoteMessage;

import br.com.vagotche.vagotcheapp.util.IabBroadcastReceiver;
import br.com.vagotche.vagotcheapp.util.IabHelper;
import br.com.vagotche.vagotcheapp.util.IabResult;
import br.com.vagotche.vagotcheapp.util.Inventory;
import br.com.vagotche.vagotcheapp.util.Purchase;

public class CreditosActivity extends AppCompatActivity
        implements IabBroadcastReceiver.IabBroadcastListener
{

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, valor, cred5, cred10, cred15, cred20, cred40, cred60;
    Double saldoVar = 0.00;
    Double saldo = 0.00;
    Button btnComprar, btnLimparCred;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

    //Valores dos Creditos
    Double credVago5 = 5.00;
    Double credVago10 = 10.00;
    Double credVago15 = 15.00;
    Double credVago20 = 20.00;
    Double credVago40 = 40.00;
    Double credVago60 = 60.00;

    Date data = new Date();

    //Formato Data completa
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    //Formato Hora
    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

    //Formato de moeda
    DecimalFormatSymbols dfs = new DecimalFormatSymbols (new Locale("pt", "BR"));
    // Formato com sinal de menos -5.000,00
    //DecimalFormat df1 = new DecimalFormat ("#,##0.00", dfs);
    // Formato com parêntese (5.000,00)
    DecimalFormat df2 = new DecimalFormat ("#,##0.00;(#,##0.00)", dfs);


    // Debug tag, for logging
    static final String TAG = "VagoTchê";

    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    //static final String SKU_PREMIUM = "premium";
    static final String credito5 = "credito5";
    static final String credito10 = "credito10";
    static final String credito15 = "credito15";
    static final String credito20 = "credito20";
    static final String credito40 = "credito40";
    static final String credito60 = "credito60";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        //Saldo
        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditos);
        seuSaldo.setText("R$" + getIntent().getStringExtra("saldo"));

        // Get IDs
        cred5 = (TextView) findViewById(R.id.creditos5);
        cred10 = (TextView) findViewById(R.id.creditos10);
        cred15 = (TextView) findViewById(R.id.creditos15);
        cred20 = (TextView) findViewById(R.id.creditos20);
        cred40 = (TextView) findViewById(R.id.creditos40);
        cred60 = (TextView) findViewById(R.id.creditos60);
        //valor = (TextView) findViewById(R.id.viewSaldoCreditos);
//        btnComprar = (Button) findViewById(R.id.btnComprarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarCredito);
//        btnLimparCred = (Button) findViewById(R.id.btnLimparCred);
//        // Button listeners
//        creditos5.setOnClickListener(this);
//        creditos10.setOnClickListener(this);
//        creditos15.setOnClickListener(this);
//        btnComprar.setOnClickListener(this);
        //btnVoltar.setOnClickListener((View.OnClickListener) this);
//        btnLimparCred.setOnClickListener(this);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiI/sOn+Gs8sr/zhQn0zR0J7HsQJ8Fnrrvu4lRXwT5vsq7Zhnv6gg7/kr0TZznQrs45SQOyRriUW4VIZ6h4vbNbbvq/CxeTE27zPBXHemaoHMP7hEbmLe8J5HWnvxuPhxMF4iXfz0+z777h7wh8phgITMaYi6Ek7jeCY5QeSJ1qEjUP9jzLsNgPBb4sahb5N52xiIfTFnUe6rziLG4pdZqK2vzk7pFYt5XErf+wikPtkFl4NaNdki8qnb3xoE9YYmn1MzhwKjc1h2luRNRTDfVrj/Zj6neXtzuzE7TI2AHgwFMU+rc9U/41/J/Jc8pjKXNSFWy4Pji3TmxTJBU3H14wIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(CreditosActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    private void ComprarCredito() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/ComprarCreditos";

            parametros = "saldo=" + saldo + "&cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        }

    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            //Data Atual do Celular
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            Date data_atual = cal.getTime();

            String data_completa = dateFormat.format(data_atual);
            String hora_atual = dateFormat_hora.format(data_atual);

            if (resultado.contains("credito_adquirido")) {
                alert("" + getIntent().getBooleanExtra("status3", true));

                if (getIntent().getBooleanExtra("status3", true)) {
                    showNotification("vagoCRED", "Adquirido R$" + df2.format(saldoVar) + " reais de crédito." +
                            "\n" + "Data: " + data_completa + "Hora: " + hora_atual);
                }
                //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);i
                //String number = tm.getLine1Number();
                //alert("numero: " +number);
                //SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage("51997152881", null, "VagoTchê: Adquirido R$"+ df2.format(saldo) +" para o credVAGO." + " Data: " + data_completa, null, null);

            }

        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

//            // Do we have the premium upgrade?
//            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

//            // First find out which subscription is auto renewing
//            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
//            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
//            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
//                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
//                mAutoRenewEnabled = true;
//            } else if (gasYearly != null && gasYearly.isAutoRenewing()) {
//                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
//                mAutoRenewEnabled = true;
//            } else {
//                mInfiniteGasSku = "";
//                mAutoRenewEnabled = false;
//            }

//            // The user is subscribed if either subscription exists, even if neither is auto
//            // renewing
//            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
//                    || (gasYearly != null && verifyDeveloperPayload(gasYearly));
//            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
//                    + " infinite gas subscription.");
//            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(credito5);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(credito5), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                return;
            }

            //updateUi();
            //setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    // User clicked the "Buy Credito" button
    public void onBuyCreditoButtonClicked(View view) {
        Log.d(TAG, "Buy credito button clicked.");

        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }

//        if (mTank >= TANK_MAX) {
//            complain("Your tank is full. Drive around a bit!");
//            return;
//        }

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        //setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for credito.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        if (view.getId() == R.id.creditos5) {
            try {
                mHelper.launchPurchaseFlow(this, credito5, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Erro ao iniciar o fluxo de compras para o credito 5. Outra operação assíncrona em andamento.");
                //setWaitScreen(false);
            }
        } else if (view.getId() == R.id.creditos10) {
            try {
                mHelper.launchPurchaseFlow(this, credito10, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                //setWaitScreen(false);
            }
        } else if (view.getId() == R.id.creditos15) {
            try {
                mHelper.launchPurchaseFlow(this, credito15, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                //setWaitScreen(false);
            }
        } else if (view.getId() == R.id.creditos20) {
            try {
                mHelper.launchPurchaseFlow(this, credito20, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                //setWaitScreen(false);
            }
        } else if (view.getId() == R.id.creditos40) {
            try {
                mHelper.launchPurchaseFlow(this, credito40, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                //setWaitScreen(false);
            }
        } else if (view.getId() == R.id.creditos60) {
            try {
                mHelper.launchPurchaseFlow(this, credito60, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
                //setWaitScreen(false);
            }
        }

    }

//    // User clicked the "Upgrade to Premium" button.
//    public void onUpgradeAppButtonClicked(View arg0) {
//        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
//        //setWaitScreen(true);
//
//        /* TODO: for security, generate your payload here for verification. See the comments on
//         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//         *        an empty string, but on a production app you should carefully generate this. */
//        String payload = "";
//
//        try {
//            mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
//                    mPurchaseFinishedListener, payload);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error launching purchase flow. Another async operation in progress.");
//            //setWaitScreen(false);
//        }
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvVoltarCredito:
                finish();
                break;
        }
    }

//         // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
//         // flow for subscription.
//         public void onInfiniteGasButtonClicked(View arg0) {
//             if (!mHelper.subscriptionsSupported()) {
//                 complain("Subscriptions not supported on your device yet. Sorry!");
//                 return;
//             }
//
//             CharSequence[] options;
//             if (!mSubscribedToInfiniteGas || !mAutoRenewEnabled) {
//                 // Both subscription options should be available
//                 options = new CharSequence[2];
//                 options[0] = getString(R.string.subscription_period_monthly);
//                 options[1] = getString(R.string.subscription_period_yearly);
//                 mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
//                 mSecondChoiceSku = SKU_INFINITE_GAS_YEARLY;
//             } else {
//                 // This is the subscription upgrade/downgrade path, so only one option is valid
//                 options = new CharSequence[1];
//                 if (mInfiniteGasSku.equals(SKU_INFINITE_GAS_MONTHLY)) {
//                     // Give the option to upgrade to yearly
//                     options[0] = getString(R.string.subscription_period_yearly);
//                     mFirstChoiceSku = SKU_INFINITE_GAS_YEARLY;
//                 } else {
//                     // Give the option to downgrade to monthly
//                     options[0] = getString(R.string.subscription_period_monthly);
//                     mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
//                 }
//                 mSecondChoiceSku = "";
//             }
//
//             int titleResId;
//             if (!mSubscribedToInfiniteGas) {
//                 titleResId = R.string.subscription_period_prompt;
//             } else if (!mAutoRenewEnabled) {
//                 titleResId = R.string.subscription_resignup_prompt;
//             } else {
//                 titleResId = R.string.subscription_update_prompt;
//             }
//
//             Builder builder = new Builder(this);
//             builder.setTitle(titleResId)
//                     .setSingleChoiceItems(options, 0 /* checkedItem */, this)
//                     .setPositiveButton(R.string.subscription_prompt_continue, this)
//                     .setNegativeButton(R.string.subscription_prompt_cancel, this);
//             android.app.AlertDialog dialog = builder.create();
//             dialog.show();
//         }


//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        showNotification(remoteMessage.getData().get("message"));
//    }

    private void showNotification(String title, String message) {

//        Intent i = new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSmallIcon(R.drawable.cast_ic_stop_circle_filled_white);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.vagotcheestacionamento40x40px);
            builder.setLargeIcon(bm);
                //.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

//    @Override
//    public void onClick(DialogInterface dialog, int id) {
//        if (id == 0 /* First choice item */) {
//            mSelectedSubscriptionPeriod = mFirstChoiceSku;
//        } else if (id == 1 /* Second choice item */) {
//            mSelectedSubscriptionPeriod = mSecondChoiceSku;
//        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
//            /* TODO: for security, generate your payload here for verification. See the comments on
//             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//             *        an empty string, but on a production app you should carefully generate
//             *        this. */
//            String payload = "";
//
//            if (TextUtils.isEmpty(mSelectedSubscriptionPeriod)) {
//                // The user has not changed from the default selection
//                mSelectedSubscriptionPeriod = mFirstChoiceSku;
//            }
//
//            List<String> oldSkus = null;
//            if (!TextUtils.isEmpty(mInfiniteGasSku)
//                    && !mInfiniteGasSku.equals(mSelectedSubscriptionPeriod)) {
//                // The user currently has a valid subscription, any purchase action is going to
//                // replace that subscription
//                oldSkus = new ArrayList<String>();
//                oldSkus.add(mInfiniteGasSku);
//            }
//
//            //setWaitScreen(true);
//            Log.d(TAG, "Launching purchase flow for gas subscription.");
//            try {
//                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
//                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                complain("Error launching purchase flow. Another async operation in progress.");
//                //setWaitScreen(false);
//            }
//            // Reset the dialog options
//            mSelectedSubscriptionPeriod = "";
//            mFirstChoiceSku = "";
//            mSecondChoiceSku = "";
//        } else if (id != DialogInterface.BUTTON_NEGATIVE) {
//            // There are only four buttons, this should not happen
//            Log.e(TAG, "Unknown button clicked in subscription dialog: " + id);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                //complain("Error purchasing: " + result);
                //setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                //setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Compra efetuada com sucesso.");

            if (purchase.getSku().equals(credito5)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito5. Starting credito5 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito5. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            } else if (purchase.getSku().equals(credito10)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito10. Starting credito10 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito10. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            } else if (purchase.getSku().equals(credito15)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito15. Starting credito15 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito15. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            } else if (purchase.getSku().equals(credito20)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito20. Starting credito20 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito20. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            } else if (purchase.getSku().equals(credito40)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito40. Starting credito40 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito40. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            } else if (purchase.getSku().equals(credito60)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is credito60. Starting credito60 consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming credito60. Another async operation in progress.");
                    //setWaitScreen(false);
                    return;
                }
            }
//            else if (purchase.getSku().equals(SKU_PREMIUM)) {
//                // bought the premium upgrade!
//                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
//                alert("Thank you for upgrading to premium!");
//                mIsPremium = true;
//                //updateUi();
//                //setWaitScreen(false);
//            }
//            else if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
//                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY)) {
//                // bought the infinite gas subscription
//                Log.d(TAG, "Infinite gas subscription purchased.");
//                alert("Thank you for subscribing to infinite gas!");
//                mSubscribedToInfiniteGas = true;
//                mAutoRenewEnabled = purchase.isAutoRenewing();
//                mInfiniteGasSku = purchase.getSku();
//                mTank = TANK_MAX;
//                //updateUi();
//                //setWaitScreen(false);
//            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (purchase.getSku().equals(credito5) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago5;
                saldo = Double.parseDouble(valor) + credVago5;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            } else if (purchase.getSku().equals(credito10) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago10;
                saldo = Double.parseDouble(valor) + credVago10;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            } else if (purchase.getSku().equals(credito15) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago15;
                saldo = Double.parseDouble(valor) + credVago15;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            } else if (purchase.getSku().equals(credito20) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago20;
                saldo = Double.parseDouble(valor) + credVago20;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            } else if (purchase.getSku().equals(credito40) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago40;
                saldo = Double.parseDouble(valor) + credVago40;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            } else if (purchase.getSku().equals(credito60) && result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Compra efetuada com sucesso. Provisionando.");

                String valor = seuSaldo.getText().toString().replace("R$", "").replace(",", ".");

                saldoVar = credVago60;
                saldo = Double.parseDouble(valor) + credVago60;
                seuSaldo.setText("R$" + saldo.toString());
                //alert("Saldo: " +saldo + "Id: " +cdUsuario);
                ComprarCredito();
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //alert("Crédito adquirido: " + purchase + "Result: " +result);
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();
            //setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

//    // Drive button clicked. Burn gas!
//    public void onDriveButtonClicked(View arg0) {
//        Log.d(TAG, "Drive button clicked.");
//        if (!mSubscribedToInfiniteGas && mTank <= 0) alert("Oh, no! You are out of gas! Try buying some!");
//        else {
//            if (!mSubscribedToInfiniteGas) --mTank;
//            saveData();
//            alert("Vroooom, you drove a few miles.");
//            //updateUi();
//            Log.d(TAG, "Vrooom. Tank is now " + mTank);
//        }
//    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

//         // updates UI to reflect model
//         public void updateUi() {
//             // update the car color to reflect premium status or lack thereof
//             ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);
//
//             // "Upgrade" button is only visible if the user is not premium
//             findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);
//
//             ImageView infiniteGasButton = (ImageView) findViewById(R.id.infinite_gas_button);
//             if (mSubscribedToInfiniteGas) {
//                 // If subscription is active, show "Manage Infinite Gas"
//                 infiniteGasButton.setImageResource(R.drawable.manage_infinite_gas);
//             } else {
//                 // The user does not have infinite gas, show "Get Infinite Gas"
//                 infiniteGasButton.setImageResource(R.drawable.get_infinite_gas);
//             }
//
//             // update gas gauge to reflect tank status
//             if (mSubscribedToInfiniteGas) {
//                 ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
//             }
//             else {
//                 int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
//                 ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
//             }
//         }

//         // Enables or disables the "please wait" screen.
//         void setWaitScreen(boolean set) {
//             findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
//             findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
//         }

    void complain(String message) {
        Log.e(TAG, "**** Vago Tchê Error: " + message);
        alert("Cancelamento: " + message);
        //alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

//    void saveData() {
//
//        /*
//         * WARNING: on a real application, we recommend you save data in a secure way to
//         * prevent tampering. For simplicity in this sample, we simply store the data using a
//         * SharedPreferences.
//         */
//
//        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
//        spe.putInt("tank", mTank);
//        spe.apply();
//        Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
//    }

//    void loadData() {
//        SharedPreferences sp = getPreferences(MODE_PRIVATE);
//        mTank = sp.getInt("tank", 2);
//        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
//    }


}
