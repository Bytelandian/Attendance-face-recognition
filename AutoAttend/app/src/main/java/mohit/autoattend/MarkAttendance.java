package mohit.autoattend;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MarkAttendance extends IntentService {


    public MarkAttendance() {
        super("MarkAttendance");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Attendance", "ENER  ");
        if (intent != null) {
            ResultReceiver receiver = intent.getParcelableExtra("receiver");

            Bundle b=new Bundle();
            Log.e("Attendance", MainActivity.RESULT_OK+ "  ");
            receiver.send(MainActivity.RESULT_OK,b);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
