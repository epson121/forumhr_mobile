package com.example.fhr;


import java.io.IOException;

import com.example.fhr.ThreadsActivity.ThreadAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PostsActivity extends Activity {
	
	private ForumPostParser fpp;
	private ForumPost[] fpl;
	private int count;
	private ListView postList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		Bundle b = getIntent().getExtras();
		String threadName = b.getString("threadName");
		String threadUri = b.getString("threadUrl");
		//int threadNumOfPages = b.getInt("threadNumOfPages");
		
		setTitle(threadName);
		postList = (ListView) findViewById(R.id.posts_feed);
		Log.d("APP", threadUri);
		new Async(PostsActivity.this).execute(threadUri);	
	}
	
	private class Async extends AsyncTask<String, Void, String>{
		
		private ProgressDialog progressDialog;
		private Activity act;
		private Context con;
		
		public Async(Activity a) {
			this.act = a;
			con = this.act;
			progressDialog = new ProgressDialog(con);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Loading..");
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				fpp = new ForumPostParser(params[0]);
				fpl = fpp.getPostList();
				count = fpp.getSize();
				return "ok";
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			return "error";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("ok")){
				postList.setAdapter(new PostAdapter());
			}
			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
		}
	}
	
	
	private class PostAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return count;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			final int pos = position;
			LayoutInflater li = getLayoutInflater();
			v = li.inflate(R.layout.post_list_row, null);
			
			
			TextView username = (TextView) v.findViewById(R.id.post_username_text);
			TextView date = (TextView) v.findViewById(R.id.post_date_text);
			WebView postText = (WebView) v.findViewById(R.id.post_text);
			//ImageView userAvatar = (ImageView) v.findViewById(R.id.post_avatar_image);
			
			//ImageButton favThread = (ImageButton) findViewById(R.id.thread_favorite);
			
			username.setText(fpl[position].postAuthor);
			date.setText(fpl[position].postDate);
			postText.loadDataWithBaseURL("", fpl[position].postHtml, null, "UTF-8", "");

			
			return v;
		}
		
	}

}
