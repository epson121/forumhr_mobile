package fhr.dialogs;

import com.example.fhr.R;

import fhr.activities.PostsActivity;
import fhr.activities.ThreadsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GoToPageDialog extends Activity{
	
	private AlertDialog.Builder builder;
	private Context con;
	
	private int maxNumPages, threadOrPost;
	private String Url, name;

	public GoToPageDialog(Context c, int maxNumPages, String Url, String Name, int threadOrPost) {
		this.con = c;
		this.maxNumPages = maxNumPages;
		this.Url = Url;
		this.name = Name;
		this.threadOrPost = threadOrPost;
	}
	
	public AlertDialog createDialog(){
		builder = new Builder(con);
		
		LayoutInflater inflater = ((Activity) con).getLayoutInflater();
		View goToPage = inflater.inflate(R.layout.dialog_go_to_page, null);

        final EditText pNum = (EditText) goToPage.findViewById(R.id.page_number);
		builder
		.setTitle("Go to page (1 - " + maxNumPages + ") :")
		.setView(goToPage)
		.setPositiveButton("Go", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String txt = pNum.getText().toString();
				if (!txt.equals("")){
					int pageNum = Integer.parseInt(txt);
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
	}

}
