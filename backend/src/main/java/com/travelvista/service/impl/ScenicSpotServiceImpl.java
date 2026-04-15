package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.ScenicSpotMapper;
import com.travelvista.service.ScenicSpotService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicSpotServiceImpl extends ServiceImpl<ScenicSpotMapper, ScenicSpot> implements ScenicSpotService {

    @Override
    public IPage<ScenicSpot> getScenicList(Integer pageNum, Integer pageSize, String keyword, String province, String city, String type, String sort) {
        Page<ScenicSpot> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(ScenicSpot::getStatus, 1);
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ScenicSpot::getName, keyword);
        }
        if (province != null && !province.isEmpty()) {
            wrapper.eq(ScenicSpot::getProvince, province);
        }
        if (city != null && !city.isEmpty()) {
            wrapper.eq(ScenicSpot::getCity, city);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq(ScenicSpot::getType, type);
        }
        
        if ("rating".equals(sort)) {
            wrapper.orderByDesc(ScenicSpot::getRating);
        } else if ("view".equals(sort)) {
            wrapper.orderByDesc(ScenicSpot::getViewCount);
        } else if ("review".equals(sort)) {
            wrapper.orderByDesc(ScenicSpot::getReviewCount);
        } else {
            wrapper.orderByDesc(ScenicSpot::getCreateTime);
        }
        
        return page(page, wrapper);
    }

    @Override
    public ScenicSpot getScenicDetail(Long id) {
        ScenicSpot scenicSpot = getById(id);
        if (scenicSpot == null) {
            throw new BusinessException("景点不存在");
        }
        return scenicSpot;
    }

    @Override
    public List<ScenicSpot> getHotScenic(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1)
                .orderByDesc(ScenicSpot::getViewCount)
                .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public List<ScenicSpot> getRecommendScenic(Long userId, Integer limit) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1)
                .orderByDesc(ScenicSpot::getRating)
                .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public void addViewCount(Long id) {
        ScenicSpot scenicSpot = getById(id);
        if (scenicSpot != null) {
            scenicSpot.setViewCount(scenicSpot.getViewCount() + 1);
            updateById(scenicSpot);
        }
    }
}
