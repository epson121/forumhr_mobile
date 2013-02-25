package com.example.fhr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BaseDialogActivity extends Activity {

	HashMap<String, String> hm;
	String[] urls;
	Context con;
	
	public BaseDialogActivity(Context c) {
		con = c;
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
	protected Dialog onCreateDialog(int id, ForumTopic ft, final int maxNumPages){
		if (ft != null)
			hm = ft.subTopics;
		AlertDialog.Builder builder = new AlertDialog.Builder(con);

		switch(id){
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
					int pn = Integer.parseInt(pNum.getText().toString());
					if (pn > maxNumPages || pn < 1){
						Toast.makeText(con, "Nonexisting page. Try again.", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(con, "Going to page: " + pn, Toast.LENGTH_SHORT).show();
					}
				}
			})
			.setCancelable(true);
			return builder.create();
		case 3:
			
			
			
		default:
			return null;	
		}
		
	}
	
}
