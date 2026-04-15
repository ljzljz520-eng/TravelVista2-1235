package com.travelvista.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.Review;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.mapper.ReviewMapper;
import com.travelvista.service.ReviewService;
import com.travelvista.service.ScenicSpotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Resource
    private ScenicSpotService scenicSpotService;

    @Override
    @Transactional
    public boolean addReview(Review review) {
        if (StrUtil.isBlank(review.getContent())) {
            throw new RuntimeException("评论内容不能为空");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5分之间");
        }
        
        Long userId = StpUtil.getLoginIdAsLong();
        review.setUserId(userId);
        review.setStatus(1);
        review.setLikeCount(0);
        
        boolean result = save(review);
        if (result) {
            updateScenicSpotRating(review.getScenicSpotId());
        }
        return result;
    }

    @Override
    public IPage<Review> pageByScenicSpotId(Integer page, Integer pageSize, Long scenicSpotId) {
        Page<Review> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Review> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Review::getScenicSpotId, scenicSpotId);
        queryWrapper.eq(Review::getStatus, 1);
        queryWrapper.orderByDesc(Review::getCreateTime);
        return page(pageParam, queryWrapper);
    }

    @Override
    public IPage<Review> pageByUserId(Integer page, Integer pageSize, Long userId) {
        Page<Review> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Review> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Review::getUserId, userId);
        queryWrapper.orderByDesc(Review::getCreateTime);
        return page(pageParam, queryWrapper);
    }

    @Override
    public boolean likeReview(Long id) {
        Review review = getById(id);
        if (review == null) {
            throw new RuntimeException("评论不存在");
        }
        review.setLikeCount(review.getLikeCount() + 1);
        return updateById(review);
    }

    private void updateScenicSpotRating(Long scenicSpotId) {
        LambdaQueryWrapper<Review> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Review::getScenicSpotId, scenicSpotId);
        queryWrapper.eq(Review::getStatus, 1);
        long count = count(queryWrapper);
        
        Double avgRating = baseMapper.selectObjs(queryWrapper.select(Review::getRating))
                .stream()
                .mapToInt(rating -> (Integer) rating)
                .average()
                .orElse(0.0);
        
        ScenicSpot scenicSpot = scenicSpotService.getById(scenicSpotId);
        scenicSpot.setReviewCount((int) count);
        scenicSpot.setRating(BigDecimal.valueOf(avgRating).setScale(1, BigDecimal.ROUND_HALF_UP));
        scenicSpotService.updateById(scenicSpot);
    }
}
