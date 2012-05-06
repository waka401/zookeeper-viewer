package org.zkviewer.node;

import java.util.HashSet;

public class DataNode implements INode{

	private byte data[];
	private DataNode parent;
	private String path;
	private String pathName;
	
	private HashSet<String> children = new HashSet<String>();

	public DataNode(byte rowData[]) {
		this(null, rowData);
	}

	public DataNode(DataNode parent, byte rowData[]) {
		this.parent = parent;
		if (null != rowData) {
			this.data = rowData;
		}
		this.children = new HashSet<String>();
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public DataNode getParent() {
		return parent;
	}

	public void setParent(DataNode parent) {
		this.parent = parent;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setChildren(HashSet<String> children) {
		this.children = children;
	}

	public HashSet<String> getChildren() {
		return this.children;
	}

	@Override
	public String renderToString() {
		if(null != data){
			return new String(data);
		}
		return "";
	}

	@Override
	public String getValue(String key) {
		return null;
	}

}
