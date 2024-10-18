package com.quiz.quiz_service.controller;


import com.quiz.quiz_service.entity.QuestionWrapper;
import com.quiz.quiz_service.entity.Response;
import com.quiz.quiz_service.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam String category, @RequestParam int numQ,
                                         @RequestParam String title){
        return quizService.createQuiz(category, numQ, title);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id ){
        return quizService.deleteById(id);
    }

    @GetMapping("/getQuiz/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuestion(@PathVariable Integer id){
        return quizService.getQuestion(id);
    }

    @PostMapping("/quizResult/{id}")
    public ResponseEntity<Integer> Results(@PathVariable Integer id, @RequestBody List<Response> response){
        return quizService.getResult(id, response);
    }
}
