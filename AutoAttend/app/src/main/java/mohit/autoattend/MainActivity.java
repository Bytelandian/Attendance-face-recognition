package mohit.autoattend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    static ArrayList<CourseData> data=new ArrayList<CourseData>();
    ListView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Attendance","Hi");

        results = (ListView) findViewById(R.id.courses);
        results.setAdapter(new EfficientAdapter(this));
        results.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                Log.d("Attendance",String.valueOf(position));
                Log.d("Attendance","OOOO");
                Log.d("Attendance","Thank God");

                Bundle bundle = new Bundle();
                bundle.putString("course_id",data.get(position).course_code);
                bundle.putInt("semester",data.get(position).semester);
                bundle.putInt("year",data.get(position).year);
                bundle.putString("teaching",data.get(position).teaches.toString());

                Intent i=new Intent(getApplicationContext(),InstructorActivity.class);
                Log.d("Attendance","Hello");
                i.putExtras(bundle);
                startActivity(i);

        //        String data = resultList.get(position).link;
          //      String titledata = resultList.get(position).title;
            //    displayPopup(resultList.get(position));
                // startDownload(data, titledata);
            }

        });

        try {
            displaycourses(new JSONArray("[{\"course_id\":\"CSL355\",\"semester\":\"2\",\"year\":\"2014\",\"teaching\":[]},{\"course_id\":\"CSL707\",\"semester\":\"2\",\"year\":\"2014\",\"teaching\":[{\"id\":\"1\",\"password\":\"1987\",\"name\":\"Sodhi\",\"email_id\":\"sodhi@iitrpr.ac.in\"}]},{\"course_id\":\"CYL355\",\"semester\":\"2\",\"year\":\"2014\",\"teaching\":[{\"id\":\"3\",\"password\":\"1132\",\"name\":\"rajendra\",\"email_id\":\"rajendra@iitrpr.ac.in\"},{\"id\":\"4\",\"password\":\"1132\",\"name\":\"taramani\",\"email_id\":\"taramani@iitrpr.ac.in\"}]},{\"course_id\":\"HUL472\",\"semester\":\"2\",\"year\":\"2014\",\"teaching\":[{\"id\":\"5\",\"password\":\"1132\",\"name\":\"behera\",\"email_id\":\"behera@iitrpr.ac.in\"}]}]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displaycourses(JSONArray j) {

        data.clear();

        try {
        for (int i=0;i<j.length();i++)
        {
            Log.e("Attendance",i+"asd");

                data.add(new CourseData((JSONObject)j.get(i)));

        }
        results.setAdapter(new EfficientAdapter(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class EfficientAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public EfficientAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            Log.d("Attendance", "Called");
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);


                holder = new ViewHolder();

                holder.title = (TextView) convertView.findViewById(R.id.course);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(data.get(position).course_code);

            return convertView;
        }

        class ViewHolder {
            TextView title;
        }
    }

}
