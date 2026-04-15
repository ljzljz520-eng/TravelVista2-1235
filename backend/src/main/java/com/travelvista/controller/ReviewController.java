package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.dto.ReviewAddDTO;
import com.travelvista.entity.Review;
import com.travelvista.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @SaCheckLogin
    @PostMapping("/add")
    public Result<Void> addReview(@Validated @RequestBody ReviewAddDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        reviewService.addReview(userId, dto);
        return Result.success("评论发表成功", null);
    }

    @GetMapping("/scenic/{scenicId}")
    public Result<IPage<Review>> getScenicReviews(
            @PathVariable Long scenicId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Review> page = reviewService.getScenicReviews(scenicId, pageNum, pageSize);
        return Result.success(page);
    }

    @SaCheckLogin
    @GetMapping("/my")
    public Result<IPage<Review>> getUserReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        IPage<Review> page = reviewService.getUserReviews(userId, pageNum, pageSize);
        return Result.success(page);
    }
}
