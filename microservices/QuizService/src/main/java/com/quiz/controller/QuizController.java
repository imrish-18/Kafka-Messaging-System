package com.quiz.controller;


import com.quiz.entity.Quiz;
import com.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/postQuiz")
    public Quiz saveQuiz(@RequestBody Quiz quiz){
        Quiz quiz1= quizService.saveQuiz(quiz);
        return quiz1;
    }

    @GetMapping("/getQuizById/{id}")
    public Quiz getQuizById(@PathVariable String id){
        return quizService.getQuizById(id);
    }

    @GetMapping("/message")
    public String getMessage(){
        return "service is up and running ";
    }
}
