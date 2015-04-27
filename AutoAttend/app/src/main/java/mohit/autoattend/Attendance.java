package mohit.autoattend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Attendance extends ActionBarActivity {

/*
    public static int inRow = 0, inCol = 0;
    public static int[] inImage = null;                        // mn x 1
    public static double[] meanImage = null, diff = null;        // mn x 1
    public static int mnsize = 0;

    public static double[][] eigenFace = null;                // mn x p-1
    public static int eRow = 0, eCol = 0;

    public static double[] projTestImg = null;                // p-1 x 1

    public static double[][] projImages = null;                // p-1 x train_no
    public static int projRow = 0, projCol = 0;

    public static double[] eucDist = null;

    public static Person[] person = null;

    public static int dRow = 0;
    public static int train_no = 1;
    public  static int projTestRow = 0, projTestCol = 0;

    public static BufferedReader trainInfoReader = null;
    public  static BufferedReader mReader = null;
    public static BufferedReader eigenReader = null;
    public static BufferedReader projImgReader = null;

    public static  String line;
   public static int i, j;
*/





    public MyReceiver receiver;
    JSONArray tempstudentList,studentAttendance;
  //  public MyDataReceiver datareceiver;
    CharSequence[] res;

//    public static ReadData data;

    String course_code,name,currentDate;
    int semester,year,password;
    int count=0;
    String folderName;
    AlertDialog alert;
//    AlertDialog alertData;
    static final int CAPTURE_IMAGE_ACTIVITY=1;

    @Override
    public void onBackPressed() {
        Log.d("Attendance", "Back Pressed");

//        Boolean close=true;

        AlertDialog.Builder builder = new AlertDialog.Builder(Attendance.this);
        // Get the layout inflater
        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        builder.setView(inflater.inflate(R.layout.dialog, null))
                .setTitle("Enter Pin...")
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.e("Attendance", "HI");
                        Dialog f = (Dialog) dialog;
                        EditText e = (EditText) f.findViewById(R.id.pin);
                        String text = e.getText().toString();
                        Log.d("Attendance", text);
                        if (!text.equals(""))
                        {

                            int enteredPin = Integer.parseInt(text);
                        Log.d("Attendance", "+" + enteredPin);

                        if (password == enteredPin) {



                            Log.d("Attendance", "Hello");

                            Log.d("Attendance","Correct Pin");

                            ConfirmedClose();



                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),"Wrong Pin Entered",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        }else {
                            Toast toast = Toast.makeText(getApplicationContext(),"Wrong Pin Entered",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Dialog f = (Dialog) dialog;
                        f.cancel();
                    }
                });
        AlertDialog confirmCancel =  builder.create();
        confirmCancel.show();

    //    super.onBackPressed();
    }

    private void ConfirmedClose() {

        DatabaseSchema db = new DatabaseSchema(getApplicationContext());

        for (int i=0;i<studentAttendance.length();i++)
        {
            JSONObject temp=null;
            try {
                temp=(JSONObject) studentAttendance.get(i);
                Bundle b=new Bundle();
                Log.e("Attendance",course_code);
                Log.e("Attendance",currentDate);
                Log.e("Attendance",semester+" ");
                Log.e("Attendance",year+" ");
                Log.e("Attendance",temp.get("id").toString());
                Log.e("Attendance",temp.get("attendance").toString());


                b.putString("course_id",course_code);
                b.putString("date", currentDate);
                b.putInt("semester", semester);
                b.putInt("year", year);
                b.putString("id",temp.get("id").toString());
                b.putInt("attendance", Integer.valueOf(temp.get("attendance").toString()));
                db.insert(b);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Attendance",requestCode+" ");
        Log.e("Attendance",resultCode+" ");

        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == RESULT_OK) {

            Log.d("Attendance", "Pic saved");
  //          Toast toast = Toast.makeText(getApplicationContext(),"Processing Image!!!",Toast.LENGTH_LONG);
    //        toast.show();
            Log.d("Attendance", "Pic saved");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("Attendance", "Pic saved");

            Log.d("Attendance", "Pic saved");
            Log.d("Attendance", "Pic saved");

            Log.d("Attendance",photo.getHeight()+" ");
            Log.d("Attendance",photo.getWidth()+"  ");
            photo = Bitmap.createScaledBitmap(photo, 100, 100, true);
            Log.d("Attendance",photo.getHeight()+" ");
            Log.d("Attendance",photo.getWidth()+"  ");

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(folderName+"/"+count  + ".jpeg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

            MediaScannerConnection.scanFile(this, new String[]{folderName+"/"+count  + ".jpeg"}, null, null);

            Intent i= new Intent(this,MarkAttendance.class);
            Bundle b=new Bundle();
            b.putString("path",folderName+"/"+count  + ".jpeg");
            i.putExtras(b);
            i.putExtra("receiver",receiver);
            startService(i);

            AlertDialog.Builder builder = new AlertDialog.Builder(Attendance.this);
            final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            builder.setView(inflater.inflate(R.layout.loading_dialog, null));
            builder.setTitle("Recognizing Image...");
            builder.setCancelable(false);;
            alert =  builder.create();
            alert.show();



        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Image Capture Failed, Try Again!!!",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        try {
            Log.d("LOL",MainActivity.students.toString());
            tempstudentList = MainActivity.students;
            studentAttendance = new JSONArray();
            for (int i=0;i<tempstudentList.length();i++)
            {
                JSONObject temp = new JSONObject();
                temp.put("id",((JSONObject)tempstudentList.get(i)).getString("id"));
                temp.put("attendance",0);
                studentAttendance.put(temp);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("CompleteList",studentAttendance.toString());

        setupServiceReceiver();
        //setupDataReceiver();
        
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sf.format(new Date());

        Bundle bundle=getIntent().getExtras();
        course_code = bundle.getString("course_id");
        year=bundle.getInt("year");
        semester=bundle.getInt("semester");
        Log.d("Semester-put",semester+" ");
        name=bundle.getString("name");
        password=bundle.getInt("password");

        folderName=getString(R.string.folder) + "/" + course_code + "-" + currentDate;

        Log.d("Attendance",folderName);

        File dir=new File(folderName);
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdirs();
        }
        if (success) {
            Log.d("Attendance","Success");
            // Do something on success
        } else {
            Log.d("Attendance","Failure");
            // Do something else on failure
        }

        Button takeAttendance = (Button) findViewById(R.id.attendance);

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //              File file = new File(folderName+"/"+count+".jpg");
//                Uri outputFileUri = Uri.fromFile(file);
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //       intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY);

            }
        });

        Button Cancel = (Button) findViewById(R.id.end);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Log.d("Attendance",folderName);
        File f=new File(folderName);
        File[] mf=f.listFiles();
        for (int i=0;i<mf.length;i++)
        {
            Log.d("Attendance", mf[i].getAbsolutePath());
        }



        //     dir.mkdirs();

    //    MediaScannerConnection.scanFile (this, new String[]{folderName.toString()}, null, null);

     /*   Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        for (int i=0;i<sizes.size();i++)
        {
            Log.d("Attendance",sizes.get(i)+" ");
        } */

//        setData();
/*        AlertDialog.Builder builder = new AlertDialog.Builder(Attendance.this);
        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setTitle("Loading Data...");
        builder.setCancelable(false);;
        alertData =  builder.create();
        alertData.show();

        Intent i= new Intent(this,ReadDataService.class);
        i.putExtra("receiver",datareceiver);
        startService(i);
        startService(i);
*/
    }

  /*  private void setupDataReceiver() {

        datareceiver = new MyDataReceiver(new Handler());
        datareceiver.setReceiver(new MyDataReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle result) {

                Log.e("Here","Completed");
                alertData.hide();

                if (resultCode == RESULT_OK) {


                }

            }
        });


    }
*/
  //  private void setData() {

