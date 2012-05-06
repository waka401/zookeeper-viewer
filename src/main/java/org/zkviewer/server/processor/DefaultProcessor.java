package org.zkviewer.server.processor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkviewer.jackson.TreeNode;
import org.zkviewer.node.DataNode;
import org.zkviewer.node.INode;
import org.zkviewer.node.SearchService;
import org.zkviewer.server.MainServlet;
import org.zkviewer.server.Processor;
import org.zkviewer.server.Request;
import org.zkviewer.server.Response;
import org.zkviewer.zookeeper.ZkViewerConstants;


public class DefaultProcessor implements Processor {
	private final static Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);
	
	private SearchService service;

	@Override
	public void setSearchService(SearchService service) {
		this.service = service;
	}

	@Override
	public void process(Request req, Response rep) {
		String path = req.getParameter("path");
		rep.setTemplate("json.vm");
		if(null == path){
			return;
		} 
		List<INode> nodeList = service.getChildrenNodes(path);
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		for (INode node : nodeList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(node.getPath());
			treeNode.setText(node.getPathName());
			treeNode.setData(node.renderToString());
			treeNode.setChildren(new ArrayList<TreeNode>());
			treeNode.setIsexpand(false);
			treeNodeList.add(treeNode);
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			StringWriter writer = new StringWriter();
			mapper.writeValue(writer, treeNodeList);
			rep.assign("json", writer.toString());
			return;
		} catch (JsonGenerationException e) {
			logger.error("" + e.toString());
		} catch (JsonMappingException e) {
			logger.error("" + e.toString());
		} catch (IOException e) {
			logger.error("" + e.toString());
		}
		rep.assign("json", "error");
		
	}

}
