package com.example.fhr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ThreadsActivity extends Activity {
	
	private ListView threadList;
	protected String topicUrl;
	protected String topicName;
	protected ForumThreadParser fthp;
	protected ForumThread[] fth;
	protected int count;
	private BaseDialogActivity bda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);
		final Bundle b = getIntent().getExtras();
		
		topicUrl = b.getString("topicUrl");
		topicName = b.getString("topicName");
		
		setTitle(topicName);
		
		threadList = (ListView) findViewById(R.id.threads_feed);
		new Async(ThreadsActivity.this).execute(new String[] {topicUrl, topicName});	
	}
	
	private class Async extends AsyncTask<String[], Void, String>{
		
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
		protected String doInBackground(String[]... params) {
			try {
				fthp = new ForumThreadParser(params[0][0]);
				fth = fthp.getThreadList();
				count = fthp.getCount();
			} 
			catch (Exception e) {
				return "";
			}
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("ok"))
				threadList.setAdapter(new ThreadAdapter());
			else{
				//generate error alert dialog
			}

			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
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
			final int pos = position;
			if (v == null){
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.threads_list_row, null);
				Log.d("APP", "THREAD new inflation");
			}
			
			TextView threadName = (TextView) v.findViewById(R.id.thread_name);
			TextView threadAuthor = (TextView) v.findViewById(R.id.thread_author);
			TextView threadLastPostInfo = (TextView) v.findViewById(R.id.thread_last_post_info);
			ImageButton goToPage = (ImageButton) v.findViewById(R.id.thread_go_to_page);
			
			//ImageButton favThread = (ImageButton) findViewById(R.id.thread_favorite);

			if (fth[position].isTop)
				threadName.setText("TOP " + fth[position].threadName);
			else
				threadName.setText(fth[position].threadName);
			threadAuthor.setText(fth[position].threadAuthor);
			threadLastPostInfo.setText(fth[position].lastPostInfo);
			
			
			threadName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent postActivity = new Intent(getApplicationContext(), PostsActivity.class);
					postActivity.putExtra("threadUrl", fth[pos].threadUrl);
					postActivity.putExtra("threadName", fth[pos].threadName);
					postActivity.putExtra("threadNumOfPages", fth[pos].numOfPages);
					startActivity(postActivity);
					
				}
			});
			
			goToPage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					bda = new BaseDialogActivity(ThreadsActivity.this);
					AlertDialog dialog = (AlertDialog) bda.onCreateDialog(2, null, fth[pos].numOfPages);
					dialog.show();
					//Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
				}
			});
			
			return v;
		}
	}
}
