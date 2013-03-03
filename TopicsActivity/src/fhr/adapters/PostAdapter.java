package fhr.adapters;

import fhr.R;
import fhr.activities.Helper;
import fhr.activities.PostsActivity;
import fhr.dialogs.BaseDialogActivity;
import fhr.entities.ForumPost;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {

	private Context con;
	private int counter;
	private ForumPost[] fpl;
	private BaseDialogActivity bda;
	private String threadName, threadUri, cleanUri;
	private int currentPage, threadNumOfPages;
	
	public PostAdapter(Context c, int counter, ForumPost[] fpl, String threadName, String threadUri,
					   						   int threadNumOfPages, int currentPage, String cleanUri){
		this.con = c;
		this.counter = counter;
		this.fpl = fpl;
		this.threadName = threadName;
		this.threadUri = threadUri;
		this.threadNumOfPages = threadNumOfPages;
		this.currentPage = currentPage;
		this.cleanUri = cleanUri;
	}
	
	@Override
	public int getCount() {
		return counter + 2;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public int getItemViewType(int position) {
    	if (position == 0 || position == getCount()-1){
    		return 0;
		}
    	else{
    		return 1;
    	}
    }

	// Count of different layouts
    @Override
    public int getViewTypeCount() {
    	return 2; 
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (convertView == null){
			LayoutInflater li;
			if (getItemViewType(position) == 0){
				li = LayoutInflater.from(con);
				v = li.inflate(R.layout.page_navigation, parent, false);
			}
			else{
				li = LayoutInflater.from(con);
				v = li.inflate(R.layout.post_list_row, parent, false);
			}
		}
		
		if (position > 0 && position < getCount()-1){
			TextView username = (TextView) v.findViewById(R.id.post_username_text);
			TextView date = (TextView) v.findViewById(R.id.post_date_text);
			WebView postText = (WebView) v.findViewById(R.id.post_text);

			username.setText(fpl[position-1].getPostAuthor());
			date.setText(fpl[position-1].getPostDate());
			postText.loadDataWithBaseURL("", fpl[position-1].getPostHtml(), null, "UTF-8", "");

		}
		else{
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
		}
		return v;
	}

}
