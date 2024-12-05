package org.news.newsapp.util;

import org.bson.Document;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.news.newsapp.model.Reader;

/**
 * @Class: RecommendationEngine
 * @Author: Senuli Wickramage
 * @Description: The RecommendationEngine class computes article recommendations for a given user based on collaborative filtering.
 * It uses a similarity matrix and ratings from other users to generate personalized suggestions.
 */
public class RecommendationEngine {
    private int userId; // ID of the target user for whom recommendations are generated
    private int[][] userRatings; // 2D array containing ratings of articles by users
    private double[][] similarityMatrix; // 2D array containing similarity values between users
    private Document readerDocument; // The reader document that contains the user's rating information
    // Path to the CSV file containing ratings data
    private final String filePath = "C:\\Users\\Senuli\\Documents\\OOP\\newsApp\\src\\main\\resources\\org\\news\\newsapp\\Data\\user_article_ratings_simple.csv";
    private List<Integer> recommendations; // List of recommended article IDs for the user

    /**
     * Constructor that initializes the RecommendationEngine with a given user.
     * It loads the user's rating data and computes the similarity matrix.
     *
     * @param reader The reader object for which recommendations will be generated.
     */
    public RecommendationEngine (Reader reader){
        readerDocument = reader.toDocument();
        try {
            loadCsvFile(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        computeSimilarityMatrix();
    }

    /**
     * Method to get article recommendations for the user.
     * Uses collaborative filtering to find the most relevant articles.
     *
     * @return A list of article IDs recommended for the user.
     */
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

    /**
     * Method to load user ratings from a CSV file.
     * It maps the ratings from the file to the user's ratings based on the user ID.
     *
     * @param filePath The path to the CSV file containing user ratings.
     * @throws FileNotFoundException If the file is not found at the specified path.
     */
    private void loadCsvFile(String filePath) throws FileNotFoundException {
        // Retrieve rated articles and their respective ratings for the target user
        List<Document> ratedArticles = (List<Document>) readerDocument.get("ratedArticles");
        List<Integer> articleRatings = (List<Integer>) readerDocument.get("ratings");

        // Try to load the CSV file containing ratings
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

        // Map the user's ratings to the appropriate articles
        for (int i=0; i<rows.size(); i++){
            for(Document article: ratedArticles){
                if ((Integer) article.get("id") == i) {
                    ratings[i] = articleRatings.get(ratedArticles.indexOf(article));
                }
            }
        }

        // Set the user ID based on the ratings list and add the ratings to the rows
        userId = rows.indexOf(ratings);
        rows.add(ratings);

        // Convert the list of rows to a 2D array
        userRatings = rows.toArray(new int[0][]);
    }

    /**
     * Method to compute the similarity matrix between users.
     * It uses cosine similarity to calculate how similar each user is to every other user.
     */
    private void computeSimilarityMatrix() {
        int noOfUsers = userRatings.length;
        similarityMatrix = new double[noOfUsers][noOfUsers];

        // Calculate the cosine similarity for each pair of users
        for (int i = 0; i < noOfUsers; i++) {
            for (int j = 0; j < noOfUsers; j++) {
                similarityMatrix[i][j] = cosineSimilarity(userRatings[i], userRatings[j]);
            }
        }
    }

    /**
     * Method to calculate the cosine similarity between two users' ratings.
     * Cosine similarity measures the cosine of the angle between two vectors, which in this case are the users' ratings.
     *
     * @param ratingsA The ratings of user A.
     * @param ratingsB The ratings of user B.
     * @return The cosine similarity value between the two users.
     */
    private double cosineSimilarity(int[] ratingsA, int[] ratingsB) {
        double dotProduct = 0.0; // Dot product of the two rating vectors
        double magnitudeA = 0.0; // Magnitude of user A's rating vector
        double magnitudeB = 0.0; // Magnitude of user B's rating vector

        // Calculate the dot product and magnitudes
        for (int i = 0; i < ratingsA.length; i++) {
            dotProduct += ratingsA[i] * ratingsB[i];
            magnitudeA += Math.pow(ratingsA[i], 2);
            magnitudeB += Math.pow(ratingsB[i], 2);
        }

        // Return the cosine similarity
        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    /**
     * Method to generate article recommendations for the target user.
     * It calculates weighted sums of article ratings based on the similarity between users and generates a list of recommended items.
     *
     * @return A list of article IDs recommended for the user.
     */
    private List<Integer> recommendItems() {
        double[] weightedSums = new double[userRatings[0].length];
        double[] similaritySums = new double[userRatings[0].length];

        // Loop through all users to compute weighted sums for each article
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
