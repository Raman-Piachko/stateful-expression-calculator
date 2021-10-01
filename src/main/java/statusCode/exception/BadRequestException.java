package statusCode.exception;

public class BadRequestException extends Exception {
    private Integer code;
    private String message;

    public BadRequestException() {
        super();
        code = 404;
        message="BAD";
    }
}
