package fhr.activities;


import com.example.fhr.R;

import fhr.entities.ForumUser;
import fhr.parsers.ForumUserParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity{
	
	private String userUsername;
	private String userUri;
	private ForumUserParser fup;
	private ForumUser userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
		Bundle b = getIntent().getExtras();
		userUsername = b.getString("userUsername");
		userUri = b.getString("userUri");
		
		if (Helper.isNetworkAvailable(UserInfoActivity.this)){
			new Async(UserInfoActivity.this).execute(userUri);	
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
				fup = new ForumUserParser(params[0]);
				userInfo = fup.getUserData();
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
				
				LinearLayout l1 = (LinearLayout) findViewById(R.id.user_info);
				
				TextView txtUsername = (TextView) findViewById(R.id.info_username_text);
				ImageView imgAvatar = (ImageView) findViewById(R.id.info_avatar_image);
				
				txtUsername.setText(userUsername);
				
				View v = getLayoutInflater().inflate(R.layout.user_info_row, null);
				
				TextView info_key = (TextView) v.findViewById(R.id.info_key);
				TextView info_value = (TextView) v.findViewById(R.id.info_value);
				
				l1.addView(v);
				
			}
		}
	}
	

}
