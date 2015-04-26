package mohit.autoattend;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by mohit on 26/4/15.
 */
public class MyReceiver extends ResultReceiver {
    private Receiver receiver;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public MyReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public interface Receiver{
        public void onReceiveResult(int resultCode,Bundle result);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver!=null)
        {
            receiver.onReceiveResult(resultCode,resultData);
        }
    }
}
