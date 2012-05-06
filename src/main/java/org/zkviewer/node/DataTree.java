package org.zkviewer.node;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkviewer.exception.ZkViewerException;
import org.zkviewer.exception.ZkViewerException.NoNodeException;
import org.zkviewer.exception.ZkViewerException.NodeExistsException;
import org.zkviewer.util.StringUtil;
import org.zkviewer.zookeeper.ZkViewerConstants;


public class DataTree {
	
	private static final Logger logger = LoggerFactory.getLogger(DataTree.class);
	
	private ConcurrentHashMap<String, DataNode> nodes = new ConcurrentHashMap<String, DataNode>();

	public DataNode getNode(String path) {
		return nodes.get(path);
	}

	private DataNode root = null;

	public DataTree(String rootPath) {
		root = new DataNode(new byte[0]);
		String _rootPath = ZkViewerConstants.DIR_SPLITER;
		if(!StringUtil.isEmpty(rootPath)){
			_rootPath = rootPath;
		}
		root.setPath(_rootPath);
		int lastDirIndex = StringUtil.lastIndexOf(_rootPath, ZkViewerConstants.DIR_SPLITER);
		lastDirIndex = StringUtil.lastIndexOf(_rootPath, ZkViewerConstants.DIR_SPLITER);
		if( lastDirIndex < 0 || _rootPath.equals(ZkViewerConstants.DIR_SPLITER)){
		    root.setPathName(rootPath);
		}else{
			root.setPathName(StringUtil.substring(_rootPath, lastDirIndex+1));
		}
		nodes.put(root.getPath(), root);
	}

	public String createNode(String path, byte data[]) throws NoNodeException,
			NodeExistsException {
		int lastSlash = path.lastIndexOf('/');
		String parentName = path.substring(0, lastSlash);
		String childName = path.substring(lastSlash + 1);
		DataNode parent = nodes.get(parentName);
		if (parent == null) {
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (parent) {
			if (parent.getChildren().contains(childName)) {
				throw new ZkViewerException.NodeExistsException();
			}
			DataNode child = new DataNode(parent, data);
			child.setPathName(childName);
			child.setPath(path);
			parent.getChildren().add(childName);
			nodes.put(path, child);
		}
		return path;
	}

	public String createOrReplaceNode(String path, byte data[])
			throws NoNodeException, NodeExistsException {
		int lastSlash = path.lastIndexOf('/');
		String parentName = path.substring(0, lastSlash);
		String childName = path.substring(lastSlash + 1);
		DataNode parent = nodes.get(parentName);
		if (parent == null) {
			logger.error("parent node is empty, when try to create node. [path]:" + path);
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (parent) {
			if (parent.getChildren().contains(childName)) {
				DataNode children = nodes.get(childName);
				parent.getChildren().remove(childName);
				children.setParent(null);
			}
			DataNode child = new DataNode(parent, data);
			parent.getChildren().add(childName);
			child.setPathName(childName);
			child.setPath(path);
			nodes.put(path, child);
		}
		return path;
	}

	public void deleteNode(String path) throws ZkViewerException.NoNodeException {
		int lastSlash = path.lastIndexOf('/');
		String parentName = path.substring(0, lastSlash);
		String childName = path.substring(lastSlash + 1);
		DataNode node = nodes.get(path);
		if (node == null) {
			throw new ZkViewerException.NoNodeException();
		}
		nodes.remove(path);
		DataNode parent = nodes.get(parentName);
		if (parent == null) {
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (parent) {
			parent.getChildren().remove(childName);
			node.setParent(null);
		}
	}

	public Stat setData(String path, byte data[])
			throws ZkViewerException.NoNodeException {
		Stat s = new Stat();
		DataNode n = nodes.get(path);
		if (n == null) {
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (n) {
			n.setData(data);
		}
		return s;
	}

	public byte[] getData(String path, Stat stat, Watcher watcher)
			throws ZkViewerException.NoNodeException {
		DataNode n = nodes.get(path);
		if (n == null) {
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (n) {
			return n.getData();
		}
	}

	public ArrayList<String> getChildren(String path)
			throws ZkViewerException.NoNodeException {
		DataNode n = nodes.get(path);
		if (n == null) {
			throw new ZkViewerException.NoNodeException();
		}
		synchronized (n) {
			ArrayList<String> children = new ArrayList<String>();
			children.addAll(n.getChildren());
			return children;
		}
	}
	
	public DataNode getRoot(){
		return root;
	}

}
