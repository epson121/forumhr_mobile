package fhr.activities;

import com.example.fhr.R;

import fhr.adapters.PostAdapter;
import fhr.entities.ForumPost;
import fhr.parsers.ForumPostParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

public class PostsActivity extends Activity {
	
	private ForumPostParser fpp;
	private ForumPost[] fpl;
	private ListView postList;
	
	private int count, threadNumOfPages, currentPage = 1;
	private String cleanUri, threadName, threadUri;
	
	private Handler reloadHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		reloadHandler = new Handler();
		
		Bundle b = getIntent().getExtras();
		threadName = b.getString("threadName");
		threadUri =  b.getString("threadUrl");
		threadNumOfPages = b.getInt("threadNumOfPages");
				
		if (threadUri.contains("&page=")){
			cleanUri = threadUri.split("&page=")[0];
			currentPage = Integer.parseInt(threadUri.split("&page=")[1]);
		}
		else{
			cleanUri = threadUri;
		}
		setTitle(threadName);
		postList = (ListView) findViewById(R.id.posts_feed);
		
		if (Helper.isNetworkAvailable(PostsActivity.this)){
			new Async(PostsActivity.this).execute(threadUri);	
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
				fpp = new ForumPostParser(params[0]);
				fpl = fpp.getPostList();
				count = fpp.getSize();
			} 
			catch (Exception e) {
				return "";
			}
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("ok")){
				PostAdapter ad = new PostAdapter(
								 PostsActivity.this, count, fpl,
								 threadName, threadUri, threadNumOfPages, 
								 currentPage, cleanUri
								 );
				postList.setAdapter(ad);
			}
			else{
				String[] reloadData = new String[]{threadUri, threadName, threadNumOfPages + "" };
				Helper h = new Helper(PostsActivity.this, 3, reloadData ); 
				reloadHandler.post(h);
			}
			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
		}
	}
	
	
	
	
}
	