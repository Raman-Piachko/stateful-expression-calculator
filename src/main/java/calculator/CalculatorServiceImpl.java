package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorServiceImpl implements Calculator {

    @Override
    public int calculate(String expression, Map<String, String> attributeValueMap) {
        MathParser mathParser = MathParserFactory.create();
        String finalExpression = getFinalExpression(expression, attributeValueMap);

        return mathParser.calculate(finalExpression)
                .doubleValue()
                .intValue();
    }

    private String getFinalExpression(String expression, Map<String, String> attributeValueMap) {
        List<String> expressionList = Arrays.asList(expression.split(EMPTY_SYMBOL));

        while (isExpressionWithVariables(attributeValueMap, expressionList)) {
            convertExpressionWithValue(attributeValueMap, expressionList);
        }

        return deleteSpacesAndConvertListToString(expressionList);
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