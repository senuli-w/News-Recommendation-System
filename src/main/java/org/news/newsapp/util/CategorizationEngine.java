package org.news.newsapp.util;

import org.news.newsapp.db.ArticleService;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: CategorizationEngine
 * @Author: Senuli Wickramage
 * @Description: This class handles the categorization of articles based on user recommendations.
 * It ensures that users only see new articles that they haven't viewed yet.
 */

public class CategorizationEngine{
    private static final ArticleService articleService = new ArticleService();

    /**
     * Method to categorize articles based on user recommendations.
     *
     * @param recommendations A list of article IDs recommended to the user.
     * @return A list of articles that are newly recommended but not yet viewed by the user.
     */
    public static ArrayList<Article> CategorizeArticles(List<Integer> recommendations){
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        ArrayList<Article> finalArticles = new ArrayList<>();

        for (Integer recommendation: recommendations){
            Article article = articleService.get(String.valueOf(recommendation));

            // Check if the user has already viewed this article
            if (!currentUser.getViewedArticles().contains(article)){
                finalArticles.add(article);
            }
            if (finalArticles.size() == 6){
                break;
            }
        }
        return finalArticles;
    }
}
