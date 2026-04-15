package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.entity.Review;
import com.travelvista.service.ReviewService;
import com.travelvista.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/list")
    public Result<IPage<Map<String, Object>>> getReviewList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long scenicId) {
        IPage<Map<String, Object>> page = reviewService.getReviewList(pageNum, pageSize, scenicId, null);
        return Result.success(page);
    }

    @PostMapping("/add")
    public Result<Void> addReview(@RequestBody Review review, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        review.setUserId(userId);
        reviewService.addReview(review);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<IPage<Map<String, Object>>> getMyReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        IPage<Map<String, Object>> page = reviewService.getUserReviews(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteReview(@PathVariable Long id) {
        reviewService.removeById(id);
        return Result.success();
    }
}
