package ch.heigvd.sym.labo3.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import ch.heigvd.sym.labo3.R;

public class NFCActivity extends AppCompatActivity {

    Button lowSecurity;
    Button middleSecurity;
    Button HighSecurity;
    CountDownTimer timer;
    TextView Temps;
    long minuteur = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Temps = findViewById(R.id.tmp);
        Temps.setText("80");
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

       timer = new CountDownTimer(80000, 1000) {
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
    }
}
