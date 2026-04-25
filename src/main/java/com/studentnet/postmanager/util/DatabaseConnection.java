package com.studentnet.postmanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Утилітний клас для керування підключенням до бази даних.
 * Зчитує конфігурацію з файлу db.properties.
 */
public class DatabaseConnection {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Хай йому грець! Не можу знайти файл db.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Встановлює та повертає нове з'єднання з базою даних.
     * Обов'язково закривайте Connection після використання (через try-with-resources).
     *
     * @return Об'єкт Connection для роботи з БД
     * @throws SQLException якщо не вдалося встановити з'єднання
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}