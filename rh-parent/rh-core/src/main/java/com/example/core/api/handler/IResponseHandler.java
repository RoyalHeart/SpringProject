package com.example.core.api.handler;

import com.example.core.api.res.BaseResponse;

public interface IResponseHandler {

    public BaseResponse handleSuccessRequest(Object data, long start);

    public BaseResponse handleErrorRequest(Exception e, Object data, long start);
}
