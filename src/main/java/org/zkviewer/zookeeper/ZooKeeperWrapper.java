package org.zkviewer.zookeeper;

import java.io.IOException;
import java.util.List;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.api.CuratorListener;
import com.netflix.curator.retry.RetryOneTime;


public class ZooKeeperWrapper implements ZkViewerConstants{
    private CuratorFramework client;
    private String zkServerUrl;
    private int connectionTimeout;
    private int sessionTimeout;
    
    public ZooKeeperWrapper() {
    }

    public ZooKeeperWrapper(String zkServerUrl, int connectionTimeout) throws IOException {
        this.zkServerUrl = zkServerUrl;
        this.connectionTimeout = connectionTimeout;
        this.sessionTimeout = connectionTimeout;
        init();
    }

    public void init() throws IOException {
        Builder builder = CuratorFrameworkFactory.builder();
        client = builder.connectString(zkServerUrl)
                .connectionTimeoutMs(connectionTimeout)
                .sessionTimeoutMs(sessionTimeout)
                //.namespace(DDBS_ROOT)
                .retryPolicy(new RetryOneTime(1))
                .build(); 
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void addListener(CuratorListener listener) {
        client.getCuratorListenable().addListener(listener);
    }

    public boolean existsWithWatch(String path) throws Exception {
        return null != client.checkExists().watched().forPath(path);
    }

    public List<String> getWatchedChildren(String path) throws Exception {
        return client.getChildren().watched().forPath(path);
    }

    public byte[] getWatchedData(String path) throws Exception {
        return client.getData().watched().forPath(path);
    }

    public void close() {
        client.close();
    }

    public void setZkServerUrl(String zkServerUrl) {
        this.zkServerUrl = zkServerUrl;
    }

    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}

