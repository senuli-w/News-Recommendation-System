package org.news.newsapp.util;


import org.news.newsapp.db.ArticleService;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.Reader;

import java.util.ArrayList;
import java.util.List;

public class CategorizationEngine{
    private static final ArticleService articleService = new ArticleService();
    public static ArrayList<Article> CategorizeArticles(List<Integer> recommendations){
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        ArrayList<Article> finalArticles = new ArrayList<>();

        for (Integer recommendation: recommendations){
            Article article = articleService.get(String.valueOf(recommendation));
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
