package com.example.checkscam.service;

import com.example.checkscam.entity.News;
import com.example.checkscam.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    //get all news
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // get news by id
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    //post news
    public News createNews(News news) {
        return newsRepository.save(news);
    }

    // put news
    public News updateNews(Long id, News news) {
        Optional<News> existingNews = newsRepository.findById(id);
        if (existingNews.isPresent()) {
            News updatedNews = existingNews.get();
            updatedNews.setName(news.getName());
            updatedNews.setShortDescription(news.getShortDescription());
            updatedNews.setContent(news.getContent());
            return newsRepository.save(updatedNews); // Lưu bản ghi đã cập nhật
        } else {
            return null;
        }
    }

    // DELETE news
    public boolean deleteNews(Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            newsRepository.delete(news.get());
            return true;
        } else {
            return false;
        }
    }

}
