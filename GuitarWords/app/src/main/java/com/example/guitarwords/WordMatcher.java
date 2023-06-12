package com.example.guitarwords;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordMatcher {
    private List<String> wordList;
    private WordMatchListener listener;

    public WordMatcher(InputStream inputStream) {
        wordList = new ArrayList<>();
        loadWordList(inputStream);
    }

    private void loadWordList(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim().toLowerCase();
                wordList.add(trimmedLine);
                Log.d("WordMatcher", "Loaded word: " + trimmedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isWordMatch(String inputWord) {
        String lowercaseInput = inputWord.trim().toLowerCase();
        return wordList.contains(lowercaseInput);
    }

    public void setWordMatchListener(WordMatchListener listener) {
        this.listener = listener;
    }

    private void notifyWordMatch(String word) {
        if (listener != null) {
            listener.onWordMatch(word);
        }
    }

    private void notifyWordMismatch(String word) {
        if (listener != null) {
            listener.onWordMismatch(word);
        }
    }
}
