package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import repository.Repository;
import repository.RepositoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static controllers.ControllerConstants.ABS_RANGE;
import static controllers.ControllerConstants.DIVIDE;
import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static controllers.ControllerConstants.EXPRESSION;
import static controllers.ControllerConstants.MINUS;
import static controllers.ControllerConstants.MULTIPLY;
import static controllers.ControllerConstants.PLUS;
import static controllers.ControllerConstants.STATUS_BAD_REQUEST;
import static controllers.ControllerConstants.STATUS_CREATED;
import static controllers.ControllerConstants.STATUS_FORBIDDEN;
import static controllers.ControllerConstants.STATUS_OK;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorService implements Calculator {
    private static final Repository REPOSITORY = RepositoryImpl.getInstance();

    @Override
    public int calculate(String ID) {
        String expression = getFinalExpression(ID);
        MathParser mathParser = MathParserFactory.create();

        return mathParser.calculate(expression)
                .doubleValue()
                .intValue();
    }

    @Override
    public int addData(String ID, String parameterName, String paramValue) {
        String valueFromRepository = getValueFromRepository(ID, parameterName);
        if (!isDataExists(ID)) {
            REPOSITORY.putNewData(ID);
        }
        int statusCode = getStatusCode(valueFromRepository, parameterName, paramValue);
        if (!isBadFormatExpression(paramValue, parameterName) && !isParameterHasOverLimitValue(paramValue)) {
            REPOSITORY.update(ID, parameterName, paramValue);
        }
        return statusCode;
    }

    @Override
    public void deleteData(String ID, String parameterName) {
        Map<String, String> data = REPOSITORY.getDataByID(ID);

        data.remove(parameterName);
    }

    private String getFinalExpression(String ID) {
        List<String> expression = getExpression(ID);
        Map<String, String> repositoryData = REPOSITORY.getDataByID(ID);

        while (isExpressionWithVariables(repositoryData, expression)) {
            convertExpressionWithValue(expression, ID);
        }

        return deleteSpacesAndConvertListToString(expression);
    }

    private List<String> getExpression(String ID) {
        Map<String, String> data = REPOSITORY.getDataByID(ID);

        return Arrays.asList(data
                .get(EXPRESSION)
                .split(EMPTY_SYMBOL));
    }

    private boolean isDataExists(String ID) {
        return REPOSITORY.getRepositoryData().containsKey(ID);
    }

    private void convertExpressionWithValue(List<String> expression, String ID) {
        Map<String, String> data = REPOSITORY.getDataByID(ID);

        for (int i = 0; i < expression.size(); i++) {
            String key = expression.get(i);
            if (data.containsKey(key)) {
                expression.set(i, data.get(key));
            }
        }
    }

    private boolean isExpressionWithVariables(Map map, List<String> expression) {
        return expression.stream().anyMatch(map::containsKey);
    }

    private String getValueFromRepository(String ID, String parameterName) {
        Map<String, String> data = REPOSITORY.getDataByID(ID);
        if (data == null) {
            return null;
        } else {
            return data.get(parameterName);
        }
    }

    private boolean isBadFormatExpression(String paramValue, String parameterName) {
        return parameterName.equals(EXPRESSION) &&
                !(paramValue.contains(PLUS) || paramValue.contains(MINUS) ||
                        paramValue.contains(DIVIDE) || paramValue.contains(MULTIPLY));
    }

    private boolean isParameterHasOverLimitValue(String paramValue) {
        try {
            int i = Integer.parseInt(paramValue);
            return Math.abs(i) > ABS_RANGE;
        } catch (Exception e) {
            return false;
        }
    }

    private int getStatusCode(String valueFromRepository, String parameterName, String paramValue) {
        if (valueFromRepository == null) {
            return STATUS_CREATED;
        } else if (isParameterHasOverLimitValue(paramValue)) {
            return STATUS_FORBIDDEN;
        } else if (isBadFormatExpression(paramValue, parameterName)) {
            return STATUS_BAD_REQUEST;
        } else {
            return STATUS_OK;
        }
    }
}