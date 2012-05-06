package org.zkviewer.node;

import java.util.HashSet;

public interface INode {
    
	String getPath();
	
	String getPathName();
	
	HashSet<String> getChildren();
	
	String renderToString();
    
	String getValue(String key);
	
}
