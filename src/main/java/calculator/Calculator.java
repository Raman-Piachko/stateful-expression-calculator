package calculator;

import java.util.Map;

public interface Calculator {
    int calculate(String expression, Map<String, String> attributeValueMap);
}