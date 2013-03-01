package com.example.fhr;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TopicsActivity extends Activity {
	
	ListView topicList;
	private int count = 0;
	private ForumTopicParser ftp = null;
	private ForumTopic[] forumTopicList;
	private BaseDialogActivity bda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		topicList = (ListView) findViewById(R.id.topics_feed);
		new Async(TopicsActivity.this).execute("");	
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		TopicsActivity.this.finish();
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
			progressDialog.setCancelable(false);
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
			catch(HttpResponseException e){
				Intent threadsActivity = new Intent(getApplicationContext(), TopicsActivity.class);
				startActivity(threadsActivity);
			}
			catch (IOException e) {                          
	            e.printStackTrace();                           
	        }       
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			ImageAdapter adapter = new ImageAdapter();
			adapter.notifyDataSetChanged();
			topicList.setAdapter(adapter);
			
			if (progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
		}	
	}
	public class ImageAdapter extends BaseAdapter {
		
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
        public int getItemViewType(int position) {
         // Define a way to determine which layout to use, here it's just evens and odds.
        	if (forumTopicList[position].subTopics.isEmpty()){
				return 0;
			}
        	else{
        		return 1;
        	}
        }

        @Override
        public int getViewTypeCount() {
         return 2; // Count of different layouts
        }
 
        //notify set changed
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int pos = position;
			View v = convertView;
			LayoutInflater li;
			Log.d("APP", "Point_getView and position: " +  position + " , and count " + count );
			if (convertView == null){
				if (getItemViewType(position) == 0){
					li = getLayoutInflater();
					v = li.inflate(R.layout.topics_list_row_no_subtopics, null);
					Log.d("APP", "Inflation 1");
				}
				else{
					li = getLayoutInflater();
					v = li.inflate(R.layout.topics_list_row_with_subtopics, null);
					Log.d("APP", "Inflation 2");
				}
				Log.d("APP", "new inflation");
			}
			
			TextView tName = (TextView) v.findViewById(R.id.topic_name);
			TextView tDesc = (TextView) v.findViewById(R.id.topic_description);
			ImageView subImage = (ImageView) v.findViewById(R.id.topic_options);
			
			tName.setText(forumTopicList[position].name.replace("&amp;", "&"));
			tDesc.setText(forumTopicList[position].description);	

			tName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
						Intent threadsActivity = new Intent("android.intent.action.FHR.THREAD.ACTIVITY");
						threadsActivity.putExtra("topicUrl", forumTopicList[pos].uri);
						threadsActivity.putExtra("topicName", forumTopicList[pos].name);	
						startActivity(threadsActivity);
				}
			});
			
			if (getItemViewType(position) == 1){
				subImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {	
						bda = new BaseDialogActivity(TopicsActivity.this, 1, forumTopicList[pos]);
						AlertDialog adialog = (AlertDialog) bda.onCreateDialog();
						adialog.show(); 
					}
				});
			}
			return v;
		}  
	}
}
