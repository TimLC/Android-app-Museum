package fr.app.outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String dataBaseName = "ListeMusee.db";
    private static final int dataBaseVersion = 1;
    private String musee = "create table musee ("
                            + "id TEXT,\n"
                            + "nom TEXT,\n"
                            + "periode_ouverture TEXT,\n"
                            + "adresse TEXT,\n"
                            + "ville TEXT,\n"
                            + "ferme TEXT,\n"
                            + "fermeture_annuelle TEXT,\n"
                            + "site_web TEXT,\n"
                            + "cp INTEGER,\n"
                            + "region TEXT,\n"
                            + "dept TEXT);";

    private String photoMusee = "create table photoMusee ("
                                + "id TEXT,\n"
                                + "idMusee TEXT,\n"
                                + "photo BLOB);";

    public MySQLiteOpenHelper(Context context) {
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(musee);
        db.execSQL(photoMusee);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
