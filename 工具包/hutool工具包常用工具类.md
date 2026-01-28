# 拷贝属性



```
# 已知user对象
UserDTO userDTO = BeanUtils.copyProperties(user,userDTO.class);
```



```
# 已知user对象
UserDTO userDTO = new UserDTO();
BeanUtils.copyProperties(user,userDTO);
```





# 非hutool的工具

> 迁移到另一个文档内

创建JWT，需要jsonwebtoken库



