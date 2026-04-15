package com.travelvista.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travelvista.entity.Review;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
}
