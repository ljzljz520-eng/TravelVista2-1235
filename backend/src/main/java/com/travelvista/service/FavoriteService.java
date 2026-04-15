package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.Favorite;

import java.util.Map;

public interface FavoriteService extends IService<Favorite> {
    void addFavorite(Long userId, Long scenicId);
    void removeFavorite(Long userId, Long scenicId);
    boolean isFavorite(Long userId, Long scenicId);
    IPage<Map<String, Object>> getUserFavorites(Long userId, Integer pageNum, Integer pageSize);
}
