package com.travelvista.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.ScenicQueryDTO;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.ScenicSpotMapper;
import com.travelvista.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicSpotServiceImpl extends ServiceImpl<ScenicSpotMapper, ScenicSpot> implements ScenicSpotService {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    @Override
    public IPage<ScenicSpot> searchScenic(ScenicQueryDTO dto) {
        Page<ScenicSpot> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return scenicSpotMapper.searchScenic(page, dto.getKeyword(), dto.getCategoryId(),
                dto.getProvince(), dto.getCity(), dto.getMinPrice(), dto.getMaxPrice(), dto.getMinRating());
    }

    @Override
    public ScenicSpot getScenicDetail(Long id) {
        ScenicSpot scenic = getById(id);
        if (scenic == null || scenic.getStatus() == 0) {
            throw new BusinessException("景点不存在或已下架");
        }
        return scenic;
    }

    @Override
    public void incrementViewCount(Long id) {
        ScenicSpot scenic = getById(id);
        if (scenic != null) {
            scenic.setViewCount(scenic.getViewCount() + 1);
            updateById(scenic);
        }
    }

    @Override
    public List<ScenicSpot> getHotScenic(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1)
                .eq(ScenicSpot::getIsHot, 1)
                .orderByDesc(ScenicSpot::getViewCount)
                .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public List<ScenicSpot> getRecommendedScenic(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getStatus, 1)
                .eq(ScenicSpot::getIsRecommended, 1)
                .orderByDesc(ScenicSpot::getRating)
                .last("LIMIT " + limit);
        return list(wrapper);
    }
}
