package com.contrast.demo.controller;

import com.contrast.demo.model.User;
import com.contrast.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class DemoController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Java Spring Boot Demo");
        model.addAttribute("framework", "Spring Boot 2.7.18");
        model.addAttribute("language", "Java 11");
        model.addAttribute("port", "8080");
        return "index";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        return "users";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("newUser") User user, 
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "users";
        }
        
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            try {
                // Intentionally vulnerable SQL-like search for security testing
                List<User> users = userService.searchUsers(query);
                model.addAttribute("users", users);
                model.addAttribute("searchQuery", query);
            } catch (Exception e) {
                model.addAttribute("searchQuery", query);
                model.addAttribute("error", e.getMessage());
            }
        }
        return "search";
    }

    @GetMapping("/file")
    public String fileOperations(@RequestParam(required = false) String filename, Model model) {
        if (filename != null && !filename.isEmpty()) {
            // Intentionally vulnerable file operation for security testing
            String content = userService.readFile(filename);
            model.addAttribute("fileContent", content);
            model.addAttribute("filename", filename);
        }
        return "file";
    }

    @GetMapping("/command")
    public String commandExecution(@RequestParam(required = false) String cmd, Model model) {
        if (cmd != null && !cmd.isEmpty()) {
            // Intentionally vulnerable command execution for security testing
            String output = userService.executeCommand(cmd);
            model.addAttribute("commandOutput", output);
            model.addAttribute("command", cmd);
        }
        return "command";
    }

    @GetMapping("/info")
    @ResponseBody
    public String getSystemInfo(HttpServletRequest request) {
        StringBuilder info = new StringBuilder();
        info.append("Application: Java Spring Boot Demo\n");
        info.append("Framework: Spring Boot 2.7.18\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("OS: ").append(System.getProperty("os.name")).append("\n");
        info.append("Server: ").append(request.getServerName()).append(":").append(request.getServerPort()).append("\n");
        info.append("Context Path: ").append(request.getContextPath()).append("\n");
        info.append("Servlet Container: ").append(request.getServletContext().getServerInfo()).append("\n");
        info.append("User Agent: ").append(request.getHeader("User-Agent")).append("\n");
        return info.toString();
    }
}
