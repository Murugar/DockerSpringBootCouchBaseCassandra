package com.iqmsoft.cb.couchbase;

import com.couchbase.client.java.document.json.JsonObject;

public class Item implements com.iqmsoft.cb.core.Item {
    private final JsonObject json;

    public Item(JsonObject json) {
        this.json = json;
    }

    @Override
    public int getId() {
        return json.getInt("id");
    }

    @Override
    public String getName() {
        return json.getString("name");
    }

    @Override
    public int getPrice() {
        return json.getInt("price");
    }
}
