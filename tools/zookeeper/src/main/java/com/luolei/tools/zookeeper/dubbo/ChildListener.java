package com.luolei.tools.zookeeper.dubbo;

import java.util.List;

public interface ChildListener {

	void childChanged(String path, List<String> children);
}
