package com.example.forestfeast;

import android.widget.ImageView;

import java.util.Arrays;

public class Question {

    private int[] answerImageIds;
    private String[] answerTexts;
    private int correctAnswerIndex;

    public Question(int[] answerImageIds, String[] answerTexts, int correctAnswerIndex) {
        this.answerImageIds = answerImageIds;
        this.answerTexts = answerTexts;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int[] getAnswerImageIds() {
        return answerImageIds;
    }

    public String[] getAnswerTexts() {
        return answerTexts;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public boolean isCorrectAnswer(int answerId) {
        return answerId == correctAnswerIndex;
    }

//    @Override
//    public String toString() {
//        return "Question{" +
//                "answerImageIds=" + Arrays.toString(answerImageIds) +
//                ", answerTexts=" + Arrays.toString(answerTexts) +
//                ", correctAnswerIndex=" + correctAnswerIndex +
//                '}';
//    }
}
