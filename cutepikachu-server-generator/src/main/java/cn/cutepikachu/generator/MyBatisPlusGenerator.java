package cn.cutepikachu.generator;

import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Mybatis-Plus Generator
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class MyBatisPlusGenerator {

    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/cutepikachu-server-generator/src/main/java";

    private static final String AUTH = "笨蛋皮卡丘";

    public static final String VERSION = "1.0";

    private static final String OUTPUT_PACKAGE = "cn.cutepikachu.generator";

    private static final String PROJECT_PACKAGE = "cn.cutepikachu";

    private static final String DATABASE = "cutepikachu_cn_ai";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        String url = String.format("jdbc:mysql://localhost:3306/%s?remarks=true&useInformationSchema=true&tinyInt1isBit=true", DATABASE);

        DataSourceConfig.Builder dataSourceConfig = new DataSourceConfig.Builder(url, USERNAME, PASSWORD);

        FastAutoGenerator
                // 数据源配置
                .create(dataSourceConfig)
                // 全局配置
                .globalConfig(globalConfigBuilder -> globalConfigBuilder
                        .outputDir(OUTPUT_DIR) // 输出目录
                        // 禁止打开输出目录
                        .disableOpenDir()
                        // 作者名
                        .author(AUTH)
                        // 时间策略
                        .dateType(DateType.TIME_PACK)
                        .commentDate("yyyy-MM-dd HH:mm:ss")
                ).packageConfig(packageConfigBuilder -> packageConfigBuilder
                        .parent(OUTPUT_PACKAGE) // 父包名
                        // .moduleName("application") // 父包模块名
                        .entity("model.entity") // Entity 包名
                        .service("service") // Service 包名
                        .serviceImpl("service.impl") // Service Impl 包名
                        .mapper("mapper") // Mapper 包名
                        .xml("mapper.xml") // Mapper XML 包名
                        .controller("controller") // Controller 包名
                )
                // 策略配置
                .strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.enableCapitalMode().enableSkipView()

                        // 实体策略配置
                        .entityBuilder().enableColumnConstant() // 开启生成字段常量
                        .enableChainModel() // 开启链式模型
                        .enableLombok() // 开启 lombok 模型
                        .enableTableFieldAnnotation() // 开启生成实体时生成字段注解
                        .enableActiveRecord() // 开启 ActiveRecord 模型
                        .enableRemoveIsPrefix() // 开启移除 is 前缀
                        .superClass(BaseEntity.class) // 继承的 Entity 类
                        .logicDeleteColumnName("is_delete") // 逻辑删除字段名(数据库字段)
                        .naming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                        .columnNaming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略
                        // .addIgnoreColumns("create_time", "update_time", "is_delete") // 忽略字段
                        .addTableFills(new Column("create_time", FieldFill.INSERT), new Column("update_time", FieldFill.INSERT_UPDATE)) // 添加表字段填充
                        // .idType(IdType.ASSIGN_ID) // 全局主键类型
                        .enableFileOverride() // 覆盖文件
                        .javaTemplate("/templates/model/entity/entity.java") // 模板

                        // controller 策略配置
                        .controllerBuilder().enableRestStyle() // 开启生成 @RestController 控制器
                        .enableFileOverride() // 覆盖文件
                        .enableHyphenStyle() // 开启驼峰转连字符
                        .template("/templates/controller/controller.java") // 模板
                        .disable()

                        // mapper 策略配置
                        .mapperBuilder().mapperAnnotation(Mapper.class) // 开启 @Mapper 注解
                        .enableBaseResultMap() // 启用 BaseResultMap 生成
                        .enableBaseColumnList() // 启用 BaseColumnList
                        .enableFileOverride() // 覆盖文件
                        .mapperTemplate("/templates/mapper/mapper.java") // 模板
                        .mapperXmlTemplate("/templates/mapper/xml/mapper.xml") // XML 模板

                        // service 策略配置
                        .serviceBuilder().superServiceClass(IService.class) // Service 接口父类
                        .superServiceImplClass(ServiceImpl.class) // Service 实现类父类
                        .enableFileOverride() // 覆盖文件
                        .serviceTemplate("/templates/service/service.java") // 模板
                        .serviceImplTemplate("/templates/service/impl/serviceImpl.java") // 模板
                )
                // 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())
                // 注入配置
                .injectionConfig(injectionConfigBuilder -> {
                    // VO 模板
                    CustomFile voFile = new CustomFile.Builder()
                            .fileName("VO.java")
                            .packageName("model.vo")
                            .templatePath("/templates/model/vo/vo.java.ftl")
                            .enableFileOverride()
                            .build();
                    // Controller 模板
                    CustomFile controllerFile = new CustomFile.Builder()
                            .fileName("Controller.java")
                            .packageName("controller")
                            .templatePath("/templates/controller/controller.java.ftl")
                            .enableFileOverride()
                            .build();
                    // QueryDTO 模板
                    CustomFile queryRequestFile = new CustomFile.Builder()
                            .fileName("QueryDTO.java")
                            .packageName("model.dto")
                            .templatePath("/templates/model/dto/queryDTO.java.ftl")
                            .enableFileOverride()
                            .build();
                    // CreateDTO 模板
                    CustomFile createRequest = new CustomFile.Builder()
                            .fileName("CreateDTO.java")
                            .packageName("model.dto")
                            .templatePath("/templates/model/dto/createDTO.java.ftl")
                            .enableFileOverride()
                            .build();
                    // UpdateDTO 模板
                    CustomFile updateRequest = new CustomFile.Builder()
                            .fileName("UpdateDTO.java")
                            .packageName("model.dto")
                            .templatePath("/templates/model/dto/updateDTO.java.ftl")
                            .enableFileOverride()
                            .build();
                    injectionConfigBuilder.customFile(voFile);
                    injectionConfigBuilder.customFile(controllerFile);
                    injectionConfigBuilder.customFile(queryRequestFile);
                    injectionConfigBuilder.customFile(createRequest);
                    injectionConfigBuilder.customFile(updateRequest);

                    // 自定义参数
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("projectPackage", PROJECT_PACKAGE);
                    customMap.put("version", VERSION);
                    injectionConfigBuilder.customMap(customMap);

                    // 自定义输出文件
                    injectionConfigBuilder.beforeOutputFile((tableInfo, objectMap) -> {
                        String entity = objectMap.get("entity").toString();
                        String vo = entity + "VO";
                        objectMap.put("vo", vo);
                        String service = entity.substring(0, 1).toLowerCase() + entity.substring(1) + "Service";
                        objectMap.put("service", service);
                    });
                }).execute();
    }
}
