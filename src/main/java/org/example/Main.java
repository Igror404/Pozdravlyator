package org.example;

import static org.example.Menu.*;

public class Main {
    public static void main(String[] args) {
        DatabaseUtils.createPersonTable();
        addFiveTestPersonsInTable();
        showUpcomingBirthdays();

        while (true) {
            printMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> showAllPersons();
                case 2 -> showUpcomingBirthdays();
                case 3 -> addPerson();
                case 4 -> deletePerson();
                case 5 -> updatePerson();
                case 0 -> exit();
                default -> System.out.println("Некорректный ввод. Попробуйте снова.");
            }
        }
    }


}