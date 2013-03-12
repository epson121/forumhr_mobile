package fhr.entities;

import java.util.HashMap;

public class ForumUser {

	// all users have them
	private String userName;
	private String userLastActivity;
	private String avatarUri;
	//some may have different things
	private HashMap<String, String> userDefinedInfo;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLastActivity() {
		return userLastActivity;
	}

	public void setUserLastActivity(String userLastActivity) {
		this.userLastActivity = userLastActivity;
	}
	
	public String getAvatarUri() {
		return avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}

	public HashMap<String, String> getUserDefinedInfo() {
		return userDefinedInfo;
	}

	public void setUserDefinedInfo(HashMap<String, String> userDefinedInfo) {
		this.userDefinedInfo = userDefinedInfo;
	}

	
	
}
