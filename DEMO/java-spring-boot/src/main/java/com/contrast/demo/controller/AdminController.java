package com.contrast.demo.controller;

import com.contrast.demo.model.AdminUser;
import com.contrast.demo.repository.AdminUserRepository;
import com.contrast.demo.service.SqlExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Controller - Simple session-based authentication
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @Autowired
    private SqlExecutionService sqlExecutionService;
    
    /**
     * Admin login page
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "admin-login";
    }
    
    /**
     * Handle admin login
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session) {
        
        // Simple authentication - check username and password
        AdminUser user = adminUserRepository.findByUsername(username).orElse(null);
        
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            session.setAttribute("adminUser", user);
            return "redirect:/admin/dashboard";
        }
        
        return "redirect:/admin/login?error=true";
    }
    
    /**
     * Admin dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AdminUser user = (AdminUser) session.getAttribute("adminUser");
        model.addAttribute("username", user.getUsername());
        
        // Get all users for display
        List<AdminUser> allUsers = adminUserRepository.findAll();
        model.addAttribute("users", allUsers);
        
        return "admin-dashboard";
    }
    
    /**
     * Logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
    
    /**
     * SQL Query Tool - VULNERABILITY: SQL Injection
     */
    @GetMapping("/sql")
    public String sqlPage() {
        return "admin-sql";
    }
    
    /**
     * Execute SQL query - VULNERABILITY: Allows arbitrary SQL execution
     */
    @PostMapping("/sql/execute")
    @ResponseBody
    public Map<String, Object> executeSql(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        try {
            // VULNERABILITY: Direct SQL execution without sanitization
            Object result = sqlExecutionService.executeRawSql(query);
            response.put("success", true);
            response.put("result", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
    
    /**
     * Debug endpoint - VULNERABILITY: Information disclosure
     */
    @GetMapping("/debug")
    @ResponseBody
    public Map<String, Object> debug(HttpServletRequest request) {
        Map<String, Object> debugInfo = new HashMap<>();
        
        // VULNERABILITY: Exposes sensitive information
        debugInfo.put("sessionId", request.getSession().getId());
        
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(name -> 
            headers.put(name, request.getHeader(name))
        );
        debugInfo.put("headers", headers);
        
        Map<String, String> cookies = new HashMap<>();
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        debugInfo.put("cookies", cookies);
        
        debugInfo.put("remoteAddr", request.getRemoteAddr());
        debugInfo.put("method", request.getMethod());
        debugInfo.put("uri", request.getRequestURI());
        
        return debugInfo;
    }
}
