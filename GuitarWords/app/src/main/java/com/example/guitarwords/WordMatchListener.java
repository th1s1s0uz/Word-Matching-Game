package com.example.guitarwords;

import java.util.Set;

public interface WordMatchListener {
    void onWordMatch(String word);
    void onWordMismatch(String word);

}

