package org.egov.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class AuthRequestWrapper extends HttpServletRequestWrapper {
    public AuthRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getMethod() {
        return "POST";
    }
}
