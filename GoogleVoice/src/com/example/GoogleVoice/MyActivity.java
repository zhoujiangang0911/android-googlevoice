package com.example.GoogleVoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static final  String TULIN_KEY="70618612770ae02f1f9955211f381c99";


    private Button button1,button2;
    private TextView textView1,tv3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button2 = (Button) findViewById(R.id.bt_2);

        button1 = (Button) findViewById(R.id.bt_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                displaySpeechRecognizer();
            }
        });


        textView1 = (TextView) findViewById(R.id.tv_1);
        tv3 = (TextView) findViewById(R.id.tv_3);
    }


    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            
            new GetTulingThread(spokenText).start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public class GetTulingThread extends Thread{
        String   spokenText;

        public  GetTulingThread(String mspokenText){
            this.spokenText =    mspokenText;
        }
        @Override
        public void run() {
            Tulingjiqiren(spokenText);
            super.run();

        }
    }




    private void Tulingjiqiren( String spokenText) {


        String requesturl = "http://www.tuling123.com/openapi/api?key="+TULIN_KEY+"&info="+spokenText;
        HttpGet request = new HttpGet(requesturl);
        HttpResponse response = null;
        try {
            response = new DefaultHttpClient().execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //200即正确的返回码
        if(response.getStatusLine().getStatusCode()==200){
            String result = null;
            try {
                result = EntityUtils.toString(response.getEntity());
                JSONObject JSONObject = new JSONObject(result);
                final String strResult = JSONObject.getString("text");
                System.out.println(strResult);
                  button2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(final View v) {
                          Toast.makeText(getApplicationContext(),strResult,Toast.LENGTH_LONG).show();
                      }
                  });

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
