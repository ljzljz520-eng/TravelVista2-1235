package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.Favorite;

public interface FavoriteService extends IService<Favorite> {

    void addFavorite(Long userId, Long scenicId);

    void removeFavorite(Long userId, Long scenicId);

    boolean isFavorite(Long userId, Long scenicId);

    IPage<Favorite> getUserFavorites(Long userId, Integer pageNum, Integer pageSize);
}
