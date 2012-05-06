package org.zkviewer.node;

import java.util.List;

public interface SearchService {
	
	String getRootPath();
	
	List<INode> getChildrenNodes(String path);

	List<INode> searchNodes(String path);
	   
}
