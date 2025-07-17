package com.example.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.category.feign.DocumentFeignClient;
import com.example.category.service.CategoryService;
import com.example.common.R;
import com.example.domain.Category;
import com.example.domain.Document;
import com.example.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DocumentFeignClient documentFeignClient;

    @Override
    public Category getCategoryByName(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCtgName, name);
        return this.getOne(queryWrapper);
    }

    @Override
    @GlobalTransactional
    public R updateCategory(Category category) {

            // 级联更新文档
            String ctgName = category.getCtgName();
            R data = documentFeignClient.getDocumentListByCtgName(ctgName);
            List<Document> documentList = (List<Document>) data.getData();

            log.info("获取到文档列表：{}", documentList);

            if(documentList != null && !documentList.isEmpty()) {
                log.info("开始级联更新文档");
                for(Document document : documentList) {
                    document.setCategory(ctgName);
                    documentFeignClient.updateByDocumentId(document);
                }
            }


            // 更新类别
            this.updateById(category);

            log.info("文档类别更新成功");
            R<Category> result = R.success();
            result.setMsg("文档类别更新成功");
            return result;



    }
}
