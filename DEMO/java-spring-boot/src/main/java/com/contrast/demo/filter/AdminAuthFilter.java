package com.contrast.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Simple session-based admin authentication filter
 */
public class AdminAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getServletPath();
        
        // Allow login page and logout
        if (path.equals("/admin/login") || path.equals("/admin/logout")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check session for authenticated user
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("adminUser") != null) {
            chain.doFilter(request, response);
            return;
        }
        
        // Not authenticated, redirect to login
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login");
    }
    
    @Override
    public void destroy() {
    }
}
