package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.ReviewAddDTO;
import com.travelvista.entity.Review;

public interface ReviewService extends IService<Review> {

    void addReview(Long userId, ReviewAddDTO dto);

    IPage<Review> getScenicReviews(Long scenicId, Integer pageNum, Integer pageSize);

    IPage<Review> getUserReviews(Long userId, Integer pageNum, Integer pageSize);
}
