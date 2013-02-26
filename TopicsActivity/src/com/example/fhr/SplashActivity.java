package com.example.fhr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity{

	
	  public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_splash);
	        
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
	        			Intent topicsActivity = new Intent(getApplicationContext(), TopicsActivity.class);
	    				startActivity(topicsActivity);
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
