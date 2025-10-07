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


    private static EntityManagerFactory emf; // for the developing
    private static EntityManagerFactory emfTest; // for the tests
    private static Boolean isTest = false;

    // sets if we drive test og normal
    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }

    // return dev emf og test emf
    public static EntityManagerFactory getEntityManagerFactory(String DBName) {
        if (!getTest()) {
            if (emf == null) {
                emf = createEMF(false, DBName); // normal DB
            }
            return emf;
        } else {
            return getEntityManagerFactoryForTest();
        }
    }

    // return emf for integration test with test containers
    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null){
            setTest(true); // flag for test
            emfTest = createEMF(true,"");  // No DB needed for test
        }
        return emfTest;
    }

    // TODO: IMPORTANT: Add Entity classes here for them to be registered with Hibernate
    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(Hotel.class);
        configuration.addAnnotatedClass(Room.class);
        //configuration.addAnnotatedClass(User.class);
        //configuration.addAnnotatedClass(Role.class);
    }

    // method to build emf for test db in docker container
    private static EntityManagerFactory createEMF(boolean forTest, String DBName) {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();

            // Base properties needed for all environments
            setBaseProperties(props);

            if (forTest) {
                // adding test-specific properties
                props = setTestProperties(props);
            } else if (System.getenv("DEPLOYED") != null) {
                // if deployed (fx cloud), use environment variable
                setDeployedProperties(props, DBName);
            } else {
                // developing environment
                props = setDevProperties(props, DBName);
            }

            configuration.setProperties(props);
            getAnnotationConfiguration(configuration);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
            return sf.unwrap(EntityManagerFactory.class);

        } catch (Throwable ex) {
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
        // Testcontainers driver og URL
        props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
        props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
        //container login
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");

        // Autodetection of entity-classes
        props.put("hibernate.archive.autodetection", "class");

        // SQL logging for test
        props.put("hibernate.show_sql", "true");

        // making schema and dropping it after tests
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // update for production
        return props;
    }
}
