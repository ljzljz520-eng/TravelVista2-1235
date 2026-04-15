package com.travelvista.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.Category;
import com.travelvista.mapper.CategoryMapper;
import com.travelvista.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
