package repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class RepositoryImpl implements Repository {
    private static Repository repositoryImpl;

    private static final Map<String, Map<String, String>> REPOSITORY_DATA = new ConcurrentHashMap<>();

    private RepositoryImpl() {
    }

    public static Repository getInstance() {
        if (repositoryImpl == null) {
            return new RepositoryImpl();
        }
        return repositoryImpl;
    }

    public void putNewData(String key) {
        REPOSITORY_DATA.put(key, new ConcurrentHashMap<>());
    }

    public void update(String sessionID, String parameterName, String paramValue) {
        REPOSITORY_DATA.get(sessionID)
                .put(parameterName, paramValue);
    }

    public Map<String, String> getDataByID(String sessionID) {
        return REPOSITORY_DATA.get(sessionID);
    }

    public boolean existData(String id) {
        return REPOSITORY_DATA.containsKey(id);
    }

    public Optional<String> getValue(String id, String parameterName) {
        Map<String, String> data = getDataByID(id);
        if (data == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(data.get(parameterName));
    }

    public void removeData(String id, String parameterName) {
        Map<String, String> data = getDataByID(id);

        data.remove(parameterName);
    }
}