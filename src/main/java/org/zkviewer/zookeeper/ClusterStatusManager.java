package org.zkviewer.zookeeper;

import java.util.List;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkviewer.config.ConfigContext;
import org.zkviewer.node.Container;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.api.CuratorEvent;
import com.netflix.curator.framework.api.CuratorEventType;
import com.netflix.curator.framework.api.CuratorListener;

public class ClusterStatusManager implements CuratorListener,ZkViewerConstants{
	
	private static final Logger logger = LoggerFactory.getLogger(ClusterStatusManager.class);
	
	private ZooKeeperWrapper zooKeeper;
	
	private ConfigContext configContext;
	
	private Container container;

	public void init(){
		zooKeeper.addListener(this);
		initSchema();
	}

	@Override
	public void eventReceived(CuratorFramework client, CuratorEvent event)
			throws Exception {
		if (event.getType() != CuratorEventType.WATCHED)
			return;
		EventType type = event.getWatchedEvent().getType();
		switch (type) {
		case NodeCreated:
			container.addNode(event.getPath(), event.getData());
			System.out.println("NodeCreated:"+event.getPath());
			break;
		case NodeDeleted:
			container.removeNode(event.getPath());
			System.out.println("NodeDeleted:"+event.getPath());
			break;
		case NodeDataChanged:
			container.updateData(event.getPath(), event.getData());
			System.out.println("NodeDataChanged:"+event.getPath());
			break;
		case NodeChildrenChanged:
			container.removeNode(event.getPath());
			iterateSchema(event.getPath());
			System.out.println("NodeChildrenChanged:"+event.getPath());
			break;
		}
	}	
	
	private void initSchema(){
		try {
			List<String> schemaPathList = this.zooKeeper.getWatchedChildren(container.getRootPath());
			if(null == schemaPathList){
				return;
			}
			for(String subPath : schemaPathList){
				String rootPath = container.getRootPath() + ZkViewerConstants.DIR_SPLITER + subPath;
				byte rowData[] = this.zooKeeper.getWatchedData(rootPath);
				container.addNode(rootPath, rowData);
			    iterateSchema(rootPath);
			}
		} catch (Exception e) {
			logger.error("fail to init DDBS Schema",e);
		}
	}
	
	private void iterateSchema(String path) throws Exception{
		List<String> schemaPathList = this.zooKeeper.getWatchedChildren(path);
		if(null == schemaPathList){
			return;
		}
		for(String subPath : schemaPathList){
			String subFullPath = path + ZkViewerConstants.DIR_SPLITER + subPath;
			logger.error(subFullPath);
			byte rowData[] = this.zooKeeper.getWatchedData(subFullPath);
			container.addNode(subFullPath, rowData);
			iterateSchema(subFullPath);
		}
	}

	public void setZooKeeper(ZooKeeperWrapper zooKeeper) {
		this.zooKeeper = zooKeeper;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}
	
}
