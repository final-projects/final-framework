package org.finalframework.data.sharding;


import java.io.Serializable;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2020-01-18 00:23:58
 * @since 1.0
 */
public class MasterSlaveRuleProperties implements Serializable {
    private static final long serialVersionUID = -4796666194553926329L;
    private String name;
    private String master;
    private List<String> slaves;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<String> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<String> slaves) {
        this.slaves = slaves;
    }
}

