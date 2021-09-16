package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static calculator.CalculatorConstants.EMPTY_SYMBOL;
import static calculator.CalculatorConstants.SPACES;

public  class СonversionUtil {

    private СonversionUtil() {
    }

    public static Map<String, List<String>> convertMapWithArrayValueToListValue(Map<String, String[]> resourceMap) {
        return resourceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringEntry -> Arrays.asList(stringEntry.getValue()), (a, b) -> b));
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }
}