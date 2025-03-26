package com.softlines.fastpos.security.securityconfiguration;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class FilterResponseWrapper extends HttpServletResponseWrapper {



    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public FilterResponseWrapper(HttpServletResponse response) {
        super(response);

    }
}
