package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if entitySerialVersionUID>
import java.io.Serial;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
</#if>
import ${package.Parent}.model.vo.${vo};

/**
 * ${table.comment!}
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version ${version}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
@AllArgsConstructor
@NoArgsConstructor
</#if>
<#if table.convert>
@TableName(value = "`${schemaName}${table.name}`", autoResultMap = true)
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<${entity}, ${vo}><#if entitySerialVersionUID> implements Serializable</#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#elseif entitySerialVersionUID>
public class ${entity} implements Serializable {
<#else>
public class ${entity} {
</#if>

<#if entitySerialVersionUID>
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

</#if>
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @TableId(value = "`${field.columnName}`", type = IdType.AUTO)
        <#elseif idType??>
    @TableId(value = "`${field.columnName}`", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("`${field.columnName}`")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "`${field.columnName}`", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("`${field.columnName}`")
    </#if>
<#-- 乐观锁注解 -->
    <#if field.versionField>
    @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if field.logicDeleteField>
    @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
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
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
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
<#if entityColumnConstant>
    <#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";

    </#list>
</#if>
<#if !superEntityClass?? && activeRecord>
    @Override
    public Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

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
