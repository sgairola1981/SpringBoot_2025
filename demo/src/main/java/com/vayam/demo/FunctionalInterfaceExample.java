package com.vayam.demo;

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        // Lambda expression implementing the functional interface
        Calculator addition = (a, b) -> a + b;
        Calculator multiplication = (a, b) -> a * b;
        Calculator Division = (a, b) -> a / b;

        // Using the functional interface
        System.out.println("Addition: " + addition.operate(5, 3));  // Output: 8
        System.out.println("Multiplication: " + multiplication.operate(5, 3)); // Output: 15

        System.out.println("Division: " + Division.operate(15, 3)); // Output:
    }
}