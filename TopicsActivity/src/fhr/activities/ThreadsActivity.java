package fhr.activities;

import com.example.fhr.R;

import fhr.adapters.ThreadAdapter;
import fhr.entities.ForumThread;
import fhr.parsers.ForumThreadParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class ThreadsActivity extends Activity {
	
	private ListView threadList;
	protected ForumThreadParser fthp, moreFthp;
	protected ForumThread[] fth;
	
	protected int count, currentPage;
	protected String topicUrl, topicName;
	
	private Handler reloadHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);
		
		reloadHandler = new Handler();
		
		final Bundle b = getIntent().getExtras();
		topicUrl = b.getString("topicUrl");
		topicName = b.getString("topicName");
		setTitle(topicName);
		
		currentPage = Integer.parseInt( Helper.getUri(topicUrl, 3)[1]);
		threadList = (ListView) findViewById(R.id.threads_feed);
		
		if (Helper.isNetworkAvailable(ThreadsActivity.this)){
			new Async(ThreadsActivity.this).execute(new String[] {topicUrl, topicName});
		}
		else{
			Toast.makeText(getApplicationContext(), "Internet connection not available.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class Async extends AsyncTask<String[], Void, String>{
		
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
		protected String doInBackground(String[]... params) {
			try {
				fthp = new ForumThreadParser(params[0][0]);
				fth = fthp.getThreadList();
				count = fthp.getCount();
			} 
			catch (Exception e) {
				e.printStackTrace();
				Log.e("APP", e.getMessage());
				return "";
			}
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("ok")){
				threadList.setAdapter(new ThreadAdapter(ThreadsActivity.this, count, fth, topicName, topicUrl, currentPage));
			}
			else{
				String[] reloadData = new String[]{topicUrl, topicName };
				Helper h = new Helper(ThreadsActivity.this, 2, reloadData ); 
				reloadHandler.post(h);
			}
			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
		}
	}	
}
