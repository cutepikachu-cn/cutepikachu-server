package ${package.ServiceImpl};

<#list table.importPackages as pkg>
    <#if !pkg?contains("mybatisplus") && !pkg?contains("BaseEntity") && !pkg?contains("Serializable")>
        import ${pkg};
    </#if>
</#list>
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if table.serviceInterface>
    import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
* ${table.comment!} 服务实现类
*
* @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
* @version ${version}
* @since ${date}
*/
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if table.serviceInterface> implements ${table.serviceName}</#if> {

}
