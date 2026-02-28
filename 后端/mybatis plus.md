

> 具体场景中是否使用mybatis plus、怎么使用，还是得看团队规范







![[MyBatis-Plus 快速上手]]













# 核心功能







## 自定义SQL



* 应用场景：

  * 团队要求使用mp，且sql语句除了where条件之外的部门没办法利用mp更方便的实现、只能是拼接(这会违背企业开发规范)，这种场景下建议使用  wrapper构建条件+自定义其他部分sql语句

  * 将复杂的where条件的构建 交给mp实现(mp擅长的领域)，其余sql自定义

  * 将mp构建好的条件往下传递给mapper层，在mapper中实现sql语句的组装

    > 不再业务层中处理，更符合企业的开发规范



* 步骤

  1. 基于Wrapper构建where条件

     ```
     List<Long> ids = List.of(1L,2L,4L);
     int amount = 200;
     LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().in(User::getId,ids);
     
     ```

     

  2. 自定义mapper方法调用

     ```
     userMapper.updateBalanceByIds(wrapper,amount);
     ```

     mapper方法的参数需要用注解声明变量名称(**这里wrapper变量名称必须是ew**)

     > ew：entity wrapper

     ```
     void updateBalanceByIds(@Param("ew") LambdaQueryWrapper<User> wrapper,@Param("amount") int amount)
     ```

  3. 自定义xml中书写sql语句前半部分后半部分使用Wrapper构造的条件

     ```xml
     <update id="updateBalanceByIds">
         update user set balance = balance -#{amount} ${ew.customSqlSegment}
     </update>
     
     ```

     

     





* 案例：将id在指定范围内的用户（例如1、2、4）的余额扣减指定值 

  ```xml
  update user
      set balance = balance - #{amount}
  	where id in
  	<foreach collection="ids" separator="," item="id" open="(" close=")">
          #{id}
  </foreach>
  ```

  

  



> ew：EntityWrapper



























## Service接口

![image-20251026133721117](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251026133721117.png)

* 使用中
  我们的接口需要继承IService接口
  我们的实现类需要集成ServiceImpl接口

  ```
  public classUserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{}
  ```

  > 仍然实现我们自定义的接口，同时继承mp提供的ServiceImpl
  > 【这里泛型填写用到的Mapper层和操作的实体类】
  >
  > 



![image-20251026132805227](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251026132805227.png)

* 提供了封装好的批处理方法
* saveOrUpdate增或改，会判断是否存在当前id，智能选择修改或更新
* 查询
  * 单个查询调用get 查多个调用list
  * count查数量
  * page分页查询
  * 复杂条件的查询：lambdaQuery()



* 更新
  * 复杂条件的更新：lambdaUpdate()



#### 实际使用



* 测试方法

```
@Autowired
private UserService userService;
//...省略构造user的代码
userService.save(user);



```

* 增删改查用户

  ![image-20251026134854727](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251026134854727.png)

  **新增用户**

  ```
  @ApiOperation("新增用户接口")
  @PostMapping
  public Result saveUser(@RequestBody UserFormDTO userDTO){
  	//拷贝
  	User user = BeanUtil.copyProperties(userDTO,User.class);
  	//（补充属性）
  	//新增
  	userService.save(user);//(省掉了自己写service层方法)	
  	return Result.success();
  }
  
  
  ```

  **删除用户**

  ```
  @ApiOperation("删除用户接口")
  @DeleteMapping("/{id}")
  public Result deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id){
  	//删除
  	userService.removeById(id);//(省掉了自己写service层方法)	
  	return Result.success();
  }
  ```

  **根据id查询用户接口**

  ```
  @ApiOperation("查询用户接口")
  @GetMapping("{id}")
  public Result queryUserById(@ApiParam("用户id") @PathVariable("id") Long id){
  	//查询
  	User user = userService.getById(id);
  	//拷贝到vO
  	UserVO userVO = BeanUtil.copyProperties(user,UserVO.class);
  	//(执行业务逻辑、补充属性)
  	//返回
  	return Result.success(userVO);
  }
  ```

  **批量id查询用户接口**

  ```
  @ApiOperation("批量查询用户接口")
  @GetMapping
  public Result<List<UserVO>> queryUserByIds(@ApiParam("用户id集合") @PathVariable("ids") List<Long> ids){
  	//查询
  	List<USer> userList = userService.listById(ids);
  	//拷贝到vO
  	List<UserVO> userVOList = BeanUtil.copyToList(user,UserVO.class);
  	//(执行业务逻辑、补充属性)
  	//返回
  	return Result.success(userVOList);
  }
  ```

  > **拷贝集合使用copyToList方法**



> 没什么业务逻辑的简单方法可以直接在controller层使用mp搞定，无需自定义service
>
> 复杂业务逻辑的方法还是要自定义Service层
>
> 复杂业务逻辑中如果要自定义sql语句，就要用到Mapper层

* 根据id扣减余额

  ```
  @ApiOperation("扣减用户余额接口")
  @PutMapping("/{id}/deduction/{money}")
  public Result deleteUserById(
  @ApiParam("用户id") @PathVariable("id") Long id
  @ApiParam("扣减金额") @PathVariable("money") Integer money){
  	userService.deductBalance(id,money);
  	return Result.success();
  }
  ```

  ```
  public interface UserService extends IService<User>{
  	void deductBalance(Long id,Integer money);
  }
  ```

  ```
  public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
  	@Override
  	public void deductBalance(Long id,Integer money){
  		//1. 查询用户
  		User user = getById();
  		//User user = this.getById();
  		
  		//2. 检验用户状态用户
  		
  		if(user==null || user.getStatus() == 2){
  			throw new RuntimeException("用户状态异常");
  		}
  		
  		
  		//3. 检验余额是否充足
  		if(user.getBalance() < money){
  			throw new RuntimeException("用户余额不足");
  		}
  		
  		//4. 扣减金额
  		user.setBalance(user.getBalance().substract(money));
  		update(user);
  		
  		
  		
  	}
  }
  ```

  > 采用反向校验，如果不符合条件，就抛出异常，最后直接写一个扣减金额的逻辑
  >
  > 如果采用多层if嵌套的方式，代码就不够简洁，可读性差







* Lambda查询

  ![image-20251026152759977](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251026152759977.png)

  查询用户列表接口

  ```
  @ApiOperation("批量查询用户接口")
  @GetMapping("/list")
  public Result<List<UserVO>> queryUsers(UserQuery query){
  	List<UserVO> userVOList = userService.queryUsers(query);
  	return Result.success(userVOList);
  }
  ```

  ```
  public List<UserVO> queryUsers(UserQuery query){
  	lambdaQuery().
  		.like(name !=null,User::getUsername,query.getName())
  		.eq(status !=null,User::getStatus,query.getStatus())
  		.gt(minBalance != null,User::getBalance,query.getMinBalance())
  		.lt(maxBalance != null,User::getBalance,query.getMaxBalance())
  		.list();//查多个，使用list()
  	return
  }
  
  
  
  ```

  



* 



## 代码生成器



* 三种配置方式
  * 按照官方文档写“生成代码”的代码
  * 使用mybatixX插件
  * 使用MyBatisPlus插件



* 这里使用的是MyBatisPlus插件



1. 连接数据库

   

   

2. 







> 
>









> 
>
> 
> 
> 











