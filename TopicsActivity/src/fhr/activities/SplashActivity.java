package fhr.activities;

import com.example.fhr.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity{

	  public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_splash);
	        
	        final Handler noInternetHandler = new Handler();
	        final Runnable showMessage = new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "Internet connection not available.", Toast.LENGTH_SHORT).show();
				}
			};
	        
	        // Delayed login activity
	        Thread timer = new Thread(){
	        	public void run()
	        	{
	        		try
	        		{
	        			int timer = 0;
	        			while (timer < 1500)
	        			{
	        				sleep(100);
	        				timer += 100;
	        			}
	        			if (Helper.isNetworkAvailable(SplashActivity.this)){
	        				Intent topicsActivity = new Intent(getApplicationContext(), TopicsActivity.class);
		    				startActivity(topicsActivity);
	        			}
	        			else{
	        				noInternetHandler.post(showMessage);
	        			}
	        		} 
	        		catch (InterruptedException e){
					}
	        		finally
	        		{
	        			finish();
	        		}
	        	}
	        };
	        timer.start();
	    }
	  
	 
}
