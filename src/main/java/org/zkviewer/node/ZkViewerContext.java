package org.zkviewer.node;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkviewer.config.ConfigContext;
import org.zkviewer.exception.ZkViewerException.NoNodeException;
import org.zkviewer.exception.ZkViewerException.NodeExistsException;
import org.zkviewer.util.StringUtil;
import org.zkviewer.zookeeper.ZkViewerConstants;


public class ZkViewerContext implements Container{
	
	private static final Logger logger = LoggerFactory.getLogger(ZkViewerContext.class);
	private DataTree tree ;
	private ConfigContext configContext;
	
	@Override
	public void init() throws Exception{
		if(null == configContext){
			throw new Exception("configContext is null, fail to init zkviewer context.");
		}
		String rootPath = configContext.getProperty("zookeeper.rootpath");
		if(StringUtil.isEmpty(rootPath)){
			throw new Exception("can not read the [zookeeper.rootpath]'s value from 'config.properties' file."); 
		}
		tree = new DataTree(rootPath);
	}

	@Override
	public boolean addNode(String path, byte[] rowData) {
		try {
			tree.createOrReplaceNode(path, rowData);
		} catch (NoNodeException e) {
			logger.error("parent node is empty, when try to create node. [path]:" + path);
			return false;
		} catch (NodeExistsException e) {
			logger.error("you want to create an exist node below [path]:" + path);
			return false;
		}
		return true;
	}

	@Override
	public boolean removeNode(String path) {
		try {
			tree.deleteNode(path);
		} catch (NoNodeException e) {
			logger.error("node is empty, when try to delete node. [path]:" + path);
			return false;
		}
		return true;
	}

	@Override
	public INode getNode(String path) {
		return tree.getNode(path);
	}

	@Override
	public boolean updateData(String path, byte[] rowData) {
		try {
			tree.setData(path, rowData);
		} catch (NoNodeException e) {
			logger.error("update an no exist path. [path]:" + path);
			return false;
		}
		return true;
	}

	@Override
	public List<INode> getChildrenNodes(String path) {
		List<INode> nodeList = new ArrayList<INode>();
		try {
			INode currentNode = getNode(path);
			List<String> subPathList = tree.getChildren(path);
			for(String subPath : subPathList){
				INode childNode = getNode(currentNode.getPath()+"/"+subPath);
				if(null != childNode){
					nodeList.add(childNode);
				}
			}
		} catch (NoNodeException e) {
			logger.error("get no exist path. [path]:" + path);
			return null;
		}
		return nodeList;
	}

	@Override
	public List<INode> searchNodes(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRootPath() {
		return tree.getRoot().getPath();
	}

	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}

}
