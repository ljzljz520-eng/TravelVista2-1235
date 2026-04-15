package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.Favorite;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.FavoriteMapper;
import com.travelvista.service.FavoriteService;
import com.travelvista.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long scenicId) {
        ScenicSpot scenic = scenicSpotService.getById(scenicId);
        if (scenic == null) {
            throw new BusinessException("景点不存在");
        }

        if (isFavorite(userId, scenicId)) {
            throw new BusinessException("已收藏该景点");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setScenicId(scenicId);
        save(favorite);

        scenic.setFavoriteCount(scenic.getFavoriteCount() + 1);
        scenicSpotService.updateById(scenic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long scenicId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId).eq(Favorite::getScenicId, scenicId);
        Favorite favorite = getOne(wrapper);
        
        if (favorite != null) {
            removeById(favorite.getId());
            
            ScenicSpot scenic = scenicSpotService.getById(scenicId);
            if (scenic != null && scenic.getFavoriteCount() > 0) {
                scenic.setFavoriteCount(scenic.getFavoriteCount() - 1);
                scenicSpotService.updateById(scenic);
            }
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long scenicId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId).eq(Favorite::getScenicId, scenicId);
        return count(wrapper) > 0;
    }

    @Override
    public IPage<Favorite> getUserFavorites(Long userId, Integer pageNum, Integer pageSize) {
        Page<Favorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId).orderByDesc(Favorite::getCreateTime);
        return page(page, wrapper);
    }
}
