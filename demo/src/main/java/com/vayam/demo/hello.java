package com.vayam.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class hello {
	public static void main(String[] args) {
		// Parallel sorting for an array of primitives
		int[] numbers = {9, 3, 1, 5, 13, 12, 7, 4, 11, 6};
		System.out.println("Original array: " + Arrays.toString(numbers));
		Arrays.parallelSort(numbers);
		System.out.println("Sorted array: " + Arrays.toString(numbers));
		// Parallel sorting for an array of objects with a custom comparator
		String[] fruits = {"Peach", "Apple", "Orange", "Banana", "Grape", "Pear"};
		System.out.println("\nOriginal array: " + Arrays.toString(fruits));
		// Using a lambda expression for the comparator to sort in reverse alphabetical order
		Arrays.parallelSort(fruits, Comparator.reverseOrder());
		//Arrays.parallelSort(fruits);
		System.out.println("Sorted array in reverse order: " + Arrays.toString(fruits));

		Arrays.parallelSort(fruits);
		System.out.println("Sorted array in Ascending order: " + Arrays.toString(fruits));


		List<String> p = Arrays.stream(fruits).filter(n -> n.startsWith("P")).collect(Collectors.toList());

		System.out.println("Startt with P ====" + p);

		p.forEach(System.out::println );

		p.forEach(a-> System.out.print(a+" "));




	}
}

