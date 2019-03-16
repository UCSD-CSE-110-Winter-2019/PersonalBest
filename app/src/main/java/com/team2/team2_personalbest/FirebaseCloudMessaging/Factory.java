package com.team2.team2_personalbest.FirebaseCloudMessaging;

import java.util.HashMap;
import java.util.Map;

/*
  function: factory pattern
 */
public class Factory<T> {

    private Map<String, Blueprint<T>> blueprints = new HashMap<>();

    public void put(String key, Factory.Blueprint<T> blueprint) {
        blueprints.put(key, blueprint);
    }

    public T get(String key) {
        return getOrDefault(key, () -> null);
    }

    public T getOrDefault(String key, Factory.Blueprint<T> defaultBlueprint) {
        Factory.Blueprint<T> blueprint = blueprints.get(key);
        if (blueprint == null) {
            return defaultBlueprint.create();
        }

        return blueprint.create();
    }

    public interface Blueprint<T> {
        T create();
    }
}
