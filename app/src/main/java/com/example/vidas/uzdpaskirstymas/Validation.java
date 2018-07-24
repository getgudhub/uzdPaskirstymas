package com.example.vidas.uzdpaskirstymas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String VALID_CREDENTIALS_REGEX ="^[A-Za-z0-9]{5,15}$";
    private static final String VALID_FULLNAME_REGEX ="^[A-Za-z ]{7,24}$";
    private static final String VALID_EMAIL_ADDRESS_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
    private static final String VALID_GROUP_REGEX = "^[A-Za-z0-9/. ]{3,15}$";
    private static final String VALID_ASSIGNMENT_REGEX ="^[A-Za-z0-9/. ]{4,66}$";
    private static final String VALID_DATE_REGEX ="^((((0?[1-9]|[12]\\d|3[01])[\\.\\-\\/](0?[13578]|1[02])[\\.\\-\\/]((1[6-9]|[2-9]\\d)?\\d{2}))|((0?[1-9]|[12]\\d|30)[\\.\\-\\/](0?[13456789]|1[012])[\\.\\-\\/]((1[6-9]|[2-9]\\d)?\\d{2}))|((0?[1-9]|1\\d|2[0-8])[\\.\\-\\/]0?2[\\.\\-\\/]((1[6-9]|[2-9]\\d)?\\d{2}))|(29[\\.\\-\\/]0?2[\\.\\-\\/]((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00)))|(((0[1-9]|[12]\\d|3[01])(0[13578]|1[02])((1[6-9]|[2-9]\\d)?\\d{2}))|((0[1-9]|[12]\\d|30)(0[13456789]|1[012])((1[6-9]|[2-9]\\d)?\\d{2}))|((0[1-9]|1\\d|2[0-8])02((1[6-9]|[2-9]\\d)?\\d{2}))|(2902((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00))))$";

    public static boolean isValidCredentials(String credentials){
        Pattern credentialsPattern = Pattern.compile(VALID_CREDENTIALS_REGEX);
        Matcher credentialsMatcher = credentialsPattern.matcher(credentials);
        return credentialsMatcher.find();
    }

    public static boolean isValidFullName(String name){
        Pattern namePattern = Pattern.compile(VALID_FULLNAME_REGEX);
        Matcher matcher = namePattern.matcher(name);
        return matcher.find();
    }

    public static boolean isValidEmail(String email){
        Pattern emailPattern = Pattern.compile(VALID_EMAIL_ADDRESS_REGEX);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.find();
    }

    public static boolean isValidGroup(String group){
        Pattern groupPattern = Pattern.compile(VALID_GROUP_REGEX);
        Matcher matcher = groupPattern.matcher(group);
        return matcher.find();
    }

    public static boolean isValidAssignment(String assignment){
        Pattern pattern = Pattern.compile(VALID_ASSIGNMENT_REGEX);
        Matcher matcher = pattern.matcher(assignment);
        return matcher.find();
    }

    public static boolean isValidDate(String date){ // valids dd/MM/yyyy AND d/M/yyyy
        Pattern pattern = Pattern.compile(VALID_DATE_REGEX);
        Matcher matcher = pattern.matcher(date);
        return matcher.find();
    }
}