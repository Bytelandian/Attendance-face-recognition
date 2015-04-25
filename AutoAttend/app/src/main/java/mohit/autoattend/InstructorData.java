package mohit.autoattend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohit on 25/4/15.
 */
public class InstructorData {

        String name,email_id;
        int id,password;

       InstructorData(JSONObject j)
        {
            Log.d("Attendance", "Adding Data");
            try {
                name = j.getString("name");
                password = j.getInt("password");
                id = j.getInt("id");
                email_id =  j.getString("email_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


}
