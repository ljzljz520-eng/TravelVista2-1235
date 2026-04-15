package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.ScenicSpot;
import java.math.BigDecimal;
import java.util.List;

public interface ScenicSpotService extends IService<ScenicSpot> {
    IPage<ScenicSpot> pageScenicSpot(Integer page, Integer pageSize, String name, String province, String city, String type, String level, BigDecimal minRating, BigDecimal maxRating);
    
    ScenicSpot getDetailById(Long id);
    
    List<ScenicSpot> getHotScenicSpot(Integer limit);
    
    List<ScenicSpot> getRecommendScenicSpot(Integer limit);
}
