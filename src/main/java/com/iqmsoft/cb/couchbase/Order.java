package com.iqmsoft.cb.couchbase;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.iqmsoft.cb.core.Item;
import com.iqmsoft.cb.core.OrderTotal;

import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.IntStream.range;


public class Order implements com.iqmsoft.cb.core.Order {
    public final JsonObject json;

    /**
     * Creates a new order object
     * @param id unique ID
     * @param tax tax as decimal percentage (e.g. 0.09 for 9%)
     * @throws IllegalArgumentException if ID is empty
     * @throws IllegalArgumentException if tax is negative
     */
    public Order(String id, double tax) {
        this(JsonObject.empty());
        Assert.hasText(id, "id must have text");
        Assert.isTrue(tax >= 0.0, "tax cannot be negative");
        json.put("id", id);
        json.put("type", DocumentType.ORDER.toString());
        json.put("tax", tax);
        json.put("number", 0);
        json.put("items", JsonArray.empty());
    }

    /** Creates a new order object with the specified JSON */
    public Order(@NotNull JsonObject json) {
        this.json = json;
    }

    @Override
    public String getId() {
        return json.getString("id");
    }

    @Override
    public boolean isSubmitted() {
        return json.getInt("number") != 0;
    }

    /**
     * @see com.iqmsoft.cb.core.Order#setNumber(int)
     * @throws IllegalArgumentException if number is negative
     */
    @Override
    public void setNumber(int number) {
        Assert.isTrue(number >= 0, "number cannot be negative");
        json.put("number", number);
    }

    @Override
    public OrderTotal getTotal() {
        final OrderTotal total = new OrderTotal();
        final JsonArray items = getItems();
        total.subtotal = range(0, items.size())
                .mapToObj(items::getObject)
                .mapToInt(item -> item.getInt("price") * item.getInt("quantity"))
                .sum();
        total.tax = (int) Math.floor(total.subtotal * json.getDouble("tax"));
        return total;
    }

    private JsonArray getItems() {
        return json.getArray("items");
    }

    @Override
    public boolean isEmpty() {
        return getItems().size() == 0;
    }

    @Override
    public void removeItem(final int id) {
        final JsonArray items = getItems();
        final JsonArray newItems = JsonArray.empty();
        range(0, items.size())
                .mapToObj(items::getObject)
                .filter(obj -> obj.getInt("id") != id)
                .forEach(newItems::add);
        json.put("items", newItems);
    }

    @Override
    public boolean containsItem(int id) {
        return findItem(id).isPresent();
    }

    private Optional<JsonObject> findItem(final int id) {
        final JsonArray items = getItems();
        return range(0, items.size())
                .mapToObj(items::getObject)
                .filter(obj -> obj.getInt("id") == id)
                .findFirst();
    }

    /**
     * @see com.iqmsoft.cb.core.Order#addItem(Item, int)
     * @throws IllegalArgumentException if quantity is zero or negative
     */
    @Override
    public void addItem(@NotNull Item item, int quantity) {
        Assert.isTrue(quantity > 0, "quantity must be positive");
        final JsonArray items = getItems();
        final JsonObject jsonItem = JsonObject.empty();
        jsonItem.put("id", item.getId());
        jsonItem.put("price", item.getPrice());
        jsonItem.put("quantity", quantity);
        items.add(jsonItem);
    }

    /**
     * @see com.iqmsoft.cb.core.Order#updateItem(int, int, Function)
     * @throws IllegalArgumentException if quantity is zero or negative
     */
    @Override
    public void updateItem(int id, int quantity, @NotNull Function<Integer, Item> itemFunc) {
        Assert.isTrue(quantity > 0, "quantity must be positive");
        final Optional<JsonObject> item = findItem(id);
        if (item.isPresent()) {
            item.get().put("quantity", quantity);
        } else {
            addItem(itemFunc.apply(id), quantity);
        }
    }
}
