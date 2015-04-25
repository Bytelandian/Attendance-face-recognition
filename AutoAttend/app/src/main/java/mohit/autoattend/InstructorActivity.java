package mohit.autoattend;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InstructorActivity extends ActionBarActivity {

    String course_id;
    int year,semester;
    JSONArray teaching;
    static ArrayList<InstructorData> data=new ArrayList<InstructorData>();
    ListView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        Log.d("Attendance","LOL");

        Bundle bundle=getIntent().getExtras();
        Log.d("Attendance", bundle.getString("course_id"));
        Log.d("Attendance", bundle.getString("course_id"));
        course_id=bundle.getString("course_id");
        year=bundle.getInt("year");
        semester=bundle.getInt("semester");
        Log.d("Attendance", bundle.getString("course_id"));
        Log.d("Attendance", bundle.getString("course_id"));


        results = (ListView) findViewById(R.id.instructor);
        results.setAdapter(new EfficientAdapter(this));
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1,
                                    final int position, long arg3) {
                // TODO Auto-generated method stub
                Log.d("Attendance",String.valueOf(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(InstructorActivity.this);
                // Get the layout inflater
                final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog, null))
                        // Add action buttons
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Log.e("Attendance", "HI");
                                Dialog f = (Dialog) dialog;
                                EditText e = (EditText) f.findViewById(R.id.pin);
                                String text = e.getText().toString();
                                Log.d("Attendance", text);
                                int enteredPin = Integer.parseInt(text);
                                Log.d("Attendance", "+" + enteredPin);
                                Log.d("Attendance", "  " + position);
                                Log.d("Attendance", data.get(position).password + " ");

                                if (data.get(position).password == enteredPin) {


                                    Bundle bundle=new Bundle();
                                    bundle.putString("course_id",course_id);
                                    bundle.putString("name",data.get(position).name);
                                    bundle.putInt("password",data.get(position).password);
                                    bundle.putInt("year",year);
                                    bundle.putInt("semseter",semester);

                                    Intent i=new Intent(getApplicationContext(),Attendance.class);
                                    Log.d("Attendance","Hello");
                                    i.putExtras(bundle);
                                    startActivity(i);


                                } else {
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
              AlertDialog alert =  builder.create();
                alert.show();



        /*        Bundle bundle = new Bundle();
                bundle.putString("course_id",data.get(position).course_code);
                bundle.putInt("semester",data.get(position).semester);
                bundle.putInt("year",data.get(position).year);
                bundle.putString("teaching",data.get(position).teaches.toString());

                Intent i=new Intent(getApplicationContext(),InstructorActivity.class);
                i.putExtras(bundle);
                startActivity(i);  */
         }

        });

        try {
            teaching=new JSONArray(bundle.getString("teaching"));
            Log.e("Attendance",teaching.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        displaycourses(teaching);


    }

    private void displaycourses(JSONArray teaching) {

        data.clear();

        try {
            for (int i=0;i<teaching.length();i++)
            {
                Log.e("Attendance",i+"asd");

                data.add(new InstructorData((JSONObject)teaching.get(i)));

            }
            results.setAdapter(new EfficientAdapter(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instructor, menu);
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
            Log.d("Attendance", "Instructor Called");
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

            holder.title.setText(data.get(position).name);

            return convertView;
        }

        class ViewHolder {
            TextView title;
        }
    }
}
