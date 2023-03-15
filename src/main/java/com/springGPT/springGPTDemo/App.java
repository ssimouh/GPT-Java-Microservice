package com.springGPT.springGPTDemo;

import java.io.IOException;
import java.util.Scanner;

import com.springGPT.controller.ChatGptController;

public class App {
    public static void main( String[] args ) throws IOException
    {
    	Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your question: ");
        String question = scanner.nextLine();
        
    	String answer = ChatGPTMicroservice.answerQuestion(question);
    	System.out.printf("Answer for [%s] is [%s]%n", question, answer);
    	
    	
    	
    	scanner.close();
    }
}
