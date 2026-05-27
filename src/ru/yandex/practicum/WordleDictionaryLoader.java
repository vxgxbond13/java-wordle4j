package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary load(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                line = line.trim().toLowerCase();

                if (line.length() != 5) {
                    continue;
                }

                line = line.replace("ё", "е");

                words.add(line);
            }
        }

        if (words.isEmpty()) {
            throw new RuntimeException("Словарь не содержит слов длиной 5 букв");
        }

        return new WordleDictionary(words);
    }
}
