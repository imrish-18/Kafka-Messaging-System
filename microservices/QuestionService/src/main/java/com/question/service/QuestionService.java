package com.question.service;


import com.question.config.RestTemplateConfig;
import com.question.entities.Questions;
import com.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuestionService {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Questions createQuestions(Questions questions){
        return questionRepository.save(questions);
    }

    public List<Questions> getAllQuestions(){
        return questionRepository.findAll();
    }

    public Questions getQuestionById(Long id){
        return questionRepository.findById(id).orElseThrow(()->new RuntimeException("question is not found "));
    }
    public String fetchDataFromExternalApi() {
        // Example: GET Request
        String url = "http://localhost:9000/api/quiz/message";

         String message=restTemplate.getForObject(url, String.class);
        System.out.printf(message);
        return message;
    }
}
