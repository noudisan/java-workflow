package com.it.cloud.common.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.cloud.common.constants.PageConstant;
import com.it.cloud.common.xss.SQLFilter;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import java.util.Map;

/**
 *
 * @author 司马缸砸缸了
 * @date 2019-07-12
 * @description 查询参数
 */
@Data
public class Query<T> {

    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private long curPage = 1;

    /**
     * 每页条数
     */
    private long limit = 10;

    public Query(Map<String, Object> params) {
        if (params.get(PageConstant.PAGE) != null) {
            curPage = Long.parseLong((String)params.get(PageConstant.PAGE));
        }
        if (params.get(PageConstant.LIMIT) != null) {
            limit = Long.parseLong((String)params.get(PageConstant.LIMIT));
        }

        // 分页对象
        this.page = new Page<>(curPage, limit);

        // 分页参数
        params.put(PageConstant.PAGE, page);

        // 排序字段
        // 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(PageConstant.ORDER_FIELD));
        String order = (String)params.get(PageConstant.ORDER);

        // 前端字段排序
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (PageConstant.ASC.equalsIgnoreCase(order)) {
                this.page.addOrder(OrderItem.asc(orderField));
            } else {
                this.page.addOrder(OrderItem.desc(orderField));
            }
        }
    }
}
