package feedDownload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import xmlparse.*;

public class Solution {
	private final static String feedURL = 
			"http://feeds.feedburner.com/r1_digitund?format=xml";
	// By default, download into current folder.
	private final static String targetFolder = ".";
	private final static int POOLSIZE = 3;
	
	public static ArrayList<String> getMp3List(String url) throws MalformedURLException, SAXException, IOException {
		DomFilter df = new DomFilter("enclosure","url");
		Document dom = XMLUtils.getDomFromUrl(url);
		return df.parse(dom);
	}
	
	public static void main(String[] args) throws Exception {
		ArrayList<String> mp3s = getMp3List(feedURL);
		
		ExecutorService threadPool = Executors.newFixedThreadPool(POOLSIZE);
		CompletionService<String> taskCompletionService = 
				new ExecutorCompletionService<String>(threadPool);
		
		for (String url: mp3s)
			taskCompletionService.submit(new DownloadTask(url, targetFolder));
		
		Future<String> completed;
		int tasksDone = 0;
		while (tasksDone < mp3s.size()) {
			completed = taskCompletionService.take();
			// This could be done using a wrapper and proper signalling, but since
			// I won't be extending this (fingers crossed) - no need to.
			System.out.println(++tasksDone + " reports: "+completed.get());
		}
		threadPool.shutdownNow();
		System.out.println("Done, exiting.");
	}
}