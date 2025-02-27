package com.vayam.demo;

@FunctionalInterface
interface Calculator {
    int operate(int a, int b); // Abstract method (only one allowed)
}
