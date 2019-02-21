package com.ilikly.finalframework.json;


import com.ilikly.finalframework.core.Assert;

/**
 * Json service register
 * @author likly
 * @version 1.0
 * @date 2018-09-26 21:27
 * @since 1.0
 */
public class JsonRegistry {
    private static final JsonRegistry instance = new JsonRegistry();
    private JsonService jsonService;

    private JsonRegistry() {
    }

    public static JsonRegistry getInstance() {
        return instance;
    }

    JsonService getJsonService() {
        Assert.isNull(jsonService, "json service had not been registered.");
        return jsonService;
    }

    public void register(JsonService jsonService) {
        this.jsonService = jsonService;
    }
}
