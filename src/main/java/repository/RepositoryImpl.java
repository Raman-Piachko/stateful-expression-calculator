package repository;

import java.util.concurrent.ConcurrentHashMap;

public final class RepositoryImpl implements Repository {
    private static Repository repositoryImpl;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> repositoryData = new ConcurrentHashMap<>();

    private RepositoryImpl() {
    }

    public static Repository getInstance() {
        if (repositoryImpl == null) {
            return new RepositoryImpl();
        }
        return repositoryImpl;
    }

    public void updateRepositoryData(String key, ConcurrentHashMap value) {
        repositoryData.put(key, value);
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getRepositoryData() {
        return repositoryData;
    }

    public void setRepositoryData(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> repositoryData) {
        this.repositoryData = repositoryData;
    }
}
