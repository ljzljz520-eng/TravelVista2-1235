package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.Review;

import java.util.Map;

public interface ReviewService extends IService<Review> {
    IPage<Map<String, Object>> getReviewList(Integer pageNum, Integer pageSize, Long scenicId, Long userId);
    void addReview(Review review);
    IPage<Map<String, Object>> getUserReviews(Long userId, Integer pageNum, Integer pageSize);
    IPage<Map<String, Object>> getAllReviews(Integer pageNum, Integer pageSize, String keyword);
}
