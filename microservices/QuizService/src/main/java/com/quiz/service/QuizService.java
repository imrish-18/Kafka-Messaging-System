package com.quiz.service;


import com.quiz.entity.Quiz;
import com.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz saveQuiz(Quiz quiz){
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(String id){
        return quizRepository.findById(id).get();
    }
}
