package fhr.adapters;

import fhr.R;
import fhr.dialogs.BaseDialogActivity;
import fhr.entities.ForumTopic;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAdapter extends BaseAdapter{
	
	private Context con;
	private int count;
	private ForumTopic[] forumTopicList;
	private BaseDialogActivity bda;
	
	public TopicAdapter(Context c, int count, ForumTopic[] ftl){
		this.con = c;
		this.count = count;
		this.forumTopicList = ftl;
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
	
	@Override
    public int getItemViewType(int position) {
    	if (forumTopicList[position].getSubTopics().isEmpty()){
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
		final int pos = position;
		View v = convertView;
		LayoutInflater li;
		if (convertView == null){
			if (getItemViewType(position) == 0){
				li = LayoutInflater.from(con);
				v = li.inflate(R.layout.topics_list_row_no_subtopics, null);
			}
			else{
				li = LayoutInflater.from(con);
				v = li.inflate(R.layout.topics_list_row_with_subtopics, null);
			}
		}
		
		TextView tName = (TextView) v.findViewById(R.id.topic_name);
		TextView tDesc = (TextView) v.findViewById(R.id.topic_description);
		ImageView subImage = (ImageView) v.findViewById(R.id.topic_options);
		
		tName.setText(forumTopicList[position].getName().replace("&amp;", "&"));
		tDesc.setText(forumTopicList[position].getDescription());	

		tName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					Intent threadsActivity = new Intent("android.intent.action.FHR.THREAD.ACTIVITY");
					threadsActivity.putExtra("topicUrl", forumTopicList[pos].getUri());
					threadsActivity.putExtra("topicName", forumTopicList[pos].getName());	
					con.startActivity(threadsActivity);
			}
		});
		
		if (getItemViewType(position) == 1){
			subImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {	
					bda = new BaseDialogActivity(con, forumTopicList[pos]);
					AlertDialog adialog = (AlertDialog) bda.onCreateDialog();
					adialog.show(); 
				}
			});
		}
		return v;
	}

}
