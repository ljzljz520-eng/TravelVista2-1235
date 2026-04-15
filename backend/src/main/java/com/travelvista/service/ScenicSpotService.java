package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.ScenicSpot;

import java.util.List;

public interface ScenicSpotService extends IService<ScenicSpot> {
    IPage<ScenicSpot> getScenicList(Integer pageNum, Integer pageSize, String keyword, String province, String city, String type, String sort);
    ScenicSpot getScenicDetail(Long id);
    List<ScenicSpot> getHotScenic(Integer limit);
    List<ScenicSpot> getRecommendScenic(Long userId, Integer limit);
    void addViewCount(Long id);
}
