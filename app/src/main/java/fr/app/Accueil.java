package fr.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Accueil extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trouve ton Musée");

    }

    @OnClick(R.id.QRCode)
    public void onAddClickQRCode()
    {
        Intent intent = new Intent(this, QRCode.class);
        startActivity(intent);
    }

    @OnClick(R.id.histoMusee)
    public void onAddClickHistoMusee()
    {
        Intent intent = new Intent(this, HistoMusee.class);
        startActivity(intent);
    }
}
