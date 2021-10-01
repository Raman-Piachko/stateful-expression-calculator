package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import repository.Repository;
import repository.RepositoryFactory;
import statusCode.exception.BadRequestException;
import statusCode.exception.ForbiddenException;
import statusCode.goodStatus.StatusCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static controllers.ControllerConstants.ABS_RANGE;
import static controllers.ControllerConstants.DIVIDE;
import static controllers.ControllerConstants.EMPTY_SYMBOL;
import static controllers.ControllerConstants.EXPRESSION;
import static controllers.ControllerConstants.MINUS;
import static controllers.ControllerConstants.MULTIPLY;
import static controllers.ControllerConstants.PLUS;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorServiceImpl implements CalculatorService {
    private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();
    private static final Repository REPOSITORY = REPOSITORY_FACTORY.createRepository();


    @Override
    public int calculate(String id) {
        String expression = getFinalExpression(id);
        MathParser mathParser = MathParserFactory.create();
       System.console().printf(expression);
        return mathParser.calculate(expression)
                .doubleValue()
                .intValue();
    }

    @Override
    public StatusCode addData(String id, String parameterName, String paramValue) throws Exception {
        Optional<String> valueFromRepository = REPOSITORY.getValue(id, parameterName);
        if (!REPOSITORY.existData(id)) {
            REPOSITORY.putNewData(id);
        }
        StatusCode statusCode = getStatusCode(valueFromRepository, parameterName, paramValue);
        if (isValidExpression(paramValue, parameterName) && !isParameterHasOverLimitValue(paramValue)) {
            REPOSITORY.update(id, parameterName, paramValue);
        }
        return statusCode;
    }

    @Override
    public void deleteData(String id, String parameterName) {
        REPOSITORY.removeData(id, parameterName);
    }

    private String getFinalExpression(String id) {
        List<String> expression = getExpression(id);
        Map<String, String> repositoryData = REPOSITORY.getDataByID(id);

        while (isExpressionWithVariables(repositoryData, expression)) {
            convertExpressionWithValue(expression, id);
        }

        return deleteSpacesAndConvertListToString(expression);
    }

    private List<String> getExpression(String id) {
        Map<String, String> data = REPOSITORY.getDataByID(id);

        return Arrays.asList(data
                .get(EXPRESSION)
                .split(EMPTY_SYMBOL));
    }

    private void convertExpressionWithValue(List<String> expression, String id) {
        Map<String, String> data = REPOSITORY.getDataByID(id);

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

    private boolean isValidExpression(String paramValue, String parameterName) {
        return parameterName.equals(EXPRESSION)
                && isSpecialSymbol(paramValue);
    }

    private boolean isSpecialSymbol(String paramValue) {
        return paramValue.contains(PLUS)
                || paramValue.contains(MINUS)
                || paramValue.contains(DIVIDE)
                || paramValue.contains(MULTIPLY);
    }

    private boolean isParameterHasOverLimitValue(String paramValue) {
        try {
            int i = Integer.parseInt(paramValue);
            return Math.abs(i) > ABS_RANGE;
        } catch (Exception e) {
            return false;
        }
    }

    private StatusCode getStatusCode(Optional<String> valueFromRepository, String parameterName, String paramValue) throws Exception {
        if (!valueFromRepository.isPresent()) {
            return StatusCode.CREATED;
        } else if (isParameterHasOverLimitValue(paramValue)) {
            throw new ForbiddenException();
        } else if (!isValidExpression(paramValue, parameterName)) {
            throw new BadRequestException();
        } else {
            return StatusCode.INSERTED;
        }
    }
}