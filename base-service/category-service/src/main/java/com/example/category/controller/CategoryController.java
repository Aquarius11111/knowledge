package com.example.category.controller;


//import com.example.category.feign.ProductFeignClient;
import com.example.category.feign.DocumentFeignClient;
import com.example.category.service.CategoryService;
import com.example.common.R;
import com.example.domain.Category;
import com.example.domain.dto.CategoryDTO;
import com.example.domain.Document;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "CategoryController:类别控制器")
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

     @Autowired
     private CategoryService categoryService;

     @Autowired
     private DocumentFeignClient documentFeignClient;

     @Autowired
     DiscoveryClient discoveryClient;

     @Operation(summary = "创建文档类别")
     @PostMapping("/add")
     public R createCategory(@RequestBody CategoryDTO ctgDTO) throws Exception{
         Category category = new Category();
         if(ctgDTO.getDescription() == null){
             category.setCtgDesc("暂无描述");
         } else
             category.setCtgDesc(ctgDTO.getDescription());

         if(ctgDTO.getStatus() == null){
             category.setCtgEnabled(true);
         } else
             category.setCtgEnabled(ctgDTO.getStatus() == 1);

         category.setCtgName(ctgDTO.getName());
         category.setCtgParentId(-1);
         category.setCtgCreatedTime(new Date());
         category.setCtgDocCount(0);

         log.info("新增文档类别信息:"+category);
         categoryService.save(category);
         log.info("创建文档类别成功");

         R<String> result = R.success();
         result.setMsg("文档类别创建成功," + category);
         return result;

     }

     @Operation(summary = "删除单个文档类别")
     @DeleteMapping("/delete")
     public R<Category> deleteCategory(@RequestBody CategoryDTO ctgDTO) throws Exception{
         Integer ctgId = ctgDTO.getId();
         log.info("删除文档类别信息:"+ctgId);
         if(categoryService.removeById(ctgId)){
             log.info("文档类别不存在");
             R<Category> result = R.error();
             result.setMsg("文档类别不存在");
             return R.error();
         } else {
             if(categoryService.getById(ctgId).getCtgDocCount() > 0) {
                 log.info("文档类别删除成功");
                 R<Category> result = R.success();
                 result.setMsg("文档类别删除成功");
                 return R.success();
             } else {
                 log.info("该类别下还有文档，无法删除");
                 R<Category> result = R.error();
                 result.setMsg("该类别下还有文档，无法删除");
                 return result;
             }
         }

     }

     @Operation(summary = "删除多个文档类别")
     @DeleteMapping("/batchDelete")
     public R<String> batchDeleteCategory(@RequestBody Map<String, Object> map) throws Exception{
         List<Integer> ids = map.get("ids") == null ? null : (List<Integer>) map.get("ids");
         log.info("批量删除文档类别id列表:"+ids);
         if(ids == null || ids.size() == 0) {
             log.info("批量删除文档类别id列表为空");
             R<String> result = R.error();
             result.setMsg("未选择删除文件");
             return result;
         }

         List<Category> categoryList = categoryService.listByIds(ids);
         for(Category category : categoryList) {
             if(category.getCtgDocCount() > 0) {
                 log.info("文档类别"+category.getCtgName()+"下还有文档，无法删除");
                 R<String> result = R.error();
                 result.setMsg("文档类别"+category.getCtgName()+"下还有文档，无法删除");
                 return result;
             }
         }

         if(categoryService.removeByIds(ids)){
             log.info("批量删除文档类别成功");
             R<String> result = R.success();
             result.setMsg("批量删除文档类别成功");
             return result;
         } else {
             log.info("批量删除文档类别失败");
             R<String> result = R.error();
             result.setMsg("批量删除文档类别失败");
             return result;
         }
     }

     @Operation(summary = "更新文档类别")
     @PutMapping("/update")
     public R<Category> updateCategory(@RequestBody CategoryDTO ctgDTO) throws Exception{
         Category category = categoryService.getById(ctgDTO.getId());
         category.setCtgName(ctgDTO.getName());
         category.setCtgDesc(ctgDTO.getDescription());
         category.setCtgEnabled(ctgDTO.getStatus() == 1);
         log.info("更新文档类别信息:ctgId="+category.getCtgId()+",category="+category);

         return categoryService.updateCategory(category);
     }

     @Operation(summary = "修改文档类别启用状态")
     @PutMapping("/enable/{ctgId}")
     public R<Category> enableCategory(@PathVariable Integer ctgId) throws Exception{
         log.info("修改文档类别启用状态:ctgId="+ctgId);
         Category category = categoryService.getById(ctgId);
         if(category!= null){
             category.setCtgEnabled(!category.getCtgEnabled());
             if(categoryService.updateById(category)){
                 log.info("文档类别启用状态切换成功");
                 R<Category> result = R.success();
                 result.setMsg("文档类别启用状态切换成功，当前状态为："+category.getCtgEnabled());
                 return result;
             } else {
                 log.info("文档类别不存在");
                 R<Category> result = R.error();
                 result.setMsg("文档类别不存在");
                 return result;
             }
         } else {
             log.info("文档类别不存在");
             R<Category> result = R.error();
             result.setMsg("文档类别不存在");
             return result;
         }
     }

    @Operation(summary = "获取文档类别列表")
    @GetMapping("/list")
    public R<List<CategoryDTO>> listCategory() {
        log.info("获取文档类别列表");
        List<Category> categoryList = categoryService.list();

        // 转换为 DTO
        List<CategoryDTO> dtoList = categoryList.stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getCtgId());
            dto.setName(category.getCtgName());
            dto.setDescription(category.getCtgDesc());
            dto.setStatus(category.getCtgEnabled() ? 1 : 0); // Boolean转Integer
            dto.setDocumentCount(category.getCtgDocCount());
            dto.setCreateTime(category.getCtgCreatedTime());
            return dto;
        }).collect(Collectors.toList());

        R<List<CategoryDTO>> result = R.success(dtoList);
        result.setMsg("获取文档类别列表成功");
        return result;
    }

    // 可扩展部分

     @Operation(summary = "获取文档类别详情")
     @GetMapping("/{ctgId}")
     public R<Category> getCategory(@PathVariable Integer ctgId) throws Exception{
         log.info("获取文档类别详情:ctgId="+ctgId);
         Category category = categoryService.getById(ctgId);
         if(category != null){
             log.info("文档类别详情:"+category);
             R<Category> result = R.success();
             result.setData(category);
             result.setMsg("获取文档类别详情成功");
             return result;
         } else {
             log.info("文档类别不存在");
             R<Category> result = R.error();
             result.setMsg("文档类别不存在");
             return result;
         }
     }

    @Operation(summary = "根据种类名称获取对应种类详细信息")
    @GetMapping("/name/{ctgName}")
    public R getCategoryByName(@PathVariable String ctgName) throws Exception{
        Category ctg = categoryService.getCategoryByName(ctgName);
        R<Category> result = R.success(ctg);
        result.setMsg("找到对应种类的文件");

        return result;
    }

    @GlobalTransactional
    @Operation(summary = "根据种类名称获取对应的所有文件")
    @GetMapping("/{ctgName}/document")
    public R getCategoryById(@PathVariable  String ctgName) throws Exception{

        log.info("开始根据类别编号获取商品类别信息...");

        System.out.println("load category, id="+ctgName);
        Category ctg = categoryService.getCategoryByName(ctgName);

        R data = documentFeignClient.getDocumentListByCtgName(ctgName);
        List<Document> documentList = (List<Document>) data.getData();
        ctg.setDocumentList(documentList);

        log.info("通过OpenFeign获取数据，得到文件数量:"+ documentList.size());


        R<Category> result = R.success(ctg);
        result.setMsg("文件类别所对应的文件列表加载完成！");

        return result;
    }

    @GlobalTransactional
    @Operation(summary = "强制级联删除文档类别")
    @DeleteMapping("/forceDelete/{ctgId}")
    public R<String> forceDeleteCategory(@PathVariable Integer ctgId) throws Exception{
         log.info("强制级联删除文档类别:ctgId="+ctgId);
         if(categoryService.removeById(ctgId)){
             log.info("文档类别不存在");
             R<String> result = R.error();
             result.setMsg("文档类别不存在");
             return result;
         } else {

             // 级联删除文档
             String ctgName = categoryService.getById(ctgId).getCtgName();
             R data = documentFeignClient.getDocumentListByCtgName(ctgName);
             List<Document> documentList = (List<Document>) data.getData();

             for(Document document : documentList) {
                 documentFeignClient.deleteDocumentById(document.getDocumentId());
             }

             log.info("文档类别删除成功");
             R<String> result = R.success();
             result.setMsg("文档类别删除成功");
             return result;
         }
    }

}
