package fhr.entities;

public class ForumPost {

	private String postAuthor;
	private String postAuthorUri;
	private String postDate;
	private String postText;
	private String postHtml;
	private String postAuthorAvatarPath;

	public String getPostAuthor() {
		return postAuthor;
	}
	public void setPostAuthor(String postAuthor) {
		this.postAuthor = postAuthor;
	}
	
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	
	public String getPostText() {
		return postText;
	}
	public void setPostText(String postText) {
		this.postText = postText;
	}
	
	public String getPostHtml() {
		return postHtml;
	}
	public void setPostHtml(String postHtml) {
		this.postHtml = postHtml;
	}
	
	public String getPostAuthorAvatarPath() {
		return postAuthorAvatarPath;
	}
	public void setPostAuthorAvatarPath(String postAuthorAvatarPath) {
		this.postAuthorAvatarPath = postAuthorAvatarPath;
	}
	
	public String getPostAuthorUri() {
		return postAuthorUri;
	}
	public void setPostAuthorUri(String postAuthorUri) {
		this.postAuthorUri = postAuthorUri;
	}

	
}
