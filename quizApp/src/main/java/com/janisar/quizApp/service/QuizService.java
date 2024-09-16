package com.janisar.quizApp.service;

import com.janisar.quizApp.dao.QuestionDao;
import com.janisar.quizApp.dao.QuizDao;
import com.janisar.quizApp.model.Question;
import com.janisar.quizApp.model.QuestionWrapper;
import com.janisar.quizApp.model.Quiz;
import com.janisar.quizApp.model.Response;
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
    QuizDao quizDau;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions =questionDao.findRandomQuestionByCategory(category,numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);

        quizDau.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Integer id) {
        Optional<Quiz> quiz = quizDau.findById(id);

        List<Question> questionFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionForUser = new ArrayList<>();

        for (Question q : questionFromDB){
            QuestionWrapper qw = new QuestionWrapper(q.getId(),q.getQuestiontitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            questionForUser.add(qw);
        }

        return new ResponseEntity<>(questionForUser , HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        Quiz quiz = quizDau.findById(id).get();
        List<Question> questions = quiz.getQuestions();

        int right =0;
        int i=0;

        for(Response response : responses){
            if (response.getResponse().equals(questions.get(i).getRightanswer()))
                right++;

            i++;

        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }
}
