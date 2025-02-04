package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static final Scanner scanner = new Scanner(System.in);

    public static void addFiveTestPersonsInTable() {
        DatabaseUtils.addPerson(new Person("Костя", "Иванов",LocalDate.of(2003,5,6)));
        DatabaseUtils.addPerson(new Person("Иван", "Князев",LocalDate.of(1999,2,5)));
        DatabaseUtils.addPerson(new Person("Петр", "Васев",LocalDate.of(2007,2,15)));
        DatabaseUtils.addPerson(new Person("Михаил", "Ферстапин",LocalDate.of(2003,11,29)));
        DatabaseUtils.addPerson(new Person("Абрам", "Соларлабов",LocalDate.of(1989,9,13)));
    }

    public static void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Показать весь список дней рождения");
        System.out.println("2. Показать ближайшие дни рождения");
        System.out.println("3. Добавить запись");
        System.out.println("4. Удалить запись");
        System.out.println("5. Редактировать запись");
        System.out.println("0. Выйти");
        System.out.print("Выберите действие: ");
    }

    public static int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void showAllPersons() {
        List<Person> persons = DatabaseUtils.getAllPersons();
        if (persons.isEmpty()) {
            System.out.println("Список пуст.");
        }
    }

    public static void showUpcomingBirthdays() {
        List<Person> persons = DatabaseUtils.getUpcomingBirthdays();
        if (persons.isEmpty()) {
            System.out.println("Нет предстоящих дней рождения.");
        }
    }

    public static void addPerson() {
        System.out.print("Введите имя: ");
        String firstName = scanner.next();
        System.out.print("Введите фамилию: ");
        String lastName = scanner.next();
        System.out.print("Введите дату рождения (ГГГГ-ММ-ДД): ");
        String birthdateStr = scanner.next();

        try {
            LocalDate birthdate = LocalDate.parse(birthdateStr);
            DatabaseUtils.addPerson(new Person(firstName, lastName, birthdate));
            System.out.println("Запись добавлена!");
        } catch (Exception e) {
            System.out.println("Ошибка ввода даты. Убедитесь, что формат правильный!");
        }
    }

    public static void deletePerson() {
        System.out.print("Введите ID записи для удаления: ");
        int id = getChoice();
        DatabaseUtils.deletePerson(id);
        System.out.println("Запись удалена!");
    }

    public static void updatePerson() {
        System.out.print("Введите ID записи для редактирования: ");
        int id = getChoice();
        System.out.print("Введите новое имя: ");
        String firstName = scanner.next();
        System.out.print("Введите новую фамилию: ");
        String lastName = scanner.next();
        System.out.print("Введите новую дату рождения (ГГГГ-ММ-ДД): ");
        String birthdateStr = scanner.next();

        try {
            LocalDate birthdate = LocalDate.parse(birthdateStr);
            DatabaseUtils.updatePerson(id, new Person(firstName, lastName, birthdate));
            System.out.println("Запись обновлена!");
        } catch (Exception e) {
            System.out.println("Ошибка ввода даты!");
        }
    }

    public static void exit() {
        System.out.println("Выход из программы...");
        scanner.close();
        System.exit(0);
    }
}