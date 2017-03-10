package org.egov.model;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sumansn on 3/10/17.
 */
public class AuthHttpServletRequestWrapper {
    private HttpServletRequest request;

    public AuthHttpServletRequestWrapper(HttpServletRequest originalRequest){
        this.request = originalRequest;
    }
}
