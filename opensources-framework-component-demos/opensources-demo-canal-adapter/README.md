## 1.工程总体结构说明

    工程总体结构如下图示，通常会包含api、biz、boot等三个核心子模块：
        |-- org.opensourceframework.canal.apapter.demo.boot 
            |-- config                             ESSyncConfig使用annotation定义配置类,目前注解只支持单表或hash分表
                |--database_schema_name_1          数据库的schema名_1
                    |--table表对应的驼峰类.java      数据库表名对应驼峰类型的类名,单表的索引名即为数据表的表名
                    |--......
                |--database_schema_name_2          数据库的schema名_2
                |--database_schema_name_3          数据库的schema名_3
                |--.....
                |--database_schema_n                   
            |-- DemoCanalAdapterBoot.java
        |--es                                     canal-adapter-es配置文件目录
           |--database_schema_name_1
              |--es_index_name_1.yml               es_index_name_1对应es中的索引名 
              |--es_index_name_2.yml
              |--es_index_name_3.yml
              |--.....
              |--es_index_name_n.yml
           |-database_schema_name_2
              |--es_index_name_1.yml
              |--......                        
        |--META-INF  
           |--spring.factories                     Spring Starter定义自动加载类
        |--application.yml                         canal配置
        |--bootstrap.yml                           全局总体配置
        |--logback-spring.xml                      logback日志配置文件    
        
## 2.注解@ESDocument、@ESField说明
    
    @ESDocument
    
    @ESField
## 3.Application.yml配置说明

## 4.ESMapping配置文件说明
## 4.1 Sql支持多表关联自由组合, 但是有一定的限制:
   
    --  主表不能为子查询语句
    --  只能使用left outer join即最左表一定要是主表
    --  关联从表如果是子查询不能有多张表
    --  主sql中不能有where查询条件(从表子查询中可以有where条件但是不推荐, 可能会造成数据同步的不一致, 比如修改了where条件中的字段内容)
    --  关联条件只允许主外键的'='操作不能出现其他常量判断比如: on a.role_id=b.id and b.statues=1 
    --  关联条件必须要有一个字段出现在主查询语句中比如: on a.role_id=b.id  其中的 a.role_id 或者 b.id 必须出现在主select语句中
    
## 4.2 单表同步 普通单表 /resources/es/opensourceframework_demo/demo_canal_single_table_adress.yml ,关键属性说明:
    
    # 数据源key 对应application.yml中定义的 canal.conf.srcDataSources.opensourceframework_demo
    dataSourceKey: opensourceframework_demo
    # 对应application.yml中  canal.conf.canalAdapters.groups.outerAdapters.key
    outerAdapterKey: opensourceframework_demo
    #对应application.yml中   canal.conf.canalAdapters.instance
    destination: CANAL_DEMO_TOPIC
    #对应application.yml中   canal.conf.canalAdapters.groups.groupId
    groupId: opensourceframework_demo_20200930
    esDocument:
      # 索引名
      _index: demo_canal_single_table_adress
      # type值同索引名
      _type: demo_canal_single_table_adress
      # es 的_id, 如果不配置该项必须配置下面的pk项_id则会由es自动分配
      _id: id
      # 如果不需要_id, 则需要指定一个属性为主键属性
      # pk: id
      # 全量同步数据sql语句 在多表聚合同步时，有时会根据sql查询结果更新es
      sql: "SELECT a.id , a.province_code , a.province_name , a.city_code , a.city_name , a.user_id,
                  a.county_code , a.county_name ,a.application_id, a.create_person, a.create_time ,
           	   a.create_time_stamp, a.update_person, a.update_time, a.update_time_stamp, a.dr
           FROM demo_canal_single_table_address a"
      # 条件语句 条件语句不可在sql中
      #etlCondition: "where u.create_time>={}"
      commitBatch: 3000
      # es索引的mapping
      mappings: '{}'

## 4.3 单表同步 Hash分表  /resources/es/opensourceframework_demo/demo_canal_sharding_table_yml.yml,关键属性说明:
    
    sql: "SELECT m.id , m.map_key , m.serial_no ,m.sys_no, m.core_serial_no ,m.application_id,m.create_person,
            	 m.create_time,m.create_time_stamp,m.update_person,m.update_time,m.update_time_stamp,m.dr
          FROM demo_canal_sharding_table_yml m "
    # table属性标明sql中的虚拟主表名
    table: demo_canal_sharding_table_yml
    # hash分表的分表数
    shardingTableCount: 16
    
    其他属性同普遍单表
    --  【Hash分表】、【普通分表】支持annotation 例子参见 org.opensourceframework.canal.adapter.demo.boot.config.opensourceframework_demo.*.java

  
