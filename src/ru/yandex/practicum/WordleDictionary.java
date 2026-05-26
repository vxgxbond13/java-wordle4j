package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>();

        for (String word : words) {
            if (word.length() == 5) {
                this.words.add(word);
            }
        }

    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public int size() {
        return words.size();
    }
}
