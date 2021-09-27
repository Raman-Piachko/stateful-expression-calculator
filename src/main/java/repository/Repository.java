package repository;

import java.util.Map;

public interface Repository {

    Map<String, Map<String, String>> getRepositoryData();

    void putNewData(String sessionID);

    void update(String sessionID, String parameterName, String paramValue);

    Map<String, String> getDataByID(String sessionID);
}