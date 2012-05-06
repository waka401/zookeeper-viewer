package org.zkviewer.server;

import java.util.Map;

import org.zkviewer.config.ConfigContext;


public interface Handler {
	
	void init(ConfigContext config);
	
	void setProcessors(Map<String,Processor> processorMap);
	  
	Response process(Request req);
	
}
