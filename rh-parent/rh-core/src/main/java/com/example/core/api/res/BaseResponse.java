package com.example.core.api.res;

import java.util.ArrayList;
import java.util.List;

import com.example.core.api.error.ErrorDetail;

import lombok.Data;

@Data
public class BaseResponse {
    private String message;
    private long took;
    private Object data;
    private List<ErrorDetail> errors;

    public void addError(ErrorDetail errorDetail) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(errorDetail);
    }
}
