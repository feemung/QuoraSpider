package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.UserInfo;

/**
 * Created by feemung on 16/6/26.
 */
public class CheckSQLUpdateData {
    public static boolean checkAnswer(Answer answer){
        if(answer.getQuestion()==null||answer.getQuestion().isEmpty()){
            return false;
        }
        if(answer.getAnswerUser()==null||answer.getAnswerUser().isEmpty()){
            return false;
        }
        return true;
    }
    public static boolean checkUserInfo(UserInfo userInfo){
        return true;
    }
}