//        data=new ReadData();

    //}

    private void setupServiceReceiver() {

        receiver = new MyReceiver(new Handler());
        receiver.setReceiver(new MyReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle result) {

                alert.hide();

                if (resultCode == RESULT_OK) {
//                    String resultValue = result.getString("resultValue");
                    //           Log.d("Attendance",result.toString());


//                    JSONArray res=null;
                    //        try {
                    //                      String tres=(String)result.get("result");
                    //                    res= new JSONArray(tres);
                    //              } catch (JSONException e) {
                    //                e.printStackTrace();
                    //          }
                    //      Log.e("Attendance",res.toString());

                    res=result.getCharSequenceArray("result");

                    Log.d("Attendance",res.toString());
                    Log.d("Attendance", res[0].toString());


                    if (res.length==1)
                    {
                        markAttendance(res[0]);
                        Toast.makeText(Attendance.this, "Marked Attendance of "+res[0]  , Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(Attendance.this, "YOOOOOO", Toast.LENGTH_SHORT).show();

                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Attendance.this);
                        builder.setTitle("Select your ID")
                                .setCancelable(false)
                                .setItems(res, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("Attendance", which + " ");
                                        markAttendance(res[which]);
                                        Log.d("Attendance", res[which].toString());

                                        // The 'which' argument contains the index position
                                        // of the selected item
                                    }
                                });
                        builder.create().show();
                    }

                    count+=1;
                }

            }
        });
    }

    private void markAttendance(CharSequence re) {

//        JSONArray a=studentAttendance;

        Log.d("LOLAttendance",studentAttendance.toString());

        for (int i=0;i<studentAttendance.length();i++)
        {
            try {
                JSONObject temp=(JSONObject)studentAttendance.get(i);
                JSONObject marked=null;
                Log.d("Attendance",temp.get("id").toString());
                if (temp.get("id").equals(re))
                {
                    marked=new JSONObject();
                    marked.put("id",re.toString());
                    marked.put("attendance",1);
                    studentAttendance.remove(i);
                    studentAttendance.put(marked);
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
