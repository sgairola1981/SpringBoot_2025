package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
interface FuncInterface{

    void abstractFun(int x);
    default void normalFun(){
        System.out.println("Hello");
    }
}
public class demo {
    public static void main(String[] args){

        FuncInterface fobj = (int x) -> System.out.println(2 * x);
        fobj.abstractFun(5);

        List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Adam");

        System.out.println("All names:");
        names.forEach(name -> System.out.println(name));
        List<String> result = names.stream().filter(a->a.startsWith("A")).collect(Collectors.toList());

        System.out.println(result); // [Amit, Ankit]

        List<String> collect = names.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println(collect); // [Amit, Ankit]
    }
}

