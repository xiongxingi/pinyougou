package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品数据
     */
    public void importItemData(){
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//已审核的数据
        List<TbItem> itemList = itemMapper.selectByExample(example);

        System.out.println("=======商品列表========");
        for (TbItem item : itemList) {
            Map specMap = JSON.parseObject(item.getSpec());//将spec字段中的json数据转换为Map
            item.setSpecMap(specMap);//给带注解的字段赋值
            System.out.println(item.getTitle());
        }
        //solrTemplate.saveBeans(itemList);
        
        SolrDataQuery query = new SimpleQuery("*:*");
		solrTemplate.delete(query );
        solrTemplate.commit();
        

        System.out.println("=========结束========");
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }
}