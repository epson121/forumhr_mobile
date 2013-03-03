package fhr.dialogs;

import fhr.activities.PostsActivity;
import fhr.activities.ThreadsActivity;
import fhr.activities.TopicsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class ErrorDialog extends Activity{
	
	private AlertDialog.Builder builder;
	private Context con;
	private int reloadId;
	private String[] reloadData;
	
	public ErrorDialog(Context c, int reloadId, String[] reloadData){
		con = c;
		this.reloadId = reloadId;
		this.reloadData = reloadData; 
	}
	
	public AlertDialog createDialog(){
		builder = new Builder(con);
		
		builder
        .setTitle("An error has occured..")
        .setCancelable(true)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        })
        .setPositiveButton("Reload", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent reloadPage;
				/*
				 * 1. reload Topic
				 * 2. reload Thread
				 * 3. reload Post
				 */
				switch(reloadId){
					case 1:
						Log.d("APP", con.toString());
						reloadPage = new Intent(con, TopicsActivity.class);
						con.startActivity(reloadPage);
						break;
					case 2:
						reloadPage = new Intent(con, ThreadsActivity.class);
						reloadPage.putExtra("topicUrl", reloadData[0]);
						reloadPage.putExtra("topicName", reloadData[1]);	
						con.startActivity(reloadPage);
						break;
					case 3:
						reloadPage = new Intent(con, PostsActivity.class);
						reloadPage.putExtra("threadUrl", reloadData[0]);
						reloadPage.putExtra("threadName", reloadData[1]);
						reloadPage.putExtra("threadNumOfPages", reloadData[2]);
						con.startActivity(reloadPage);
					default:
						return;
				}
			}
		})
		.setCancelable(true);
		return builder.create();
	}

}
