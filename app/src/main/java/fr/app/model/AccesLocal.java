package fr.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.app.outils.MySQLiteOpenHelper;

public class AccesLocal {

    public MySQLiteOpenHelper accesBDD;
    private Context context;
    private SQLiteDatabase db;

    public AccesLocal(Context context) {
        this.context = context;
        accesBDD = new MySQLiteOpenHelper(context);
    }

    public void ajoutMusee(Musee musee) {
        db = accesBDD.getWritableDatabase();
        String req = "insert into musee (id, nom, periode_ouverture, adresse, ville, ferme, fermeture_annuelle, site_web, cp, region, dept) values ";
        req += "(\""+musee.getId()+"\", \""+musee.getNom()+"\", \""+musee.getPeriode_ouverture()+"\", \""+musee.getAdresse()+"\", \""+musee.getVille()+"\", \""+musee.getFerme()+"\", \""+musee.getFermeture_annuelle()+"\", \""+musee.getSite_web()+"\", "+musee.getCp()+", \""+musee.getRegion()+"\", \""+musee.getDept()+"\")";
        db.execSQL(req);
    }

    public void ajoutPhoto(String id, String idMusee, Bitmap photo) {
        db = accesBDD.getWritableDatabase();
        byte[] test = getBytesFromBitmap(photo);
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("idMusee", idMusee);
        values.put("photo", getBytesFromBitmap(photo));
        db.insert("photoMusee", null, values);
    }

    public List<String> idPhoto() {
        List<String> idPhoto = new ArrayList<String>();
        db = accesBDD.getReadableDatabase();
        String req = "select id from photoMusee";
        Cursor cursor = db.rawQuery(req, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idPhoto.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return idPhoto;
    }

    public List<String> idMuseePhoto() {
        List<String> idMuseePhoto = new ArrayList<String>();
        db = accesBDD.getReadableDatabase();
        String req = "select idMusee from photoMusee";
        Cursor cursor = db.rawQuery(req, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idMuseePhoto.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return idMuseePhoto;
    }

    public List<Bitmap> listPhoto(String idMusee) {
        List<Bitmap> listPhoto = new ArrayList<Bitmap>();
        db = accesBDD.getReadableDatabase();
        String req = "select photo from photoMusee Where idMusee = '" + idMusee + "'";
        Cursor cursor = db.rawQuery(req, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] resultat = cursor.getBlob(0);
            listPhoto.add(getBitmapFrompBytes(resultat));
            cursor.moveToNext();
        }
        cursor.close();
        return listPhoto;
    }

    public void supprimeMusee(Musee musee) {
        db = accesBDD.getWritableDatabase();
        String req = "delete from musee where id = '" + musee.getId() + "'";
        db.execSQL(req);
    }

    public void supprimePhoto(String id) {
        db = accesBDD.getWritableDatabase();
        String req = "delete from photoMusee where idMusee = '" + id + "'";
        db.execSQL(req);
    }

    public List<Musee> listMusee() {
        db = accesBDD.getReadableDatabase();
        List<Musee> listeDesMusee = new ArrayList<Musee>();
        String req = "select * from musee";
        Cursor cursor = db.rawQuery(req, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            listeDesMusee.add(new Musee(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8),cursor.getString(9),cursor.getString(10)));
            cursor.moveToNext();
        }
        cursor.close();
        return listeDesMusee;
    }

    public List<String> idMusee() {
        List<String> idDesMusee = new ArrayList<String>();
        String req = "select id from musee";
        db = accesBDD.getReadableDatabase();
        Cursor cursor = db.rawQuery(req, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idDesMusee.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return idDesMusee;
    }

    public boolean testIndexBDD() {
        String req = "select id from musee";
        db = accesBDD.getReadableDatabase();
        Cursor cursor = db.rawQuery(req, null);
        if(cursor.getCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean testIndexBDDPhoto() {
        String req = "select id from photoMusee";
        db = accesBDD.getReadableDatabase();
        Cursor cursor = db.rawQuery(req, null);
        if(cursor.getCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();

    }

    public static Bitmap getBitmapFrompBytes(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
