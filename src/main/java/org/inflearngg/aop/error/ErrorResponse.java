package org.inflearngg.aop.error;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 참고자료
 * https://luvstudy.tistory.com/220#article-4-1--problemdetail-%EA%B8%B0%EB%B3%B8-%EC%82%AC%EC%9A%A9
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private int status;
    private List<FieldError> errors = new ArrayList<>();

    @Builder
    public ErrorResponse(String code, String message, int status, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }


    private List<FieldError> initErrors(List<FieldError> errors) {
        return errors == null ? new ArrayList<>() : errors;
    }

    @Getter
    public static class FieldError {
        private String field;
        private String value;
        private String reason;


        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

}
