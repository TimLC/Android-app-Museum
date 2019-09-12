package fr.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.app.model.AccesLocal;
import fr.app.model.Musee;
import fr.app.outils.MyPhotoAdapter;
import fr.app.service.Post;
import fr.app.service.RequeteMusee;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FicheMusee extends AppCompatActivity {

    public static TextView textNom;
    public static TextView textAdresse;
    public static TextView textVille;
    public static TextView textCP;
    public static TextView textDept;
    public static TextView textRegion;
    public static TextView textSite_web;
    public static TextView textPeriode_ouverture;
    public static TextView textFermeture_annuelle;
    private static Musee musee;
    private String idMusee;
    private static List<String> listeIdMusee;
    private static TextView test;
    private List<Bitmap> idBmpPhoto = new ArrayList<Bitmap>();
    private RecyclerView mRecyclerView;
    private MyPhotoAdapter monAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

    private List<String> saveIdPhoto = new ArrayList<String>();
    private List<Bitmap> savePhoto = new ArrayList<Bitmap>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_musee);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        idMusee = intent.getStringExtra("varQRCode");
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trouve ton Musée");

        importMusee();

    }

    @OnClick(R.id.buttonAjouter)
    public void onAddClickAjouter()
    {
        AccesLocal accesLocal = new AccesLocal(this);
        int cmp = 0;
        List<String> repIdMusee = accesLocal.idMusee();
        if(!repIdMusee.contains(musee.getId())) {
            accesLocal.ajoutMusee(musee);
        }
        List<String> repIdPhoto = accesLocal.idPhoto();
        if(accesLocal.testIndexBDDPhoto()) {

            for(String idPhoto : saveIdPhoto)
            {
                boolean test=true;
                for(int i=0; i<repIdPhoto.size(); i++)
                {
                    for(int j=0; j<repIdMusee.size(); j++) {
                        if (((repIdPhoto.get(i)).equals(idPhoto)) && ((repIdMusee.get(j)).equals(idMusee))) {
                            test=false;
                        }
                    }
                }
                if(test) {
                    accesLocal.ajoutPhoto(idPhoto, idMusee, savePhoto.get(cmp));
                }
                cmp++;
            }
        }
        else {
            for(String idPhoto : saveIdPhoto)
            {
               accesLocal.ajoutPhoto(idPhoto, idMusee, savePhoto.get(cmp));
               cmp++;
            }
        }
        Toast.makeText(this, "Enregistrement", Toast.LENGTH_LONG).show();
        finish();
    }

    public void importMusee()
    {
        textNom = (TextView)findViewById(R.id.textNom);
        textAdresse = (TextView)findViewById(R.id.textAdresse);
        textVille = (TextView)findViewById(R.id.textVille);
        textCP = (TextView)findViewById(R.id.textCP);
        textDept = (TextView)findViewById(R.id.textDept);
        textRegion = (TextView)findViewById(R.id.textRegion);
        textSite_web = (TextView)findViewById(R.id.textSite_web);
        textPeriode_ouverture = (TextView)findViewById(R.id.textPeriode_ouverture);
        textFermeture_annuelle = (TextView)findViewById(R.id.textFermeture_annuelle);

        final List<String> listPhotoMusee = new ArrayList<String>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vps449928.ovh.net/api/musees/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequeteMusee jsonPlaceHolderApi = retrofit.create(RequeteMusee.class);

        Call<Post> call = jsonPlaceHolderApi.getMusee(idMusee);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    textNom.setText("Code: " + response.code());
                    return;
                }

                Post post = response.body();

                textNom.setText("Nom : " + post.getNom());
                textAdresse.setText("Adresse : " + post.getAdresse());
                textVille.setText("Ville : " + post.getVille());
                textCP.setText("Code postal : " + post.getCp());
                textDept.setText("Département : " + post.getDept());
                textRegion.setText("Région : " + post.getRegion());
                textSite_web.setText("Site web : " + post.getSite_web());
                textPeriode_ouverture.setText("Période d'ouverture : " + post.getPeriode_ouverture());
                textFermeture_annuelle.setText("Fermeture annuelle : " + post.getFermeture_annuelle());

                musee = new Musee(post.getId(), post.getNom(), post.getPeriode_ouverture(), post.getAdresse(), post.getVille(), post.getFerme(), post.getFermeture_annuelle(), post.getSite_web(), post.getCp(), post.getRegion(), post.getDept());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textNom.setText("test" + t.getMessage());
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl("http://vps449928.ovh.net/api/musees/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(RequeteMusee.class);

        Call<List<String>> callIdPhoto = jsonPlaceHolderApi.getListePhoto(idMusee);

        callIdPhoto.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if (!response.isSuccessful()) {
                    test.setText("Code: " + response.code());
                    return;
                }

                List<String> ids = response.body();

                for (String id : ids) {
                    listPhotoMusee.add(id);
                }
                importPhoto(listPhotoMusee);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    public void importPhoto(List<String> listPhotoMusee) {
        String url="http://vps449928.ovh.net/api/musees/";
        if (listPhotoMusee != null) {
            for (String id : listPhotoMusee) {
                final String idPhoto[] = id.split("/");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequeteMusee jsonPlaceHolderApi = retrofit.create(RequeteMusee.class);
                Call<ResponseBody> callPhoto = jsonPlaceHolderApi.getImageMusee(idPhoto[5], idPhoto[8]);

                callPhoto.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        AccesLocal accesLocal = new AccesLocal(getApplicationContext());
                        InputStream is = response.body().byteStream();
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        idBmpPhoto.add(bmp);
                        mRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView2);
                        monAdapter = new MyPhotoAdapter(idBmpPhoto);
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setAdapter(monAdapter);
                        mRecyclerView.setLayoutManager(layoutManager);

                        saveIdPhoto.add(idPhoto[8]);
                        savePhoto.add(bmp);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
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
}