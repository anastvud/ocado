package com.ocado.basket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;


public class BasketSplitter {

    Map<String, List<String>> deliveryConfig = new HashMap<>();

    public BasketSplitter(String absolutePathToConfigFile) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(absolutePathToConfigFile);
            Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
            deliveryConfig = gson.fromJson(reader, type);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> split(List<String> items) {

        Map<String, List<String>> deliveryGroups = new HashMap<>();

        for (String item : items) {
            boolean addedToGroup = false;
            List<String> availableDeliveryMethods = deliveryConfig.getOrDefault(item, Collections.emptyList());
            for (String deliveryMethod : availableDeliveryMethods) {
                if (!deliveryGroups.containsKey(deliveryMethod)) {
                    deliveryGroups.put(deliveryMethod, new ArrayList<>());
                }
                deliveryGroups.get(deliveryMethod).add(item);
                addedToGroup = true;
                break;
            }
            if (!addedToGroup) {
                if (!deliveryGroups.containsKey("no_group")) {
                    deliveryGroups.put("no_group", new ArrayList<>());
                }
                deliveryGroups.get("no_group").add(item);
            }
        }

        return deliveryGroups;
    }

    public static void main(String[] args) {
        BasketSplitter basketSplitter = new BasketSplitter("/home/nastia/ocado/src/main/resources/config.json");

        List<String> items = Arrays.asList(
                "Steak (300g)",
                "Carrots (1kg)",
                "Soda (24x330ml)",
                "AA Battery (4 Pcs.)",
                "Espresso Machine",
                "Garden Chair"
        );

        Map<String, List<String>> result = basketSplitter.split(items);

        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }


    }
}