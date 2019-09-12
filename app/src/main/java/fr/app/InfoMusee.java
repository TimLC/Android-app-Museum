package fr.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.app.model.AccesLocal;
import fr.app.model.Musee;
import fr.app.outils.MyPhotoAdapter;
import fr.app.service.RequeteMusee;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoMusee extends AppCompatActivity {

    public static TextView textNom;
    public static TextView textAdresse;
    public static TextView textVille;
    public static TextView textCP;
    public static TextView textDept;
    public static TextView textRegion;
    public static TextView textSite_web;
    public static TextView textPeriode_ouverture;
    public static TextView textFermeture_annuelle;
    private Musee musee;
    private RecyclerView mRecyclerView;
    private MyPhotoAdapter monAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    private List<Bitmap> listPhoto;
    private static final int CAMERA_REQUEST_CCODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_musee);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trouve ton Musée");

        Intent intent = getIntent();
        musee = (Musee)intent.getSerializableExtra("musee");

        textNom = (TextView)findViewById(R.id.textNomHisto);
        textAdresse = (TextView)findViewById(R.id.textAdresseHisto);
        textVille = (TextView)findViewById(R.id.textVilleHisto);
        textCP = (TextView)findViewById(R.id.textCPHisto);
        textDept = (TextView)findViewById(R.id.textDeptHisto);
        textRegion = (TextView)findViewById(R.id.textRegionHisto);
        textSite_web = (TextView)findViewById(R.id.textSite_webHisto);
        textPeriode_ouverture = (TextView)findViewById(R.id.textPeriode_ouvertureHisto);
        textFermeture_annuelle = (TextView)findViewById(R.id.textFermeture_annuelleHisto);

        textNom.setText("Nom : " + musee.getNom());
        textAdresse.setText("Adresse : " + musee.getAdresse());
        textVille.setText("Ville : " + musee.getVille());
        textCP.setText("Code postal : " + musee.getCp());
        textDept.setText("Département : " + musee.getDept());
        textRegion.setText("Région : " + musee.getRegion());
        textSite_web.setText("Site web : " + musee.getSite_web());
        textPeriode_ouverture.setText("Période d'ouverture : " + musee.getPeriode_ouverture());
        textFermeture_annuelle.setText("Fermeture annuelle : " + musee.getFermeture_annuelle());

        AccesLocal accesLocal = new AccesLocal(this);
        listPhoto = accesLocal.listPhoto(musee.getId());

        mRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView3);
        monAdapter = new MyPhotoAdapter(listPhoto);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(monAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @OnClick(R.id.buttonUpload)
    public void onAddClickUpoad()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST_CCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CCODE) {
            if(resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                Uri image = getImageUri(this, bitmap);
                String filePath = getRealPathFromURIPath(image, InfoMusee.this);
                File file = new File(filePath);

                RequestBody mFile = RequestBody.create(MediaType.parse("/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), mFile);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://vps449928.ovh.net/api/musees/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequeteMusee jsonPlaceHolderApi = retrofit.create(RequeteMusee.class);

                Call<String> call = jsonPlaceHolderApi.uploadImage(body ,musee.getId());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                save();

            }
        }
    }

    private void save() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vps449928.ovh.net/api/musees/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequeteMusee jsonPlaceHolderApi = retrofit.create(RequeteMusee.class);

        Call<List<String>> callIdPhoto = jsonPlaceHolderApi.getListePhoto(musee.getId());

        callIdPhoto.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                importPhoto(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    public void importPhoto(List<String> listPhotoMusee) {
        String url = "http://vps449928.ovh.net/api/musees/";
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

                        List<String> repIdMusee = accesLocal.idMusee();
                        List<String> repIdPhoto = accesLocal.idPhoto();
                        boolean test=true;
                        for(int i=0; i<repIdPhoto.size(); i++)
                        {
                            for(int j=0; j<repIdMusee.size(); j++) {
                                if (((repIdPhoto.get(i)).equals(idPhoto[8])) && ((repIdMusee.get(j)).equals(musee.getId()))) {
                                    test=false;
                                }
                            }
                        }
                        if(test) {
                            accesLocal.ajoutPhoto(idPhoto[8], musee.getId(), bmp);
                            listPhoto = accesLocal.listPhoto(musee.getId());
                            mRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView3);
                            monAdapter = new MyPhotoAdapter(listPhoto);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setAdapter(monAdapter);
                            mRecyclerView.setLayoutManager(layoutManager);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @OnClick(R.id.buttonSupprimer)
    public void onAddClickSupprimer()
    {
        AccesLocal accesLocal = new AccesLocal(this);
        accesLocal.supprimeMusee(musee);
        accesLocal.supprimePhoto(musee.getId());
        Intent intent = new Intent(this, Accueil.class);
        startActivity(intent);
        finish();

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

            Intent intent = new Intent(this, HistoMusee.class);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Retour", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
