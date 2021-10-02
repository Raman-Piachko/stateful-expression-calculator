package utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static controllers.ControllerConstants.SPACES;

public class ConversionUtil {

    private ConversionUtil() {
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }

    public static Map<String, List<String>> convertMapWithArrayValueToListValue(Map<String, String> resourceMap) {
        return resourceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringEntry -> List.of(stringEntry.getValue()), (a, b) -> b));
    }
}