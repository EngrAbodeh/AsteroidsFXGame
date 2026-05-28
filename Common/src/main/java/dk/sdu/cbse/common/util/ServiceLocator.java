package dk.sdu.cbse.common.util;

import java.util.*;

public enum ServiceLocator {
    INSTANCE;

    private static final Map<Class, ServiceLoader> loadermap = new HashMap<>();

    public <T> List<T> locateAll(Class<T> service) {
        ServiceLoader<T> loader = loadermap.get(service);
        if (loader == null) {
            loader = ServiceLoader.load(ModuleLayer.boot(), service);
            loadermap.put(service, loader);
        }
        List<T> list = new ArrayList<>();
        try {
            for (T instance : loader) {
                list.add(instance);
            }
        } catch (ServiceConfigurationError e) {
            e.printStackTrace();
        }
        return list;
    }
}