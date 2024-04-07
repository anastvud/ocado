package com.ocado.basket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class BasketSplitter {
    Map<String, List<String>> deliveryConfig = new HashMap<>();
    public boolean isValidJson(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            parser.parse(jsonString);
            return true;
        } catch (JsonSyntaxException e) {
            throw new com.google.gson.JsonSyntaxException("Invalid JSON");
        }
    }

    public BasketSplitter(String absolutePathToConfigFile) throws FileNotFoundException {
        Gson gson = new Gson();

        String fileContent;
        try (FileReader reader = new FileReader(absolutePathToConfigFile);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            fileContent = contentBuilder.toString();
            isValidJson(fileContent);

            Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
            deliveryConfig = gson.fromJson(fileContent, type);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found");
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

    public static void main(String[] args) throws FileNotFoundException, RuntimeException {
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