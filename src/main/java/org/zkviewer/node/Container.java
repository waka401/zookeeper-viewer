package org.zkviewer.node;


public interface Container extends SearchService{

	void init() throws Exception ;

	boolean addNode(String path, byte rowData[]);

	boolean removeNode(String path);
	
	boolean updateData(String path,byte rowData[]);

	INode getNode(String path);
	
}
