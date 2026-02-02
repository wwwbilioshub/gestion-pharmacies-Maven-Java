package com.pharmacie.util;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	 private static HikariDataSource dataSource;
	    private static final String CONFIG_FILE = "config/database.properties";
	    
	    static {
	        try {
	            initializeDataSource();
	        } catch (Exception e) {
	            throw new RuntimeException("Erreur lors de l'initialisation de la connexion à la base de données", e);
	        }
	    }
	    
	    /**
	     * Initialise le pool de connexions HikariCP
	     */
	    private static void initializeDataSource() throws IOException {
	        Properties props = loadProperties();
	        
	        HikariConfig config = new HikariConfig();
	        
	        // Configuration de la base de données
	        config.setJdbcUrl(props.getProperty("db.url", "jdbc:mysql://localhost:3306/gestion_pharmacies"));
	        config.setUsername(props.getProperty("db.username", "root"));
	        config.setPassword(props.getProperty("db.password", ""));
	        config.setDriverClassName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
	        
	        // Configuration du pool de connexions
	        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size", "10")));
	        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.min-idle", "5")));
	        config.setConnectionTimeout(Long.parseLong(props.getProperty("db.pool.timeout", "30000")));
	        config.setIdleTimeout(Long.parseLong(props.getProperty("db.pool.idle-timeout", "600000")));
	        config.setMaxLifetime(Long.parseLong(props.getProperty("db.pool.max-lifetime", "1800000")));
	        
	        // Propriétés additionnelles pour MySQL
	        config.addDataSourceProperty("cachePrepStmts", "true");
	        config.addDataSourceProperty("prepStmtCacheSize", "250");
	        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
	        config.addDataSourceProperty("useServerPrepStmts", "true");
	        config.addDataSourceProperty("useLocalSessionState", "true");
	        config.addDataSourceProperty("rewriteBatchedStatements", "true");
	        config.addDataSourceProperty("cacheResultSetMetadata", "true");
	        config.addDataSourceProperty("cacheServerConfiguration", "true");
	        config.addDataSourceProperty("elideSetAutoCommits", "true");
	        config.addDataSourceProperty("maintainTimeStats", "false");
	        
	        // Encodage UTF-8
	        config.addDataSourceProperty("characterEncoding", "utf8");
	        config.addDataSourceProperty("useUnicode", "true");
	        
	        // Nom du pool
	        config.setPoolName("PharmacieHikariPool");
	        
	        // Test de connexion
	        config.setConnectionTestQuery("SELECT 1");
	        
	        dataSource = new HikariDataSource(config);
	        
	        System.out.println("✓ Pool de connexions initialisé avec succès");
	    }
	    
	    /**
	     * Charge les propriétés depuis le fichier de configuration
	     */
	    private static Properties loadProperties() throws IOException {
	        Properties props = new Properties();
	        
	        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
	            if (input == null) {
	                System.out.println("⚠ Fichier de configuration non trouvé, utilisation des valeurs par défaut");
	                return props;
	            }
	            props.load(input);
	        }
	        
	        return props;
	    }
	    
	    /**
	     * Obtenir une connexion depuis le pool
	     */
	    public static Connection getConnection() throws SQLException {
	        if (dataSource == null) {
	            throw new SQLException("Le pool de connexions n'est pas initialisé");
	        }
	        return dataSource.getConnection();
	    }
	    
	    /**
	     * Fermer le pool de connexions
	     * À appeler lors de l'arrêt de l'application
	     */
	    public static void closeDataSource() {
	        if (dataSource != null && !dataSource.isClosed()) {
	            dataSource.close();
	            System.out.println("✓ Pool de connexions fermé");
	        }
	    }
	    
	    /**
	     * Obtenir des informations sur le pool
	     */
	    public static String getPoolInfo() {
	        if (dataSource != null) {
	            return String.format(
	                "Pool: %s | Actives: %d | Idle: %d | Total: %d | En attente: %d",
	                dataSource.getPoolName(),
	                dataSource.getHikariPoolMXBean().getActiveConnections(),
	                dataSource.getHikariPoolMXBean().getIdleConnections(),
	                dataSource.getHikariPoolMXBean().getTotalConnections(),
	                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
	            );
	        }
	        return "Pool non initialisé";
	    }
	    
	    /**
	     * Tester la connexion à la base de données
	     */
	    public static boolean testConnection() {
	        try (Connection conn = getConnection()) {
	            return conn != null && !conn.isClosed();
	        } catch (SQLException e) {
	            System.err.println("Erreur de connexion : " + e.getMessage());
	            return false;
	        }
	    }
}
