package app.config;

import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {

    private static EntityManagerFactory emf;
    private static EntityManagerFactory emfTest;
    private static Boolean isTest = false;

    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }

    public static EntityManagerFactory getEntityManagerFactory(String DBName) {
        if (emf == null)
            emf = createEMF(getTest(), DBName);
        return emf;
    }

    // for tests
    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null){
            setTest(true);
            emfTest = createEMF(getTest(), "");  // No DB needed for test
        }
        return emfTest;
    }

    // TODO: IMPORTANT: Add Entity classes here for them to be registered with Hibernate
    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(Hotel.class);
        configuration.addAnnotatedClass(Room.class);
    }

    private static EntityManagerFactory createEMF(boolean forTest, String DBName) {
        // Parameters:
        // - forTest: a boolean flag that says if we are running a test environment.
        // - DBName: the name of the database to connect to.
        // Returns an EntityManagerFactory, which is used to manage database connections and entities.

        try {
            // Creates a new Hibernate Configuration object.
            // This object holds all the settings Hibernate needs to connect to the database.
            Configuration configuration = new Configuration();

            // Creates a Properties object to hold key-value pairs for Hibernate configuration.
            Properties props = new Properties();

            // Calls a helper method to set the basic Hibernate properties
            // like dialect, driver class, show_sql, //hbm2ddl.auto, etc.
            setBaseProperties(props);

            // If running tests, replace or add test-specific properties.
            // Example: use an in-memory or test container database, and auto-create/drop tables.
            if (forTest) {
                props = setTestProperties(props);

                // If the DEPLOYED environment variable exists, use production/deployed database properties.
                // This usually comes from environment variables like DB_USERNAME, DB_PASSWORD, CONNECTION_STR.
            } else if (System.getenv("DEPLOYED") != null) {
                setDeployedProperties(props, DBName);
            } else {
                // Otherwise, use development database settings (local DB with default username/password).
                props = setDevProperties(props, DBName);
            }

            // Apply all the properties to the Hibernate Configuration object.
            configuration.setProperties(props);

            // Registers all annotated entity classes (e.g., Hotel.class, Room.class) with Hibernate.
            // Hibernate needs to know which classes represent database tables.
            getAnnotationConfiguration(configuration);

            // Builds a ServiceRegistry, which is Hibernate’s internal object
            // that knows about the environment, database connections, and other services.
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // Builds a SessionFactory from the configuration and service registry.
            // The SessionFactory is the main object used by Hibernate to create database sessions.
            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);

            // Converts the Hibernate SessionFactory into a JPA EntityManagerFactory.
            // This allows the rest of your app to use JPA (standard API) instead of Hibernate-specific code.
            EntityManagerFactory emf = sf.unwrap(EntityManagerFactory.class);

            // Returns the EntityManagerFactory to the caller, ready to use.
            return emf;
        }
        catch (Throwable ex) {
            // If anything goes wrong (//database unreachable, wrong config, etc.), print the error.
            // Throws an ExceptionInInitializerError, which stops the application from starting.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties setBaseProperties(Properties props) {
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        return props;
    }

    private static Properties setDeployedProperties(Properties props, String DBName) {
        props.setProperty("hibernate.connection.url", System.getenv("CONNECTION_STR") + DBName);
        props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        return props;
    }

    private static Properties setDevProperties(Properties props, String DBName) {
        props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/" + DBName);
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        return props;
    }

    private static Properties setTestProperties(Properties props) {
        //props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
        props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        props.put("hibernate.archive.autodetection", "class");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // update for production
        return props;
    }
}
