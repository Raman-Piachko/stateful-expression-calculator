package calculator;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.Repository;
import response.codes.codes.StatusCode;
import response.codes.exception.BadRequestException;
import response.codes.exception.ForbiddenException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static constants.ControllerConstants.ABS_RANGE;
import static constants.ControllerConstants.DIVIDE;
import static constants.ControllerConstants.EMPTY_SYMBOL;
import static constants.ControllerConstants.EXPRESSION;
import static constants.ControllerConstants.MINUS;
import static constants.ControllerConstants.MULTIPLY;
import static constants.ControllerConstants.PARSING_INFO;
import static constants.ControllerConstants.PLUS;
import static utils.ConversionUtil.deleteSpacesAndConvertListToString;


public class CalculatorServiceImpl implements CalculatorService {
    private static final WebCalculatorFactory FACTORY = new WebCalculatorFactory();
    private static final Repository REPOSITORY = FACTORY.createRepository();
    private static final Logger LOGGER = LogManager.getLogger(CalculatorServiceImpl.class);

    @Override
    public int calculate(String id) {
        String expression = getFinalExpression(id);
        MathParser mathParser = MathParserFactory.create();

        return mathParser.calculate(expression)
                .doubleValue()
                .intValue();
    }

    @Override
    public StatusCode addData(String id, String parameterName, String paramValue) throws Exception {
        Optional<String> valueFromRepository = REPOSITORY.getValue(id, parameterName);
        REPOSITORY.insertNewData(id);
        StatusCode statusCode = getStatusCode(valueFromRepository, parameterName, paramValue);
        REPOSITORY.update(id, parameterName, paramValue);

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

        String expression = data.get(EXPRESSION);
        return Arrays.asList(expression
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

    private boolean isExpressionWithVariables(Map<String, String> map, List<String> expression) {
        return expression.stream().anyMatch(map::containsKey);
    }

    private boolean isValidExpression(String paramValue) {
        return isSpecialSymbol(paramValue);
    }

    private boolean isExpressionName(String parameterName) {
        return EXPRESSION.equalsIgnoreCase(parameterName);
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
            return Math.abs(i) <= ABS_RANGE;
        } catch (Exception e) {
            LOGGER.info(String.format(PARSING_INFO, paramValue));
            return true;
        }

    }

    private StatusCode getStatusCode(Optional<String> valueFromRepository, String parameterName, String paramValue) throws Exception {
        if (isExpressionName(parameterName)) {
            if (isValidExpression(paramValue)) {
                if (valueFromRepository.isEmpty()) {
                    return StatusCode.CREATED;
                } else return StatusCode.INSERTED;
            } else throw new BadRequestException();
        } else if (isParameterHasOverLimitValue(paramValue)) {
            if (valueFromRepository.isEmpty()) {
                return StatusCode.CREATED;
            } else return StatusCode.INSERTED;
        } else throw new ForbiddenException();
    }
}