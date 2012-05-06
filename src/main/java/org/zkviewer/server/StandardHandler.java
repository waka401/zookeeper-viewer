package org.zkviewer.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkviewer.config.ConfigContext;


public class StandardHandler implements Handler {
	private final static Logger logger = LoggerFactory.getLogger(StandardHandler.class);
	private Map<String,Processor> processors;
	
	private void addProcessor(String key,Processor processor){
		if(null == processors){
			processors = new HashMap<String,Processor>();
		}
		processors.put(key, processor);
	}
	 
	@Override
	public void init(ConfigContext config) {
		
	}

	@Override
	public Response process(Request req) {
		Response rep = new Response();
		Processor processor= processors.get(req.getProcessorKey());
		if(null != processor){
			processor.process(req, rep);
		}
		return rep;
	}

	@Override
	public void setProcessors(Map processorMap){
		if(null==processorMap){
			return;
		}
		Set<Object> entry = processorMap.keySet();
		for(Object key:entry){
			Processor processor = (Processor)processorMap.get(key);
			addProcessor(key.toString(),processor);
		}
	}
	
}
