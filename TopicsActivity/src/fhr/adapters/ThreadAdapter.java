package fhr.adapters;

import com.example.fhr.R;

import fhr.activities.Helper;
import fhr.activities.PostsActivity;
import fhr.activities.ThreadsActivity;
import fhr.dialogs.BaseDialogActivity;
import fhr.entities.ForumThread;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ThreadAdapter extends BaseAdapter {
	
	private Context con;
	private ForumThread[] fth;
	private BaseDialogActivity bda;
	
	private String topicName, topicUrl;
	private int currentPage, count = 0;
	
	public ThreadAdapter(Context c, int count, ForumThread[] fth, String topicName, String topicUrl, int currentPage){
		this.con = c;
		this.count = count;
		this.fth = fth;
		this.topicName = topicName;
		this.topicUrl = topicUrl;
		this.currentPage = currentPage;
	}

	@Override
	public int getCount() {
		return count;   
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
		Log.d("APP", "POSITION: " + position);
		Log.d("APP", "COUNT: " + getCount());
       	if (position == getCount()-1){
				return 0;
		}
       	else{
       		if (fth[position].getNumOfPages() == 1)
       			return 1;
       		else
       			return 2;
       	}
    }
	// Count of different layouts
	@Override
    public int getViewTypeCount() {
		return 3; 
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final int pos = position;
		Log.d("APP", position + "");
		if (v == null){
			LayoutInflater li = LayoutInflater.from(con);
			switch (getItemViewType(position)) {
			case 0:
				v = li.inflate(R.layout.page_navigation, null);
				break;
			case 1:
				v = li.inflate(R.layout.threads_list_row_1_page, null);
				break;
			case 2:
				v = li.inflate(R.layout.threads_list_row, null);
				break;
			default:
				break;
			}
		}
		
		if (position < getCount() - 1){
			TextView threadName = (TextView) v.findViewById(R.id.thread_name);
			TextView threadAuthor = (TextView) v.findViewById(R.id.thread_author);
			TextView threadLastPostInfo = (TextView) v.findViewById(R.id.thread_last_post_info);
			ImageButton goToPage = (ImageButton) v.findViewById(R.id.thread_go_to_page);
			
			if (fth[position].isTop())
				threadName.setText("TOP " + fth[position].getThreadName());
			else
				threadName.setText(fth[position].getThreadName());
			
			threadAuthor.setText("By: " + fth[position].getThreadAuthor());
			threadLastPostInfo.setText("Last post: " + fth[position].getLastPostInfo());
			
			threadName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent postActivity = new Intent(con, PostsActivity.class);
					postActivity.putExtra("threadUrl", fth[pos].getThreadUrl());
					postActivity.putExtra("threadName", fth[pos].getThreadName());
					postActivity.putExtra("threadNumOfPages", fth[pos].getNumOfPages());
					con.startActivity(postActivity);
				}
			});
			if (getItemViewType(position) == 2){
				goToPage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							bda = new BaseDialogActivity(con, fth[pos].getNumOfPages(), fth[pos].getThreadUrl(), topicName, 2);
							AlertDialog dialog = (AlertDialog) bda.onCreateDialog();
							dialog.show();
						}
				});
			}
		}
		else{
			ImageView prevPage = (ImageView) v.findViewById(R.id.post_prev_page);
			ImageView findPage = (ImageView) v.findViewById(R.id.post_find_page);
			ImageView nextPage = (ImageView) v.findViewById(R.id.post_next_page);
			TextView  pageInfo = (TextView)  v.findViewById(R.id.page_info);
			
			pageInfo.setText("page " + currentPage + " of " + ForumThread.TopicNumOfPages);
			
			prevPage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent goToPrevPage = new Intent(con, ThreadsActivity.class);
					goToPrevPage.putExtra("topicUrl", Helper.getUri(topicUrl, 1)[0]);
					goToPrevPage.putExtra("topicName", topicName);
					con.startActivity(goToPrevPage);
				}
			});
			
			findPage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bda = new BaseDialogActivity(
						  con, Integer.parseInt(ForumThread.TopicNumOfPages),
						  Helper.getUri(topicUrl, 3)[0], topicName, 1 
						  );
					AlertDialog dialog = (AlertDialog) bda.onCreateDialog();
					dialog.show();
				}
			});
			
			nextPage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent goToNextPage = new Intent(con, ThreadsActivity.class);
					goToNextPage.putExtra("topicUrl", Helper.getUri(topicUrl, 2)[0]);
					goToNextPage.putExtra("topicName", topicName);
					con.startActivity(goToNextPage);
				}
			});
		}
		return v;
	}
}
