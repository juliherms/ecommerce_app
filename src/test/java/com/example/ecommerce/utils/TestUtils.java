package com.example.ecommerce.utils;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if(wasPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("juliherms");
        user.setPassword("password");
        user.setCart(createCart(user));

        return user;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = createItems();
        cart.setItems(createItems());
        cart.setTotal(items.stream().map(Item::getPrice).reduce(BigDecimal::add).get());
        cart.setUser(user);

        return cart;
    }

    public static List<Item> createItems() {

        List<Item> items = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            items.add(createItem(i));
        }

        return items;
    }

    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);

        item.setPrice(BigDecimal.valueOf(id * 1.2));

        item.setName("Item " + item.getId());

        item.setDescription("Description ");
        return item;
    }

    public static List<UserOrder> createOrders(){
        List<UserOrder> orders = new ArrayList<>();

        IntStream.range(0,2).forEach(i -> {
            UserOrder order = new UserOrder();
            Cart cart = createCart(createUser());

            order.setItems(cart.getItems());
            order.setTotal(cart.getTotal());
            order.setUser(createUser());
            order.setId((long) i);

            orders.add(order);
        });
        return orders;
    }

}
