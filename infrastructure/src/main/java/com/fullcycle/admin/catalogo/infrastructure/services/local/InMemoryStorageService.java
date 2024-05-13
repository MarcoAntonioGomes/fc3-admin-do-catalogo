package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.domain.video.Resource;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();

    }

    public void reset() {
        this.storage.clear();
    }

    public  Map<String, Resource> storage(){
        return this.storage;
    }

    @Override
    public void deleteAll(Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public Resource get(String name) {
        return this.storage.get(name);
    }

    @Override
    public void store(String name, Resource resource) {
        this.storage.put(name, resource);
    }

    @Override
    public List<String> list(String prefix) {
       if(prefix == null || prefix.isBlank()){
           return Collections.emptyList();
       }
        return this.storage.keySet().stream()
                .filter(name -> name.startsWith(prefix))
                .toList();
    }
}
