package com.sashutosh.streams.exercise;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileStreams {

    public static long countUniqueWords(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName), Charset.defaultCharset())) {
            return lines
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
