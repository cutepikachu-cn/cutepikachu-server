package ${package.Parent}.model.dto;

<#list table.importPackages as pkg>
    <#if !pkg?contains("mybatisplus") && !pkg?contains("BaseEntity")>
import ${pkg};
    </#if>
</#list>
import ${projectPackage}.common.request.PageDTO;
<#if entitySerialVersionUID>
import java.io.Serial;
</#if>
<#if entityLombokModel>
import lombok.Data;
</#if>

/**
 * 查询 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
</#if>
public class ${entity}QueryDTO extends PageDTO<#if entitySerialVersionUID> implements Serializable</#if> {

<#if entitySerialVersionUID>
    @Serial
    private static final long serialVersionUID = 1L;

</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if !field.logicDeleteField>
        <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
        </#if>
    private ${field.propertyType} ${field.propertyName};

    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

        <#if chainModel>
    public ${entity}QueryDTO set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if chainModel>
        return this;
        </#if>
    }

    </#list>
</#if>
<#if !entityLombokModel>
    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
                "${field.propertyName} = " + ${field.propertyName} +
        <#else>
                ", ${field.propertyName} = " + ${field.propertyName} +
        </#if>
    </#list>
            "}";
    }

</#if>
}
