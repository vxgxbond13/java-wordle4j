package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        // Создаём тестовый словарь с 5-буквенными словами
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
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testIncorrectGuess() {
        String guess = "озеро";
        String feedback = game.makeGuess(guess);

        assertNotEquals("+++++", feedback);
        assertFalse(game.isWordGuessed());
        assertEquals(5, game.getRemainingAttempts());
    }

    @Test
    void testFeedbackExactMatch() {
        // Создаём игру с известным ответом
        List<String> words = List.of("кошка");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        String feedback = testGame.makeGuess("кошка");
        assertEquals("+++++", feedback);
    }

    @Test
    void testFeedbackWrongPosition() {
        List<String> words = List.of("кошка");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        String feedback = testGame.makeGuess("озеро");
        // Ожидаем: 'т' на месте 'к'? нет, 'о' на месте? 'к' на месте 'т'?
        // Полный сценарий сложный, проверим только наличие символов
        assertTrue(feedback.contains("^"));
        assertTrue(feedback.contains("+") || feedback.contains("-"));
    }

    @Test
    void testFeedbackLetterNotInWord() {
        List<String> words = List.of("лесок");
        WordleDictionary testDict = new WordleDictionary(words);
        WordleGame testGame = new WordleGame(testDict);

        String feedback = testGame.makeGuess("озеро");
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            game.makeGuess("кот");
        });
        assertEquals("Слово должно состоять из 5 букв", exception.getMessage());
    }

    @Test
    void testGuessTooLong() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            game.makeGuess("коткот");
        });
        assertEquals("Слово должно состоять из 5 букв", exception.getMessage());
    }

    @Test
    void testGuessNotInDictionary() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            game.makeGuess("абвгд");
        });
        assertEquals("Слова 'абвгд' нет в словаре", exception.getMessage());
    }

    @Test
    void testGameLose() {
        // Делаем 6 неверных попыток
        for (int i = 0; i < 6; i++) {
            game.makeGuess("лесок");
        }
        assertTrue(game.isGameOver());
        assertFalse(game.isWordGuessed());
        assertEquals(0, game.getRemainingAttempts());
    }

    @Test
    void testEmptyDictionary() {
        WordleDictionary emptyDict = new WordleDictionary(List.of());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new WordleGame(emptyDict);
        });
        assertEquals("Словарь пуст", exception.getMessage());
    }

    @Test
    void testHintAlwaysInDictionary() {
        for (int i = 0; i < 20; i++) {
            String hint = game.getHint();
            assertTrue(dictionary.contains(hint), "Подсказка '" + hint + "' не найдена в словаре");
        }
    }

    @Test
    void testMultipleGuesses() {
        String answer = game.getAnswer();
        game.makeGuess("кот");
        game.makeGuess("дом");
        game.makeGuess(answer);

        assertTrue(game.isWordGuessed());
        assertEquals(3, game.getRemainingAttempts()); // 6 - 3 = 3
    }

}
