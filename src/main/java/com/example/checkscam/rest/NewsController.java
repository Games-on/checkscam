package com.example.checkscam.rest;

import com.example.checkscam.entity.News;
import com.example.checkscam.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsServiceImpl newsService;

    // GET all news
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    // GET news by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<News>> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isPresent()) {
            return new ResponseEntity<>(news, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST news
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    // PUT news
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        News updatedNews = newsService.updateNews(id, news);
        if (updatedNews != null) {
            return new ResponseEntity<>(updatedNews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE news
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        boolean isDeleted = newsService.deleteNews(id);
        Map<String, String> response = new HashMap<>();
        response.put("message","Deleted News successfully");
        return ResponseEntity.ok(response);
    }
}