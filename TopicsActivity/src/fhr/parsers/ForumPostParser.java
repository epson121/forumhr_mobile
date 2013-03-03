package fhr.parsers;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fhr.entities.ForumPost;



public class ForumPostParser {

	private Document doc;
	private Elements  postList;
	
	//catch socket timeout connection
	
	public ForumPostParser(String threadUri) throws IOException{
		doc = Jsoup.connect(threadUri).get();
		//postList = doc.getElementById("posts").select("table[id~=post[0-9]+");
		Element th_list = doc.getElementById("posts");
		if (th_list == null){
			threadUri = threadUri.replace("show.hr/forum", "forum.hr");
			doc = Jsoup.connect(threadUri + "&iframed=1#").timeout(15*1000).get();
			postList = doc.getElementById("posts").select("div[id~=edit[0-9]+");
		}
		else{
			postList = th_list.select("div[id~=edit[0-9]+");
		}
		
	}
		
	public int getSize(){
		return postList.size();
	}
	
	public ForumPost[] getPostList(){
		
		ForumPost fp;
		ForumPost[] fpList = new ForumPost[getSize()];
		int counter = 0;
		
		for(Element el : postList){
			Element table = el.select("table[id~=post[0-9]+]").first();
			Iterator<Element> ite = table.select("tr").iterator();
			fp = new ForumPost();
			
			//first <tr> is the date
			//time of the post
			fp.setPostDate(ite.next().text().split("#")[0]);
			
			//get part of the post where name and text are located (<tr>)
			Elements secondTR = ite.next().getElementsByTag("td");
			
			//get post authors name
			//some can be empty
			String authorName = secondTR.select("a[class=bigusername]").text();
			if (!authorName.equals(null))
				fp.setPostAuthor(authorName);
			else
				fp.setPostAuthor(" ");
			
			//get author avatar url 
			String avatarUrl = secondTR.select("div[class=smallfont]").select("img").attr("src");
			if (avatarUrl.equals(null))
				fp.setPostAuthorAvatarPath("");
			else
				fp.setPostAuthorAvatarPath(avatarUrl);
			
			//get post text in form like this:
			/*
			 * <quote> Quoted text goes here </quote> Unquoted text goes here <quote> ... </quote> ..
			 * or
			 * <quote> quoted text 1 </quote> <quote> quoted text 2 </quote>
			 * or 
			 * plain text, no quotes
			 * 
			 */
			Element a = secondTR.select("[id~=post_message_[0-9]+]").first();
			String wholeText = a.text(); 
			Elements quotes = a.select("table");
			for (Element elem : quotes){
				if (wholeText.contains(elem.text())){
					wholeText = wholeText.replace(elem.text(), "<quote>" + elem.text() + "</quote>").replace("Quote:", "");
				}
			}
		
			//probat parse sa regexom (?:<quote>.+</quote>.+)*
			fp.setPostText(wholeText);
			
			//get HTML
			String wholeHtml = a.html();
			fp.setPostHtml(wholeHtml);
		
			fpList[counter] = fp;
			counter += 1;
		}
		
		return fpList;
	}
}
