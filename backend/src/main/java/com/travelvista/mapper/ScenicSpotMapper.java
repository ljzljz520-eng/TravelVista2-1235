package com.travelvista.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travelvista.entity.ScenicSpot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScenicSpotMapper extends BaseMapper<ScenicSpot> {

    Page<ScenicSpot> searchScenic(Page<ScenicSpot> page,
                                   @Param("keyword") String keyword,
                                   @Param("categoryId") Long categoryId,
                                   @Param("province") String province,
                                   @Param("city") String city,
                                   @Param("minPrice") java.math.BigDecimal minPrice,
                                   @Param("maxPrice") java.math.BigDecimal maxPrice,
                                   @Param("minRating") java.math.BigDecimal minRating);
}
