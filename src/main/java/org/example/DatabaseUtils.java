package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    public static void createPersonTable() {
        String createPersonStatement = """
                CREATE TABLE IF NOT EXISTS person (
                    id SERIAL PRIMARY KEY,
                    first_name VARCHAR(100),
                    last_name VARCHAR(100),
                    birthdate DATE
                )
                """;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createPersonStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(Person person) {
        String insertPersonStatement = """
           INSERT INTO person (first_name, last_name, birthdate)
           VALUES(?, ?, ?)
           """;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertPersonStatement)) {
            pstmt.setString(1, person.getFirstName());
            pstmt.setString(2, person.getLastName());
            pstmt.setDate(3, Date.valueOf(person.getBirthday()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePerson(Integer id) {
        String deletePersonStatement = "DELETE FROM person WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(deletePersonStatement)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePerson(Integer id, Person person) {
        String updatePersonStatement = """
           UPDATE person SET first_name = ?, last_name = ?, birthdate = ? WHERE id = ?
           """;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updatePersonStatement)) {
            pstmt.setString(1, person.getFirstName());
            pstmt.setString(2, person.getLastName());
            pstmt.setDate(3, Date.valueOf(person.getBirthday()));
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Person> getAllPersons() {
        String query = "SELECT id, first_name, last_name, birthdate FROM person ORDER BY id";
        List<Person> persons = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Person person = new Person(rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
                person.setId(rs.getInt(1));
                persons.add(person);
                System.out.println(person.getId() + ". " + person.getFirstName() + " " + person.getLastName() + ", Дата рождения: " + person.getBirthday());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (persons.isEmpty()) {
            System.out.println("Список пуст.");
        }
        return persons;
    }
    public static List<Person> getUpcomingBirthdays() {
        String query = """
           SELECT id, first_name, last_name, birthdate,
                  (EXTRACT(DOY FROM birthdate) - EXTRACT(DOY FROM now()) + 365) % 365 AS days_until_birthday
           FROM person
           WHERE (EXTRACT(DOY FROM birthdate) - EXTRACT(DOY FROM now()) + 365) % 365 >= 0
                 AND (EXTRACT(DOY FROM birthdate) - EXTRACT(DOY FROM now()) + 365) % 365 <= 7
           ORDER BY days_until_birthday;
           """;
        List<Person> persons = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Person person = new Person(rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
                person.setId(rs.getInt(1));
                persons.add(person);
                System.out.println(person.getId() + ". " + person.getFirstName() + " " + person.getLastName() + ", Дата рождения: " + person.getBirthday() + ", Дней до дня рождения: " + rs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (persons.isEmpty()) {
            System.out.println("Нет предстоящих дней рождения.");
        }
        return persons;
    }
}