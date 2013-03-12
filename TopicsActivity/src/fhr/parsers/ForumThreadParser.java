package fhr.parsers;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fhr.entities.ForumThread;

public class ForumThreadParser {

	private Document doc;
	private Elements  threadList;
	private int c = 1;
	
	public ForumThreadParser(String url){
		try{
			doc = Jsoup.connect(url).get();
			Element th_list = doc.getElementById("threadslist");
			if (th_list == null){
				doc = Jsoup.connect(url + "&iframed=1#").timeout(15*1000).get();
				threadList = doc.getElementById("threadslist").getElementsByTag("tr");
			}
			else{
				threadList = th_list.getElementsByTag("tr");
			}
		}
		catch(IOException e){
			System.out.println("An error has occured");
		}
	}
	
	public int getCount(){
		return threadList.size() - c;
	}
	
	public ForumThread[] getThreadList(){
		
		int threadId = 0;
		String[] thId;
		Elements anchors, divTags, hrefs, lastPInfo, thAuthor;
		int counter = 0, i = 0;
		
		//get number of pages in topic
		ForumThread.TopicNumOfPages = doc.select("div[class=pagenav]").select("td[class=vbmenu_control]").get(0).text().split("od ")[1];
		
		for (Element e2: threadList){
			if (!e2.select("img[alt=Najava/obavijest]").isEmpty())
				c += 1;
			else
				break;
		}
		
		ForumThread thList[] = new ForumThread[getCount()];
		
		for (Element e : threadList){
			ForumThread thread = new ForumThread();
			//first element is not a thread
			if (counter > c){
				//get thread id
				thId = e.child(0).attr("id").split("_");
				if (!thId[2].equals("")){
					threadId = Integer.parseInt(thId[2]);
					thread.setId(threadId);
				}
				
				//get thread url
				thread.setThreadUrl("http://forum.hr/showthread.php?t=" + threadId);
				
				//get thread name
				hrefs = e.getElementsByTag("div").get(0).getElementsByTag("a");
				for (Element a : hrefs){
					if (a.attr("id").matches("thread_title_" + threadId)){
						thread.setThreadName(a.text());
						break;
					}
				}
				//get number of pages
				anchors = e.getElementsByTag("span").tagName("a");
				if (!anchors.select("[href^=showthread.php]").isEmpty())
					thread.setNumOfPages(Integer.parseInt(anchors.select("[href^=showthread.php]").last().attr("href").split("&page=")[1]));
				if (thread.getNumOfPages() == 0)
					thread.setNumOfPages(1);
				
				//get isTop thread
				divTags  = e.getElementsByTag("img").select("[alt=Top tema]");
				if (divTags.isEmpty()){
					thread.setTop(false);
				}
				else{
					thread.setTop(true);
				}
				
				//get information about last post
				divTags  = e.getElementsByTag("div").select("[class=smallfont]");
				if (!divTags.isEmpty()){
					lastPInfo = divTags.select("[style=text-align:right; white-space:nowrap]");
					if (!lastPInfo.isEmpty())
						thread.setLastPostInfo(lastPInfo.get(0).text());
					thAuthor = divTags.select("[style=cursor:pointer]"); 
					if (!thAuthor.isEmpty())
						thread.setThreadAuthor(thAuthor.get(0).text());
					else{
						thread.setThreadAuthor(divTags.select("div[class=smallfont]").get(0).text());
					}
				}	
				
				thList[i] = thread;
				i += 1;
			}
			counter += 1;
		}
		return thList;
	}
}
