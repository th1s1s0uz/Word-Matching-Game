package com.example.guitarwords;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements WordMatchListener {

    private TextView timerTextView;
    private TextView wordTextView;
    private TextView scoreTextView;

    private Button replayButton;

    private Button resetAlphabetButton;

    private CountDownTimer timer;
    private int score;

    private String[] allLetters = {"a", "b", "c", "d", "e", "f", "g", "h", "ı", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "y", "z", "ö", "ü", "ç", "ş", "i", "ğ"};

    private TextView[] lane1TextViews = new TextView[9];
    private TextView[] lane2TextViews = new TextView[9];
    private TextView[] lane3TextViews = new TextView[9];
    private TextView[] lane4TextViews = new TextView[9];

    private List<String> lane1Letters;
    private List<String> lane2Letters;
    private List<String> lane3Letters;
    private List<String> lane4Letters;

    private static final String ALPHABET = "abcçdefgğhıijklmnoöprsştuüvyz";

    private WordMatcher wordMatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        wordTextView = findViewById(R.id.wordTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        LinearLayout lane1 = findViewById(R.id.lane1);
        LinearLayout lane2 = findViewById(R.id.lane2);
        LinearLayout lane3 = findViewById(R.id.lane3);
        LinearLayout lane4 = findViewById(R.id.lane4);

        lane1Letters = new ArrayList<>(Arrays.asList(allLetters));
        lane2Letters = new ArrayList<>(Arrays.asList(allLetters));
        lane3Letters = new ArrayList<>(Arrays.asList(allLetters));
        lane4Letters = new ArrayList<>(Arrays.asList(allLetters));

        for (int i = 0; i < 9; i++) {
            lane1TextViews[i] = createLetterTextView(lane1);
            lane2TextViews[i] = createLetterTextView(lane2);
            lane3TextViews[i] = createLetterTextView(lane3);
            lane4TextViews[i] = createLetterTextView(lane4);
        }
        replayButton = findViewById(R.id.replayButton);
        replayButton.setVisibility(View.GONE); // Başlangıçta gizli olarak ayarlayın

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tekrar oyna butonuna tıklanınca yapılacak işlemler
                resetGame(); // Oyunu sıfırla ve başlat
            }
        });

        Button resetAlphabetButton = findViewById(R.id.resetAlphabetButton);
        resetAlphabetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAlphabet();
            }
        });




        // Kelime listesini yükleme
        InputStream wordListInputStream = getResources().openRawResource(R.raw.words);
        wordMatcher = new WordMatcher(wordListInputStream);
        wordMatcher.setWordMatchListener(this);

        wordTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Değişiklik öncesi yapılması gerekenler
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Her karakter değiştiğinde yapılması gerekenler
                String currentWord = s.toString().toLowerCase();
                if (currentWord.length() > 10) {
                    wordTextView.setText(""); // 10 harften fazla ise sıfırla
                } else if (wordMatcher.isWordMatch(currentWord)) {
                    // Eşleşme durumunda yapılması gerekenler
                    calculateWordScore(currentWord);
                    wordTextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Değişiklik sonrası yapılması gerekenler
            }
        });



        setLetterTexts();
        startLetterFlow();
        startTimer();


    }
    private void resetGame() {
        // Skoru sıfırla
        score = 0;
        scoreTextView.setText("Score: " + score);

        // Kelime TextView'ını sıfırla
        wordTextView.setText("");

        // Harfleri yeniden ayarla
        setLetterTexts();

        // Zamanlayıcıyı yeniden başlat
        startTimer();

        // Tekrar Oyna butonunu gizle
        replayButton.setVisibility(View.GONE);
    }





    private TextView createLetterTextView(LinearLayout parentLayout) {
        TextView letterTextView = new TextView(this);
        letterTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        letterTextView.setGravity(Gravity.CENTER);
        letterTextView.setTextSize(40); // Harf boyutunu 36 olarak ayarlayabilirsiniz

        letterTextView.setTextColor(getResources().getColor(R.color.white));

        // Tıklanabilirlik özelliği ekleme
        letterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tıklanan harf ile ilgili işlemleri burada yapabilirsiniz
                String letter = ((TextView) v).getText().toString().toLowerCase();
                wordTextView.append(letter);
            }
        });

        parentLayout.addView(letterTextView);

        return letterTextView;
    }




    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
                timerTextView.setText(time);
            }

            @Override
            public void onFinish() {
                timer.cancel();
                replayButton.setVisibility(View.VISIBLE);
            }
        };
        timer.start();
    }
    private void resetAlphabet() {
        Random random = new Random();

        for (TextView[] laneTextViews : new TextView[][]{lane1TextViews, lane2TextViews, lane3TextViews, lane4TextViews}) {
            for (TextView textView : laneTextViews) {
                int randomIndex = random.nextInt(allLetters.length);
                String letter = allLetters[randomIndex];
                textView.setText(letter);
            }
        }
    }



    private void startLetterFlow() {
        List<TextView[]> lanesTextViews = new ArrayList<>();
        lanesTextViews.add(lane1TextViews);
        lanesTextViews.add(lane2TextViews);
        lanesTextViews.add(lane3TextViews);
        lanesTextViews.add(lane4TextViews);

        for (TextView[] laneTextViews : lanesTextViews) {
            new CountDownTimer(60000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    // Süre dolduğunda yapılacak işlemler
                    // Skora herhangi bir şey eklenmemesi için bu bloğu kaldırabilirsiniz
                }
            }.start();
        }
    }


    private void calculateWordScore(String word) {
        int wordLength = word.length();
        int wordScore = wordLength * 10;
        score += wordScore;

        scoreTextView.setText("Score: " + score);
    }

    private String getCurrentWord() {
        StringBuilder builder = new StringBuilder();
        for (TextView[] laneTextViews : new TextView[][]{lane1TextViews, lane2TextViews, lane3TextViews, lane4TextViews}) {
            for (TextView textView : laneTextViews) {
                builder.append(textView.getText().toString());
            }
        }
        return builder.toString();
    }

    private void calculateScore() {
        String currentWord = getCurrentWord();
        calculateWordScore(currentWord);
    }

    private void setLetterTexts() {
        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            // Lane 1
            if (lane1Letters.size() > 0) {
                int randomIndex = random.nextInt(lane1Letters.size());
                String letter = lane1Letters.remove(randomIndex);
                lane1TextViews[i].setText(letter);
            } else {
                lane1TextViews[i].setText("");
            }

            // Lane 2
            if (lane2Letters.size() > 0) {
                int randomIndex = random.nextInt(lane2Letters.size());
                String letter = lane2Letters.remove(randomIndex);
                lane2TextViews[i].setText(letter);
            } else {
                lane2TextViews[i].setText("");
            }

            // Lane 3
            if (lane3Letters.size() > 0) {
                int randomIndex = random.nextInt(lane3Letters.size());
                String letter = lane3Letters.remove(randomIndex);
                lane3TextViews[i].setText(letter);
            } else {
                lane3TextViews[i].setText("");
            }

            // Lane 4
            if (lane4Letters.size() > 0) {
                int randomIndex = random.nextInt(lane4Letters.size());
                String letter = lane4Letters.remove(randomIndex);
                lane4TextViews[i].setText(letter);
            } else {
                lane4TextViews[i].setText("");
            }
        }
    }
    @Override
    public void onWordMatch(String word) {
        // Eşleşen kelimenin skorunu hesaplayıp skor TextView'ına yazma işlemleri
        calculateWordScore(word);
        wordTextView.setText("");
    }
    @Override
    public void onWordMismatch(String word) {
        if (wordTextView.getText().toString().length() > 10) {
            wordTextView.setText("");
        }
        // Diğer işlemler
    }


}