package com.example.core.api.handler.impl;

import org.springframework.stereotype.Component;

import com.example.core.api.error.ErrorDetail;
import com.example.core.api.handler.IResponseHandler;
import com.example.core.api.res.BaseResponse;
import com.example.core.exception.CustomException;

@Component
public class ResponseHandler implements IResponseHandler {
    public BaseResponse handleSuccessRequest(Object data, long start) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(data);
        long took = System.currentTimeMillis() - start;
        baseResponse.setTook(took);
        return baseResponse;
    }

    public BaseResponse handleErrorRequest(Exception e, Object data, long start) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(data);
        long took = System.currentTimeMillis() - start;
        baseResponse.setTook(took);
        switch (e) {
            case CustomException customException -> {
                ErrorDetail errorDetail = new ErrorDetail();
                errorDetail.setMessage(customException.getMessage());
                baseResponse.addError(errorDetail);
                return baseResponse;
            }
            case Exception exception -> {
                ErrorDetail errorDetail = new ErrorDetail();
                errorDetail.setMessage(exception.getMessage());
                baseResponse.addError(errorDetail);
                return baseResponse;
            }
        }

    }
}
