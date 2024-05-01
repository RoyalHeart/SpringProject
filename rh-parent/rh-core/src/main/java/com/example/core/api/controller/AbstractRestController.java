package com.example.core.api.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.core.api.handler.IResponseHandler;

public abstract class AbstractRestController {
    @Autowired
    protected IResponseHandler responseHandler;
}
