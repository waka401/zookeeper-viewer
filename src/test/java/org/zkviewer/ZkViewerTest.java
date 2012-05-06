package org.zkviewer;
import java.io.IOException;

import org.junit.Test;
import org.zkviewer.ZkViewerServer;

public class ZkViewerTest {
	
	@Test
	public void test() {
		System.setProperty("spring.context", "zk-viewer.xml");
		ZkViewerServer.main(null);
		try {
			System.in.read(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void testHandler() {
		System.setProperty("spring.context", "processor.xml");
		
	} 
	
	/*@Override
	public void renderNode(String path,PrintWriter writer) {
		if(null == render){
	    	render = new TreeRender();
	    }
		List<INode> nodeList = getChildrenNodes(path);
		for(INode childNode:nodeList){
			render.display(childNode, writer);
		}
	}

	@Override
	public void setRender(IRender render) {
		this.render = render;
	}*/
}
