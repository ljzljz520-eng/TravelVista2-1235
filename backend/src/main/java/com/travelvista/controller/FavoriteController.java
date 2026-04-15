package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.entity.Favorite;
import com.travelvista.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @SaCheckLogin
    @PostMapping("/add/{scenicId}")
    public Result<Void> addFavorite(@PathVariable Long scenicId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.addFavorite(userId, scenicId);
        return Result.success("收藏成功", null);
    }

    @SaCheckLogin
    @DeleteMapping("/remove/{scenicId}")
    public Result<Void> removeFavorite(@PathVariable Long scenicId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.removeFavorite(userId, scenicId);
        return Result.success("取消收藏成功", null);
    }

    @SaCheckLogin
    @GetMapping("/check/{scenicId}")
    public Result<Boolean> checkFavorite(@PathVariable Long scenicId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean isFavorite = favoriteService.isFavorite(userId, scenicId);
        return Result.success(isFavorite);
    }

    @SaCheckLogin
    @GetMapping("/list")
    public Result<IPage<Favorite>> getUserFavorites(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        IPage<Favorite> page = favoriteService.getUserFavorites(userId, pageNum, pageSize);
        return Result.success(page);
    }
}
