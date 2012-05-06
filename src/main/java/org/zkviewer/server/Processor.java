package org.zkviewer.server;

import org.zkviewer.node.SearchService;

public interface Processor {
	
	void setSearchService(SearchService service);
	
	void process(final Request req,final Response rep);
	
}
