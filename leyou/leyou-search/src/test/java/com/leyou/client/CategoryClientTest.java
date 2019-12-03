package com.leyou.client;

import com.leyou.LeyouSearchApplication;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsRepository goodsReponsitory;

    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Test
    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
    }
    @Test
    public void createIndex1(){
        // 创建索引库，以及映射
        this.template.createIndex(Goods.class);
        this.template.putMapping(Goods.class);
    }
    @Test
    public void createIndex(){
        // 创建索引
        this.template.createIndex(Goods.class);
        // 配置映射
        this.template.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;

        do {
            // 分批查询spuBo
            PageResult<SpuBo> pageResult = this.goodsClient.querySpuBoByPage(null, true, page, rows);
            // 遍历spubo集合转化为List<Goods>
            List<Goods> goodsList = pageResult.getItems().stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods((Spu) spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            this.goodsReponsitory.saveAll(goodsList);

            // 获取当前页的数据条数，如果是最后一页，没有100条
            rows = pageResult.getItems().size();
            // 每次循环页码加1
            page++;
        } while (rows == 100);
    }
}