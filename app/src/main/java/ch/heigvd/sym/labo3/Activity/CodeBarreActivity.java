package ch.heigvd.sym.labo3.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.R;

/**
 * @Class       : NFCActivity
 * @Author(s)   : Spinelli Isaia et Simonet Yoann
 * @Date        : 15.11.2019
 * @Reference : https://github.com/journeyapps/zxing-android-embedded#adding-aar-dependency-with-gradle
 */
public class CodeBarreActivity extends AppCompatActivity {

    private TextView resCodeBarre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_barre);

        resCodeBarre = findViewById(R.id.resCodeBarre);

        /*
            Les codes QR peuvent stocker jusqu'à 7 089 caractères numériques,
            4 296 caractères alphanumériques,
            bien au-delà de la capacité du code-barres (de 10 à 13 caractères).
         */

        // https://stackoverflow.com/questions/8831050/android-how-to-read-qr-code-in-my-application
        try {
            // Lance l'app de Barcode Scanner
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            // Donne la fonction de callback
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            // Si il y a une exception, propose de telecharger l'app
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Si il y a bien un résultat
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Récupère le resultat
                String contents = intent.getStringExtra("SCAN_RESULT"); // String
                // récupère le format
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT"); // QR_CODE
                // Affiche le résultat
                resCodeBarre.setText(contents);

            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
            }
        }
    }
}
