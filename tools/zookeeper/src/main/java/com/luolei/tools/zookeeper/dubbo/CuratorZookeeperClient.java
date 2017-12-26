package com.luolei.tools.zookeeper.dubbo;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;

public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher> {

	private final CuratorFramework client;
	
	public CuratorZookeeperClient(URL url) {
		super(url);
		try {
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(url.getConnectString())
                    .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                    .connectionTimeoutMs(5000);
            String authority = url.getAuthority();
            builder.namespace(url.getPath());
            if (authority != null && authority.length() > 0) {
                builder = builder.authorization("digest", authority.getBytes());
            }
            client = builder.build();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    if (state == ConnectionState.LOST) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
                    } else if (state == ConnectionState.CONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
                    } else if (state == ConnectionState.RECONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
                    }
                }
            });
            client.start();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	public void delete(String path) {
		try {
            client.delete().forPath(path);
        } catch (NoNodeException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	public List<String> getChildren(String path) {
		try {
            return client.getChildren().forPath(path);
        } catch (NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

	@Override
	protected void doClose() {
		client.close();
	}

	@Override
	protected void createPersistent(String path, String data) {
		try {
			String returnPath = client.create().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());
            logger.info("创建持久节点成功 : {}", returnPath);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	protected void createEphemeral(String path, String data) {
		try {
            String returnPath = client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
            logger.info("创建临时节点成功 : {}", returnPath);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	protected CuratorWatcher createTargetChildListener(String path, ChildListener listener) {
		return new CuratorWatcherImpl(listener);
	}

	@Override
	protected List<String> addTargetChildListener(String path, CuratorWatcher listener) {
		try {
            return client.getChildren().usingWatcher(listener).forPath(path);
        } catch (NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	@Override
	protected void removeTargetChildListener(String path, CuratorWatcher listener) {
		((CuratorWatcherImpl) listener).unwatch();
	}
	
	public CuratorFramework getCuratorFramework() {
		return client;
	}
	
	private class CuratorWatcherImpl implements CuratorWatcher {

        private volatile ChildListener listener;

        public CuratorWatcherImpl(ChildListener listener) {
            this.listener = listener;
        }

        public void unwatch() {
            this.listener = null;
        }

		@Override
		public void process(WatchedEvent event) throws Exception {
			logger.info("事件触发 : {}", event);
			if (listener != null) {
                listener.childChanged(event.getPath(), client.getChildren().usingWatcher(this).forPath(event.getPath()));
            }
		}
    }

}
