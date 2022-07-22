# I. 前期配置

+ ## Maven配置阿里云仓库（加快依赖下载速度）

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


+ ## 用YML替代properties配置数据源

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

# II. 集成MyBatis实现数据查询

+ ## 使用方法
1. __利用Postman，向localhost:9090/user发送一个GET请求。返回值为该表内全部数据的json串形式。__

+ ## ___com.toland.springboot.entity	User.java___

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

+ ## ___com.toland.springboot.mapper	UserMapper.java___

   1. 创建一个找到全部字段的查询接口，其上加入___@Mapper___注解，<u>**实现了把UserMapper内的Bean注入到Spring Boot中**</u>

      ```java
      @Mapper
      public interface UserMapper
      {
          @Select("SELECT * FROM user_info")
          List<User> findAll();
      }
      ```

+ ## ___com.toland.springboot.Controller	UserController.java___

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

# III. 实现增删改查

+ ## 使用方法
  1. __利用Postman，向localhost:9090/user这个地址POST一个json串，若是库中不包含的id则新增条目，若已包含则更新。返回操作的条目数__
  2. __利用Postman，对localhost:9090/user/{id}这个地址DELETE。返回操作的条目数__

+ ## ___com.toland.springboot.mapper	UserMapper.java___

  1. 使用___@Insert___注解实现向数据库插入条目

     ```java
     @Insert("INSERT INTO user_info(username,password,nickname,email,phone,address) VALUES (#{username}, #{password}, #{nickname},#{email}, #{phone}, #{address})")
     int insert(User user);
     ```

  2. ~~使用___@Update___注解实现对数据库条目的更新。~~其交互方式为使用Postman向localhost:9090/user POST一个json串，在id已知的情况下替换数据。但是这种情况会导致假如在只更新个别数据的时候，会使其他没有更新的数据被清除，逻辑不完善,使用动态SQL进行完善，最终会将@Update注解完全删除

     ```java
     @Update("UPDATE user_info SET username=#{username}, password=#{password},nickname=#{nickname},email=#{email},phone=#{phone},address=#{address} WHERE id=#{id}")
     int update(User user);
     ```

  3. 使用@Delete注解实现通过id检索删除条目

     ```java
     @Delete("DELETE FROM user_info WHERE id = #{id}")   //这个id与下一行的id一一对应
     Integer deleteById(@Param("id") Integer id);
     ```


+ ## ___com.toland.springboot.Controller	UserController.java___

  1. 在类名前添加___@RequestMapping___，得到的整个url需为类前的___@RequestMapping___的参数与类内的***@GetMapping***的参数所拼接的总体。***@GetMapping***不附带任何内容时注解的方法即为直接访问___@RequestMapping___的参数的返回结果。

  2. 追加新增或修改方法。使用___@RequestBody___，可以将json数据映射为User对象

     ```java
     @PostMapping
     public Integer save(@RequestBody User user)     //实现新增或者更新数据
     {
         return userService.save(user);      //返回操作的条目数
     }
     ```

  3. 追加删除条目方法。___@PathVariable___表示url参数

     ```java
     @DeleteMapping("/{id}") //这个id与下一行的id一一对应
     public Integer delete(@PathVariable Integer id) //   //实现删除数据
     {
         return userMapper.deleteById(user.getId(id));   //返回删除的条目数
     }
     ```

+ ## ___com.toland.springboot.Service	UserService.java___

  1. 类前加注解***@Service***，用来吧Service类注入到Spring Boot容器中，类似___UserMapper.java__中的***@Mapper***。

  2. 使用一个if语句来判断所使用的方法为新增还是更新

     ```java
     public int save(User user)
     {
         if (user.getId() == null)    //如果user没有id说明为新增内容,需要添加
         {
             return userMapper.insert(user);
         }
         else    //否则需要更新内容
         {
             return userMapper.update(user);
         }
     }
     ```

+ ## ___resources.mapper	User.xml___

  1. 首先是网上找到配置头模板，注意namespace需要定位到自己的UserMapper接口

  	```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE mapper
             PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
             "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     <mapper namespace="com.toland.springboot.mapper.UserMapper">
         
     </mapper>

  2. 在插件商店内下载安装__MyBatisX__，用以快捷在左侧切换到UserMapper接口

  3. 利用动态SQL做一个判断逻辑，这里的逻辑是不会将空数据写入服务器，即任何值都不能在写入后再置空。id内的内容对应UserMapper中方法的名字。

  	```xml
         <update id="update">
             UPDATE user_info
             <set>
                 
                 <if test="username!=null">
                     username=#{username},
                 </if>
     
     <!--            <if test="password!=null">          一般来说密码不在SQL里修改，比较危险-->
     <!--                password=#{password},-->
     <!--            </if>-->
     
                 <if test="nickname!=null">
                     nickname=#{nickname},
                 </if>
     
                 <if test="email!=null">
                     email=#{email},
                 </if>
     
                 <if test="phone!=null">
                     phone=#{phone},
                 </if>
     
                 <if test="address!=null">
                     address=#{address},
                 </if>
                 
             </set>
                 
             <where>
                 id = #{id}
             </where>
     
         </update>
     ```

  4. 实现前其他改动

     + 在UserMapper中将id对应方法前的注解删除，即删除___@Update___行

     + 在application.yml中配置MyBatis类地址，使得框架可以寻找到该文件。configuration中的内容为了在终端输出日志，其中包含实际运行的SQL语句以便观察

        ```yaml
        mybatis:
            mapper-locations: classpath:mapper/*.xml
            configuration:
                log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        ```

---

# IV. 实现分页查询

+ ## 使用方法

  1. 
+ ## ___com.toland.springboot.Controller	UserController.java___

  1. 追加分页查询方法。使用___@RequestParam___，其接口路径为_/user/page?pageNumber=1&pageSize=10_。
  
     ```java
     @GetMapping("/page")
     public List<User> findPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize)    //实现分页查询，接收页面数与页面大小两个数据
     {   //limit的第一个参数 = (pageNumber - 1) * pageSize,其原理来源于MySQL语句
         //limit的第二个参数 = pageSize
         pageNumber = pageNumber - 1 * pageSize;
         userMapper.selectPage(pageNumber, pageSize)
     }
     ```

+ ## ___com.toland.springboot.mapper	UserMapper.java___

  1. 使用@Select实现分页查询

     ```java
     @Select("SELECT * FROM user_info LIMIT #{pageNumber},#{pageSize}")
     List<User> selectPage(Integer pageNumber,Integer pageSize);
     ```
