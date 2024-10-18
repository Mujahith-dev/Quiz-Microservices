package com.quiz.quiz_service.services;


import com.quiz.quiz_service.entity.Question;
import com.quiz.quiz_service.entity.QuestionWrapper;
import com.quiz.quiz_service.entity.Quiz;
import com.quiz.quiz_service.entity.Response;
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
    QuestionRepository questionRepository;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
       try{
        List<Question> questions = questionRepository.findRandomQuestionByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Quiz created", HttpStatus.CREATED);
        }
       catch (Exception e){
           e.printStackTrace();
       }
       return new ResponseEntity<> ("quiz not created", HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseEntity<List<QuestionWrapper>> getQuestion(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        List<Question> questionFromDb = quiz.get().getQuestions();
        List<QuestionWrapper> quesForUser = new ArrayList<>();
        for(Question q : questionFromDb){
            QuestionWrapper qw = new QuestionWrapper(q.getId(),q.getQuestionTitle(),
                    q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
                    quesForUser.add(qw);
        }

        return new ResponseEntity<>(quesForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getResult (Integer id, List<Response> responses){
        Quiz quiz = quizRepository.findById(id).get();
        List<Question> question = quiz.getQuestions();
        int result = 0;
        int i=0;
        for (Response res : responses) {
            if (res.getResponses().equals(question.get(i).getRightAnswer()))
                result++;

            i++;
        }
        return new ResponseEntity<>(result, HttpStatus.OK) ;
    }
}
