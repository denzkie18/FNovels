package com.fnovels.model.api;

public class Provider {

    private String name;
    private String route;

    public Provider(String name, String route) {
        this.name = name;
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public String getRoute() {
        return route;
    }

    public void setProviderName(String name) {
        this.name = name;
    }

    public void setProviderRoute(String route) {
        this.route = route;
    }
}
