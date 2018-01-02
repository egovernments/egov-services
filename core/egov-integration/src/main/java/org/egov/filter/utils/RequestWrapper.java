package org.egov.filter.utils;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
	
	private String payload;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        convertInputStreamToString(request);
    }

    private void convertInputStreamToString(HttpServletRequest request) {
        try {
            payload = IOUtils.toString(request.getInputStream());
            //request.getRequestURI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload){
        this.payload = payload;
    }

    @Override
    public int getContentLength() {
        return payload.length();
    }

    @Override
    public long getContentLengthLong() {
        return payload.length();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamWrapper(payload.getBytes());
    }
    
    /*@Override
    public String getRequestURI() {
      //  final String originalUri = ;
    	System.out.println("getRequestURI>>>>>>>");
        return "/abc/xyz"; 
    }

    @Override
    public StringBuffer getRequestURL() {
      //  finStringBufferal String originalUri = ;
    	System.out.println("getRequestURL>>>>>>>");
    	StringBuffer stringBuffer = new StringBuffer("/abc/xyz");
        return stringBuffer; 
    }*/
}
