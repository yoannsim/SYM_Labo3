package ch.heigvd.sym.labo3.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.R;
import ch.heigvd.sym.labo3.utileNFC.NFCloginActivity;
import ch.heigvd.sym.labo3.utileNFC.NdefReaderTaskSuper;

public class NFCActivity extends AppCompatActivity {

    Button lowSecurity;
    Button middleSecurity;
    Button HighSecurity;
    CountDownTimer timer;
    TextView Temps;
    long minuteur = 0;

    private NfcAdapter mNfcAdapter;
    static final long RESET_MINUTEUR = 65000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Temps = findViewById(R.id.tmp);
        Temps.setText(String.valueOf(RESET_MINUTEUR/1000));
        lowSecurity = findViewById(R.id.LowSecur);
        lowSecurity.setOnClickListener(v -> {
            if(minuteur > 20)   Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
        });

        middleSecurity = findViewById(R.id.midSecur);
        middleSecurity.setOnClickListener(v -> {
            if(minuteur > 40)   Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
        });

        HighSecurity = findViewById(R.id.highSecu);
        HighSecurity.setOnClickListener(v -> {
            if(minuteur > 60)   Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
        });

       timer = new CountDownTimer(RESET_MINUTEUR, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minuteur = millisUntilFinished/1000;
                Temps.setText(String.valueOf(minuteur));
            }

            @Override
            public void onFinish() {
                minuteur = 0;
                Temps.setText(String.valueOf(minuteur));
                Temps.setTextColor(Color.RED);
            }
        };
       timer.start();

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
                Log.d("NFC", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    private class NdefReaderTask extends NdefReaderTaskSuper {

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("test")){
              minuteur = RESET_MINUTEUR;
              timer.start();
            }
        }
    }
}
