package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.ReviewAddDTO;
import com.travelvista.entity.Review;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.ReviewMapper;
import com.travelvista.service.ReviewService;
import com.travelvista.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReview(Long userId, ReviewAddDTO dto) {
        ScenicSpot scenic = scenicSpotService.getById(dto.getScenicId());
        if (scenic == null) {
            throw new BusinessException("景点不存在");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setScenicId(dto.getScenicId());
        review.setContent(dto.getContent());
        review.setImages(dto.getImages());
        review.setRating(dto.getRating());
        review.setStatus(1);
        review.setLikeCount(0);
        save(review);

        updateScenicRating(dto.getScenicId());
    }

    private void updateScenicRating(Long scenicId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getScenicId, scenicId).eq(Review::getStatus, 1);
        List<Review> reviews = list(wrapper);

        if (!reviews.isEmpty()) {
            BigDecimal totalRating = reviews.stream()
                    .map(Review::getRating)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avgRating = totalRating.divide(BigDecimal.valueOf(reviews.size()), 2, RoundingMode.HALF_UP);

            ScenicSpot scenic = scenicSpotService.getById(scenicId);
            if (scenic != null) {
                scenic.setRating(avgRating);
                scenic.setReviewCount(reviews.size());
                scenicSpotService.updateById(scenic);
            }
        }
    }

    @Override
    public IPage<Review> getScenicReviews(Long scenicId, Integer pageNum, Integer pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getScenicId, scenicId)
                .eq(Review::getStatus, 1)
                .orderByDesc(Review::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public IPage<Review> getUserReviews(Long userId, Integer pageNum, Integer pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getUserId, userId)
                .orderByDesc(Review::getCreateTime);
        return page(page, wrapper);
    }
}
