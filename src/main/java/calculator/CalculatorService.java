package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static controllers.ControllerConstants.EXPRESSION;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorService implements Calculator {

    @Override
    public int calculate(HttpSession session) {
        String expression = getExpression(session);
        MathParser mathParser = MathParserFactory.create();

        return mathParser.calculate(expression)
                .doubleValue()
                .intValue();
    }

    private String getExpression(HttpSession session) {
        String expression = (String) session.getAttribute(EXPRESSION);
        List<String> expressionList = Arrays.asList(expression.split(EMPTY_SYMBOL));

        Map<String, String> attributeValueMap = getAttributeValueMap(session);

        while (isExpressionWithVariables(attributeValueMap, expressionList)) {
            convertExpressionWithValue(attributeValueMap, expressionList);
        }
        return deleteSpacesAndConvertListToString(expressionList);

    }

    private Map<String, String> getAttributeValueMap(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributeValueMap = new ConcurrentHashMap<>();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributeValueMap.put(attributeName, (String) session.getAttribute(attributeName));
        }

        return attributeValueMap;
    }


    private void convertExpressionWithValue(Map<String, String> parameters, List<String> expression) {
        for (String item : expression) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase(item)) {
                    int replaceableIndex = expression.indexOf(item);
                    expression.set(replaceableIndex, value);
                }
            }
        }
    }

    private boolean isExpressionWithVariables(Map<String, String> parameters, List<String> expression) {
        for (String s : expression) {
            if (parameters.containsKey(s)) {
                return true;
            }
        }
        return false;
    }
}