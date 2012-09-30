package feedDownload;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;

public class DownloadTask implements Callable<String> {
	private final String url;
	private final String filename;
	private final String destination;
	
	public DownloadTask(String url, String destinationFolder) {
		this(url, getFilename(url), destinationFolder);
	}
	
	public DownloadTask(String url, String filename, String destinationFolder) {
		this.url = url;
		this.filename = filename;
		this.destination = destinationFolder;
	}
	
	public static String getFilename(String url) {
		int slash = url.lastIndexOf("/");
		return url.substring(slash+1);
	}

	public String call() {
		// TODO Auto-generated method stub
		System.out.println("Starting to download "+this.url);
		File dir = new File(this.destination); dir.mkdirs();
		File dest = new File(this.destination, this.filename);
		try {
			FileUtils.copyURLToFile(new URL(this.url), dest);
		} catch (MalformedURLException e) {
			return "Failed to download "+this.url+" - failed to fetch url.";
		} catch (IOException e) {
			return "Failed to download "+this.url+" - failed to write file.";
		}
		return "Done downloading "+this.url;
	}
}