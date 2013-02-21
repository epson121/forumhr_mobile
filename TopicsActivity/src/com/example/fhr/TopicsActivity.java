package com.example.fhr;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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
		new Async().execute("");	
	}

	private class Async extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			 try {                                              
	        	ftp = new ForumTopicParser();
	        	forumTopicList = ftp.getTopicList();
	        	count = ftp.getCount();
	        } 
			catch (IOException e) {                          
	            e.printStackTrace();                           
	        }           
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			topicList.setAdapter(new ImageAdapter());
			
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			final int pos = position;
			Log.d("APP", "Point_getView and position: " +  position + " , and count " + count );
			//if (v == null){
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.topics_list_row, null);
				Log.d("APP", "new inflation");
			//}
			
			TextView tName = (TextView) v.findViewById(R.id.topic_name);
			TextView tDesc = (TextView) v.findViewById(R.id.topic_description);
			ImageView subImage = (ImageView) v.findViewById(R.id.topic_options);
			
			tName.setText(forumTopicList[position].name.replace("&amp;", "&"));
			tDesc.setText(forumTopicList[position].description);	
			
			
			if (!forumTopicList[position].subTopics.isEmpty()){
				subImage.setVisibility(1);
			}
			
			tName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
						Intent threadsActivity = new Intent("android.intent.action.FHR.THREAD.ACTIVITY");
						threadsActivity.putExtra("topicUrl", forumTopicList[pos].uri);
						threadsActivity.putExtra("topicName", forumTopicList[pos].name);	
						startActivity(threadsActivity);
				}
			});
			
			subImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					bda = new BaseDialogActivity(TopicsActivity.this);
					AlertDialog adialog = (AlertDialog) bda.onCreateDialog(1, forumTopicList[pos], 0);
					adialog.show(); 
				}
				
				
			});
			
			return v;
		}
		
        
	}

}
