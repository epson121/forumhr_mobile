
package fhr.entities;

import java.util.HashMap;

public class ForumTopic {
	
	private String name;
	private String uri;
	private String description;
	private HashMap<String, String> subTopics = new HashMap<String, String>();
	
	//add isFavorite
	
	public ForumTopic(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<String, String> getSubTopics() {
		return subTopics;
	}

	public void setSubTopics(HashMap<String, String> subTopics) {
		this.subTopics = subTopics;
	}
	
}
