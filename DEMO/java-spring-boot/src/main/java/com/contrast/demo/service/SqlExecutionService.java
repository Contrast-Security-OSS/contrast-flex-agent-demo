package com.contrast.demo.service;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for executing raw SQL queries - INTENTIONALLY VULNERABLE
 * WARNING: This service allows arbitrary SQL execution without any validation
 */
@Service
public class SqlExecutionService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * VULNERABILITY: Executes arbitrary SQL without any sanitization or validation
     * This allows SQL injection attacks and unauthorized data access/modification
     */
    @Transactional
    public Object executeRawSql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        
        // Try to determine if it's a SELECT query
        String trimmedSql = sql.trim().toUpperCase();
        if (trimmedSql.startsWith("SELECT")) {
            List<?> results = query.getResultList();
            
            // Convert results to a more readable format
            List<Map<String, Object>> formattedResults = new ArrayList<>();
            for (Object result : results) {
                if (result instanceof Object[]) {
                    Object[] row = (Object[]) result;
                    Map<String, Object> rowMap = new HashMap<>();
                    for (int i = 0; i < row.length; i++) {
                        rowMap.put("column_" + i, row[i]);
                    }
                    formattedResults.add(rowMap);
                } else {
                    Map<String, Object> rowMap = new HashMap<>();
                    rowMap.put("value", result);
                    formattedResults.add(rowMap);
                }
            }
            return formattedResults;
        } else {
            // For INSERT, UPDATE, DELETE, etc.
            int affectedRows = query.executeUpdate();
            Map<String, Object> result = new HashMap<>();
            result.put("affectedRows", affectedRows);
            result.put("message", "Query executed successfully");
            return result;
        }
    }
}
