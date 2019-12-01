package ch.heigvd.sym.labo3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.Activity.CodeBarreActivity;
import ch.heigvd.sym.labo3.utileNFC.NFCloginActivity;

/**
 * @Class       : MainActivity
 * @Author(s)   : Spinelli Isaia et Simonet Yoann
 * @Date        : 15.11.2019
 */
public class MainActivity extends AppCompatActivity {

    Button btnNFC;
    Button btnCodeBare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Appelle l'activité NFC */
        btnNFC = findViewById(R.id.ButtonNFC);
        btnNFC.setOnClickListener(v -> {
            Intent intent = new Intent(btnNFC.getContext(), NFCloginActivity.class);
            btnNFC.getContext().startActivity(intent);
        });

        /* Appelle l'activité du code barre */
        btnCodeBare = findViewById(R.id.buttonCodeBarre);

        btnCodeBare.setOnClickListener(v -> {
            Intent intent = new Intent(btnCodeBare.getContext(), CodeBarreActivity.class);
            btnCodeBare.getContext().startActivity(intent);
        });
    }
}
