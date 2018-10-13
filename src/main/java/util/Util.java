package util;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Util {

    public static String readStream(InputStream is) {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

        return buffer.lines().collect(Collectors.joining("\n"));
    }

    static String asString(ServletInputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String parseOrderNumber(String text) {
        String removedQuotes = text.replace("{", "").replace("}", "").trim();
        String[] JSONItemsWithValues = removedQuotes.split(",");

        for (String itemWithValue : JSONItemsWithValues) {
            String trimmedItemWithValue = itemWithValue.trim();

            if (trimmedItemWithValue.startsWith("\"orderNumber\"")) {
                String[] separatedItemAndValue = trimmedItemWithValue.split(":");
                return separatedItemAndValue[1].replace("\"", "").trim();
            }
        }
        return null;
    }
}
