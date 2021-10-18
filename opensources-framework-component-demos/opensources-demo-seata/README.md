# SpringBoot + Dubbo + Mybatis + Nacos + Seata

Integration SpringBoot + Dubbo + Mybatis + Nacos + Seata

seata server包地址： svn://10.53.144.173/RDC/10.培训材料/框架升级培训
### 1. clone the code 
  
   - opensources-demo-common  common module
       
   - opensources-demo-account  user account module
     
   - opensources-demo-order  order module
   
   - opensources-demo-storage  storage module

   - opensources-demo-business  business module

### 2. prepare database 

create database （默认为：seata），import db_seata.sql to database 

then you will see ：

```
+-------------------------+
| Tables_in_seata         |
+-------------------------+
| t_account               |
| t_order                 |
| t_storage               |
| undo_log                |
+-------------------------+
```

### 3. start Nacos（使用1.1.0版本，防止因为dubbo，nacos因版本不匹配出现的心跳请求出错的情况。 v1.1.0地址：https://github.com/alibaba/nacos/releases/tag/1.1.0）

Nacos quickstart：https://nacos.io/en-us/docs/quick-start.html

enter the  Nacos webconsole：http://10.53.157.115:8848/nacos/index.html
   
### 4. start Seata Server
  
download page：https://github.com/seata/seata/releases

download and unzip seata-server，cd the bin dictory, and run 

```bash
sh seata-server.sh 8091 file
```

### 5. start the demo module

start opensources-demo-account、opensources-demo-order、opensources-demo-storage、opensources-demo-business

use Nacos webconsole to ensure the registry is ok: http://10.53.157.115:8848/nacos/#/serviceManagement

> check the datasource config in application.properties is right.
    
### 6. start the normal request

use postman to send a post request：http://localhost:8104/business/dubbo/buy  

body：

```json
{
    "userId":"1",
    "commodityCode":"C201901140001",
    "name":"fan",
    "count":2,
    "amount":"100"
}
```

or use curl：

```bash
curl -H "Content-Type:application/json" -X POST -d '{"userId":"1","commodityCode":"C201901140001","name":"风扇","count":2,"amount":"100"}' localhost:8104/business/dubbo/buy
``` 

then this will send a pay request,and return code is 200

### 7. test the rollback request

enter opensources-demo-business , change  BusinessServiceImpl, uncomment the following code ：

```
if (!flag) {
  throw new RuntimeException("测试抛异常后，分布式事务回滚！");
}
```

restart the  opensources-demo-business module, and execute the step 6.

测试案例：http://localhost:8104/business/dubbo/buy?userId=1&commodityCode=C201901140001&name=%E6%B0%B4%E6%9D%AF&count=5
&amount=200
