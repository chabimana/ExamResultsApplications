package student.com.examresultsapplications;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private Button searchButton;

    private EditText searchStringEditText;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton=findViewById(R.id.search_marks_button);
        searchStringEditText=findViewById(R.id.search_string_edit_text);

        progressBar =  findViewById(R.id.progressBar);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(searchStringEditText.getText().toString()==null || searchStringEditText.getText().toString().equals("")){
                    searchStringEditText.setError("Please Enter Reg Number");
                    searchStringEditText.requestFocus();
                    return;
                }
                else{
                    //if validation passes
                    HashMap<String, String> params = new HashMap<>();
                    params.put("regNumber", searchStringEditText.getText().toString().trim());

                    //Calling the create hero API
                    PerformNetworkRequest request = new PerformNetworkRequest(API.URL_GET_RESULT, params, CODE_POST_REQUEST);
                    request.execute();

                }
            }
        });
        progressBar.setVisibility(View.GONE);
    }



    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                   System.out.println(object.getJSONArray("courses"));
                    //Move to the next screen to view the student results

                    Intent intent=new Intent(MainActivity.this, StudentResultActivity.class);
                    intent.putExtra("result",object.toString());
                    startActivityForResult(intent, 1);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            Log.i("Request: ", params.toString());
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);
            return null;
        }
    }

}
