package fhr.dialogs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fhr.activities.ThreadsActivity;
import fhr.entities.ForumTopic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ThreadSubtopicsDialog extends Activity{
	
	private HashMap<String, String> hm;
	private AlertDialog.Builder builder;
	private Context con;
	private String[] urls;
	
	public ThreadSubtopicsDialog(Context c, ForumTopic ft) {
		con = c;
		if (ft != null)
			hm = ft.getSubTopics();
	
	}
	
	// creates two string arrays (keys and urls) from hashmap
	@SuppressWarnings("rawtypes")
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
	
	public AlertDialog createDialog(){
		builder = new Builder(con);
		
		builder
        .setTitle("Subtopics:")
        .setItems(createThreadDialogList(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = urls[which];
				Intent threadsActivity = new Intent(con, ThreadsActivity.class);
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
	}

}
