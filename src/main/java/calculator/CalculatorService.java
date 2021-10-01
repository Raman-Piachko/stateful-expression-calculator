package calculator;

import responseCodes.codes.StatusCode;

public interface CalculatorService {
    int calculate(String expression);

    StatusCode addData(String sessionID, String parameterName, String paramValue) throws Exception;

    void deleteData(String sessionID, String parameterName);
}