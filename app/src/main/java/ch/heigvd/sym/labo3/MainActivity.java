package ch.heigvd.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ch.heigvd.sym.labo3.Activity.*;

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

        btnNFC = findViewById(R.id.ButtonNFC);
        btnNFC.setOnClickListener(v -> {
            Intent intent = new Intent(btnNFC.getContext(), NFCActivity.class);
            btnNFC.getContext().startActivity(intent);
        });

        btnCodeBare = findViewById(R.id.buttonCodeBarre);

        btnCodeBare.setOnClickListener(v -> {
            Intent intent = new Intent(btnCodeBare.getContext(), CodeBarreActivity.class);
            btnCodeBare.getContext().startActivity(intent);
        });
    }
}
