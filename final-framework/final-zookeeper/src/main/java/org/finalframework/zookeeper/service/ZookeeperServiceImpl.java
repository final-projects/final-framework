package org.finalframework.zookeeper.service;

import lombok.Setter;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/17 15:35:16
 * @since 1.0
 */
@Service
@ConditionalOnBean(ZooKeeper.class)
public class ZookeeperServiceImpl implements ZookeeperService {

    @Setter
    @Resource
    private ZooKeeper zooKeeper;


    @Override
    public List<String> getChildren(String path) {
        try {
            List<String> children = zooKeeper.getChildren(path, false);
            children.sort(String::compareTo);
            return children;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    @Override
    public void delete(@NonNull String path, int version) {
        try {
            zooKeeper.delete(path, version);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }
}
