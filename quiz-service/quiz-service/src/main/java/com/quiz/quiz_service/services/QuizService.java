package com.quiz.quiz_service.services;

import com.quiz.quiz_service.entity.QuestionWrapper;
import com.quiz.quiz_service.entity.Quiz;
import com.quiz.quiz_service.entity.Response;
import com.quiz.quiz_service.feign.QuizInterface;
import com.quiz.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Quiz created", HttpStatus.CREATED);
    }


    public ResponseEntity<String> deleteById(Integer id) {
        try{
        quizRepository.deleteById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error in deleting", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        List<Integer> questionIds = quiz.get().getQuestionIds();
       ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> getResult (Integer id, List<Response> responses){
       ResponseEntity<Integer> result = quizInterface.getScore(responses);
        return result ;
    }
}
