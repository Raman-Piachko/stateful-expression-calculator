package repository;

import java.util.Map;
import java.util.Optional;

public interface Repository {

    void update(String sessionID, String parameterName, String paramValue);

    Map<String, String> getDataByID(String sessionID);

    Optional<String> getValue(String id, String parameterName);

    void removeData(String id, String parameterName);

    void insertNewData(String id);
}