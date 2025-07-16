package com.example.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.R;
import com.example.domain.Category;

public interface CategoryService extends IService<Category> {

    Category getCategoryByName(String name);

    R updateCategory(Category category);
}
