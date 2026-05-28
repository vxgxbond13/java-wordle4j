package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class WordleTest {

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> words = List.of("море", "небо", "поле", "гора", "река", "озеро", "лесок");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(dictionary);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getAnswer());
        assertEquals(6, game.getRemainingAttempts());
        assertFalse(game.isGameOver());
        assertFalse(game.isWordGuessed());
    }

    @Test
    void testCorrectGuess() {
        String answer = game.getAnswer();
        String feedback = game.makeGuess(answer);
        assertEquals("+++++", feedback);
        assertTrue(game.isWordGuessed());
    }

    @Test
    void testIncorrectGuess() {
        String guess = "озеро";
        // Если ответ "озеро", пропускаем
        if (game.getAnswer().equals(guess)) {
            return;
        }
        String feedback = game.makeGuess(guess);
        assertNotEquals("+++++", feedback);
        assertFalse(game.isWordGuessed());
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testFeedbackExactMatch() {
        List<String> words = List.of("кошка");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);
        String feedback = testGame.makeGuess("кошка");
        assertEquals("+++++", feedback);
    }

    @Test
    void testFeedbackWrongPosition() {

        List<String> words = List.of("кошка", "мышка");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        String feedback = testGame.makeGuess("мышка");

        assertTrue(feedback.contains("^") || feedback.contains("+"));
    }

    @Test
    void testFeedbackLetterNotInWord() {
        List<String> words = List.of("лесок", "тяпра");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        // Берём слово из словаря, которое точно не совпадает с ответом
        String answer = testGame.getAnswer();
        String guess;

        if (answer.equals("лесок")) {
            guess = "тяпра";
        } else {
            guess = "лесок";
        }

        String feedback = testGame.makeGuess(guess);
        assertEquals("-----", feedback);
    }

    @Test
    void testHintNotNull() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void testGuessTooShort() {
        assertThrows(IllegalArgumentException.class, () -> game.makeGuess("кот"));
    }

    @Test
    void testGuessTooLong() {
        assertThrows(IllegalArgumentException.class, () -> game.makeGuess("коткот"));
    }

    @Test
    void testGuessNotInDictionary() {
        assertThrows(IllegalArgumentException.class, () -> game.makeGuess("абвгд"));
    }

    @Test
    void testGameLose() {
        List<String> words = List.of("лесок", "озеро", "небо");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        // Получаем загаданное слово
        String answer = testGame.getAnswer();

        // Выбираем другое слово из словаря (не ответ)
        String wrongGuess;
        if (answer.equals("лесок")) {
            wrongGuess = "озеро";
        } else {
            wrongGuess = "лесок";
        }

        for (int i = 0; i < 6; i++) {
            testGame.makeGuess(wrongGuess);
        }

        assertTrue(testGame.isGameOver());
        assertFalse(testGame.isWordGuessed());
        assertEquals(0, testGame.getRemainingAttempts());
    }

    @Test
    void testEmptyDictionary() {
        WordleDictionary emptyDict = new WordleDictionary(List.of());
        assertThrows(RuntimeException.class, () -> new WordleGame(emptyDict));
    }

    @Test
    void testHintAlwaysInDictionary() {
        for (int i = 0; i < 20; i++) {
            String hint = game.getHint();
            assertTrue(dictionary.contains(hint), "Подсказка '" + hint + "' не найдена в словаре");
        }
    }
}
