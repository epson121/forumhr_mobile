
package com.example.fhr;

import java.util.HashMap;

public class ForumTopic {
	
	protected String name;
	protected String uri;
	protected String description;
	protected HashMap<String, String> subTopics = new HashMap<String, String>();
	
	//add isFavorite
	
	public ForumTopic(){
	}
	
}
