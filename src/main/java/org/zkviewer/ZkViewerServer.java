package org.zkviewer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;
import org.zkviewer.util.StringUtil;


public class ZkViewerServer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        initLoggerConfig();
		final String allSpringConfig = System.getProperty("spring.context", "zk-viewer.xml,");
		String[] springConfigs = allSpringConfig.split(",");
        doInitSpringContext(springConfigs);
		System.out.println("==========ZkViewerServer init Spring===========");
	}

    static void initLoggerConfig() {
        String log4jFile = System.getProperty("log4j.configuration", "classpath:log4j.xml");
        String refreshInterval = System.getProperty("log4j.refreshInterval");
        long interval = StringUtil.isNotBlank(refreshInterval) ? Long.valueOf(refreshInterval) : 2000;
        try {
            Log4jConfigurer.initLogging(log4jFile, interval);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("no find the log4j file. file = " + log4jFile, e);
        }
    }

	public static ApplicationContext doInitSpringContext(String... configFile) {
		List<String> list = new ArrayList<String>();
		for (String config : configFile) {
			if (StringUtil.isNotBlank(config)) {
				list.add(config);
			}
		}
		return new ClassPathXmlApplicationContext(list.toArray(new String[] {}));
	}
	
}
