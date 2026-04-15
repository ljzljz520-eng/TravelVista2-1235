package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.entity.Review;
import com.travelvista.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @PostMapping("/add")
    @SaCheckLogin
    public Result<Boolean> add(@RequestBody Review review) {
        try {
            boolean result = reviewService.addReview(review);
            return Result.success("评论发表成功", result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/page/scenic/{scenicSpotId}")
    public Result<IPage<Review>> pageByScenicSpot(
            @PathVariable Long scenicSpotId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Review> pageResult = reviewService.pageByScenicSpotId(page, pageSize, scenicSpotId);
        return Result.success(pageResult);
    }

    @GetMapping("/page/user")
    @SaCheckLogin
    public Result<IPage<Review>> pageByUser(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        IPage<Review> pageResult = reviewService.pageByUserId(page, pageSize, userId);
        return Result.success(pageResult);
    }

    @PostMapping("/like/{id}")
    public Result<Boolean> like(@PathVariable Long id) {
        try {
            boolean result = reviewService.likeReview(id);
            return Result.success("点赞成功", result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
