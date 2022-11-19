package com.abbkit.project.codegenerator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.util.Collections;
import java.util.Scanner;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("jdbc:mysql://mysql.ainobug.com:50002/dataio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true"
                , "lemon", "cslg@05126585")
                .dbQuery(new MySqlQuery())
                .schema("dataio")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .build();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator(dataSourceConfig);

        com.baomidou.mybatisplus.generator.config.GlobalConfig globalConfig = new com.baomidou.mybatisplus.generator.config.GlobalConfig.Builder()
                .fileOverride()
                .outputDir("D:\\J\\git\\blog\\server\\server-code-generator\\src\\test\\java")
                .author("J")
                .dateType(DateType.TIME_PACK)
                .commentDate("yyyy-MM-dd")
                .build();
        mpg.global(globalConfig);


        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.baomidou.mybatisplus.samples.generator")
                .moduleName("blog")   //配置模块名称，会出现在controller的请求路径中
                .entity("po")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller")
                .other("other")
                .pathInfo(Collections.singletonMap(OutputFile.xml, "D://"))
                .build();

        mpg.packageInfo(packageConfig);

        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(scanner("表"))
                .addTablePrefix("t_")
                .entityBuilder()
                .disableSerialVersionUID()
                .enableChainModel()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
//                .enableActiveRecord()
                .versionColumnName("version")
                .versionPropertyName("version")
                .logicDeleteColumnName("deleted")
                .logicDeletePropertyName("deleted")
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .addSuperEntityColumns("id", "create_id", "create_time", "update_id", "update_time","deleted","version")
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                .idType(IdType.ASSIGN_ID)
                .formatFileName("%sEntity")
                //----------- controller start
                .controllerBuilder()
                .enableRestStyle()
                .formatFileName("%sController")
                //----------- service start
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .build();
        mpg.strategy(strategyConfig);

        TemplateConfig templateConfig = new TemplateConfig.Builder()
                .disable(TemplateType.ENTITY)
                .entity("/templates/entity.java")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .mapper("/templates/mapper.java")
                .xml("/templates/mapper.xml")
                .controller("/templates/controller.java")
                .build();
        mpg.template(templateConfig);

        mpg.execute();
    }

}