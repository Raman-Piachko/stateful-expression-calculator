package repository;

import java.util.concurrent.ConcurrentHashMap;

public interface Repository {

    ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getRepositoryData();

    void updateRepositoryData(String sessionID, ConcurrentHashMap<Object, Object> objectObjectConcurrentHashMap);
}