## 4.4 同库2表聚合同步 /resources/es/opensourceframework_demo/demo_canal_address_to_user.yml,关键属性说明:
  
    # 有则更新，没有则进行插入
    upsert: true
    sql: "SELECT u.id , u.name as user_name , u.account , u.member_card_no ,
                 a.id as address_id ,a.province_code , a.city_code , a.county_code,
                 u.application_id, u.create_person, u.create_time,u.create_time_stamp, 
                 u.update_person, u.update_time, u.update_time_stamp, u.dr
          FROM demo_canal_single_table_user u
          LEFT JOIN demo_canal_single_table_address a ON u.id = a.user_id"
    -- 主表为left jion 最左边的表 : demo_canal_single_table_user
    -- 主sql中不能有where查询条件,查询条件可放到etlCondition属性中
    -- 关联关系中只允许主外键'='操作: ON u.id = a.user_id
    -- 关联条件必须要有一个字段出现在主查询语句: u.id = a.user_id --> SELECT u.id ....
    
## 4.5 同库3表聚合同步/resources/es/opensourceframework_demo/demo_canal_address_to_user_to_company.yml /resources/es/opensourceframework_demo/demo_canal_user_to_company_to_police_office.yml,关键属性说明: 
  
    sql: "SELECT u.id , u.name as user_name , u.account , u.member_card_no ,
                 u.application_id, u.create_person, u.create_time,u.create_time_stamp,
                 u.update_person, u.update_time, u.update_time_stamp, u.dr,
           	     a.id as address_id ,a.province_code , a.city_code , a.county_code,
           	     c.id as company_id ,c.code as company_code , c.name as company_name
           FROM demo_canal_single_table_user u
           LEFT JOIN demo_canal_single_table_address a ON u.id = a.user_id
           LEFT JOIN demo_canal_single_table_company c ON u.company_id = c.id"
    -- 主表为left jion 最左边的表 : demo_canal_single_table_user
    -- 主sql中不能有where查询条件,查询条件可放到etlCondition属性中
    -- 关联关系中只允许主外键'='操作: on u.id = a.user_id    on u.company_id = c.id
    -- 关联条件必须要有一个字段出现在主查询语句: u.id = a.user_id --> SELECT u.id ....
    
## 4.6 同库多表聚合 聚合表为子查询  /resources/es/opensourceframework_demo/demo_canal_bill_main_agg_sub.yml ,关键属性说明:
  
     sql: "SELECT m.id , m.receipt_no ,m.amount , m.user_id ,
                  m.tenant_id , m.loan_invoice_id , m.repay_plan_status,
                  temp.sum_overdue_money, temp.max_stage_no , temp.max_overdue_day,
                  m.application_id,m.create_person, m.create_time,m.create_time_stamp,
                  m.update_person,m.update_time,m.update_time_stamp,m.dr
           FROM demo_canal_single_table_bill_main m
           LEFT JOIN
             (
               SELECT s.receipt_no, count(s.stage_no) as max_stage_no , max(s.overdue_day) as max_overdue_day ,
                      sum(s.overdue_money) as sum_overdue_money
               FROM demo_canal_single_table_bill_sub s GROUP BY s.receipt_no
             )temp
           ON m.receipt_no = temp.receipt_no"
     etlCondition: "where m.repay_plan_status in ('4','5') and temp.max_stage_no < 100000"
     
     --  主表不能为子查询,关联表可为子查询
     --  主sql中不能有where查询条件,查询条件可放到etlCondition属性中 etlCondition: "where m.repay_plan_status in ('4','5') and temp.max_stage_no < 100000"
     --  主表为left jion 最左边的表 : demo_canal_single_table_bill_main
     --  关联关系中只允许主外键'='操作: on m.receipt_no = temp.receipt_no
     --  关联条件必须要有一个字段出现在主查询语句: m.receipt_no = temp.receipt_no --> SELECT m.id , m.receipt_no......
     
