package cn.cutepikachu.leaf.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private long id;

    private Status status;

}
