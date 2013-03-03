package fhr.activities;

import fhr.dialogs.BaseDialogActivity;
import fhr.entities.ForumThread;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;

public class Helper implements Runnable{
	
	private Context c;
	private int reloadId;
	private String[] reloadData;
	private BaseDialogActivity bda;
	
	public Helper(Context con, int reloadId, String[] reloadData){
		c = con;
		this.reloadData = reloadData;
		this.reloadId = reloadId;
	}
	 
	@Override
	public void run() {
		bda = new BaseDialogActivity(c, reloadId, reloadData);
		AlertDialog adialog = (AlertDialog) bda.onCreateDialog();
		adialog.show(); 
	} 
	 
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() ||
			connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
				return true;
		}	
		return false;
	}
	
	public static String[] getUri(String url, int task){
		String uri;
		int currentPage;
		if (url.contains("&page=")){
			uri = url.split("&page=")[0];
			currentPage = Integer.parseInt(url.split("&page=")[1]);
		}
		else{
			uri = url;
			currentPage = 1;
		}
		switch (task) {
			case 1:
				int prevPage = currentPage - 1;
				if (currentPage == 1){
					return new String[]{uri, "1"};
				}
				else{
					return new String[]{uri + "&page=" + prevPage, currentPage + ""};
				}
			case 2:
				int nextPage = currentPage + 1;
				if (nextPage > Integer.parseInt(ForumThread.TopicNumOfPages)){
					return new String[]{uri, currentPage + ""};
				}
				else{
					return new String[] {uri + "&page=" + nextPage, currentPage + ""};
				}
			case 3:
				return new String[] {uri, currentPage + ""};
			default:
				return new String[]{"", ""};
		}
	}	
	

	
}
