package com.example.homework50.server;

import com.example.homework50.service.Candidate;
import com.example.homework50.service.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public static Map<String, String> parseUrlEncoded(String row, String delimiter) {
        String[] pairs = row.split(delimiter);
        Stream<Map.Entry<String, String>> stream = Arrays.stream(pairs)
                .map(Utils::decode)
                .filter(Optional::isPresent)
                .map(Optional::get);

        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Optional<Map.Entry<String, String>> decode(String kv) {
        if (!kv.contains("=")) {
            return Optional.empty();
        }

        String[] pair = kv.split("=");
        if (pair.length != 2) {
            return Optional.empty();
        }

        String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
        String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);

        return Optional.of(Map.entry(key, value));
    }

    public static List<Candidate> getCandidatesFromFile() throws IOException {
        Path path = Paths.get("./data/candidates.json");
        String json = Files.readString(path);
        return GSON.fromJson(json, new TypeToken<List<Candidate>>(){}.getType());
    }

    public static List<Profile> getProfilesFromFile() throws IOException {
        Path path = Paths.get("./data/profiles.json");
        String json = Files.readString(path);
        return GSON.fromJson(json, new TypeToken<List<Profile>>(){}.getType());
    }

    public static void writeProfilesToFile(List<Profile> profiles) {
        String json = GSON.toJson(profiles);
        try (PrintWriter writer = new PrintWriter(new FileWriter("./data/profileDir/profiles.json"))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBooksToFile(List<Candidate> books) {
        String json = GSON.toJson(books);
        try (PrintWriter writer = new PrintWriter(new FileWriter("./data/booksDir/books.json"))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSessionId() {
        return String.valueOf(System.currentTimeMillis()).substring(6, 8) + UUID.randomUUID().toString();
    }
}