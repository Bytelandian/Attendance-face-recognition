package mohit.autoattend;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohit on 25/4/15.
 */
public class CourseData {

    String course_code;
    int semester,year;
    JSONArray teaches;

    CourseData(JSONObject j)
    {
        Log.d("Attendance", "Adding Data");
        try {
            course_code = j.getString("course_id");
            year = j.getInt("year");
            semester = j.getInt("semester");
            teaches = (JSONArray) j.get("teaching");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
