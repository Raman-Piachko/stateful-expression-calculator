package repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RepositoryImpl implements Repository {
    private static Repository repositoryImpl;

    private Map<String, Map<String, String>> repositoryData = new ConcurrentHashMap<>();

    private RepositoryImpl() {
    }

    public static Repository getInstance() {
        if (repositoryImpl == null) {
            return new RepositoryImpl();
        }
        return repositoryImpl;
    }

    public void putNewData(String key) {
        repositoryData.put(key, new ConcurrentHashMap<>());
    }

    public Map<String, Map<String, String>> getRepositoryData() {
        return repositoryData;
    }

    public void setRepositoryData(Map<String, Map<String, String>> repositoryData) {
        this.repositoryData = repositoryData;
    }

    public Map<String, String> getDataByID(String sessionID) {
        return getRepositoryData().get(sessionID);
    }

    public void update(String ID, String parameterName, String paramValue) {
        getDataByID(ID)
                .put(parameterName, paramValue);
    }
}