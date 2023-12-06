package ru.sergeiproject.quoter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helper {
    public static void main(String[] args) throws IOException {
        Set<String> set = new HashSet<>();
        List<String> strings = Files.readAllLines(Path.of("/users/sergey/downloads/MOCK_DAT.sql"));

        for (String string : strings) {
            String[] split = string.split(",");
            String key = split[0] +" "+ split[1];
            int size1 = set.size();
            set.add(key);
            int size2 = set.size();
            if(size2==size1){
                System.out.println(key);
                break;
            }
        }
    }
}
