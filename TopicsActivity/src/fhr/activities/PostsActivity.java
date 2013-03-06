package fhr.activities;

import java.util.zip.Inflater;

import com.example.fhr.R;

import fhr.adapters.PostAdapter;
import fhr.dialogs.BaseDialogActivity;
import fhr.entities.ForumPost;
import fhr.parsers.ForumPostParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostsActivity extends Activity {
	
	private ForumPostParser fpp;
	private ForumPost[] fpl;
	private ListView postList;
	private BaseDialogActivity bda;
	
	private int count, threadNumOfPages, currentPage = 1;
	private String cleanUri, threadName, threadUri;
	
	private Handler reloadHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ll);
		
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
		Context con;
		public Async(Context con) {
			progressDialog = new ProgressDialog(con);
			this.con = con;
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
				/*
				PostAdapter ad = new PostAdapter(
								 PostsActivity.this, count, fpl,
								 threadName, threadUri, threadNumOfPages, 
								 currentPage, cleanUri
								 );
				postList.setAdapter(ad);
				*/
				
				LinearLayout l1 = (LinearLayout) findViewById(R.id.ll_posts);
				
				for (int position = 0; position < count+2; position++){
					
					if (position > 0 && position < count+1){
						View v = getLayoutInflater().inflate(R.layout.post_list_row_2, null);
					
						TextView username = (TextView) v.findViewById(R.id.post_username_text);
						TextView date = (TextView) v.findViewById(R.id.post_date_text);
						WebView postText = (WebView) v.findViewById(R.id.post_text);

						username.setText(fpl[position-1].getPostAuthor());
						date.setText(fpl[position-1].getPostDate());
						postText.loadDataWithBaseURL("", fpl[position-1].getPostHtml(), null, "UTF-8", "");
						l1.addView(v);
					}
					else{
						
						View v = getLayoutInflater().inflate(R.layout.page_navigation, null);
						
						ImageView prevPage = (ImageView) v.findViewById(R.id.post_prev_page);
						ImageView findPage = (ImageView) v.findViewById(R.id.post_find_page);
						ImageView nextPage = (ImageView) v.findViewById(R.id.post_next_page);
						TextView pageInfo = (TextView) v.findViewById(R.id.page_info);
						
						pageInfo.setText("page " + currentPage + " of " + threadNumOfPages);
						prevPage.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent goToPrevPage = new Intent(con, PostsActivity.class);
								goToPrevPage.putExtra("threadUrl", Helper.getUri(threadUri, 1)[0]);
								goToPrevPage.putExtra("threadName", threadName);
								goToPrevPage.putExtra("threadNumOfPages", threadNumOfPages);
								con.startActivity(goToPrevPage);
							}
						});			
						findPage.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								bda = new BaseDialogActivity(con, threadNumOfPages, cleanUri, threadName, 2);
								AlertDialog dialog = (AlertDialog) bda.onCreateDialog();
								dialog.show();
							}
						});
						nextPage.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent goToNextPage = new Intent(con, PostsActivity.class);
								goToNextPage.putExtra("threadUrl", Helper.getUri(threadUri, 2)[0]);
								goToNextPage.putExtra("threadName", threadName);
								goToNextPage.putExtra("threadNumOfPages", threadNumOfPages);
								con.startActivity(goToNextPage);
							}
						});
					
						l1.addView(v);
					}
				}
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
	