package com.example.fhr;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ForumThreadParser {

	private Document doc;
	private Elements  threadList;
	
	public ForumThreadParser(String url){
		try{
			doc = Jsoup.connect(url).get();
			Element th_list = doc.getElementById("threadslist");
			if (th_list == null){
				doc = Jsoup.connect(url + "&iframed=1#").timeout(10*1000).get();
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
		return threadList.size() - 2;
	}
	
	public ForumThread[] getThreadList(){
		ForumThread thList[] = new ForumThread[getCount()];
		int threadId = 0;
		String[] thId;
		Elements anchors, divTags, hrefs, lastPInfo, thAuthor;
		int counter = 0, i = 0;
		for (Element e : threadList){
			ForumThread thread = new ForumThread();
			if (counter >= 2){
				//get thread id
				thId = e.child(0).attr("id").split("_");
				if (!thId[2].equals("")){
					threadId = Integer.parseInt(thId[2]);
					thread.id = threadId;
				}
				
				//get thread url
				thread.threadUrl = "http://forum.hr/showthread.php?t=" + threadId;
				
				//get thread name
				hrefs = e.getElementsByTag("div").get(0).getElementsByTag("a");
				for (Element a : hrefs){
					if (a.attr("id").matches("thread_title_" + threadId)){
						thread.threadName = a.text();
						break;
					}
				}
				//get number of pages
				anchors = e.getElementsByTag("span").tagName("a");
				if (!anchors.select("[href^=showthread.php]").isEmpty())
					thread.numOfPages = Integer.parseInt(anchors.select("[href^=showthread.php]").last().attr("href").split("&page=")[1]);
				if (thread.numOfPages == 0)
					thread.numOfPages = 1;
				
				//get isTop thread
				divTags  = e.getElementsByTag("img").select("[alt=Top tema]");
				if (divTags.isEmpty()){
					thread.isTop = false;
				}
				else{
					thread.isTop = true;
				}
				
				//get information about last post
				divTags  = e.getElementsByTag("div").select("[class=smallfont]");
				if (!divTags.isEmpty()){
					lastPInfo = divTags.select("[style=text-align:right; white-space:nowrap]");
					if (!lastPInfo.isEmpty())
						thread.lastPostInfo = lastPInfo.get(0).text();
					thAuthor = divTags.select("[style=cursor:pointer]"); 
					if (!thAuthor.isEmpty())
						thread.threadAuthor = thAuthor.get(0).text();
				}	
				
				thList[i] = thread;
				i += 1;
			}
			counter += 1;
		}
		return thList;
	}
}
