package fr.app;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import fr.app.model.AccesLocal;
import fr.app.model.Musee;
import fr.app.outils.MyMuseeAdapter;

public class HistoMusee extends AppCompatActivity implements MyMuseeAdapter.OnNoteListener {

    private RecyclerView mRecyclerView;
    private List<Musee> listeMusee;
    private MyMuseeAdapter monAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo_musee);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trouve ton Musée");

        AccesLocal accesLocal = new AccesLocal(this);
        if(accesLocal.testIndexBDD()) {
            listeMusee = accesLocal.listMusee();
            monAdapter = new MyMuseeAdapter(listeMusee, this);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setAdapter(monAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {

            Intent intent = new Intent(this, Accueil.class);
            startActivity(intent);

            Toast.makeText(this, "Retour", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, InfoMusee.class);
        intent.putExtra("musee", listeMusee.get(position));
        startActivity(intent);
    }
}