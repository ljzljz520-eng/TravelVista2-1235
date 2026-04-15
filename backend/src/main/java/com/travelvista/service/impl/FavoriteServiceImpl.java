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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Override
    public void addFavorite(Long userId, Long scenicId) {
        if (isFavorite(userId, scenicId)) {
            throw new BusinessException("已收藏该景点");
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setScenicId(scenicId);
        save(favorite);
    }

    @Override
    public void removeFavorite(Long userId, Long scenicId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getScenicId, scenicId);
        remove(wrapper);
    }

    @Override
    public boolean isFavorite(Long userId, Long scenicId) {
        if (userId == null) {
            return false;
        }
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getScenicId, scenicId);
        return count(wrapper) > 0;
    }

    @Override
    public IPage<Map<String, Object>> getUserFavorites(Long userId, Integer pageNum, Integer pageSize) {
        Page<Favorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        
        IPage<Favorite> favoritePage = page(page, wrapper);
        
        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, favoritePage.getTotal());
        List<Map<String, Object>> records = favoritePage.getRecords().stream().map(favorite -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", favorite.getId());
            map.put("userId", favorite.getUserId());
            map.put("scenicId", favorite.getScenicId());
            map.put("createTime", favorite.getCreateTime());
            
            ScenicSpot scenic = scenicSpotService.getById(favorite.getScenicId());
            if (scenic != null) {
                Map<String, Object> scenicMap = new HashMap<>();
                scenicMap.put("id", scenic.getId());
                scenicMap.put("name", scenic.getName());
                scenicMap.put("coverImage", scenic.getCoverImage());
                scenicMap.put("rating", scenic.getRating());
                scenicMap.put("province", scenic.getProvince());
                scenicMap.put("city", scenic.getCity());
                scenicMap.put("ticketPrice", scenic.getTicketPrice());
                map.put("scenic", scenicMap);
            }
            
            return map;
        }).collect(Collectors.toList());
        
        resultPage.setRecords(records);
        return resultPage;
    }
}
