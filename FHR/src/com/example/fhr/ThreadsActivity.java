package com.example.fhr;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadsActivity extends Activity {
	
	private ListView threadList;
	protected String topicUrl;
	protected String topicName;
	protected ForumThreadParser fthp;
	protected ForumThread[] fth;
	protected int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);
		final Bundle b = getIntent().getExtras();
		
		topicUrl = b.getString("topicUrl");
		topicName = b.getString("topicName");
		
		threadList = (ListView) findViewById(R.id.threads_feed);
		new Async().execute(new String[] {topicUrl, topicName});	
	}
	
	private class Async extends AsyncTask<String[], Void, String>{

		@Override
		protected String doInBackground(String[]... params) {
			try {
				fthp = new ForumThreadParser(params[0][0]);
				fth = fthp.getThreadList();
				count = fthp.getCount();
			} 
			catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			threadList.setAdapter(new ThreadAdapter());
		}
		
	}

	public class ThreadAdapter extends BaseAdapter {
			
        public int getCount(){
			return count;   	
        }
        
        public Object getItem(int position) {
            return position;
        }
        
        public long getItemId(int position) {
            return position;
        }
  
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null){
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.threads_list_row, null);
				Log.d("APP", "THREAD new inflation");
			}
			
			TextView threadName = (TextView) v.findViewById(R.id.thread_name);
			TextView threadAuthor = (TextView) v.findViewById(R.id.thread_author);
			TextView threadLastPostInfo = (TextView) v.findViewById(R.id.thread_last_post_info);
			//ImageButton goToPage = (ImageButton) findViewById(R.id.thread_go_to_page);
			//ImageButton favThread = (ImageButton) findViewById(R.id.thread_favorite);
			Log.d("APP", "Position; " + position);
			if (fth[position].isTop)
				threadName.setText("* " + fth[position].threadName);
			else
				threadName.setText(fth[position].threadName);
			threadAuthor.setText(fth[position].threadAuthor);
			threadLastPostInfo.setText(fth[position].lastPostInfo);

			return v;
		}
	}
}
