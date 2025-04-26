package io.java.modules.autoparts.utils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL Fix Utility
 * Helps with handling reserved keywords in SQL queries
 *
 * @author Jiasi Lu
 */
public class SqlFixUtil {
    
    // List of MySQL reserved keywords that are used as column names in our entities
    private static final Map<String, String> RESERVED_KEYWORDS = new HashMap<>();
    
    static {
        // Add MySQL reserved words used as column names
        RESERVED_KEYWORDS.put("condition", "`condition`");
        // Add more reserved keywords if needed
    }
    
    /**
     * Checks if the column name is a reserved keyword
     *
     * @param columnName The column name to check
     * @return True if it's a reserved keyword, false otherwise
     */
    public static boolean isReservedKeyword(String columnName) {
        return RESERVED_KEYWORDS.containsKey(columnName.toLowerCase());
    }
    
    /**
     * Gets the quoted column name if it's a reserved keyword
     *
     * @param columnName The column name to check
     * @return The properly quoted column name if it's reserved, or the original name
     */
    public static String getQuotedColumnName(String columnName) {
        String lowerCaseName = columnName.toLowerCase();
        if (RESERVED_KEYWORDS.containsKey(lowerCaseName)) {
            return RESERVED_KEYWORDS.get(lowerCaseName);
        }
        return columnName;
    }
    
    /**
     * Executes alter table statements to fix column names that are reserved keywords
     * 
     * @param tableName The table to fix
     */
    public static String generateAlterTableStatement(String tableName) {
        StringBuilder sql = new StringBuilder();
        
        // Generate ALTER TABLE statements for each reserved keyword
        for (Map.Entry<String, String> entry : RESERVED_KEYWORDS.entrySet()) {
            String originalName = entry.getKey();
            String quotedName = entry.getValue();
            
            // Create a temp column, move data, drop original, rename temp
            sql.append("-- Fix for reserved keyword: ").append(originalName).append("\n");
            sql.append("ALTER TABLE ").append(tableName).append(" CHANGE COLUMN ")
               .append(originalName).append(" ").append(quotedName).append(" VARCHAR(20);\n\n");
        }
        
        return sql.toString();
    }
} 