## 4.7 一对一关系 跨库数据聚合同步
  
    主表index: /resources/es/opensourceframework_demo/demo_canal_cross_database_one_to_one_merge.yml
    从表index: /resources/es/opensourceframework_funds_liquidation_center/demo_canal_cross_database_one_to_one_merge.yml
    
    主表esMapping定义,关键属性说明:
    
    _index: demo_canal_cross_database_one_to_one_merge
    _type: demo_canal_cross_database_one_to_one_merge
    # 跨库聚合时定义该属性
    crossDB: true
    # 聚合表所对应的 outerAdapterKey 多个用,隔开
    crossAdapterKey: opensourceframework_funds_liquidation_center
    # 跨库数据聚合时 以id做为_id的表(主数据表) 该属性设置为true 其他为默认值false
    crossAggMain: true  
    # es 的_id, 如果不配置该项必须配置下面的pk项_id则会由es自动分配
    _id: id
    sql: "SELECT u.id, u.name as user_name , u.account , u.member_card_no, u.id as user_id,
                 null as address_id , '' as province_code , '' as city_code , '' as county_code ,
                 u.application_id, u.create_person, u.create_time,u.create_time_stamp,
                 u.update_person, u.update_time, u.update_time_stamp, u.dr
          FROM demo_canal_single_table_user u"
          
     --  sql中需要声明所有es mapping中对应的属性，没有的字段使用，null as 属性名
     --  crossAggMain: true 标明此index为主index文件
     --  主表index,type,mapping属性与从表index,type,mapping相同
    
    从表esMapping定义,关键属性说明:
   
    _index: demo_canal_cross_database_one_to_one_merge
    _type: demo_canal_cross_database_one_to_one_merge
    # 跨库聚合时定义该属性
    crossDB: true
    # 是否对索引进行删除和新建操作 跨库聚合时在小表中定义该属性
    opIndex: false
    # 聚合表所对应的 outerAdapterKey 多个用,隔开
    crossAdapterKey: opensourceframework_demo
    # 如果不需要_id, 则需要指定一个属性为主键属性 跨库的pk为表之间的关联字段
    pk: user_id
    # 有则更新，没有则进行插入
    upsert: true
    sql: "SELECT a.id as address_id ,a.province_code , a.city_code , a.county_code , a.user_id
          FROM demo_canal_cross_database_one_to_one_address a"
      
     --  opIndex:false 定义该index不会创建index，使用主表的index
     --  主表index,type,mapping属性与从表index,type,mapping相同
     --  从表select语句中主外键输出名与主表中select输出名对应 : 
         null as address_id --> a.id as address_id  
         u.id as user_id --> a.user_id
    
     
## 4.8 一对多关系 跨库数据聚合同步  
    
    主表index: /resources/es/opensourceframework_funds_liquidation_center/demo_canal_cross_database_one_to_one_merge.yml 
    从表index: /resources/es/opensourceframework_demo/demo_canal_cross_database_one_to_one_merge.yml    
    
    主表esMapping定义,关键属性说明:
    
    # 主表index type 相同
    _index: demo_canal_cross_database_one_to_many_merge
    # 是否对索引进行删除和新建操作
    _type: demo_canal_cross_database_one_to_many_merge
    # es 的_id, 如果不配置该项,必须配置下面的pk项 _id则会由es自动分配
    _id: id
    # 跨库聚合时定义该属性
    crossDB: true
    # 聚合表所对应的 outerAdapterKey 多个用,隔开
    crossAdapterKey: opensourceframework_demo
    # 跨库数据聚合时 以id做为_id的表(主数据表) 该属性设置为true 其他为默认值false
    crossAggMain: true
    sql: "SELECT a.id ,a.province_code , a.city_code , a.county_code , a.user_id ,
               null as user_name , null as account , null as member_card_no,
               null as application_id,    null as create_person, null as create_time,
               null as create_time_stamp, null as update_person, null as update_time,
               null as update_time_stamp, null as dr
          FROM demo_canal_cross_database_one_to_many_address a"
    
    
    从表esMapping定义,关键属性说明:  
   
      _index: demo_canal_cross_database_one_to_many_merge
      _type: demo_canal_cross_database_one_to_many_merge
   
     # 跨库聚合时在小表中定义该属性 比如 1对多关系  在1的表定义crossDB为true
     crossDB: true
     # 是否对索引进行删除和新建操作 跨库聚合时在小表中定义该属性
     opIndex: false
     # 聚合表所对应的 outerAdapterKey 多个用,隔开
     crossAdapterKey: opensourceframework_funds_liquidation_center
   
     # 如果不需要_id, 则需要指定一个属性为主键属性 跨库的pk为表之间的关联字段
     pk: user_id
     # 有则更新，没有则进行插入
     upsert: true    
     sql: "SELECT u.id as user_id,  u.name as user_name , u.account ,u.member_card_no,
                  u.application_id, u.create_person, u.create_time,  u.create_time_stamp,
                  u.update_person,  u.update_time, u.update_time_stamp, u.dr
           FROM demo_canal_single_table_user u"  
     
    -- 同1对1关系跨库聚合
    -- 主表与从表的定义:多表聚合时数据多的表为主表,即many方为主表,主要的属性区别为:
          crossAggMain: true 
          opIndex: true
          _id:id
