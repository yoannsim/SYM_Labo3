package ch.heigvd.sym.labo3.utileNFC;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.Activity.NFCActivity;
import ch.heigvd.sym.labo3.R;
import ch.heigvd.sym.labo3.utileNFC.NdefReaderTaskSuper;


/**
 * @Class       : NFCloginActivity
 * @Author(s)   : Spinelli Isaia et Simonet Yoann
 * @Date        : 15.11.2019
 */
public class NFCloginActivity extends AppCompatActivity {
    Button btlogin;
    EditText champMail;
    EditText champPasword;
    private NfcAdapter mNfcAdapter;
    private String TAG = "NFC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_login);
        btlogin = findViewById(R.id.ButtonLog);
        btlogin.setEnabled(false);
        btlogin.setTextColor(Color.LTGRAY);
        btlogin.setBackgroundColor(Color.rgb(240,240,240));
        champPasword = findViewById(R.id.psw);
        champMail = findViewById(R.id.email);


        btlogin.setOnClickListener(v -> {

            String mail = champMail.getText().toString();
            String passwornd = champPasword.getText().toString();

            if (mail.equals("yoyo") && passwornd.equals("1234")) {
                Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(btlogin.getContext(), NFCActivity.class);
                btlogin.getContext().startActivity(intent);
            } else {
                Toast.makeText(this, "wrong user "+ mail + passwornd, Toast.LENGTH_LONG).show();
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
        }

        handleIntent(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch();

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        btlogin.setEnabled(false);
        btlogin.setTextColor(Color.LTGRAY);
        btlogin.setBackgroundColor(Color.rgb(240,240,240));

    }

    protected void onPause() {
        super.onPause();
        stopForegroundDispatch();
    }

    // calledin onResume()private
    private void setupForegroundDispatch() {

        if (mNfcAdapter == null)
            return;

        final Intent intent = new Intent(this.getApplicationContext(), this.getClass());

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};// Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e("NFC", "MalformedMimeTypeException", e);
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
    }

    // called in onPause()
    private void stopForegroundDispatch() {
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (type.equals("text/plain")) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                   new  NdefReaderTask().execute(tag);
                   break;
                }
            }
        }
    }

    public class NdefReaderTask extends NdefReaderTaskSuper {

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("test")){
                btlogin.setEnabled(true);
                btlogin.setTextColor(Color.BLACK);
                btlogin.setBackgroundColor(Color.LTGRAY);
                Toast.makeText(getApplicationContext(), "lecture NFC " + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
