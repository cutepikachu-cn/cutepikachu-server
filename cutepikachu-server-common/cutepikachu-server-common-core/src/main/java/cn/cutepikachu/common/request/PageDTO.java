package cn.cutepikachu.common.request;

import cn.cutepikachu.common.model.enums.SortOrderEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页请求类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Data
public class PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int current;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式
     */
    private SortOrderEnum sortOrder = SortOrderEnum.ASCENDING;

}
