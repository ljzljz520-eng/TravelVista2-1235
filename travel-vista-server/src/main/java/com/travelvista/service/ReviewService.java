package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.Review;

public interface ReviewService extends IService<Review> {
    boolean addReview(Review review);
    
    IPage<Review> pageByScenicSpotId(Integer page, Integer pageSize, Long scenicSpotId);
    
    IPage<Review> pageByUserId(Integer page, Integer pageSize, Long userId);
    
    boolean likeReview(Long id);
}
