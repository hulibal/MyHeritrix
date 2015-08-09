package Minehzg;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;

public class StartCrawler_byIDE {	
	
	static CrawlStatusListener listener = null;// ������
	
	public static CrawlStatusListener getListener() {
		return listener;
	}

	public static void setListener(CrawlStatusListener listener) {
		StartCrawler_byIDE.listener = listener;
	}

	public static void  main(String []args) throws InterruptedException {
		XMLSettingsHandler handler = null; // ��ȡorder.xml�ļ��Ĵ�����
		try {
			File file = new File("E:/spider/job/order.xml"); // order.xml�ļ�
			handler = new XMLSettingsHandler(file);
			handler.initialize();// ��ȡorder.xml�еĸ�������
			//
			CrawlController controller = new CrawlController(); // Heritrix�Ŀ�����
			controller.initialize(handler);// �Ӷ�ȡ��order.xml�еĸ�����������ʼ��������
			
						
			if (listener != null) {
				controller.addCrawlStatusListener(listener);// ��������Ӽ�����
			}
			controller.requestCrawlStart();// ��ʼץȡ

			/*
			 * ���Heritrix��һֱ��������ȴ�
			 */
			while (true) {
				if (controller.isRunning() == false) {
					break;
				}
				Thread.sleep(1000);
			}

			// ���Heritrix����������ֹͣ
			controller.requestCrawlStop();

		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
