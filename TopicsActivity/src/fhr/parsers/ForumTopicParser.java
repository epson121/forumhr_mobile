package fhr.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fhr.entities.ForumTopic;

import android.util.Log;


public class ForumTopicParser{

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public Document doc = null;
	public Elements  content = null;
	
	public ForumTopicParser() throws IOException{
		doc = Jsoup.connect("http://www.forum.hr").timeout(15*1000).get();
		content = doc.getElementsByClass("alt1active");                     
	}
	
	
	public int getCount(){
		return content.size();
	}
	
	public ForumTopic[] getTopicList() throws UnsupportedEncodingException{
		
		ForumTopic[] topics = new ForumTopic[getCount()];
		Elements topicNames, topicUrls;
		String link = "http://www.forum.hr/";
		int i = 0;
		for (Element elem : content){
			
			ForumTopic topic = new ForumTopic();
			
			//get topic name
			topicNames = elem.getElementsByTag("strong");
			topic.setName(topicNames.get(0).unwrap().toString());
			Log.d("APP", topic.getName() );
			
			//get topic url
			topicUrls = elem.child(0).getElementsByAttribute("href");
        	topic.setUri(link + topicUrls.get(0).attr("href"));
        	
        	//get topic description
        	Elements descriptions = elem.getElementsByClass("smallfont");
        	String text = descriptions.text();
        	String desc = "";
        	if (text.contains("Podforum:")){
        		desc = text.split("Podforum:")[0];
        	}
        	else if (text.contains("Podforum")){
        		desc = text.split("Podforum")[0];
        	}
        	else if(text.contains("Podforumi:")){
        		desc = text.split("Podforumi:")[0];
        	}
        	else{
        		desc = text;
        	}
        	
    		topic.setDescription(desc);
        	
    		
			//get subtopics list
			for (Element el : descriptions){
				Elements subTopics = el.getElementsByTag("a");
				//System.out.println("El: " + el.toString());
				for (Element e : subTopics){
					String n = "";
					String url = "";
					Elements name = e.getElementsByTag("b");
					if (name.size() == 0){
						n = e.ownText();
					}
					else{
						n = name.get(0).unwrap().toString();
					}
					url = e.getElementsByAttribute("href").get(0).attr("href");
					topic.getSubTopics().put(n, url);
				}
			}
    	topics[i] = topic;
    	i++;
		}
		return topics;
	}
}
