package fhr.parsers;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fhr.entities.ForumUser;

public class ForumUserParser {

	private Document doc;
	private Element  userMiniStats;
	private Element mainUserInfo;
	
	public ForumUserParser(String url) throws IOException{
		//String url = "http://www.forum.hr/member.php?u=271568";
		doc = Jsoup.connect(url).timeout(15 * 1000).get();
		userMiniStats = doc.getElementById("stats_mini").select("[class=smallfont list_no_decoration profilefield_list]").get(0);
		mainUserInfo = doc.getElementById("main_userinfo");
	}
	


	public ForumUser getUserData(){
		
		ForumUser fUser = new ForumUser();
		HashMap<String, String> userDefinedInfo = new HashMap<String, String>();
		Element temp = mainUserInfo.select("h1").get(0);
		
		System.out.println(temp.text());
		if (!temp.text().equals("")){
			fUser.setUserName(temp.text());
		}
		
		fUser.setAvatarUri("www.forum.hr/" + doc.getElementById("user_avatar").attr("src"));
		
		temp = mainUserInfo.select("div[id=last_online]").get(0);
		
		if (temp.text() != null){
			fUser.setUserLastActivity(temp.text());
		}
		
		int i = 0;
		for (Element el : userMiniStats.children()){
			//check only evens for keys (on odds are values)
			if (i % 2 == 0){
				temp = userMiniStats.children().get(i);
				if (!temp.text().equals(""))
					userDefinedInfo.put(temp.text(), userMiniStats.children().get(i+1).text());
			}
			i += 1;
		}

		fUser.setUserDefinedInfo(userDefinedInfo);
		/*
		System.out.println("USername: " + fUser.getUserName());
		System.out.println("Last activity: " + fUser.getUserLastActivity());
		System.out.println("Other info: " + fUser.getUserDefinedInfo());
		System.out.println("Avatar URI: " + fUser.getAvatarUri()); 
		*/
		return fUser;
	}
	
	
}
