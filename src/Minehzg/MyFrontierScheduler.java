package Minehzg;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.postprocessor.FrontierScheduler;

public class MyFrontierScheduler extends FrontierScheduler {
	private static final long serialVersionUID = 1l;

	public MyFrontierScheduler(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		if (uri.contains("taobao.com") || uri.contains("tmall.com")) {
			System.out.println("提交链接" + uri);
			if (uri.contains("/item.htm?")) {
				try {
					startDownload(uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			getController().getFrontier().schedule(caUri);
		}
	}

	public void startDownload(String HtmlUrl) throws IOException {

		String htmlContent = getHtmlSource(HtmlUrl);
		int count = 1;
		Pattern pTitle = Pattern.compile("(?<=<title>).*(?=</title>)");
		Matcher mtitle = pTitle.matcher(htmlContent);
		String title = "sdfhksdfj"+new Random().nextInt();
		if (mtitle.find())
			title = mtitle.group().trim();
		Pattern pjpg = Pattern
				.compile("(?<=data-src=\")[a-zA-Z0-9.:/_!-]*(?=\")");
		Matcher mjpg = pjpg.matcher(htmlContent);
		File director = new File("E:\\Taobao\\"+title+"\\");		
		if(!director.exists())
			director.mkdirs();
		OutputStream os = null;
		InputStream is = null;		
		while (mjpg.find()) {
			URL url = new URL(mjpg.group().trim().replaceAll("[0-9]+x[0-9]+", "400x400"));
			File outFile = new File("E:\\Taobao\\"+title+"\\"+ (count++) + ".jpg");

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			os = new FileOutputStream(outFile);
			is = connection.getInputStream();
			byte[] buff = new byte[1024];
			while (true) {
				int readed = is.read(buff,0,1024);
				if (readed == -1) {
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);
				os.write(temp);
			}
		}
		if(is!=null)is.close();
		if(os!=null)os.close();
	}

	public String getHtmlSource(String HtmlUrl) {
		URL tempUrl;
		StringBuffer tempStore = new StringBuffer();
		try {
			tempUrl = new URL(HtmlUrl);
			HttpURLConnection connection = (HttpURLConnection) tempUrl
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// 设置代理
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "GBK"));// 读取网页全部内容
			String temp;
			while ((temp = in.readLine()) != null) {
				tempStore.append(temp);
			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("URL格式有问题!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempStore.toString();
	}

}