package fhr.activities;

import java.io.IOException;

import com.example.fhr.R;



import fhr.adapters.TopicAdapter;
import fhr.entities.ForumTopic;
import fhr.parsers.ForumTopicParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

public class TopicsActivity extends Activity {
	
	private ListView topicList;
	private ForumTopicParser ftp = null;
	private ForumTopic[] forumTopicList;
	private int count = 0;
	
	private Handler reloadHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		
		reloadHandler = new Handler();
		
		topicList = (ListView) findViewById(R.id.topics_feed);
		if (Helper.isNetworkAvailable(TopicsActivity.this)){
			new Async(TopicsActivity.this).execute("");
		}
		else{
			Toast.makeText(getApplicationContext(), "Internet connection not available.", Toast.LENGTH_SHORT).show();
		}
		
	}

	private class Async extends AsyncTask<String, Void, String>{
		
		private ProgressDialog progressDialog;
		
		public Async(Context con) {
			progressDialog = new ProgressDialog(con);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Loading..");
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			 try {      
	        	ftp = new ForumTopicParser();
	        	forumTopicList = ftp.getTopicList();
	        	count = ftp.getCount();
	        } 
			catch(IOException e){
				return "";
	        }
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("ok")){
				TopicAdapter adapter = new TopicAdapter(TopicsActivity.this, count, forumTopicList);
				topicList.setAdapter(adapter);
			}
			else{
				Helper h = new Helper(TopicsActivity.this, 1, null); 
				reloadHandler.post(h);
			}
			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
		}	
	}
	
}

