package com.vayam.demo;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import smile.io.Read;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmileDemo {
    public static void main(String[] args) throws Exception {
        // Load the Iris dataset (CSV)
        DataFrame data = Read.csv(
                "https://raw.githubusercontent.com/jbrownlee/Datasets/master/iris.csv",
                CSVFormat.DEFAULT.withHeader("sepal_length", "sepal_width", "petal_length", "petal_width", "class")
        );

        // Convert string class labels to integer class labels
        String[] stringLabels = data.stringVector("class").toArray(new String[0]);
        int[] intLabels = NominalScale.of(stringLabels).toIntArray(stringLabels);

        // Drop original class and add new integer label column
        data = data.drop("class").merge(IntVector.of("label", intLabels));

        // Convert DataFrame to a list of Tuples for shuffling
        List<Tuple> rows = new ArrayList<>();
        data.stream().forEach(rows::add);

        // Shuffle the rows
        Collections.shuffle(rows);

        // Split the data into training and testing sets (70/30)
        int splitIndex = (int) (rows.size() * 0.7);
        List<Tuple> trainRows = rows.subList(0, splitIndex);
        List<Tuple> testRows = rows.subList(splitIndex, rows.size());

        // Convert back to DataFrame
        DataFrame train = DataFrame.of(trainRows, data.schema());
        DataFrame test = DataFrame.of(testRows, data.schema());

        // Continue with the rest of your code...
    }
}