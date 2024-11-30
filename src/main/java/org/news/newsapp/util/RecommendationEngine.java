package org.example.diagramnewsrecommendation.util;

import org.bson.Document;
import org.example.diagramnewsrecommendation.model.Reader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecommendationEngine {
    private int userId;
    private int[][] userRatings;
    private double[][] similarityMatrix;
    private Document readerDocument;
    private final String filePath = "C:\\Users\\Senuli\\Documents\\OOP\\DiagramNewsRecommendation\\src\\main\\resources\\org\\example\\diagramnewsrecommendation\\Data\\user_article_ratings_simple.csv";
    private List<Integer> recommendations;

    public RecommendationEngine (Reader reader){
        readerDocument = reader.toDocument();
        try {
            loadCsvFile(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        computeSimilarityMatrix();
    }

    public List<Integer> getRecommendations(){
        recommendations = recommendItems();
        System.out.println(recommendations.size());
        int ratingCount = 0;
        for (int rating: userRatings[userId]){
            if (rating > 0) ratingCount++;
        }
        System.out.println(ratingCount);
        return recommendations;
    }

    // Method to load the CSV file into the userRatings matrix
    private void loadCsvFile(String filePath) throws FileNotFoundException {
        List<Document> ratedArticles = (List<Document>) readerDocument.get("ratedArticles");
        List<Integer> articleRatings = (List<Integer>) readerDocument.get("ratings");

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

        int[] ratings = new int[rows.size()];

        // Initialize ratings with default value (e.g., 0 for unrated articles)
        Arrays.fill(ratings, 0);

        for (int i=0; i<rows.size(); i++){
            for(Document article: ratedArticles){
                if ((Integer) article.get("id") == i) {
                    ratings[i] = articleRatings.get(ratedArticles.indexOf(article));
                }
            }
        }

        userId = rows.indexOf(ratings);
        rows.add(ratings);

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

    private double cosineSimilarity(int[] ratingsA, int[] ratingsB) {
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

    private void computeSimilarityMatrix() {
        int noOfUsers = userRatings.length;
        similarityMatrix = new double[noOfUsers][noOfUsers];

        for (int i = 0; i < noOfUsers; i++) {
            for (int j = 0; j < noOfUsers; j++) {
                similarityMatrix[i][j] = cosineSimilarity(userRatings[i], userRatings[j]);
            }
        }
    }

    private List<Integer> recommendItems() {
        double[] weightedSums = new double[userRatings[0].length];
        double[] similaritySums = new double[userRatings[0].length];

        for (int i = 0; i < userRatings.length; i++) {
            if (i == userId) continue; // Skip the target user

            for (int j = 0; j < userRatings[i].length; j++) {
                if (userRatings[userId][j] == 0 && userRatings[i][j] > 0) {
                    // calculate weighted sum and similarity sum
                    weightedSums[j] += similarityMatrix[userId][i] * userRatings[i][j];
                    similaritySums[j] += similarityMatrix[userId][i];
                }
            }
        }
        // Create a list of article indices and their scores
        List<int[]> scoredArticles = new ArrayList<>();
        for (int i = 0; i < weightedSums.length; i++) {
            if (similaritySums[i] > 0) {
                // Calculate normalized score (weighted sum / similarity sum)
                double score = weightedSums[i] / similaritySums[i];
                scoredArticles.add(new int[]{i, (int) score}); // Store article index and score
            }
        }

        // Sort articles by score in descending order
        scoredArticles.sort((a, b) -> Double.compare(b[1], a[1]));
        // printing scores for verification
        for (int i = 0; i < 12; i++){
            System.out.print(Arrays.toString(scoredArticles.get(i)) + " ");
        }

        // Extract and return sorted article indices
        List<Integer> recommendedItems = new ArrayList<>();
        for (int[] article : scoredArticles) {
            recommendedItems.add(article[0]); // Add article index
        }
        return recommendedItems;
    }
}
