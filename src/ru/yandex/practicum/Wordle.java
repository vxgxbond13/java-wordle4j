package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("game_log.txt");
             Scanner scanner = new Scanner(System.in)) {

            // 2. Создаём загрузчик словаря
            WordleDictionaryLoader loader = new WordleDictionaryLoader();

            // 3. Загружаем словарь
            WordleDictionary dictionary = loader.load("words_ru.txt");
            log.println("Словарь загружен. Количество слов: " + dictionary.size());
            System.out.println("Словарь загружен. Количество слов: " + dictionary.size());

            // 4. Создаём игру
            WordleGame game = new WordleGame(dictionary);

            System.out.println("Игра началась! У вас 6 попыток угадать слово из 5 букв.");

            // 5. Игровой цикл
            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getRemainingAttempts());
                System.out.print("Введите слово (или Enter для подсказки): ");
                String input = scanner.nextLine().trim().toLowerCase();

                // Подсказка
                if (input.isEmpty()) {
                    String hint = game.getHint();
                    System.out.println("Подсказка: " + hint);
                    log.println("Пользователь запросил подсказку: " + hint);
                    continue;
                }

                // Проверка длины
                if (input.length() != 5) {
                    System.out.println("Слово должно состоять из 5 букв!");
                    continue;
                }

                try {
                    String feedback = game.makeGuess(input);
                    System.out.println(input);
                    System.out.println(feedback);
                    log.println("Ход: " + input + " -> " + feedback);

                    if (game.isWordGuessed()) {
                        System.out.println("Поздравляю! Вы угадали слово " + game.getAnswer() + "!");
                        log.println("Пользователь выиграл. Слово: " + game.getAnswer());
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    log.println("Ошибка ввода: " + e.getMessage());
                }
            }

            // 6. Вывод результата
            if (!game.isWordGuessed()) {
                System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());
                log.println("Пользователь проиграл. Слово: " + game.getAnswer());
            }

            System.out.println("Игра завершена.");
            log.println("Игра завершена.");

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



