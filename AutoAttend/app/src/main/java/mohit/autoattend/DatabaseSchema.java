package mohit.autoattend;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohit on 26/4/15.
 */
public class DatabaseSchema extends SQLiteOpenHelper{


    private static final String DATABASE_NAME="Auto";
    private static final String TABLE_ATTENDANCE="attendance";

    private static final String COLUMN_YEAR="year";
    private static final String COLUMN_SEMESTER="semester";
    private static final String COLUMN_COURSE_ID="course_id";
    private static final String COLUMN_DATE="date";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ATTENDANCE = "attendance";

    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE
            + "(" + COLUMN_YEAR + " year," + COLUMN_SEMESTER + " INTEGER,"
            + COLUMN_COURSE_ID + " TEXT," + COLUMN_DATE + " DATE,"
            + COLUMN_ID + " TEXT," + COLUMN_ATTENDANCE + " INTEGER,"
            + " PRIMARY KEY " + "(" + COLUMN_YEAR + "," + COLUMN_SEMESTER + ","
            +  COLUMN_COURSE_ID + "," + COLUMN_DATE + "," + COLUMN_ID + ")"
            + ")";





    public DatabaseSchema(Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

          Log.d("Attendance", CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_ATTENDANCE);

    /*    Log.d("LOL","LOL");

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_ATTENDANCE+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                Log.e("LOL","Success");
            }
            cursor.close();
        }
        Log.e("LOL","Failure");
        */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Bundle b)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_ID,b.getString("course_id"));
        values.put(COLUMN_DATE,b.getString("date"));
        values.put(COLUMN_SEMESTER,b.getInt("semester"));
        values.put(COLUMN_YEAR,b.getInt("year"));
        values.put(COLUMN_ID,b.getString("id"));
        values.put(COLUMN_ATTENDANCE,b.getInt("attendance"));

        Log.d("Attendance",values.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ATTENDANCE, null, values);


    }

    public void DeleteData()
    {
        SQLiteDatabase db = this.getWritableDatabase();

//        String selectQuery = "DELETE  * FROM " + TABLE_ATTENDANCE + " WHERE 1" ;

        //Log.e("Attendance", selectQuery);
           db.delete(TABLE_ATTENDANCE,null,null);
  //      db.rawQuery(selectQuery, null);
    }

    public void syncData()
    {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE + " WHERE 1" ;

        Log.e("Attendance", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        JSONArray data=new JSONArray();

        if (c.moveToFirst()) {
            do {

                JSONObject temp=new JSONObject();
                try {
                    temp.put("course_id",c.getString(c.getColumnIndex(COLUMN_COURSE_ID)));
                    temp.put("year",c.getString(c.getColumnIndex(COLUMN_YEAR)));
                    temp.put("semester",c.getString(c.getColumnIndex(COLUMN_SEMESTER)));
                    temp.put("date",c.getString(c.getColumnIndex(COLUMN_DATE)));
                    temp.put("id",c.getString(c.getColumnIndex(COLUMN_ID)));
                    temp.put("attendance",c.getString(c.getColumnIndex(COLUMN_ATTENDANCE)));
                    Log.d("Attendance",temp.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data.put(temp);
//                Log.d("Attendance",c.getString(c.getColumnIndex(COLUMN_COURSE_ID)));
          } while (c.moveToNext());
        }

        Log.d("Attendance",data.toString());


        ServerConnect ser=new ServerConnect();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("data",data.toString()));
        ser.execute( "http://10.20.9.85/Attendance/Attendance-face-recognition/Server/sync.php", nameValuePairs);



    }

    public class ServerConnect extends AsyncTask<Object,Void,JSONArray> {
        HttpGet httpget;
        HttpClient httpclient;
        List<NameValuePair> value;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("ServerConnect","Starting Task...");
        }


        @Override
        protected JSONArray doInBackground(Object... params) {
            try {

                httpclient=new DefaultHttpClient();
                //ServerFormActivity a = (ServerFormActivity) params[0];
                //listener = (OnResponseListener) a;

                value=(ArrayList<NameValuePair>)params[1];
                String paramString = URLEncodedUtils.format(value, "utf-8");
                Log.d("ServerConnect", params[0] + "?" + paramString);
                httpget= new HttpGet((String)params[0] +"?" + paramString);
//            httppost.setEntity(new UrlEncodedFormEntity(value));

                HttpResponse response = httpclient.execute(httpget);
                Log.d("ServerConnect",response.toString());
                //Log.d("ServerConnect",Integer.toString(response.getEntity().getContent().read()));



            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(JSONArray j) {
            super.onPostExecute(j);

            DeleteData();


            Log.d("Success","Success");

            Log.d("ServerConnect","Task Completed!");
        }



    }
}