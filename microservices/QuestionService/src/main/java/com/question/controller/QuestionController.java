package com.question.controller;


import com.question.entities.Questions;
import com.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/generateQuestion")
    public Questions createQuestion(@RequestBody Questions questions){
        return questionService.createQuestions(questions);
    }

    @GetMapping("/getQuestions/{id}")
    public Questions getById(@PathVariable long id){
        return questionService.getQuestionById(id);
    }
    @GetMapping("/getQuestions")
    public List<Questions> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("/message")
    public String fetchDataFromExternalApi(){
        return questionService.fetchDataFromExternalApi();
    }
}
