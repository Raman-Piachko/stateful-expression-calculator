package repository;

import java.util.Map;
import java.util.Optional;

public interface Repository {

    Map<String, Map<String, String>> getRepositoryData();

    void putNewData(String sessionID);

    void update(String sessionID, String parameterName, String paramValue);

    Map<String, String> getDataByID(String sessionID);

    boolean existData(String id);

    Optional<String> getValue(String id, String parameterName);

    void removeData(String id, String parameterName);
}