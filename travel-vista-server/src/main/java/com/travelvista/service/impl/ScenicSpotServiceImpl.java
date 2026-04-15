package com.travelvista.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.mapper.ScenicSpotMapper;
import com.travelvista.service.ScenicSpotService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ScenicSpotServiceImpl extends ServiceImpl<ScenicSpotMapper, ScenicSpot> implements ScenicSpotService {

    @Override
    public IPage<ScenicSpot> pageScenicSpot(Integer page, Integer pageSize, String name, String province, String city, String type, String level, BigDecimal minRating, BigDecimal maxRating) {
        Page<ScenicSpot> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicSpot::getStatus, 1);
        
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like(ScenicSpot::getName, name);
        }
        if (StrUtil.isNotBlank(province)) {
            queryWrapper.eq(ScenicSpot::getProvince, province);
        }
        if (StrUtil.isNotBlank(city)) {
            queryWrapper.eq(ScenicSpot::getCity, city);
        }
        if (StrUtil.isNotBlank(type)) {
            queryWrapper.eq(ScenicSpot::getType, type);
        }
        if (StrUtil.isNotBlank(level)) {
            queryWrapper.eq(ScenicSpot::getLevel, level);
        }
        if (minRating != null) {
            queryWrapper.ge(ScenicSpot::getRating, minRating);
        }
        if (maxRating != null) {
            queryWrapper.le(ScenicSpot::getRating, maxRating);
        }
        
        queryWrapper.orderByDesc(ScenicSpot::getCreateTime);
        return page(pageParam, queryWrapper);
    }

    @Override
    public ScenicSpot getDetailById(Long id) {
        ScenicSpot scenicSpot = getById(id);
        if (scenicSpot != null && scenicSpot.getStatus() == 1) {
            scenicSpot.setViewCount(scenicSpot.getViewCount() + 1);
            updateById(scenicSpot);
        }
        return scenicSpot;
    }

    @Override
    public List<ScenicSpot> getHotScenicSpot(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicSpot::getStatus, 1);
        queryWrapper.orderByDesc(ScenicSpot::getViewCount);
        queryWrapper.last("limit " + limit);
        return list(queryWrapper);
    }

    @Override
    public List<ScenicSpot> getRecommendScenicSpot(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicSpot::getStatus, 1);
        queryWrapper.orderByDesc(ScenicSpot::getRating, ScenicSpot::getReviewCount);
        queryWrapper.last("limit " + limit);
        return list(queryWrapper);
    }
}
