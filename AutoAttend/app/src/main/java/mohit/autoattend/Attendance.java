package mohit.autoattend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Attendance extends ActionBarActivity {

    String course_code,name,currentDate;
    int semester,year,password;
    int count=0;
    String folderName;
    static final int CAPTURE_IMAGE_ACTIVITY=1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Attendance",requestCode+" ");
        Log.e("Attendance",resultCode+" ");

        if (requestCode == CAPTURE_IMAGE_ACTIVITY && resultCode == RESULT_OK) {

            Log.d("Attendance", "Pic saved");
            Toast toast = Toast.makeText(getApplicationContext(),"Processing Image!!!",Toast.LENGTH_LONG);
            toast.show();
            Log.d("Attendance", "Pic saved");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("Attendance", "Pic saved");

            Log.d("Attendance", "Pic saved");
            Log.d("Attendance", "Pic saved");

            Log.d("Attendance",photo.getHeight()+" ");
            Log.d("Attendance",photo.getWidth()+"  ");
            photo = Bitmap.createScaledBitmap(photo, 180, 200, true);
            Log.d("Attendance",photo.getHeight()+" ");
            Log.d("Attendance",photo.getWidth()+"  ");

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(folderName+"/"+count  + ".jpeg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

            count+=1;

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

        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sf.format(new Date());

        Bundle bundle=getIntent().getExtras();
        course_code = bundle.getString("course_id");
        year=bundle.getInt("year");
        semester=bundle.getInt("semester");
        name=bundle.getString("name");
        password=bundle.getInt("password");

        folderName=getString(R.string.folder) + "/" + course_code + "-" + currentDate;

        Log.d("Attendance",folderName);

        File dir=new File(folderName);
        dir.mkdirs();

     /*   Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        for (int i=0;i<sizes.size();i++)
        {
            Log.d("Attendance",sizes.get(i)+" ");
        } */


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


        Log.d("Attendance",folderName);
        File f=new File(folderName);
        File[] mf=f.listFiles();
        for (int i=0;i<mf.length;i++)
        {
            Log.d("Attendance", mf[i].getAbsolutePath());
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
