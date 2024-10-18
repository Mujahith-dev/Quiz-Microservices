package com.questions.question_service.services;


import com.questions.question_service.entity.Question;
import com.questions.question_service.entity.QuestionWrapper;
import com.questions.question_service.entity.Response;
import com.questions.question_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;
    //methods for microservice starts here
    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numOfQuestions) {
        List<Integer> questions = questionRepository
                .findRandomQuestionByCategory(categoryName, numOfQuestions);

        return new ResponseEntity<List<Integer>>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for (Integer id : questionIds) {
            questions.add(questionRepository.findById(id).get());
        }
        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<List<QuestionWrapper>>(wrappers, HttpStatus.OK);

    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right = 0;
        for( Response res : responses){
            Optional<Question> questions = questionRepository.findById(res.getId());
            if(res.getResponses().equals(questions.get().getRightAnswer())){
                right++;
            }
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }

    //method for microservice ends here
    public ResponseEntity<List<Question>> getQuestions() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepository.getQuestionByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionRepository.save(question);
            return new ResponseEntity<>("Student Added", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Not created", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            questionRepository.deleteById(id);
            return new ResponseEntity<>("Question deleted", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> editQuestion(Integer id, Question newQuestion) {
        try {
            Optional<Question> oldQuestion = questionRepository.findById(id);
            if (oldQuestion.isPresent()) {
                Question existingQuestion = oldQuestion.get();
                existingQuestion.setQuestionTitle(newQuestion.getQuestionTitle());
                existingQuestion.setCategory(newQuestion.getCategory());
                existingQuestion.setDifficultyLevel(newQuestion.getDifficultyLevel());
                existingQuestion.setOption1(newQuestion.getOption1());
                existingQuestion.setOption2(newQuestion.getOption2());
                existingQuestion.setOption3(newQuestion.getOption3());
                existingQuestion.setOption4(newQuestion.getOption4());
                existingQuestion.setRightAnswer(newQuestion.getRightAnswer());
                questionRepository.save(existingQuestion);
                return new ResponseEntity<>("Question " + id + " edited", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Not Edited", HttpStatus.BAD_REQUEST);


    }


}