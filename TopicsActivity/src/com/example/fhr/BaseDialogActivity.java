package com.example.fhr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BaseDialogActivity extends Activity {

	private HashMap<String, String> hm;
	private String[] urls;
	private Context con;
	private int id;
	private int pageNum;
	private int maxNumPages;
	private String Url;
	private String name;
	private int threadOrPost;
	private int reloadId;
	private String[] reloadData;
	
	public BaseDialogActivity(Context c, int id, ForumTopic ft) {
		con = c;
		this.id = id;
		if (ft != null)
			hm = ft.subTopics;
	}
	
	//(context, id of dialog (look below), maximum number of pages in thread/post, urlOfThread/post
	// name of thread/post, thread or post (1 | 2)
	public BaseDialogActivity(Context c, int id, int maxNumPages, String Url, String Name, int threadOrPost) {
		con = c;
		this.id = id;
		this.maxNumPages = maxNumPages;
		this.Url = Url;
		this.name = Name;
		this.threadOrPost = threadOrPost;
	}
	
	/*
	 * context, id of dialog, activityId stuff to reload (thread, topic, post)
	 */
	public BaseDialogActivity(Context c, int id, int reloadId, String[] reloadData){
		con = c;
		this.id = id;
		this.reloadId = reloadId;
		this.reloadData = reloadData; 
	}
	
	private String[] createThreadDialogList(){
		String[] keys = null;
		if (!hm.isEmpty()){
			keys = new String[hm.size()];
			urls = new String[hm.size()];
			Iterator it = hm.entrySet().iterator();
			int i = 0;
			while (it.hasNext()){
				Map.Entry pairs = (Map.Entry) it.next();
				keys[i] = (String) pairs.getKey();
				urls[i] = (String) pairs.getValue();
				i += 1;
			}
		}
		return keys;
	}
	
	
	AlertDialog alertDialog;
	
	/*
	 * 1 - Thread subtopics dialog
	 * 2 - Topic "go to page" dialog
	 * 3 - "An error has occured dialog"
	 */
	protected Dialog onCreateDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(con);

		switch(this.id){
		case 1:
			builder
            .setTitle("Subtopics:")
            .setItems(createThreadDialogList(), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String url = urls[which];
					Intent threadsActivity = new Intent("android.intent.action.FHR.THREAD.ACTIVITY");
					threadsActivity.putExtra("topicUrl", url);
					threadsActivity.putExtra("topicName", createThreadDialogList()[which]);	
					con.startActivity(threadsActivity);
				}
			})
            .setCancelable(true)
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
			return builder.create();
		
		case 2:
			LayoutInflater inflater = ((Activity) con).getLayoutInflater();
			
			View addNumber = inflater.inflate(R.layout.dialog_go_to_page, null);

	        final EditText pNum = (EditText) addNumber.findViewById(R.id.page_number);
			builder
			.setTitle("Go to page (1 - " + maxNumPages + ") :")
			.setView(addNumber)
			.setPositiveButton("Go", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//check for exceptions !!!
					String txt = pNum.getText().toString();
					if (!txt.equals("")){
						pageNum = Integer.parseInt(txt);
						if (pageNum > maxNumPages || pageNum < 1){
							Toast.makeText(con, "Nonexisting page. Try again.", Toast.LENGTH_SHORT).show();
						}
						else{
							if (threadOrPost == 2){
								Intent goToPage = new Intent(con, PostsActivity.class);
								goToPage.putExtra("threadUrl", Url + "&page=" + pageNum);
								goToPage.putExtra("threadName", name);	
								goToPage.putExtra("threadNumOfPages", maxNumPages);
								con.startActivity(goToPage);
							}
							else{
								Intent goToPage = new Intent(con, ThreadsActivity.class);
								goToPage.putExtra("topicUrl", Url + "&page=" + pageNum);
								goToPage.putExtra("topicName", name);	
								con.startActivity(goToPage);
							}
						}
					}
					else{
						//do nothing
					}
				}
			})
			.setCancelable(true);
			return builder.create();
		case 3:
			
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
					Log.d("APP","RELOAD ID: " + reloadId);
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
		default:
			return null;	
		}
		
	}
	
}
