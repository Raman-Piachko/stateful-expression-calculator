package statusCode.exception;

import static controllers.ControllerConstants.OVER_RANGE;
import static controllers.ControllerConstants.STATUS_FORBIDDEN;

public class ForbiddenException extends RuntimeException {
    private Integer statusCode = STATUS_FORBIDDEN;
    private String errorMessage = OVER_RANGE;

    public ForbiddenException() {
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
