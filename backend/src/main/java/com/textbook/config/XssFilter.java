package com.textbook.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }

    static class XssRequestWrapper extends HttpServletRequestWrapper {
        XssRequestWrapper(HttpServletRequest request) { super(request); }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? cleanXss(value) : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;
            String[] cleaned = new String[values.length];
            for (int i = 0; i < values.length; i++) cleaned[i] = cleanXss(values[i]);
            return cleaned;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return value != null ? cleanXss(value) : null;
        }

        private String cleanXss(String value) {
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("(?i)script", "scr_ipt");
            return value;
        }
    }
}
