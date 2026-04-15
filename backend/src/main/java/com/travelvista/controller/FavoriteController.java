package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.service.FavoriteService;
import com.travelvista.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/add/{scenicId}")
    public Result<Void> addFavorite(@PathVariable Long scenicId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        favoriteService.addFavorite(userId, scenicId);
        return Result.success();
    }

    @DeleteMapping("/remove/{scenicId}")
    public Result<Void> removeFavorite(@PathVariable Long scenicId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        favoriteService.removeFavorite(userId, scenicId);
        return Result.success();
    }

    @GetMapping("/check/{scenicId}")
    public Result<Boolean> checkFavorite(@PathVariable Long scenicId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserId(token);
        }
        boolean isFavorite = favoriteService.isFavorite(userId, scenicId);
        return Result.success(isFavorite);
    }

    @GetMapping("/list")
    public Result<IPage<Map<String, Object>>> getMyFavorites(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        IPage<Map<String, Object>> page = favoriteService.getUserFavorites(userId, pageNum, pageSize);
        return Result.success(page);
    }
}
