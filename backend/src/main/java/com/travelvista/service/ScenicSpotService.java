package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.ScenicQueryDTO;
import com.travelvista.entity.ScenicSpot;

import java.util.List;

public interface ScenicSpotService extends IService<ScenicSpot> {

    IPage<ScenicSpot> searchScenic(ScenicQueryDTO dto);

    ScenicSpot getScenicDetail(Long id);

    void incrementViewCount(Long id);

    List<ScenicSpot> getHotScenic(Integer limit);

    List<ScenicSpot> getRecommendedScenic(Integer limit);
}
