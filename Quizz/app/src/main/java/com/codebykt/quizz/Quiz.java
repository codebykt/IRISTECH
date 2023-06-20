package com.codebykt.quizz;

import java.util.List;

public class Quiz {
    private String quizName;
    private List<Question> questions;

    public Quiz(String quizName, List<Question> questions) {
        this.quizName = quizName;
        this.questions = questions;
    }

    public String getQuizName() {
        return quizName;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

