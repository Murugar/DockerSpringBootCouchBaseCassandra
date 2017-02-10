package com.iqmsoft.cb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iqmsoft.cb.core.DataStore;
import com.iqmsoft.cb.core.Item;

import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private DataStore data;

    /** Returns the list of all items along with some information: ID, price, and name. */
    @RequestMapping("")
    public List<Item> items() {
        return data.getItems();
    }
}

