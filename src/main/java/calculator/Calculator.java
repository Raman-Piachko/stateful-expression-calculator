package calculator;

public interface Calculator {
    int calculate(String expression);

    int addData(String sessionID, String parameterName, String paramValue);

    void deleteData(String sessionID, String parameterName);
}