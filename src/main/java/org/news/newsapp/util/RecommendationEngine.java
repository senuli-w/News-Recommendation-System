package org.news.newsapp.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommendationEngine implements Runnable{
    private int[][] userRatings;
    private double[][] similarityMatrix;
    public void checkUrl(){
        URL fileUrl = this.getClass().getClassLoader().getResource("user_article_ratings_simple.csv");
        System.out.println(fileUrl);
    }

    @Override
    public void run() {
        String filePath = "C:\\Users\\Senuli\\Documents\\OOP\\RecommenderSystem\\src\\main\\resources\\user_article_ratings_simple.csv";
        try {
            loadCsvFile(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        computeSimilarityMatrix();
        int userId = 4; // lets recommend items for user 0
        List<Integer> recommendations = recommendItems(userId, userRatings, similarityMatrix);
//        System.out.println("Recommended items for user " + userId + ": " + recommendations);
        System.out.println(recommendations.size());
        int ratingCount = 0;
        for (int rating: userRatings[userId]){
            if (rating > 0) ratingCount++;
        }
        System.out.println(ratingCount);
    }

    // Method to load the CSV file into the userRatings matrix
    public void loadCsvFile(String filePath) throws FileNotFoundException {
        URL fileUrl = this.getClass().getClassLoader().getResource("user_article_ratings_simple.csv");
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split each row by commas and parse to integers
                String[] values = line.split(",");
                int[] ratings = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    ratings[i] = Integer.parseInt(values[i]);
                }
                rows.add(ratings);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert the list of rows to a 2D array
        userRatings = rows.toArray(new int[0][]);
    }

    public void head( int n){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                System.out.print(userRatings[i][j] + " ");
            }
            System.out.println();
        }
    }


    // Getter for userRatings
    public int[][] getUserRatings() {
        return userRatings;
    }

    public double cosineSimilarity(int[] ratingsA, int[] ratingsB) {
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (int i = 0; i < ratingsA.length; i++) {
            dotProduct += ratingsA[i] * ratingsB[i];
            magnitudeA += Math.pow(ratingsA[i], 2);
            magnitudeB += Math.pow(ratingsB[i], 2);
        }

        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    public void computeSimilarityMatrix() {
        int noOfUsers = userRatings.length;
        similarityMatrix = new double[noOfUsers][noOfUsers];

        for (int i = 0; i < noOfUsers; i++) {
            for (int j = 0; j < noOfUsers; j++) {
                similarityMatrix[i][j] = cosineSimilarity(userRatings[i], userRatings[j]);
            }
        }
    }

    public static List<Integer> recommendItems(int userId, int[][] userRatings, double[][] similarityMatrix) {
        double[] weightedSums = new double[userRatings[0].length];
        double[] similaritySums = new double[userRatings[0].length];
        for (int i = 0; i < userRatings.length; i++) {
            if (i == userId) continue; // Skip the target user

            for (int j = 0; j < userRatings[i].length; j++) {
                if (userRatings[userId][j] == 0 && userRatings[i][j] > 0) {
                    weightedSums[j] += similarityMatrix[userId][i] * userRatings[i][j];
                    similaritySums[j] += similarityMatrix[userId][i];
                }
            }
        }
        List<Integer> recommendedItems = new ArrayList<>();
        for
        (int i = 0; i < weightedSums.length; i++) {
            if (similaritySums[i] > 0) {
                recommendedItems.add(i);
            }
        }
        return recommendedItems;
    }
}
