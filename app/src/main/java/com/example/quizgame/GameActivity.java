package com.example.quizgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizgame.model.WordItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
   private ImageView mQuestionImageView;
   private Button[] mButtons = new Button[4];
   private TextView mScoreText;

   private String mAnswerWord;
   private Random mRandom;
   private Integer mScore = 0;
   private Integer mCount = 0;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game);

      mScoreText = findViewById(R.id.score_text_view);

      mQuestionImageView = findViewById(R.id.question_image_view);
      mButtons[0] = findViewById(R.id.button_1);
      mButtons[1] = findViewById(R.id.button_2);
      mButtons[2] = findViewById(R.id.button_3);
      mButtons[3] = findViewById(R.id.button_4);

      mButtons[0].setOnClickListener(this);
      mButtons[1].setOnClickListener(this);
      mButtons[2].setOnClickListener(this);
      mButtons[3].setOnClickListener(this);

      mRandom = new Random();
      newQuiz();
   }

   private void newQuiz() {
      mScoreText.setText(Integer.toString(mScore)+" คะแนน");

      List<WordItem> mItemList = new ArrayList<>(Arrays.asList(WordListActivity.items));

      // สุ่ม index ของคำตอบ (คำถาม)
      int answerIndex = mRandom.nextInt(mItemList.size());
      // เข้าถึง WordItem ตาม index ที่สุ่มได้
      WordItem item = mItemList.get(answerIndex);
      // แสดงรูปคำถาม
      mQuestionImageView.setImageResource(item.imageResId);

      mAnswerWord = item.word;

      // สุ่มตำแหน่งปุ่มที่จะแสดงคำตอบ
      int randomButton = mRandom.nextInt(4);
      // แสดงคำศัพท์ที่เป็นคำตอบ
      mButtons[randomButton].setText(item.word);
      // ลบ WordItem ที่เป็นคำตอบออกจาก list
      mItemList.remove(item);

      // เอา list ที่เหลือมา shuffle
      Collections.shuffle(mItemList);

      // เอาคำศัพท์จาก list ที่ตำแหน่ง 0 ถึง 3 มาแสดงที่ปุ่ม 3 ปุ่มที่เหลือ โดยข้ามปุ่มที่เป็นคำตอบ
      for (int i = 0; i < 4; i++) {
         if (i == randomButton) { // ถ้า i คือ index ของปุ่มคำตอบ ให้ข้ามไป
            continue;
         }
         mButtons[i].setText(mItemList.get(i).word);
      }
   }

   @Override
   public void onClick(View view) {
      Button b = findViewById(view.getId());
      String buttonText = b.getText().toString();

      if (buttonText.equals(mAnswerWord)) {
         Toast.makeText(GameActivity.this, "ถูกต้องครับ", Toast.LENGTH_SHORT).show();
         mScore++;
      } else {
         Toast.makeText(GameActivity.this, "ผิดครับ", Toast.LENGTH_SHORT).show();
      }

      mCount++;
      if(mCount == 5){
         new AlertDialog.Builder(GameActivity.this)
                 .setTitle("สรุปผล")
                 .setMessage("คุณได้ "+Integer.toString(mScore)+" คะแนน\n\nต้องการเล่นเกมใหม่หรือไม่")
                 .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       newQuiz();
                       mScore = 0;
                       mCount = 0;
                       mScoreText.setText(Integer.toString(mScore)+" คะแนน");
                    }
                 })
                 .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       onBackPressed();
                    }
                 })
                 .show();
      }

      newQuiz();
   }
}
