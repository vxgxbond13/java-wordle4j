package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private final List<String> previousGuesses;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.steps = 6;
        this.previousGuesses = new ArrayList<>();

        List<String> words = dictionary.getWords();
        if (words.isEmpty()) {
            throw new RuntimeException("Словарь пуст");
        }
        Random random = new Random();
        this.answer = words.get(random.nextInt(words.size()));
    }

    public int getRemainingAttempts() {
        return steps;
    }

    public boolean isGameOver() {
        return steps <= 0 || isWordGuessed();
    }

    public boolean isWordGuessed() {
        return !previousGuesses.isEmpty() && previousGuesses.get(previousGuesses.size() - 1).equals(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public String makeGuess(String guess) {
        validateGuess(guess);

        previousGuesses.add(guess);
        steps--;

        return generateFeedback(guess);
    }

    public String getHint() {
        List<String> possibleWords = new ArrayList<>();

        for (String word : dictionary.getWords()) {
            if (isWordPossible(word)) {
                possibleWords.add(word);
            }
        }

        if (possibleWords.isEmpty()) {
            return "Нет подходящих слов";
        }

        Random random = new Random();
        return possibleWords.get(random.nextInt(possibleWords.size()));
    }

    private void validateGuess(String guess) {
        if (guess == null || guess.length() != 5) {
            throw new IllegalArgumentException("Слово должно состоять из 5 букв");
        }

        if (!dictionary.contains(guess)) {
            throw new IllegalArgumentException("Слова '" + guess + "' нет в словаре");
        }
    }

    private String generateFeedback(String guess) {
        char[] feedback = new char[5];
        Arrays.fill(feedback, '-');

        char[] answerChars = answer.toCharArray();
        char[] guessChars = guess.toCharArray();

        // Сначала ищем точные совпадения (+)
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == answerChars[i]) {
                feedback[i] = '+';
                answerChars[i] = 0;
                guessChars[i] = 0;
            }
        }

        // Теперь ищем буквы, которые есть, но не на своих местах (^)
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] != 0) {
                for (int j = 0; j < 5; j++) {
                    if (answerChars[j] != 0 && guessChars[i] == answerChars[j]) {
                        feedback[i] = '^';
                        answerChars[j] = 0;
                        break;
                    }
                }
            }
        }

        return new String(feedback);
    }

    private boolean isWordPossible(String word) {
        for (String guess : previousGuesses) {
            if (!matchesFeedback(word, guess, generateFeedback(guess))) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesFeedback(String word, String guess, String feedback) {
        char[] wordChars = word.toCharArray();
        char[] guessChars = guess.toCharArray();
        boolean[] used = new boolean[5];

        // Проверяем точные совпадения (+)
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == '+') {
                if (wordChars[i] != guessChars[i]) {
                    return false;
                }
                used[i] = true;
                wordChars[i] = 0;
                guessChars[i] = 0;
            }
        }

        // Проверяем буквы, которые есть, но не на своих местах (^)
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == '^') {
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!used[j] && guessChars[i] == wordChars[j]) {
                        found = true;
                        used[j] = true;
                        wordChars[j] = 0;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }

        // Проверяем, что букв, которых нет в слове (-), действительно нет
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == '-') {
                for (int j = 0; j < 5; j++) {
                    if (guessChars[i] == wordChars[j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}

