![image-20250703134315866](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703134315866.png)

drawable——存放图片

layout——存放布局

map——存放图标（不同xhdpi表示分辨率不同99）

value——设置字符串





```
<TextView>
```

用于显示控件

## 2. 页面



* 页面需要继承AppCompatActivity父类
* 

![image-20250705211151322](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705211151322.png)

* 一般做一些控件、数据的初始化
* 



* ![image-20250705211849950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705211849950.png)

  ![image-20250705211902140](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705211902140.png)
  通过这个方法展示布局

  ```
  比如：
  setContentView(R.layout.activity_main);
  ```

  

  #### xml三种预览视图

  ![image-20250705213120665](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705213120665.png)

```cpp
orientation="vertical"




```





#### xml布局标签书写

![image-20250705213617139](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705213617139.png)
![image-20250705213631317](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705213631317.png)

![image-20250705213748166](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250705213748166.png)

容器组件（需要存放其他组件）都是双标签

非容器一般都是单标签

#### AndroidManifest.xml文件

> 一个页面就是一个activity

![image-20250706100041018](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706100041018.png)

涉及敏感权限就要申请

![image-20250706095839950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706095839950.png)

![image-20250706100100833](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706100100833.png)

![image-20250706100154404](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706100154404.png)


![image-20250706101110931](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706101110931.png)![image-20250706101017343](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706101017343.png)



比如声明网络权限和相机权限

#### 修改应用名称和图标

![image-20250706101601903](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706101601903.png)

#### R类资源

![image-20250706120406912](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706120406912.png)



#### 创建新的界面（activity）

![image-20250706120701844](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706120701844.png)   

* 设置某一个页面成为启动的第一个页面
* ![image-20250706120811687](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706120811687.png)



#### 布局和组件



* 线性布局
  组件水平或者垂直排列
  ![image-20250706121504349](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706121504349.png)

推荐用约束布局

![image-20250706121616771](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706121616771.png)![image-20250706121721828](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706121721828.png)

![image-20250706121816470](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706121816470.png)

#### 线性布局

##### 布局方向 

![image-20250706132522783](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706132522783.png)



##### 权重

![image-20250706132612609](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250706132612609.png)

##### 对齐方式

![image-20250707091430538](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707091430538.png)



gravity的优先级更高







 

##### 分割线

![image-20250707091746833](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707091746833.png)



![image-20250707091925475](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707091925475.png)





#### 相对布局



![image-20250707092312260](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707092312260.png)

### 帧布局

![image-20250707100348417](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707100348417.png)



### textView

![image-20250707100402045](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707100402045.png)

![image-20250707100421683](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250707100421683.png)



### button组件基本用法











### Button组件的三种点击事件



### button组件样式之背景边框圆角



### button按钮图标



### button按钮禁用状态



### 按钮状态选择器



三种button组件







