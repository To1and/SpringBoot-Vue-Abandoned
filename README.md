## Spring boot实现

---

### Maven配置阿里云仓库（加快依赖下载速度）

```xml
<repositories>
    <repository>
        <id>nexus-aliyun</id>
        <name>nexus-aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>

<pluginRepositories>
    <pluginRepository>
        <id>public</id>
        <name>aliyun nexus</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </pluginRepository>
</pluginRepositories>
```

---

### 用YML替代properties配置数据源

```yaml
server:
  port: 9090

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/testdb?serverTimezone=GMT%2b8
    username: toland
    password: 990315
```

---

### Spring Boot集成MyBatis实现数据查询

+ ___com.toland.springboot.entity	User.java___（定义数据的实体类）

  1. 定义与数据库中每个字段对应的私有变量

  2. ~~使用___Alt+ Insert___一键生成Get和Set方法，用以获取查询数据~~（会生成大量冗余代码）

  3. 在User类前加上注解___@Data___，便可简单取代2所有的代码（使用了__Lombok__插件)

     ```java
     @Data
     public class User
     {
         private Integer id;
         private String username;
         private String password;
         private String nickname;
         private String email;
         private String phone;
         private String address;
     }
     ```

 + ___com.toland.springboot.mapper	UserMapper.java___（数据库查询接口）

   1. 创建一个找到全部字段的查询接口，其上加入___@Mapper___注解，<u>**实现了把UserMapper内的Bean注入到Spring Boot中**</u>

      ```java
      @Mapper
      public interface UserMapper
      {
          @Select("SELECT * FROM user_info")
          List<User> findAll();
      }
      ```

+ ___com.toland.springboot.Controller	UserController.java___（控制类）

  1. 使用***@Autowired***注入UserMapper

  2. 写一个测试方法，使用***@GetMapping***（依赖于***@RestController***）对其进行路由
  
     ```java
     @RestController
     public class UserController
     {
         @Autowired
         private UserMapper userMapper;
     
         @GetMapping("/")
         public List<User> index()
         {
             return userMapper.findAll();
         }
     }
     
     ```

---

