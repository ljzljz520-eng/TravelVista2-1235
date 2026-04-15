package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.Review;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.entity.User;
import com.travelvista.mapper.ReviewMapper;
import com.travelvista.service.ReviewService;
import com.travelvista.service.ScenicSpotService;
import com.travelvista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private UserService userService;

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Override
    public IPage<Map<String, Object>> getReviewList(Integer pageNum, Integer pageSize, Long scenicId, Long userId) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Review::getStatus, 1);
        if (scenicId != null) {
            wrapper.eq(Review::getScenicId, scenicId);
        }
        if (userId != null) {
            wrapper.eq(Review::getUserId, userId);
        }
        wrapper.orderByDesc(Review::getCreateTime);
        
        IPage<Review> reviewPage = page(page, wrapper);
        
        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, reviewPage.getTotal());
        List<Map<String, Object>> records = reviewPage.getRecords().stream().map(review -> {
            Map<String, Object> map = new HashMap<>();
            map.put("review", review);
            
            User user = userService.getById(review.getUserId());
            if (user != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("nickname", user.getNickname());
                userMap.put("avatar", user.getAvatar());
                map.put("user", userMap);
            }
            
            ScenicSpot scenic = scenicSpotService.getById(review.getScenicId());
            if (scenic != null) {
                Map<String, Object> scenicMap = new HashMap<>();
                scenicMap.put("id", scenic.getId());
                scenicMap.put("name", scenic.getName());
                scenicMap.put("coverImage", scenic.getCoverImage());
                map.put("scenic", scenicMap);
            }
            
            return map;
        }).collect(Collectors.toList());
        
        resultPage.setRecords(records);
        return resultPage;
    }

    @Override
    public void addReview(Review review) {
        review.setStatus(1);
        save(review);
        
        ScenicSpot scenic = scenicSpotService.getById(review.getScenicId());
        if (scenic != null) {
            LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Review::getScenicId, review.getScenicId())
                    .eq(Review::getStatus, 1);
            List<Review> reviews = list(wrapper);
            
            double avgRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(5.0);
            
            scenic.setRating(BigDecimal.valueOf(avgRating));
            scenic.setReviewCount(reviews.size());
            scenicSpotService.updateById(scenic);
        }
    }

    @Override
    public IPage<Map<String, Object>> getUserReviews(Long userId, Integer pageNum, Integer pageSize) {
        return getReviewList(pageNum, pageSize, null, userId);
    }

    @Override
    public IPage<Map<String, Object>> getAllReviews(Integer pageNum, Integer pageSize, String keyword) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Review::getContent, keyword);
        }
        wrapper.orderByDesc(Review::getCreateTime);
        
        IPage<Review> reviewPage = page(page, wrapper);
        
        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, reviewPage.getTotal());
        List<Map<String, Object>> records = reviewPage.getRecords().stream().map(review -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", review.getId());
            map.put("content", review.getContent());
            map.put("rating", review.getRating());
            map.put("status", review.getStatus());
            map.put("createTime", review.getCreateTime());
            
            User user = userService.getById(review.getUserId());
            if (user != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("nickname", user.getNickname());
                userMap.put("username", user.getUsername());
                map.put("user", userMap);
            }
            
            ScenicSpot scenic = scenicSpotService.getById(review.getScenicId());
            if (scenic != null) {
                Map<String, Object> scenicMap = new HashMap<>();
                scenicMap.put("id", scenic.getId());
                scenicMap.put("name", scenic.getName());
                map.put("scenic", scenicMap);
            }
            
            return map;
        }).collect(Collectors.toList());
        
        resultPage.setRecords(records);
        return resultPage;
    }
}
