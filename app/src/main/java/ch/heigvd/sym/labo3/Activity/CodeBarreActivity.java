package ch.heigvd.sym.labo3.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.R;

/**
 * @Class       : CodeBarreActivity
 * @Author(s)   : Spinelli Isaia et Simonet Yoann
 * @Date        : 15.11.2019
 * @brief       : Permet de scanner un QR code via l'application de Barcode Scanner
 * @Reference   : https://github.com/journeyapps/zxing-android-embedded#adding-aar-dependency-with-gradle
 *               https://stackoverflow.com/questions/8831050/android-how-to-read-qr-code-in-my-application
 */
public class CodeBarreActivity extends AppCompatActivity {
    // Pour afficher le résultat
    private TextView resCodeBarre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_barre);
        resCodeBarre = findViewById(R.id.resCodeBarre);

        // Essaie de lancer l'app de Barcode Scanner
        try {
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
                // Récupère le format
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT"); // QR_CODE
                // Affiche le résultat
                resCodeBarre.setText(contents);

            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
                Toast.makeText(this, "Canceled !", Toast.LENGTH_LONG).show();
            }
        }
    }
}
