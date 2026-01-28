

将自己的nodejs的两个环境变量下移到最后，就能重新使用nvm



# 1. vue3工程搭建

[02. 从0带你搭建Vue3工程_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Df2cYVEWo?spm_id_from=333.788.videopod.episodes&vd_source=67ef3bb4c8d68a96408acdaa865b1313&p=2)

* 下载nodejs/idea

* npm是一个包管理工具，可以帮助我们下载vue工程需要的依赖，
  我们给npm配置淘宝镜像，提升下载速度

  ```
  npm config set registry http://registry.npmmirror.com
  ```

  

![image-20250703165117238](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703165117238.png)

![image-20250703165511183](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703165511183.png)

* 指令启动”vue“工程

![image-20250703165642291](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703165642291.png)

第二个指令会下载相关依赖包

出现这个界面说明成功了

![image-20250703170038648](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703170038648.png)

![image-20250703170107229](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703170107229.png)

* 打开idea编辑项目
  会加载一会依赖
  ![image-20250703170602092](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703170602092.png)

  

  ![image-20250703170905928](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703170905928.png)

* 进一步精简项目结构
  ![image-20250703171554668](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703171554668.png)
  然后改一下HomeView.vue
  ![image-20250703171639316](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703171639316.png)
  改名
  改路由
  ![image-20250703192318497](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703192318497.png)

  精简成一行json
  ![image-20250703193731930](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703193731930.png)

  ![image-20250704094210374](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250704094210374.png)
  删除无用的导包

  ![image-20250703193325070](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703193325070.png)
  
* main.js里面引入global.css（全局css文件，让全局生效）

  ```css
  body{
  	margin:0;
  	padding:0
  	font-size:14px;
  	color:#333;
  }
  ```

* ##### CSS语法设置基本样式

  https://www.runoob.com/css
  简单了解

* ##### vue基本语法学习

  https://www.runoob.com/vue3/vue3-tutorial.html

#### vue基本语法

* 定义数据
* 渲染数据



1. 使用ref

![image-20250709123022249](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709123022249.png)
![image-20250709123054163](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709123054163.png)

2. 使用reactive
   ![image-20250709123126752](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709123126752.png)
   ![image-20250709123139313](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709123139313.png)



> ### 推荐使用reactive



* v-model 可以绑定数据

  ```java
  <input v-model="data.a">
  //实现输入框与data.a绑定
  ```

  * 双向绑定：
    数据可以随用户输入变化

* v-if = “条件”表示只有引号里面条件满足的情况下，这个组件才会渲染出来

  ```java
  <span style = "color:red" v-if="data.name === '佩奇'">小猪佩奇</span>
  ```

  ![image-20250709124650413](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709124650413.png)
  v-else会自动匹配最近的v-if

* **<u>v-for</u>**
  绑定数组数据
  自动渲染每一项数据
  ![image-20250709131439178](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709131439178.png)
  ![image-20250709131456162](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709131456162.png)

  

* **<u>*@（v:on）——事件绑定*</u>**
  ![image-20250709132344934](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709132344934.png)
  点击事件（一般在按钮或者div触发)、鼠标移入事件

* :(v-bind:)——动态绑定
  给组件绑定样式、图片等，实现动态设置属性

  ```java
  <div>
      <div :style="data.box"></div>
      <img :src="data.img" style="width:200px;height:200px"></img>
  </div>
  <script setup>
    //使用reactive函数也要导包
    import {reactive} from "vue";
    const data = reactive({
      box:{
        width: "200px",
        height: "200px",
        backgroundColor: "red"
      },
      img:"https://th.bing.com/th?id=ORMS.95c079e8a4fab0c1e13e42aac771550f&pid=Wdp&w=240&h=129&qlt=90&c=1&rs=1&dpr=1.5&p=0"
    })
  
  
  </script>
  
          
          
  ```

  

  > 说明**可以从后台获取数据，实现动态渲染内容**

* **onMounted**
  在页面元素完全加载完成之后触发

  ```java
  <script setup>
      import {onMounted} from "vue";
      onMounted(() =>{
      alert("页面加载完成");
      })
  </ script>
  ```

  在这个函数里面写页面元素完全加载完成之后的逻辑







# 2. vue3集成Element-plus

* 介绍
  前端UI框架
* https://element.plus.org
  https://cn.element.plus.org/

* 导入ui框架

  * 安装依赖

    ```
    npm i element-plus -S
    ```

    

  * 在main.js里面引入

    ```javascript
    import ElementPlus from 'element-plus'
    import 'element-plus/dist/index.css'
    
    app.use(ElementPlus,{
    	locale: zhCn,
    })
    ```

    ![image-20250709135239329](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709135239329.png)


    ![image-20250709135852242](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709135852242.png)
    
    **import进去的内容放在global上面，防止覆盖**
    
    注意顺序

* 全局使用icon

  * 安装依赖
    

    ```javascript
    npm install @element-plus/icons-vue
    ```

    

  * 在main.js里面引入

    ```javascript
    import * as ElementPlusIconsVue from '@element-plus/icons-vue'
    const app = create(App)
    for(const [key,component] of Object.entries(ElementPlusIconsVue)){
        app.component(key,component)
    }
    ```

    





#### 基本使用





![image-20250709142854963](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709142854963.png)

各个组件的代码见官网

![image-20250709143112881](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709143112881.png)





* ICON图标

![image-20250709144446868](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709144446868.png)

> top：用于微调（在图标上面加4px）

* 在按钮或者输入框组件内使用图标，必须要导入
  ![image-20250709145035561](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709145035561.png)

  ```javascript
  <el-button type="danger" :icon="Delete" circle/>
  
  <script setup>
    import {Delete} from "@element-plus/icons-vue";
  </script>
  ```

  















#### 控制elementUI框架主题色

* 安装依赖

  ```javascript
  npm i sass@1.71.1 -D
  npm i unplugin-auto-import -D
  npm i unplugin-element-plus -D
  npm i unplugin-vue-components -D
  ```

  

* 配置index.scss

  ```javascript
  @forward "element-plus/theme-chalk/src/common/var.scss" with ($colors:(
    "primary":("base": #0066bc),
    "success":("base": #3aaa13),
    "warning":("base": #ffad00),
    "danger":("base": #e52f2f),
    "info":("base": #442c9a),
  ));
  ```

* 配置vite.config.js

  ```javascript
  import AutoImport from 'unplugin-auto-import/vite'
  import Components from 'unplugin-vue-components/vite'
  import {ElementPlusResolver} from "unplugin-vue-components/resolvers";
  import ElementPlus from 'unplugin-element-plus/vite'
  ```

  ![image-20250709152657404](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709152657404.png)

* 按需定制主题配置
  ![image-20250709152635304](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709152635304.png)
  ![image-20250709152902105](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709152902105.png)



> ## 有bug，暂时不改主题色

# 3. 【Element-plus组件使用】速成

> # 把这些组件搞懂搞会，才有自己开发的能力





#### 文本框el-input

![image-20250709161117356](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709161117356.png)
![image-20250709161145694](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709161145694.png)

* disable和readonly可以只显示文本、无法输入

  ```javascript
   <el-input v-model="data.input" style="width: 240px" placeholder="请输入内容" disable />
   <el-input v-model="data.input" style="width: 240px" placeholder="请输入内容" readonly />
       
       
  ```

  

> 一定要有v-model属性

#### 下拉框

* 加上“clearable”可以多一个清空按钮
  加上multiple可以多选

* size可以设置
* key绑定唯一属性
  item.label绑定什么，多选就展示什么
* 

* 下拉框对象写法
  比如：options:[{id:1,name:'苹果'},{id:2,name:'香蕉'}]
  在options里面使用item.id/item.name访问
* 下拉框纯文本写法
* ![image-20250710150312004](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710150312004.png)
  这样即便两个选项label值一样，只要value值不一样，这两个选项就不一样
* **需要绑定两组数据**
  **1.用v-model绑定选定的数据**
  **2.用v-for遍历另一组数据展示成选项**
  **【options一般从后端拿过来】**

![image-20250710150457173](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710150457173.png)

![image-20250710144729975](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710144729975.png)







#### 单选框el-radio

![image-20250710151651201](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710151651201.png)

* 通过设置radioSex的初始值可以设置单选的默认值



#### 结合按钮使用，写成单选按钮组

* 常用于页面内容切换

![image-20250710154238569](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710154238569.png)

#### 多选框

* 绑定数组，数组里面存储的事value绑定的值









![image-20250710164026498](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710164026498.png)

![image-20250710163744882](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710163744882.png)







#### 图片显示

图片渲染、旋转、放大、缩小

* el-image使用网络图片地址是可以渲染(展示)图片的，但是使用本地的图片路径却无法渲染
  * <u>解决办法：先绑定，再使用</u>

![image-20250710164932353](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710164932353.png)

* preview-src-list——支持图片预览

![image-20250710165935636](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710165935636.png)





#### 轮播图el-carousel

（走马灯）

![image-20250710165753738](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710165753738.png)

* 用法比较固定



* 导入图片，设置
  imgs:[lun1,lun2,lun3]

![image-20250710171346606](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710171346606.png)

![image-20250710171353277](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710171353277.png)





![image-20250710171403665](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710171403665.png)





#### 日期时间控件el-date-picker

* 日期





需要使用日期格式转换之后才适合数据库存储

使用value-format

> format只能规定输入框的展示格式，不能规定数据的格式
>
> ![image-20250710173131673](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710173131673.png)

> **一般情况同时设置format和value-format**

* 时间

* 日期时间

* 日期范围

![image-20250710192252979](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710192252979.png)

![image-20250710192304710](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710192304710.png)



> 空数组daterange用[null]表示

![image-20250710200525680](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710200525680.png)







#### 数据表格el-table

![image-20250710200309233](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710200309233.png)

![image-20250710200317906](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710200317906.png)





> 插槽
>
> <template #default="scope">拿到行对象的数据
>
> ![image-20250710203547866](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710203547866.png)
>
> ![image-20250710203118552](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710203118552.png)



#### 分页el-pagination



* layout属性不能少
* 每页的个数发生变化触发handleSizeChange
  当前页发生就触发handleCurrentChange
  ![image-20250710201007773](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710201007773.png)
* 

![image-20250710201509759](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710201509759.png)

![image-20250710201518512](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710201518512.png)



#### Dialog弹窗

点击表格里面的编辑按钮触发edit函数

触发弹窗

![image-20250710204927666](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710204927666.png)

![image-20250710204951446](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710204951446.png)

![image-20250710205004808](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710205004808.png)











# 4. vue3集成Vue-Router实现路由跳转

![image-20250710205239550](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710205239550.png)

### 官网学习https://router.vuejs.org/zh/guide/



* **router link跳转**
  通过设置to属性实现跳转
  不用刷新就能跳转

  > ```
  > <a href="/test">通过a标签跳转到Test.vue需要刷新</a>
  > 
  > ```

  ![image-20250710212342052](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710212342052.png)

* **编程式跳转**
  **需要导入router对象**

  ![image-20250710212857861](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710212857861.png)

  ![image-20250710212515969](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710212515969.png)

> 使用push跳转之后还能回来
> 使用replace跳转之后不能返回





### 如何定义一个新路由

![image-20250710212100928](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710212100928.png)

* 名字(name)不能重复



### 可以通过重定向的方式设置默认跳转路由

![image-20250710213555906](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250710213555906.png)



### 路由传参

> 转换页面的同时传一个参数

1. 手动拼接：
   router.push('/test?id=1')

   通过router.currentRoute.value.query.id获取query对象、接收id

   ![image-20250711093030233](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250711093030233.png)

2. 单独传一个对象
   ![image-20250711093041142](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250711093041142.png)

![image-20250712131759635](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712131759635.png)

### 嵌套路由

![image-20250711095514973](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250711095514973.png)



![image-20250711095718378](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250711095718378.png)



> 有bug，localhost:5173/manager/test不能正常访问

### 路由守卫

在路由配置 文件中使用导航守卫，修改网页标题



![image-20250712132231084](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712132231084.png)

![image-20250712132138630](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712132138630.png)







![image-20250712125954219](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712125954219.png)



### 404页面



1. 定义404路由

![image-20250712135613661](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712135613661.png)

2. 编写404页面

![image-20250712140329197](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712140329197.png)





# 5. Vue3搭建后台管理系统



![image-20250712140750853](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712140750853.png)









* #### 实现点击侧边栏跳转

  ![image-20250712173521508](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712173521508.png)

  ![image-20250712175017204](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712175017204.png)

  > ![image-20250712184611300](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712184611300.png)







> ![image-20250712175648420](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712175648420.png)
>
> **一个页面只能成为一个页面的子页面**，否则无法正常跳转



manager.vue

![image-20250712214946770](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712214946770.png)

data.vue

![image-20250712215000340](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712215000340.png)

> 全局定义一个card样式，随处可用
>
> ![image-20250712165948447](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250712165948447.png)
> 

















# 6. mysql语法简介——见“Mysql”文件





# 7. springboot3工程搭建



1. 新建springboot3项目
   ![image-20250807091716563](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807091716563.png)
2. 精简项目结构
   ![image-20250713213708187](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250713213708187.png)
   ![image-20250714091904670](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714091904670.png)
   ![image-20250715084547822](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715084547822.png)
3. 设置编码
   ![image-20250713214159204](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250713214159204.png)
4. 右键pom.xml
   添加为maven项目

5. 连接数据库
   不同数据库的JDBC URL格式不同，以下是常见数据库的格式：

   | **数据库类型** | **JDBC URL格式**                                             |
   | -------------- | ------------------------------------------------------------ |
   | **MySQL**      | `jdbc:mysql://<host>:<port>/<database>?参数1=值1&参数2=值2`（如`jdbc:mysql://localhost:3306/testdb?useSSL=false`） |
   | **PostgreSQL** | `jdbc:postgresql://<host>:<port>/<database>`（如`jdbc:postgresql://localhost:5432/mydb`） |
   | **Oracle**     | `jdbc:oracle:thin:@<host>:<port>:<SID>`（如`jdbc:oracle:thin:@localhost:1521:xe`） |
   | **SQL Server** | `jdbc:sqlserver://<host>:<port>;databaseName=<db>`（如`jdbc:sqlserver://localhost:1433;databaseName=testdb`） |
   | **H2**（内存） | `jdbc:h2:mem:<database>`（如`jdbc:h2:mem:testdb`）           |



6. 新建controller类


   测试接口

   ![image-20250714092410676](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714092410676.png)

   > @GetMapping表示浏览器get请求（浏览器url访问）

   

   

   

7. ![image-20250714094546129](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714094546129.png)
   由于返回类型多种多样，所以做一个统一处理：“封装一个统一的类，告诉前端请求成功还是失败，并把数据跟随请求一起发给他”

   ![image-20250714192514855](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714192514855.png)
   




## 全局异常处理

> 避免后端异常，前端操作人员没有任何提示、完全不知道发生了具体什么情况、提示是什么

> 另一版本
>
> ![](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913185137735.png)
>
> ![image-20250913191907276](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913191907276.png)
>
> 
>
> 
>
> ![image-20250913191750568](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913191750568.png)
>
> ![image-20250913191618622](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913191618622.png)
>
> 
>
> > 在数据加了“唯一”约束的字段，一旦出现重复，就会报DuplicateKeyException异常
>
> * log记录错误日志
> * 打印错误信息（提示语）
>
> 
> 

![image-20250715162809149](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715162809149.png)



**自定义异常**

**CustomException.java**
![image-20250714194833065](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714194833065.png)

![image-20250714195122023](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714195122023.png)

![image-20250714195952389](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714195952389.png)

### (常见状态码)

> 成功响应（2XX）
>
> - **200 OK**：请求成功，服务器已正确处理了请求。
> - **204 No Content**：请求成功，但没有内容返回。
> - **206 Partial Content**：客户端请求了部分资源，服务器成功处理并返回了请求的部分内容。
>
> 重定向响应（3XX）
>
> - **301 Moved Permanently**：资源永久移动到新的URL，客户端应使用新的URL进行后续请求。
> - **302 Found**：资源临时移动，客户端应继续使用原有URI。
> - **303 See Other**：请求的资源存在于另一个URI，应使用GET方法获取。
> - **304 Not Modified**：资源未修改，客户端可以继续使用缓存的版本。
> - **307 Temporary Redirect**：临时重定向，与302类似，但要求客户端不改变请求方法。
>
> 客户端错误响应（4XX）
>
> - **400 Bad Request**：请求报文存在语法错误。
> - **401 Unauthorized**：需要认证或认证失败。
> - **403 Forbidden**：服务器拒绝执行请求。
> - **404 Not Found**：服务器无法找到请求的资源。
>
> 服务器错误响应（5XX）
>
> - **500 Internal Server Error**：服务器在处理请求时发生错误。
> - **503 Service Unavailable**：服务器超负载或停机维护，暂时无法处理请求。
>
> 
>
> > ![image-20250919171912253](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919171912253.png)
> >
> > ## 500 内部服务器错误
> >
> > **[Internal Server Error](https://zhida.zhihu.com/search?content_id=120564363&content_type=Article&match_order=1&q=Internal+Server+Error&zd_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ6aGlkYV9zZXJ2ZXIiLCJleHAiOjE3NTg0NDYzMDIsInEiOiJJbnRlcm5hbCBTZXJ2ZXIgRXJyb3IiLCJ6aGlkYV9zb3VyY2UiOiJlbnRpdHkiLCJjb250ZW50X2lkIjoxMjA1NjQzNjMsImNvbnRlbnRfdHlwZSI6IkFydGljbGUiLCJtYXRjaF9vcmRlciI6MSwiemRfdG9rZW4iOm51bGx9.VO4nzQ-RlL39uwyJn1gsUStMZ-raOo6IrxWpm-SfUNA&zhida_source=entity) 500内部服务器错误，服务器遇到未知无法解决的问题。**
> >
> > 一般情况下，出现500响应状态的原因有很多种，但是主要的是“程序代码和服务器配置”两个问题。相对于代码而言，就是对站点进行升级，网页改版，新增加了一些常用的插件。就比如WordPress插件的版本可能就需要更高版本的PHP才能兼容。
> >
> > 而相对服务器而言的话，更多的是在系统版本升级导致，就比如最开始使用的是Windows Server 2003，后期想要升级2008、2012等版本的时候配置稍有不慎就会导致Internal Server Error 500。
> >
> > ## 404 请求错误
> >
> > **[Not Found](https://zhida.zhihu.com/search?content_id=120564363&content_type=Article&match_order=1&q=Not+Found&zd_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ6aGlkYV9zZXJ2ZXIiLCJleHAiOjE3NTg0NDYzMDIsInEiOiJOb3QgRm91bmQiLCJ6aGlkYV9zb3VyY2UiOiJlbnRpdHkiLCJjb250ZW50X2lkIjoxMjA1NjQzNjMsImNvbnRlbnRfdHlwZSI6IkFydGljbGUiLCJtYXRjaF9vcmRlciI6MSwiemRfdG9rZW4iOm51bGx9.thw1luyNsCMYX-iP0p1XTu9aAZBzx_WJCzMXgLC-yJc&zhida_source=entity) 404 错误请求，因发送的请求语法错误,服务器无法正常读取**。
> >
> > 相信绝大多数的人都见过404的状态码，当用户试图请求Web服务器上一个不存在的资源时，就会触发Not Found404。出现404状态码可能是链接失效导致，也有可能是URL拼写错误，还有可能是因为Web服务器将所请求的资源移到了其他的地方。一般的网站都会设置自定义页面以防链接失效所产生不良的影响。
> >
> > ## 403 禁止访问
> >
> > **[Forbidden](https://zhida.zhihu.com/search?content_id=120564363&content_type=Article&match_order=1&q=Forbidden&zd_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ6aGlkYV9zZXJ2ZXIiLCJleHAiOjE3NTg0NDYzMDIsInEiOiJGb3JiaWRkZW4iLCJ6aGlkYV9zb3VyY2UiOiJlbnRpdHkiLCJjb250ZW50X2lkIjoxMjA1NjQzNjMsImNvbnRlbnRfdHlwZSI6IkFydGljbGUiLCJtYXRjaF9vcmRlciI6MSwiemRfdG9rZW4iOm51bGx9.6J4ZaE8jbjqGhBEPOBP-bKJa3UfxW4Te3fupof8iApw&zhida_source=entity) 403 禁止访问，客户端没有权利访问所请求内容,服务器拒绝本次请求。**
> >
> > 状态码403通常代表客户端错误，是指的服务器端有能力处理该请求，但是拒绝授权访问。这个状态码类似于401，但是进入该状态后不能再继续进行验证，该访问是长期禁止的，并且与应用逻辑密切相关，比如密码不正确等。
> >
> > ## 400 错误请求
> >
> > **[Bad Request](https://zhida.zhihu.com/search?content_id=120564363&content_type=Article&match_order=1&q=Bad+Request&zd_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ6aGlkYV9zZXJ2ZXIiLCJleHAiOjE3NTg0NDYzMDIsInEiOiJCYWQgUmVxdWVzdCIsInpoaWRhX3NvdXJjZSI6ImVudGl0eSIsImNvbnRlbnRfaWQiOjEyMDU2NDM2MywiY29udGVudF90eXBlIjoiQXJ0aWNsZSIsIm1hdGNoX29yZGVyIjoxLCJ6ZF90b2tlbiI6bnVsbH0.CioA-KZDmvklpipeOZ4i1guyRVfGDXHmNczQFDECI_0&zhida_source=entity) 400 错误请求，因发送的请求语法错误,服务器无法正常读取。**
> >
> > 状态码400表示该语法无效，服务器无法理解该请求。客服端不应该在未经修改的情况下重复此请求。一般会因为前端提交数据的字段名称，或者是字段类型和后台的实体类不一致，导致无法封装。
> >
> > ## 401 未经授权
> >
> > **[Unauthorized](https://zhida.zhihu.com/search?content_id=120564363&content_type=Article&match_order=1&q=Unauthorized&zd_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ6aGlkYV9zZXJ2ZXIiLCJleHAiOjE3NTg0NDYzMDIsInEiOiJVbmF1dGhvcml6ZWQiLCJ6aGlkYV9zb3VyY2UiOiJlbnRpdHkiLCJjb250ZW50X2lkIjoxMjA1NjQzNjMsImNvbnRlbnRfdHlwZSI6IkFydGljbGUiLCJtYXRjaF9vcmRlciI6MSwiemRfdG9rZW4iOm51bGx9.gj1nkuDIIU8Fd0aGldeqYiF8y7ljCK6mZM8Rn3GKtOM&zhida_source=entity) 401 未经授权，需要身份验证后才能获取所请求的内容,类似于403错误.不同点是.401错误后,只要正确输入帐号密码,验证即可通过。**
> >
> > 状态码401就是Web服务器认为，客户端发送的HTTP数据流浪是正确的，但是进入URL资源的时候需要身份验证，而客户端尚未提供相关的验证信息，或者是已提供但是没有通过验证。这也是通常所知的“HTTP基本验证”。
>
> 
>
> [HTTP 状态码详解：用途与含义_登录失败状态码-CSDN博客](https://blog.csdn.net/qq_38038472/article/details/147662233#:~:text=4xx：客户端错误， 400 （语法）、 401 （未登录）、 403 （无权限）。 5xx：服务器错误，,以下是 2xx、3xx、4xx、5xx 状态码 的详细说明，包括适用场景和典型用例。 表示请求已被服务器成功接收、理解并处理。 用途：标准成功响应，表示请求已成功完成。 GET 请求成功获取资源（如查询用户信息）。)





##### 为什么一个main方法就能将web应用启动？

> ![image-20250808092841582](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250808092841582.png)
>
> 由于依赖传递，tomcat的相关依赖也被引入
>
> ![image-20250808092944485](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250808092944485.png)
>
> 运行main方法之后会运行tomcat服务器，将项目部署到服务器上、然后运行起来
> 

#### (读取csv文件)

> ![image-20250811105147584](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250811105147584.png)
>
> ```java
> public class UserController{
>     @RequestMapping("/list")
>     public List<User> list() throws Exception{
>         //1. 加载并读取user.txt文件，获取用户数据
>         InputStream in = this.getClass().getClassLoader().getResourceAsStream("user.txt");
>         ArrayList<String> lines = IoUtil.readlines(in.StandardCharsets.UTF_8,new ArrayList<>());
>         
>         //2. 解析用户信息，封装为User对象
>         List<User> userList = lines.stream().map(line -> {
>             String[] parts = lines.split(",");
>             Integer id = Integer.parseInt(parts[0]);
>             String username = parts[1];
>             String password = parts[2];
>             String name = parts[3];
>             Integer age = Integer.parseInt(parts[4]);
>             LocalDateTime updateTime = LocalDateTime.parse(parts[5],DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
>             return new User(id,username,password,name,age,updateTime);
>         }).toList();
>         
>         //3.返回数据
>         return userList;
>     }
> }
> 
> 
> 
> ```

##### 三层架构

> ![image-20250811130917453](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250811130917453.png)
>
> ![on](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250811130936475.png)
>
> * 接收请求 调用 响应结果
> * 补全基础属性（如果有需要补充的话）
>   调用Mapper方法
> * sql语句
>
> > controller层的RestController能将返回的Result对象转成json字符串
>
> ![](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250812083226715.png)
>
> 
>
> ![image-20250812083538270](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250812083538270.png)
>
> ![image-20250812083712899](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250812083712899.png)
>
> #### 通过控制反转和依赖注入实现进一步的解耦合
>
> ![image-20250813153633460](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250813153633460.png)
>
> ![image-20250813153914343](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250813153914343.png)
>
> 程序运行时自动找到该类型的 对象，并赋值给这个成员变量
>
> 依赖注入注解可以使用@Autowired或@Resource 
>
> 使用"@Component"注解将类的对象交给IOC容器
>
> 
>

##### IOC和DI详解

> ![image-20250813154604040](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250813154604040.png)
>
> IOC容器里面的bean名字默认为  类名首字母小写  的形态
>
> ![image-20250814095820354](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814095820354.png) 
>
> ![image-20250814100734701](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814100734701.png)
>
> 大多选择第一种或第二种
> ![image-20250814101155160](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814101155160.png)
>
> 推荐第一种
>
> ![image-20250814101653051](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814101653051.png)
>
> 

> #### 启动项目建议使用debug模式，方便定位错误

> ```
> # 应用服务 WEB 访问端口
> server.port=8080
> #下面这些内容是为了让MyBatis映射
> #指定Mybatis的Mapper文件
> mybatis.mapper-locations=classpath:mappers/*xml
> #指定Mybatis的实体目录
> mybatis.type-aliases-package=com.bzh.springboot.mybatis.entity
> ```



##### springboot项目配置文件

![image-20250821111420515](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821111420515.png)

![image-20250821111611763](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821111611763.png)

![image-20250821111816069](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821111816069.png)

![image-20250821112053159](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821112053159.png)

```
spring:
 application:
  name: springboot-mybatis-quickstart
spring:
 $数据库连接信息
 datasource:
  type: com.alibaba.druid.pool.DruidDataSource
  url: driver-class-name: com.mysql.cj.jdbc.Driver
  username: root
  password: 1234
#mybatis相关配置
mybatis:
 configuration:
  log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
 
  
  
```

![image-20250821131603573](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821131603573.png) 









# 8. springboot3集成Mybatis

官网：





## 介绍

* 一个持久层（操控数据库的层次）支持自定义sql，可以通过简单的xml或注解来配置和映射原始类型接口和java对象为数据库中的记录
  （数据库数据<--->java集合、对象）

* > java中表示属性一般使用驼峰的形式：departmentId
  > 数据库里面表示字段一般用下划线的形式：department_id

* xml模版(基本格式)
  ![image-20250715085814053](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715085814053.png)

  ```
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          " http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="">
      
  </mapper>
  ```

  这两个mapper一一对应，**namespace填写对应的路径**
  ![image-20250715131838194](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715131838194.png)

* 点击小鸟就能在xml文件和对应的接口文件之间切换

  ![image-20250715132008015](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715132008015.png)

  ![image-20250715132112145](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715132112145.png)

* 告诉springboot如何扫描mapper包【MapperScan注解】
  ![image-20250715132345962](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715132345962.png)

  【？ 】**扫描mapper，注册为springboot里面的一个对象，运行的时候可以通过这个bean操作数据库**

* Service层
  EmployeeService

  ![image-20250715133029472](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715133029472.png)
  （Service    )标注为一个bean

* controller层

  **从controller一步步往下写**

  EmployeeController
  ![image-20250715162705608](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715162705608.png)

  ![image-20250715141916044](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715141916044.png)

  ![image-20250715141038667](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715141038667.png)

  【通过xml文件】

  ![image-20250715141540889](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715141540889.png)

  【通过注解】
  ![image-20250715180029735](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715180029735.png)

  > **简单的sql可以通过注解，如果有关联查询，建议在xml里面写**

  > ### entity包
  >
  > 里面定义java的实体类
  >
  > * 类注意添加getter、setter方法
  >   
  >
  > 

  > 插件mybatisX

  > ```
  > https://ip:port/(path)
  > ```
  >
  > 

* 不要忘了@Resource注解

#### 文件结构

> mapper：数据持久层
>
> service：
> 如果一个接口有多个实现，就在Service层里面建一个impl包：里面存接口，然后实现
> 如果只有一个接口只有一个实现，就不用多写一个接口类
>
> controller：接口层
>
> 一套文件：xml--Mapper--Service--Controller
> 
>
> Controller=>Service=>Mapper=xml
>
> 接口层用Service对象调用Service里面对应的方法，Service里面用Mapper对象调用对应的方法（其sql操作在xms文件中写）
>
> 

* <u>前端浏览器向服务器发起请求，mybatis操作mysql数据库，数据转成java对象，转成json对象显示在浏览器</u>

> ![image-20250811130557242](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250811130557242.png)
>

* 传参
  
  > 老式方法，了解
  >
  > ![image-20250910135714589](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910135714589.png)
  
  > 实用
  >
  > ![image-20250910140758097](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910140758097.png)
  > ![image-20250910190105191](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910190105191.png)
  >
  > ![image-20250910192631695](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910192631695.png)
  >
  > 路径传参：/{id}    @PathVariable
  > 

  
  【http://localhost:8080/employee/selectById/1】
  
  ![image-20250715180435379](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715180435379.png)

  【http://localhost:8080/employee/selectById？1】
  ![image-20250715180700570](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715180700570.png)
  
  
  （多个参数）
  
  【http://localhost:8080/employee/selectById/1/101】
  ![image-20250715181059924](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715181059924.png)
  
  【http://localhost:8080/employee/selectById?id=1&num=101】
  ![image-20250715181017416](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715181017416.png)
  
  > 设置第二个参数不一定要传
  > ![image-20250715181227055](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715181227055.png)
  
  
  
  **@RequestBody可以把前端传来的json字符串映射出java的对象、或者数组【即用于映射对象或数组】**
  
  > ![image-20250910185728497](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910185728497.png)
  >
  > ![image-20250910190032374](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910190032374.png)
  >
  > 
  >
  > ![image-20250910190130051](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910190130051.png)
  >
  > 
  >
  > 
  > 
  
  ![image-20250902190956667](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902190956667.png)
  
  ![image-20250715181744519](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715181744519.png)
  
  ![image-20250715181815965](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715181815965.png)
  

#### 分页查询



1. 引入pagehelp插件

   ```pom.xml
   <!--分页插件的pagehelper-->
           <dependency>
               <groupId>com.github.pagehelper</groupId>
               <artifactId>pagehelper-spring-boot-starter</artifactId>
               <version>1.4.6</version>
               <exclusions>
                   <exclusion>
                       <artifactId>mybatis</artifactId>
                       <groupId>org.mybatis</groupId>
                   </exclusion>
               </exclusions>
           </dependency>
   ```

   

2. 
   ![image-20250715190614865](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715190614865.png)

   ![image-20250715190757798](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715190757798.png)

   通过改变参数，就能查询不同pageSize情况下指定页数的数据

   ![image-20250715192108950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715192108950.png)

   

## 【后端增删改查】

> 利用mybatis
> 

1. get：查询操作

   > 

2. post：新增操作

   > 

3. put：修改操作

   > 

4. delete：删除操作

   > 

#### restful风格

![image-20250822080542076](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250822080542076.png)

*  具体对这些资源进行什么操作不用动词描述、通过请求资源描述

![image-20250822080827958](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250822080827958.png)

![image-20250822080844559](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250822080844559.png)

> 浏览器中发起的请求都是Get请求

* 不同请求方式的测试可以通过apifox测试







> #### 这些接口可以使用接口测试工具（比如postman测试）
>
> ![image-20250902075354933](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902075354933.png)
>
>
> ![image-20250902081442827](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902081442827.png)
> |
>
> 
> 



> ![image-20250901104256546](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250901104256546.png)
> **@RequestBody：可以把前端传来的json字符串映射出java的对象、或者数组**
>
> ![image-20250901104607982](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250901104607982.png)
>
> * 插入，用parameterType提供传入的参数类型
>
> * 使用#{}的形式提取出想要的参数
> * **<u>要插入的值是从java对象里面取的，使用的是驼峰命名；插入的是数据库、数据库里是下划线命名</u>**
>   （写sql使用下划线；涉及到绑定java对象值，使用驼峰）
>
> 











# 



### 安装axios分装前后端对接数据库工具

```
npm i axios -S
```



![image-20250902085135665](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902085135665.png)





* 封装request.js；通过这个工具类帮助发送请求(添加统一的请求头；对返回做统一的处理)
  ![image-20250902085703429](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902085703429.png)

  ```javascript
  import axios from "axios";
  import {ElMessage} from "element-plus";
  import router from "@/router/index.js";
  
  const request = axios.create({
      baseURL: "http://localhost:9090",
      timeout: 30000 //后台接口超时时间
  
      })
  
  //request 拦截器
  //可以自动在请求发送前对请求做一些处理
  request.interceptors.request.use(config => {
      config.headers['Content-Type'] = 'application/json;charset=utf-8';
      return config
  },error => {
      return Promise.reject(error)
  });
  
  //response 拦截器
  //可以自动在接口响应后统一处理结果
  request.interceptors.response.use(
      response => {
          let res = response.data;
          //兼容服务器返回的字符串数据
          if(typeof res === 'string'){
              res = res ? JSON.parse(res):res;
          }
          return res;
      },
      error => {
          if(error.response.status === 404) {
              ElMessage.error('未找到请求接口 ')
          }else if(error.response.status === 500){
              ElMessage.error('系统异常，请查看后端控制台报错')
          }else {
              console.error(error.message)
          }
          return Promise.reject(error)
      }
  
  )
  
  
  export default request
  ```

  **<u>核心功能：配置HTTP请求的默认行为，同时在请求和响应阶段进行统一的预处理和错误处理。减少了重复代码、提高了开发效率</u>**

  ```javascript
  import axios from "axios";
  import {ElMessage} from "element-plus";
  import router from "@/router/index.js";
  
  //基于axios创建一个请求实例，
  const request = axios.create({
      baseURL: "http://localhost:9090",
      timeout: 30000 //后台接口超时时间
      })
      //设置默认的基础地址和请求的最大超时时间（避免在每个请求中重复配置这些信息）
      
      
  //【通过拦截器对请求做一些定制化的处理】
      
  //request 拦截器
  //可以自动在请求发送前对请求做一些处理
  request.interceptors.request.use(config => {
      config.headers['Content-Type'] = 'application/json;charset=utf-8';//每次请求发送前都给请求头添加一个固定的值'application/json;charset=utf-8'———告知服务器前端发送的数据类型
      return config
  },error => {
      return Promise.reject(error)
  });
  
  //response 拦截器
  //可以自动在接口响应后统一处理结果
  request.interceptors.response.use(
      
      //如果请求成功：提取response.data，检查是否为字符串，如果是字符串，就将其解析为JSON格式
      response => {
          let res = response.data;
          //兼容服务器返回的字符串数据
          if(typeof res === 'string'){
              res = res ? JSON.parse(res):res;
          }
          return res;
      },
      
      //如果请求失败：根据错误状态码显示不同的提示信息
      error => {
          if(error.response.status === 404) {
              ElMessage.error('未找到请求接口 ')
          }else if(error.response.status === 500){
              ElMessage.error('系统异常，请查看后端控制台报错')
          }else {
              console.error(error.message)
          }
          return Promise.reject(error)
      }
  
  )
  
  //将配置好的请求实例导出
  export default request
  ```

  > ![image-20250902111541662](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902111541662.png)
  >
  > 此处设置了发起请求的原始路径

* **它导入需要发起请求的vue文件**

  ![image-20250902102011257](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902102011257.png)

  > 输入request回车一次就会出现这两个导入的选项
  > （选择第二个）

  这里的request是一个对象（由代码可知它是axios创建的实例），利用这个axios实例可以用来发起http请求（GET、POST等）

  **<u>调用request实例发起请求</u>**

  * **GET 请求**

    ```javascript
    // 获取员工列表
    request.get('/employee/selectAll')
      .then(response => {
        console.log('员工列表:', response);
        // 在这里处理成功响应
      })
      .catch(error => {
        console.error('获取员工列表失败:', error);
        // 在这里处理错误
      });
    ```

    ```javascript
    request.get('/employee/selectPage',{
        //通过这种包装，让“?pageNum=1&pageSize=10”类型的参数也能兼容
        params:{
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          name: data.name
        }
      }).then(res => {
        //给前端的tableData赋值
        data.tableData = res.data.list
        data.total = res.data.total
      })
    ```

    

  * **POST 请求**

    ```
    request.post('路径',参数(对象格式))
    .then(res=>{
    	
    }).catch(error => {
    
    
    });
    
    ```

    > 对象格式
    > {}
    > {
    > name:'新员工',
    > age:30
    >
    > }

    ```javascript
    // 添加新员工
    const newEmployee = {
      name: '新员工',
      age: 30,
      // 其他员工信息
    };
    
    request.post('/employee/add', newEmployee)
      .then(response => {
        console.log('添加成功:', response);
        // 在这里处理成功响应
      })
      .catch(error => {
        console.error('添加失败:', error);
        // 在这里处理错误
      });
    ```

    ```javascript
    const add = () =>{
      data.form.departmentId= 1;
      request.post('/employee/add',data.form).then(
          res => {
            if(res.code === '200'){
              ElMessage.success('新增成功')
              //重新加载最新的数据
              load()
            } else{
              ElMessage.error(res.msg)
            }
          }
      )
      data.formVisible = false
    }
    ```

  * PUT请求

    ```javascript
    // 更新员工信息
    const updatedEmployee = {
      id: 1,
      name: '更新后的员工名',
      age: 31,
      // 其他更新后的员工信息
    };
    
    request.put('/employee/update', updatedEmployee)
      .then(response => {
        console.log('更新成功:', response);
        // 在这里处理成功响应
      })
      .catch(error => {
        console.error('更新失败:', error);
        // 在这里处理错误
      });
    ```

    ```javascript
    request.put('/employee/update',data.form).then(
          res => {
            if(res.code === '200'){
              ElMessage.success('修改成功')
              //重新加载最新的数据
              load()
            } else{
              ElMessage.error(res.msg)
            }
          }
      )
    ```

    

  * **Delete请求**

    ```javascript
    // 删除员工
    const employeeId = 1;
    
    request.delete(`/employee/delete/${employeeId}`)
      .then(response => {
        console.log('删除成功:', response);
        // 在这里处理成功响应
      })
      .catch(error => {
        console.error('删除失败:', error);
        // 在这里处理错误
      });
    ```

    ```javascript
    //删除操作要做二次确认
      ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗','删除确认',{type:'warning'}).then(()=>{
        //点击确认就删除
        request.delete('employee/deleteById/'+id).then(res =>{
          if(res.code === '200'){
            ElMessage.success('删除成功')
            load()//删除后重新加载
          }else{
            ElMessage.error(res.msg)
          }
        })
      }).catch(
          console.error('删除局失败：',error)
      )
    ```

    

    * **示例：**

      ```java
      request.get('/employee/selectAll')
          .then(res => {
              console.log(res)
              data.employeeList = res.data;
      });
      ```

      

      ```java
      request.get('/employee/selectAll')
          .then(res => {
            // 确保 res 有数据时才赋值
            if (res && res.data) {
              console.log(res)
              data.employeeList = res.data;
            }
          })
          .catch(error => {
            console.error('请求失败:', error); // 捕获错误
          });
      ```

      ![image-20250902112401167](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902112401167.png)

      > console.log(res)可以将json数据较为清晰的打印出来



> .vue经典格式
>
> ```
> <template>
> 	<div>
> 	
> 	</div>
> </template>
> 
> <script setup>
> 
> </script>
> 
> ```
>
> 
>
> 

# 前端-增删改查



## 分页查询数据





1. 数据表格根据实际情况设计
   ![image-20250902120416813](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902120416813.png)

   > show-overflow-tooltip将过多的文字变成省略号，鼠标移上去的时候显示

2. 
   ![image-20250902122058926](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902122058926.png)

   

3. 
   ![image-20250902122144244](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902122144244.png)

   

## 动态条件查询



* 配合分页查询，单纯的分页查询就相当于条件为空的动态条件查询
  * 动态模糊查询
    ![image-20250902152425445](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902152425445.png)



## 新增数据



1. 新增弹窗部件，设置表单
   ![image-20250902162512125](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902162512125.png)

   ```java
   <el-dialog v-model="data.formVisible" title="员工信息" width="500">
         <el-form :model="data.form" style="padding-right: 50px">
   
           <el-form-item label="名称" :label-width="80">
             <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
           </el-form-item>
   
           <el-form-item label="性别" :label-width="80">
             <el-radio-group v-model="data.form.sex">
               <el-radio value="男" label="男">男</el-radio>
               <el-radio value="女" label="女">女</el-radio>
             </el-radio-group>
           </el-form-item>
   
           <el-form-item label="工号" :label-width="80">
             <el-input v-model="data.form.num" autocomplete="off" placeholder="请输入工号"/>
           </el-form-item>
   
           <el-form-item label="年龄" :label-width="80">
             <el-input-number style="width: 160px" :min="18" v-model="data.form.age" autocomplete="off" placeholder="请输入年龄"/>
           </el-form-item>
   
           <el-form-item label="个人介绍" :label-width="80">
   <!--          textarea设置多行文本，rows设置默认行高-->
             <el-input type="textarea" v-model="data.form.descr" autocomplete="off" placeholder="请输入个人介绍"/>
           </el-form-item>
   
           <el-form-item label="部门" :label-width="80">
   
           </el-form-item>
   
   
   
         </el-form>
         <template #footer>
           <div class="dialog-footer">
             <el-button @click="data.formVisible = false">取消</el-button>
             <el-button type="primary" @click="save">
               保存
             </el-button>
           </div>
         </template>
       </el-dialog>
   ```

   

2. 设置通过handleAdd打开弹窗

   ```vue
   const handleAdd = () => {
     //每一次打开都清空数据
     data.form = {}
     data.formVisible = true;
   
   }
   ```

3. 点击保存，发起请求

   ```
   const save = () =>{
     data.formVisible = false
     request.post('employee/add',data.form).then(
         res => {
          if(res.code === '200'){
            ElMessage.success('新增成功')
            //重新加载最新的数据
            load()
          } else{
            ElMessage.error(res.msg)
          }
         }
     )
   
   }
   ```

   

> #### 注意路径一定要写清楚，前面有/
>
> ```
> '/employee/add'
> ```
>
> 

## 编辑数据













1. 按钮打开弹窗
   
2. 设置弹窗数据

   

3. 调用save函数完成修改（依据是否有id区分新增和修改操作）
   ![image-20250902175429964](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902175429964.png)





## 单个删除和批量删除









**批量删除**

1. 前端

   1. 给表格增加批量删除的组件
      ![image-20250903082525620](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903082525620.png)

   2. 实时监测被选中的行

      ![image-20250903082607105](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903082607105.png)

   3. 批量删除函数
      ![image-20250903082638431](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903082638431.png)















# vue3-登录注册页面





### 修改数据库和后端java类

![image-20250903092313141](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903092313141.png)



添加属性值以及getter、setter方法

![image-20250903092758191](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903092758191.png)



**同时更新sql语句**



### 更新

**更新添加表格栏目**

![image-20250903093455491](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903093455491.png)

> 

**更新添加弹窗栏目**

![image-20250903094359304](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903094359304.png)



**【表单校验】—设置必填字段**

![image-20250903100307105](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903100307105.png)

> ![image-20250903100454628](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903100454628.png)
>
> 这个选项可以消除上一次进入对话框产生的痕迹
> 

1. 给表单增加一个ref参数、并绑定规则

![image-20250903094627008](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903094627008.png)

（pro属性让rules里面的username和这边的username对应起来）

![image-20250903095920685](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903095920685.png)

![image-20250903100000859](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903100000859.png)

2. 引入ref


   ![image-20250903100210019](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903100210019.png)![image-20250903094842865](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903094842865.png)

3. 设置保存时校验必填属性是否填写

   ![image-20250903095117771](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903095117771.png)



> **逻辑讲解**
>
> * `el-form` 是表单容器，`ref="formRef"` 用于获取表单实例，`:rules="data.rules"` 绑定了表单校验规则，`:model="data.form"` 绑定了表单数据。
>
> * 每个 `el-form-item` 表示一个表单项：
>
>   - `prop` 绑定了需要校验的字段，与 `data.form` 中的字段对应。【注意prop是放在el-form-item里面的、不是在<el-input></el-input>里面】
>   - 控件中的`autocomplete="off"` 禁用了浏览器的自动完成功能。
>
> * `data.rules` 通常是一个对象，定义了每个需要校验字段的规则
>
>   * 例如
>
>     ```javascript
>     data.rules = {
>       username: [
>         { required: true, message: '账号不能为空', trigger: 'blur' },
>         { min: 3, max: 10, message: '账号长度需在 3 到 10 个字符之间', trigger: 'blur' }
>       ],
>       name: [
>         { required: true, message: '名称不能为空', trigger: 'blur' }
>       ],
>       num: [
>         { required: true, message: '工号不能为空', trigger: 'blur' }
>       ]
>     };
>     ```
>
>     - `required: true` 表示字段必填。
>     - `min` 和 `max` 定义了输入内容的长度限制。
>     - `trigger: 'blur'` 表示在字段失去焦点时触发表单校验。
>       【校验通常在字段失去焦点 (`trigger: 'blur'`) 或输入内容变化 (`trigger: 'change'`) 时触发。】
>
> * 表单校验与提交 (`save` 方法)
>   - 点击“保存”按钮时，调用 `save` 方法。
>   - `formRef.value.validate()` 触发整个表单的校验：
>     - 如果校验通过 (`valid === true`)，就继续执行保存的逻辑
>     - 如果校验不通过，表单会显示相应的错误提示。
>
> 

> ![image-20250903165424839](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903165424839.png)
>
> 注意这里表单项的v-model是绑定到data.form.username、而不是data.username
>
> 
>
> 















#### 【登录页面编写】

**添加路由**

![image-20250903101425639](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903101425639.png)

页面代码

* 页面表单
* 背景bg.jpg

```



```

退出登录按钮

```html
 <el-button style="border:none; background-color:rgb(60, 127, 255);color:white;" @click="router.replace('/login')">退出登录</el-button>
```

> 退出登录的同时要清理缓存，把之前后端给的用户信息清理掉
>
> ![image-20250904093134458](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904093134458.png)
>
> 
> 
>
> ![image-20250904093920924](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904093920924.png)
>
> 
> 







```css


.login-box{
  background-color: white;
  position:absolute;//绝对定位
  align-items:center;//让元素居中
  right:0;//设置位置靠右
  width:50%;
  height:100%;
  
}
```



* 最外层container->右侧box->box包登录表单
  ![image-20250903145226221](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903145226221.png)



* 背景图片样式

  //如果图片太大，可以这样设置高、宽，根据实际调整：background-size:100% 80%
  //background-position:0 20px;这个属性可以移动图片（第一个参数横轴，第二个参数纵轴）



* 获取后端的用户数据

  ![image-20250903191923251](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903191923251.png)

  ![image-20250903191742794](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903191742794.png)

* 使用方式
  * ![image-20250903192122933](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903192122933.png)

#### 注册接口

* ![image-20250903204157978](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903204157978.png)


  ![image-20250903204146637](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903204146637.png)











#### hutool工具包

> ![image-20250904205126560](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904205126560.png)
>
> hutool.cn——查看官方文档
>
> ```
>        <dependency>
>             <groupId>cn.hutool</groupId>
>             <artifactId>hutool-all</artifactId>
>             <version>5.8.40</version>
>         </dependency>
> ```
>
> 





##### 管理员相关增删改查

> 和员工代码差别不大
> 但是管理员没有注册接口（新的管理员由管理员在后台新增）



**后端接口**

* entity、controller、service、mapper、mapper.xml



**前端页面**

* 路由、页面、菜单













# [登录验证]

> #### 区分角色，采用cookie

> 一个按钮实现“管理员”选项和“员工”选项下的登录

#### 









**前端**

* 增加选择部件
  ![image-20250904095014126](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904095014126.png)

* 设置默认选择管理员
  ![image-20250904095032952](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904095032952.png)

  

#### **后端接口**

1. 创建一个基类，管理员和员工继承这个基类

   

2. login中的Employee/Admin替换成基类（由于两者都是子类，所以可以用Account接收）

   ![image-20250904103036433](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904103036433.png)
   
   ```java
   @PostMapping("/login")
   public Result login(@RequestBody Account account){
       Account res = null;
       if('ADMIN'.equals(account.getRole())){
           res = adminService.login(account);
       }else{
           res = employeeService.login(account);
       }
       return Result.success(res);
   }
   
   
   ```
   
   
   
   ![image-20250919170333054](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919170333054.png)
   
   ```java
   
   public Admin login(Account admin){
       String username = admin.getUsername();
       String password = admin.getPassword();
       Admin res = adminMapper.selectByUsername();
       if(res == null){
           throw new CustomException("500","不存在该用户");
       }else{
           if(password.equals(res.getPassword())){
               return res;
           }else{
   			throw new CustomException("500","账号或密码错误");
           }
       }
       
       
   }
   
   
   
   
   
   ```
   
   ```
   select * from admin where username = #{username}
   ```



#### 对数据表中的密码做加密处理

* ![image-20250919202033133](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919202033133.png)

  

  

  

* ![image-20250919202122519](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919202122519.png)
  ![image-20250919202542806](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919202542806.png)

* ```
  String processedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
  
  
          if (!processedPassword.equals(employee.getPassword())) {
              //密码错误
              throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
          }
  ```

  















#### 登录校验(会话技术)

cookie

> 如果登录了，就允许访问对应的功能接口；如果没有登录，就返回一个错误信息
>

> ![image-20250915103838641](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915103838641.png)
>
> 使用统一拦截技术拦截前端发过来的所有接口请求并进行校验、判断这个用户是否登录过（是否有登录标记）
>
> 









### **会话技术**



![image-20250915105008673](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915105008673.png)

> cookie(存在浏览器本地)、session(存在服务器)
> 令牌技术（企业开发用的比较多）

> 验证码是服务器动态生成的，所以验证码的生成过程中前端发起了一次请求，这次请求会记录验证码的值或答案是什么；
>
> 第二次发起登录请求的数据会将前端填写的验证码和之前的答案比对（一次会话的多次请求之间需要共享数据、通过会话跟踪技术实现）
> 



**Cookie方案**

![image-20250915120241243](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915120241243.png)

![image-20250915120520187](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915120520187.png)

> 响应头的Set-Cookie设置键值对
>
> 请求头中自带cookie，将cookie携带到服务器端
>
> ![image-20250915112339677](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915112339677.png)
>
> ![image-20250915112513785](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915112513785.png)

**Session方案**

  ![image-20250915130922891](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915130922891.png)
![image-20250915130957553](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915130957553.png)

> 底层基于cookie，只不过cookie当中储存的是服务端会话对象session的id值
>
> 服务器端根据JSESSIONID的值找到对应的session，然后获取出对应的数据 

**SessionController**

```java
@Slf4j
@RestController
public class SessionController{
    
    //设置cookie
    @GetMapping("/c1")
    public Result cookie1(HttpServletResponse response){
        //调用response对象中的方法
        response.addCookie(new Cookie("login_username","itheima"));
        return Result.success();
    }
    
    //获取cookie
    @GetMapping("/c2")
    public Result cookie2(){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie :cookies){
			if(Cookie cookie :cookies){
                System.out.println("login_username："+cookie.getValue());
            }
        }
    }
}



```



```java
@Slf4j
@RestController
public class SessionController{
	@GetMapping("/s1")
	public Result session1(HttpSession session){
		log.info("HttpSession-s1:{}",session.hashCode());
		//调用方法设置属性值
		session.setAttribute("loginUser","tom");
		return Result.success();
	}
	@GetMapping("/s2")
	public Result session2(HttpSession session){
		log.info("HttpSession-s2:{}",session.hashCode());
		Object loginUser = session.getAttribute("loginUser");
		log.info("loginUser:{}",loginUser);
		return Result.success(loginUser);
		
	}

}

```









### **令牌方案-主流**

![image-20250915131359029](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915131359029.png)


![image-20250915133056698](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915133056698.png)

> “=”等号在Base64编码中是一个补位符号
> 令牌长度取决于原始内容的大小

> 一个关键点：
>
> * Signature部分会将header、payload融入并加入指定密钥(自己设置的)，如果前面两部分发生改动、和第三部分对不上   就会校验失败，从而保证token的安全性，只有服务端生成的token才能被正常解析

![image-20250915133517652](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915133517652.png)

引入JTW工具包依赖

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

**测试程序**-生成jwt令牌

```java
public void testGenJwt(){
	Map<String,Object> dataMap = new HashMap<>();
	dataMap.put("id",1);
	dataMap.put("username","itheima");
	String jwt = Jwts.builder()
        .signWith(SignatureAlgorithm.HS256,"SVRIRUlNQQ")
		.addClaims(dataMap)//添加自定义信息
		.setExpiration(new Date(System.currentTimeMills() + 12*3600*1000))//表示令牌有效期是从当前开始，往后12小时
		.compact();//生成令牌
	//System.out.println(jwt);
}
```

> 调用builder方法指定令牌
>
> "SVRIRUlNQQ"——为密钥（可以选择一个字符串base64编码之后的结果作为密钥）（比如itheima编码之后是aXRoZWltYQ==）
> https://base64.us/
>
> 
> 

**测试程序**-解析令牌

```
public void testParseJwt() throws Exception{
	String jwtToken = "eyJ0eXAioiJKV1QiLCJhbGciOiJIUzI1NiJ9...";
	Claims claims = Jwts.parser()
		.setSigningKey("SVRIRUlNQQ==")
		.parseClaimsJws(jwtToken)//解析jwt令牌
		.getBody();
    System.out.println(claims);
} 
```

![image-20250915154238316](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915154238316.png)

##### **【实战代码】**

![image-20250915154425625](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915154425625.png)

1. 定义(JWT令牌操作)工具类
   ![image-20250915154721572](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915154721572.png)

   ```java
   
   public class JwtUtils{
   	private static final String SECRET_KEY = "aXRoZWltYQ==";
   	private static final long EXPIRATION_TIME = 12*60*60*1000;
   	
   	/*
   	 * 生成JWT令牌
   	 * 传入的claims为林排钟宝行的信息
   	 */
       public static String generateToken(Map<String,Object> claims){
           return Jwts.builder()
               .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
               .addClaims(dataMap)//添加自定义信息
   			.setExpiration(new Date(System.currentTimeMills() + 12*3600*1000))//表示令牌有效期是从当前开始，往后12小时
               .compact();//生成令牌
       }
   	
   	
   	
   }
   
   
   ```

   ![image-20250915154855234](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915154855234.png)

2. 修改login接口的service层逻辑、添加生成JWT令牌的步骤

   **原：**

   ```java
   public Admin loginCheck(Account admin) {
           String username = admin.getUsername();
           Admin emp = adminMapper.selectByUsername(username);
           if(emp == null){
               throw new CustomException("500","账号不存在");//抛出自定义异常
   
           }
           String password = admin.getPassword();
           if(emp.getPassword().equals(password)){//注意这里字符串比较、使用equals
              return emp;
           }
   
           throw new CustomException("500","账号或密码错误");
   
       }
   ```

   **修改后：**

   ```java
   public LoginInfo loginCheck(Account admin) {
           String username = admin.getUsername();
           Admin emp = adminMapper.selectByUsername(username);
           if(emp == null){
               throw new CustomException("500","账号不存在");//抛出自定义异常
   
           }
           String password = admin.getPassword();
           if(emp.getPassword().equals(password)){//注意这里字符串比较、使用equals
              log.info("登录成功，员工信息：{}",emp);
              //生成令牌
              Map<String,Object> claims = new HashMap<>();
              //存id和username（id必须存）
              claims.put("id",emp.getId());
              claims.put("username",e.getUsername());
              String jwt = JwtUtils.generateToken(claims)
              
              return new LoginInfo(emp,jwt);
               //或者return new LoginInfo(emp.getId(),emp.getUsername(),emp.getName,jwt);//两种的logininfo类不一样
   
           }
   
           throw new CustomException("500","账号或密码错误");
   
       }
   ```

   （LoginInfo类）

   ```
   @Data
   @
   @
   public class LoginInfo{
   	private Employee employee;
   	private String jwt;
   }
   ```

   ![image-20250915160715900](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915160715900.png)

   > 把token字符串存储，并在每一次请求中携带token，应该是前端做的事

![image-20250915161312792](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915161312792.png)



**<u>基于过滤器Filter实现jwt解析与比对</u>**

>  ![image-20250915162737278](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915162737278.png)
>
> ![image-20250915162925309](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915162925309.png)
>
> 

![image-20250915184551867](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915184551867.png)

![image-20250915184821936](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915184821936.png)

> ![image-20250915191742646](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915191742646.png)
>
> 导这个包的filter

```java
@WebFilter(urllPatterns = "/*")
@Slf4j
public class TokenFilter implements Filter {

	
	@Override
	public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse,FilterChain filterChain){
      	
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获取请求路径(不包含协议、ip和端口)
        String uri = request.getRequestURI();
        //如果路径中包含login或register就放行
        if(uri.contains("/login") || uri.contains("/register")){
            log.info("登录或注册请求，放行");
            filterChain.doFilter(request,response);//放行
            return ;
        }
        String token = request.getHeader("token");//获取token
        if(token == null){
            log.info("令牌为空，响应401")；
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setStatus方法用于设置状态码
            return ;
        }else{
            //校验令牌
            try{
                JwtUtils.parseToken(token);
            }catch{
				//如果校验出现异常，说明校验失败了
                log.info("令牌非法，响应401")；
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setStatus方法用于设置状态码
            return ;
            }
            
            //校验通过，放行
            log.info("令牌合法，放行");
            filterChain.doFilter(request,response);
            
            
        }
        filterChain.doFilter(servletRequest,servletResponse);
        
    }

}




```

> 校验流程
>
> ![image-20250915194546130](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915194546130.png)
>
>
> 注册账号和登录账号的请求不需要校验令牌

> init 和 destroy方法提供了默认的空实现

**Application启动类加一个注解**

```
@ServletComponentScan

```

![image-20250915185938959](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915185938959.png)

> #### Filter详解
>
>  ![image-20250915202113595](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915202113595.png)
>
> ![image-20250915203209347](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915203209347.png)
>
> 
>
> ![image-20250915203153714](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915203153714.png)
>
> 
> 







**<u>基于拦截器Interceptor实现jwt解析与比对</u>**



> 定义拦截器
> ![image-20250915203710633](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915203710633.png)
>
> 注册拦截器
>
> ![image-20250915203828549](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915203828549.png)
> （addInterceptors——添加拦截器)
>
> ![image-20250915205344816](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915205344816.png)
>
> 

**实战**

![image-20250915205740946](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915205740946.png)





```java
@Component
public class TokenInterceptor implements HandlerInterceptor{
	@Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
       
        //获取请求路径(不包含协议、ip和端口)
        String uri = request.getRequestURI();
        //如果路径中包含login或register就放行
        if(uri.contains("/login") || uri.contains("/register")){
            log.info("登录或注册请求，放行");
            
            return true;
        }
        String token = request.getHeader("token");//获取token
        if(token == null){
            log.info("令牌为空，响应401")；
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setStatus方法用于设置状态码
            return false;//不放行
        }else{
            //校验令牌
            try{
                JwtUtils.parseToken(token);
            }catch{
				//如果校验出现异常，说明校验失败了
                log.info("令牌非法，响应401")；
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//setStatus方法用于设置状态码
            return false;
            }
            
            //校验通过，放行
            log.info("令牌合法，放行");
            return true;
           
        }
    }
}

```



![image-20250915204432435](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915204432435.png)



```java
/*
 * 这是一个配置类
 */


//@Configuration底层封装了@Component
@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    private TokenInterceptor tokenInterceptor;
    
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");//拦截所有请求


        
    }
	
}

```



> #### 拦截器详解
>
> ![image-20250915211848490](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915211848490.png)
> 

![image-20250915212738950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915212738950.png)

* **先经过过滤器，后经过拦截器**
* **拦截器是spring框架提供的技术，只会拦截对于spring当中资源的请求；**
  **过滤器是servlet规范中提供的技术，拦截范围更大，可以拦截所有资源**



























# 个人信息与修改密码界面



#### **个人信息界面**



> 一般是给个人看的，管理员一般不用

* 路由





* 个人信息更新-->更改数据库信息

  ​		      -->更改manager的信息展示(data.user)

  子页面触发父级页面的更新

  * 定义一个事件，更新完发射这个事件

    ![image-20250904132851489](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904132851489.png)

  * 在父级中接收这个事件

    ![image-20250904131528287](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904131528287.png)
    ![image-20250904132133866](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904132133866.png)



* **<u>个人信息页面代码</u>**

```vue
<template>
  <div class="card">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 50px">

      <el-form-item label="账号" :label-width="80" prop="username">
        <el-input disabled="disabled" v-model="data.form.username" autocomplete="off" placeholder="请输入账号名"/>
<!--        账号一般不允许修改,设置disabled-->
      </el-form-item>

      <el-form-item label="名称" :label-width="80" prop="name">
        <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
      </el-form-item>


<!--      把下面的栏目用div包起来，然后设置只有员工才显示-->
      <div v-if="data.form.role === 'EMP'">
        <el-form-item label="性别" :label-width="80">
          <el-radio-group v-model="data.form.sex">
            <el-radio value="男" label="男">男</el-radio>
            <el-radio value="女" label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>

<!--       工号一般是唯一的，设置成不可更改 -->
        <el-form-item label="工号" :label-width="80" prop="num">
          <el-input disabled="disabled" v-model="data.form.num" autocomplete="off" placeholder="请输入工号"/>
        </el-form-item>

        <el-form-item label="年龄" :label-width="80">
          <el-input-number style="width: 160px" :min="18" v-model="data.form.age" autocomplete="off" placeholder="请输入年龄"/>
        </el-form-item>

        <el-form-item label="个人介绍" :label-width="80">
          <!--          textarea设置多行文本，rows设置默认行高-->
          <el-input type="textarea" v-model="data.form.descr" autocomplete="off" placeholder="请输入个人介绍"/>
        </el-form-item>

        <el-form-item label="部门" :label-width="80">

        </el-form-item>
      </div>

      <div style="margin: auto auto">
        <el-button type="primary" @click="updateUser">更新</el-button>
      </div>


    </el-form>
  </div>
</template>


<script setup>
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

const data = reactive({
  form:{},
  rules:{
    username:[
      {required:true,message:'请输入账号',trigger:'blur'}//blur:鼠标失焦的时候触发表单校验
    ],
    name:[
      {required:true,message:'请输入名称',trigger:'blur'}//blur:鼠标失焦的时候触发表单校验
    ],
    num:[
      {required:true,message:'请输入工号',trigger:'blur'}//blur:鼠标失焦的时候触发表单校验
    ]
  },
  user:JSON.parse(localStorage.getItem('xm-pro-user')),

})

//实现更新信息的逻辑（更新后台以及父级页面）
//定义一个事件，更新完发射这个事件
const emit = defineEmits(['updateUser'])
const updateUser = () =>{
  if(data.user.role === 'EMP'){
    request.put('/employee/update',data.form).then(res => {
      if (res.code === '200'){
        ElMessage.success('更新成功')
        //更新缓存数据
        localStorage.setItem('xm-pro-user',JSON.stringify(data.form))
        //更新成功之后向父级发送一个信号，表示你更新成功了（触发父级从缓存里面取到最新的数据）
        emit('updateUser')

      }else{
        ElMessage.error(res.msg)
      }
    })
  }else{
    request.put('/admin/update',data.form).then(res => {
      if (res.code === '200'){
        ElMessage.success('更新成功')
        //更新缓存数据
        localStorage.setItem('xm-pro-user',JSON.stringify(data.form))
        //更新成功之后向父级发送一个信号，表示你更新成功了（触发父级从缓存里面取到最新的数据）
        emit('updateUser')

      }else{
        ElMessage.error(res.msg)
      }
    })



  }
}

const formRef = ref()

if(data.user.role === 'EMP'){
  request.get('/employee/selectById/'+data.user.id).then(res => {
    data.form = res.data

  })
}else{
  //如果是管理员
  data.form = data.user

}


</script>

<style scoped>
  .card{
    width:50%;
    margin: 10px auto;
    padding: 30px 35px;
  }


</style>
```



* **<u>修改密码页面</u>**























# 12.实现文件上传和下载





## 文件上传



* FileController文件
  ![image-20250904203026313](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904203026313.png)

  

  > 由于java里面有一个file类，这里路径不能写file、会产生冲突

* 
  ![image-20250912204551367](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912204551367.png)
  
  ![image-20250905122742403](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905122742403.png)
  
  
  
  ```java
      //获取当前这个项目的根路径（文件上传目录的路径）
      private static final String filePath = System.getProperty("user.dir")+ "/files/";
  
  
  //【文件上传接口】(新增，对应post)
      @PostMapping("/upload")
      public Result upload(MultipartFile file){//文件流的形式接收前端发送过来的文件
          //获取文件原始名称
          String originalFilename = file.getOriginalFilename();//xxx.png  xxx.jpg
          //设置文件上传的地址【写一个项目路径而非固定路径】
          //如果没有这个目录，就创建目录
          if(!FileUtil.isDirectory(filePath)){
              FileUtil.mkdir(filePath);
          }
          //加一个时间戳、给文件名加一个唯一的标识
          String fileName = System.currentTimeMillis()+"_"+originalFilename;
          String realPath = filePath + fileName;//拼接成完整的文件路径
          //把文件写入这个目录
          try {
              FileUtil.writeBytes(file.getBytes(),realPath);//调用hutool工具写入这个目录（文件所有的字节数组，要写入的路径）
          } catch (Exception e) {
              e.printStackTrace();
              throw new CustomException("500","文件上传失败");
          }
  
          //返回一个文件的网络路径
          String url = "http://localhost:9090/files/download/" + fileName;
          return Result.success();
      }
  ```
  
  > 文件上传的请求方式必须是POST，因为get请求大小有限制、而文件一般都比较大
  
  > 除了时间戳，也可以使用jdk提供的UUID来保证文件名不重复
  >
  > ![image-20250912210653944](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912210653944.png)

> #### 文件上传接口的测试方式
>
> ![image-20250912210938799](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912210938799.png)
>
> 
> 



> 可能会出现较大文件无法上传的情况，此时在配置文件里面配置
>
> ![image-20250912211449584](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912211449584.png)
>
> 













## 文件下载



* 把文件变成网络路径，接口把文件读出来

  ![image-20250905123205770](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905123205770.png)

  > 【【不同文件的文件名不一样，下载到客户端的时候可能会有编码错误（比如中文文件名）】】
  > 所以一般提前加一个统一的编码方式
  >
  > ```
  > response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
  >             response.setContentType("application/octet-stream");
  > ```
  >
  > 

  ```java
  @GetMapping("/download/{fileName}")
  public void download(@PathVariable String fileName, HttpServletResponse response){
      //通过文件流的方式接收文件，这个接口返回void;
  
      try {
          //【【不同文件的文件名不一样，下载到客户端的时候可能会有编码错误（比如中文文件名）】】
          //【所以一般提前加一个统一的编码方式】
          response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
          response.setContentType("application/octet-stream");
          ServletOutputStream os = response.getOutputStream();//通过这个response可以把文件用流的形式写出到客户端
          String realPath = filePath + fileName;
          //获取到文件字节数组
          byte[] bytes = FileUtil.readBytes(realPath);
          os.write(bytes);
          os.flush();
          os.close();
  
      } catch (IOException e) {
          e.printStackTrace();
          throw new CustomException("500","文件下载失败");
      }
  
  
  }
  ```











## 前端对接文件上传和下载

* 头像组件

  ![image-20250905141717201](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905141717201.png)

  **另一种能实现文件上传的前端组件**
  (form表单设置enctype属性)

  (input主键的类型设置成file)

  ```html
  <form action="/upload" method="post" enctype="multipart/form-data">
      头像：<input type="file" name="file"><br>
      <input type="submit" value="提交">
  </form>
  ```

  

  ```vue
  <el-form-item label="头像" :label-width="80" >
          <div style="width:100%;display:flex;justify-content: center;margin-bottom: 20px">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:9090/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
  
            >
              <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </div>
        </el-form-item>
  ```

  ![image-20250905141903061](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905141903061.png)

  ```css
    .avatar-uploader{
      width:120px;
      height: 120px;
      display: block;
    }
    .avatar{
      width: 120px;
      height: 120px;
      display: block;
      object-fit: cover;
    }
    .avatar-uploader{
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }
  
    .avatar-uploader:hover {
      border-color: var(--el-color-primary);
    }
  
    .avatar-uploader-icon {
      font-size: 20px;
      color: #8c939d;
      width: 120px;
      height: 120px;
      text-align: center;
    }
  
  ```

  

* 更新前端父级页面展示、后端数据

  ![image-20250905141733731](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905141733731.png)
  

  ```
  const handleAvatarSuccess = (res) => {
    console.log(res.data)
    data.form.avatar = res.data
    //完成后端数据修改
    updateUser()
  
  
    //完成前端显示
    //更新缓存数据
    localStorage.setItem('xm-pro-user',JSON.stringify(data.form))
    //更新成功之后向父级发送一个信号，表示你更新成功了（触发父级从缓存里面取到最新的数据）
    emit('updateUser')
  }
  ```





> 注意此处代码中的action链接是http，不是https











##### 增加avatar属性 

1. account添加属性、getter/setter方法
2. 更改sql语句（insert、update、）







##### 如果想让某一个部件居中

> 用一个div包裹
>
> 然后设置style = "width:100%;display:flex;justify-content:center"
>
> 



# 















### 新建**数据库表article**

![image-20250905183644849](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905183644849.png)







### 完成后端接口

1. 创建对应的实体类于entity包
2. 创建Controller接口层的对应类
3. 创建Service接口层的对应类



# 后端-【经典七接口】

* 查询所有数据--selectAll

* 添加数据--add

* 更新数据--update

* 删除单个数据--deleteById

* 批量删除数据--deleteBatch

  ![image-20250913140913896](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913140913896.png)
  ![image-20250913141206600](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913141206600.png)

  controller层
  ![image-20250902191537815](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902191537815.png)
  service层
  ![image-20250902191517479](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902191517479.png)

  

  

* 按ID查询数据--selectById

* 分页查询--selectPage

* 


#### 分层基础模版

**Controller层模版**

```java
@RestController
@RequestMapping("/admin")
//指定路径
public class SomethingController {

    //引入Service对象(不要忘了注解)
    @Resource
    private SomethingService somethingService;

    /*
     * 查询所有数据
     *
     */
    @GetMapping( "selectAll")
    public Result selectAll(Something something){
        //用Service对象调用方法
        List<Something> list = somethingService.selectAll(something);
        return Result.success(list);
    }

    /*
     * 添加数据
     */
    @PostMapping("add")
    public Result add(@RequestBody Something something){
        try {
            somethingService.add(something);
            return Result.success(something);
        } catch (Exception e) { 
            e.printStackTrace();
            return Result.error("500", "系统错误");
        }

    }

    /*
     * 更新数据
     */
    @PutMapping("update")
    public Result update(@RequestBody Something something){
        adminService.update(something);
        return Result.success(something);
    }
    /*
     *删除单个数据
     */
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    /*
     * 批量删除数据
     */
    @DeleteMapping("deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){//这里的requestBody不能省略(通义灵码说spring无法处理集合和数组这样的复杂类型，所以才要加注解，但是简单类型可以省略但不建议)
        adminService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping( "selectById/{id}")//路径传参
    public Result selectById(@PathVariable Integer id){
        //用Service对象调用方法
        Something something = somethingService.selectById(id);
        return Result.success(something);


    }


    /*
     * 分页查询
     * pageNum:当前页码
     * pageSize:每页显示的记录数
     */
    @GetMapping( "selectPage")//路径传参
    public Result selectById(Something something,@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize
    ){
        //返回分页数据
        PageInfo<Something> pageInfo = somethingService.selectPage(something,pageNum,pageSize);

        return Result.success(pageInfo);
    }


}
```

* **Service层模版**

  ```java
  @Service
  public class AclassService {
      @Resource
      private AclassMapper aobjectMapper;
      public List<Aclass> selectAll(Aclass aobject) {
          //额外的业务操作
          //xxxxxx
          //xxxxxx
  
          //在这个方法中调用mapper的方法查询数据
          List<Aclass> list =aobjectMapper.selectAll(aobject);
          return list;
      }
  
      public Aclass selectById(Integer id) {
          return aobjectMapper.selectById(id);
      }
  
      public PageInfo<Aclass> selectPage(Aclass aobject,Integer pageNum, Integer pageSize) {
          //pageinfo类由pagehelper提供（import com.github.pagehelper.PageInfo;）
          //三行实现分页查询
          PageHelper.startPage(pageNum,pageSize);
          List<Aclass> list = aobjectMapper.selectAll(aobject);
          return PageInfo.of(list);
      }
  
      public void add(Aclass aobject) {
          aMapper.insert(aobject);
      }
  
      public void update(Aclass aobject) {
          aMapper.update(aobject);
      }
  
      public void deleteById(Integer id) {
          aMapper.deleteById(id);
      }
  
      public void deleteBatch(List<Integer> ids) {
          for (Integer id :ids) {
              this.deleteById(id);
          }
      }
  
      
      
      //登录界面验证【仅部分页面需要】
      public Aclass login(Account aobject) {
          String username = aobject.getUsername();
          Aclass emp = aobjectMapper.selectByUsername(username);
          if(emp == null){
              throw new CustomException("500","账号不存在");//抛出自定义异常
  
          }
          String password = aobject.getPassword();
          if(emp.getPassword().equals(password)){//注意这里字符串比较、使用equals
             return emp;
          }
  
          throw new CustomException("500","账号或密码错误");
  
      }
  
  
      //更新密码【仅部分页面需要】
      public void updatePassword(Account account) {
          Integer id = account.getId();
          Aclass aobject = this.selectById(id);
          //判断填写的原密码是否和此账号原密码一致
          if(aobject.getPassword().equals(account.getPassword())){
              //继续执行更改密码的逻辑【注意为了更改密码，后端需要一个newPassword的属性】
              if(account.getNewPassword() == null){
                  throw new CustomException("500","新密码为空");
              }
              aobject.setPassword(account.getNewPassword());
              this.update(aobject);
          }else{
              throw new CustomException("500","原密码输入错误");
          }
  
  
  
      }
  }
  ```

* **Mapper层模版**

  ```java
  public interface AclassMapper {
  
      List<Aclass> selectAll(Aclass aobject);
  
      @Select("select * from aclass where id=#{id}")
      Aclass selectById(Integer id);
  
      void insert(Aclass aobject);
  
      void update(Aclass aobject);
  
      void deleteById(Integer id);
  
      
  
  
  
  
  }
  
  ```

* **xml文件【根据实际情况微调】**

  ```java
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.bzh.mapper.ArticleMapper">
      <select id="selectAll" resultType="com.bzh.entity.Article">
          select * from article
          <where>
              <if test="name != null">name like concat('%',#{name},'%')</if>
          </where>
          order by id desc
      </select>
  
      <insert id="insert" parameterType="com.bzh.entity.Article">
          insert into article(username,password,role,name,avatar)
          values (#{username},#{password},#{role},#{name},#{avatar})
      </insert>
  
      <update id="update" parameterType="com.bzh.entity.Article">
          update article set username=#{username},password=#{password},role=#{role},name=#{name},avatar=#{avatar}
          where id=#{id}
      </update>
  
      <delete id="deleteById" parameterType="com.bzh.entity.Article" >
          delete from article where id=#{id}
      </delete>
  
     	
  
  
  </mapper>
  ```
  
  > 批量删除的两种方法：
  >
  > 1. 在service层里面遍历ids数组，对每一个id使用serviceMapper.deleteById(id)方法
  >
  > 2. 在service层里面，调用serviceMapper.deleteByEmpIds(ids)方法
  >    然后在xml文件里面写遍历的逻辑、从而遍历删除这些id
  >
  >    ```xml
  >    //xml文件写法
  >    <delete id="deleteByIds">
  >        delete from emp where id in (
  >    	<foreach collection="ids" item="id" separator=",">
  >            #{id}
  >        </foreach>
  >            )
  >    </delete>
  >        
  >        
  >    <delete id="deleteByIds">
  >        delete from emp where id in
  >    	<foreach collection="ids" item="id" separator="," open="(" close=")">
  >            #{id}
  >        </foreach>
  >    
  >    </delete>
  >    
  >    
  >    ```
  >
  >    

##### 修改员工信息（有员工经历的情况）

> ![image-20250913163254313](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913163254313.png)
>
> 



```
@PutMapping
public Result update(@RequestBody Emp emp){
	//log.info("修改员工",emp)
	
	empService.update(emp);
	return Result.success();
}



```

service层

```
public void update(Emp emp){
	//1. 根据id修改员工的基本信息
	emp.setUpdateTime(LocalDateTime.now());
	empMapper.updateById(emp);
	//2. 根据id修改员工的工作经历信息
	//先删除原有的工作经历
	empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));//因为此次deleteByEmpIds是批量删除方法、传入的参数是List，这里将Integer类型的id转成List
	//后添加这个员工新的工作经历
	List<EmpExpr> exprList = emp.getExprList();
	if(!CollectionUtils.isEmpty(exprList)){
		exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
		empExprMapper.insertBatch(exprList);
		
	}
}


```

> 先删除，后增加
>
> 可以应对该员工本来没有工作经历、然后添加几条工作经历的情况
> 可以应对该员工将本来的一条员工经历修改，然后添加了两条工作经历的情况
> 可以应对该员工将本来的一条员工经历删除的情况

mapper层省略

```

```

xml层省略

```

```



##### 需要手动封装resultMap的情况

![image-20250913155643570](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913155643570.png)





### 完成前端页面

> 参考admin.vue，修改部分属性和文字





> :preview-src-list=[scope.row.cover] preview-teleported
>
> 给img增加这个属性之后，就可以点开图片仔细查看（支持放大、旋转等）
> 
>
> 



---



```





```





---

















# 14.数据批量导入和导出功能







> #### 完成部门信息的增删改查
>
> 
>
> 此处部门不需要add方法
>
> 部门信息的前端vue界面代码参考：
>
> ```
> 
> 
> <template>
>   <div>
>     <!--    搜索区域-->
>     <div class="card" style="margin-bottom:5px">
>       <!--（搜索框需要宽度）-->
>       <el-input v-model="data.name" style="width: 240px ;margin-right:10px" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
>       <el-button type="primary" @click="load">查询</el-button>
>       <el-button type="warning" @click="reset">重置</el-button>
>     </div>
> 
>     <div class="card" style="margin-bottom:5px">
>       <el-button type="primary"@click="handleAdd">新增</el-button>
>       <el-button type="danger" @click="delBatch">批量删除</el-button>
>       <el-button type="info">导入</el-button>
>       <el-button type="success">导出</el-button>
>     </div>
> 
>     <!--数据表格-->
>     <div class="card" style="margin-bottom:5px">
>       <el-table :data="data.tableData" stripe @Selection-change="handleSelectionChange">
>         <el-table-column type="selection" width="55"/>
>         <el-table-column label="部门名称" prop="name"/>
> 
>         <el-table-column label="操作">
> <!--          通过template拿到操作对象；（scope里面有这一行的属性）-->
>           <template #default="scope">
>             <el-button link type="primary" :icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
>             <el-button link type="primary" :icon="Delete" @click="handleDelete(scope.row.id)">删除</el-button>
> 
>           </template>
>         </el-table-column>
> 
>       </el-table>
>     </div>
> 
>     <div style="margin-top:15px">
> <!--      当current改变时触发current-change，调用load函数重新加载数据-->
> <!--     注意参数不要匹配错误了，v-model:current-page绑定的是pageNumv-->
>       <el-pagination
>           @size-change="load"
>           @current-change="load"
>           v-model:current-page="data.pageNum"
>           v-model:page-size="data.pageSize"
>           :page-sizes="[5,10,15,20]"
>           background
>           layout="total,sizes,prey,pager,next,jumper"
>           :total="data.total"/>
>     </div>
> 
>     <el-dialog v-model="data.formVisible" title="员工信息" width="500" destroy-on-close>
>       <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 50px">
> 
>         <el-form-item label="部门名称" :label-width="80" prop="name">
>           <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入部门名称"/>
>         </el-form-item>
> 
> 
> 
> 
>       </el-form>
>       <template #footer>
>         <div class="dialog-footer">
>           <el-button @click="data.formVisible = false">取消</el-button>
>           <el-button type="primary" @click="save">
>             保存
>           </el-button>
>         </div>
>       </template>
>     </el-dialog>
> 
> 
>   </div>
> </template>
> 
> <script setup>
> import {reactive,ref} from "vue";
> import {Edit,Delete, Search} from "@element-plus/icons-vue";
> import request from "@/utils/request.js";
> import {ElMessage, ElMessageBox} from "element-plus";
> const data = reactive({
> 
>   name:null,
>   tableData:[],
> //   分页组件需要配置的属性
>   pageNum:1,
>   pageSize:10,
>   total:0,
>   currentPage:1,
>   formVisible:false,
>   form:{},
> //通过可控制formVisible来控制弹窗的显示
>   ids:[],
>   rules:{
> 
>     name:[
>       {required:true,message:'请输入部门名称',trigger:'blur'}//blur:鼠标失焦的时候触发表单校验
>     ],
> 
>   }
> 
> })
> 
> 
> const formRef = ref()
> 
> 
> 
> 
> 
> const load = () => {
>   request.get('/department/selectPage',{
>     //通过这种包装，让“?pageNum=1&pageSize=10”类型的参数也能兼容
>     params:{
>       pageNum: data.pageNum,
>       pageSize: data.pageSize,
>       name: data.name
>     }
>   }).then(res => {
>     //给前端的tableData赋值
>     data.tableData = res.data.list
>     data.total = res.data.total
>   })
> }
> load()
> const reset =() =>{
>   //恢复参数，重新加载
>   data.name=null;
>   load();
> 
> }
> 
> const handleAdd = () => {
>   //每一次打开都清空数据
>   data.form = {}
>   data.formVisible = true;
> 
> }
> const add = () =>{
>   data.form.departmentId= 1;
>   request.post('/department/add',data.form).then(
>       res => {
>         if(res.code === '200'){
>           ElMessage.success('新增成功')
>           //重新加载最新的数据
>           load()
>         } else{
>           ElMessage.error(res.msg)
>         }
>       }
>   )
>   data.formVisible = false
> }
> //update和新增传递的参数是一样的，只不过一个是post、一个是put const update = () =>{
> const update = () =>{
>   data.form.departmentId= 1;
>   request.put('/department/update',data.form).then(
>       res => {
>         if(res.code === '200'){
>           ElMessage.success('修改成功')
>           //重新加载最新的数据
>           load()
>         } else{
>           ElMessage.error(res.msg)
>         }
>       }
>   )
>   data.formVisible = false;
> }
> const save = () =>{
>   //由于一个窗口同时被新增和修改操作使用，我们需要再save里面做两种操作
>   formRef.value.validate((valid) => {
>     if(valid){
> 
>       //如果有id，说明是编辑
>       if(data.form.id){
>         update()
>       }else{
>         add()
>       }
> 
> 
>     }
>   })
> }
> 
> 
> const handleUpdate = (row) => {
>   // data.form = row;//浅拷贝
>   //使用深拷贝而不是浅拷贝
>   data.form = JSON.parse(JSON.stringify(row));
>   data.formVisible = true;
> 
> }
> 
> const handleDelete = (id) =>{
>   //删除操作要做二次确认
>   ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗','删除确认',{type:'warning'}).then(()=>{
>     //点击确认就删除
>     request.delete('employee/deleteById/'+id).then(res =>{
>       if(res.code === '200'){
>         ElMessage.success('删除成功')
>         load()//删除后重新加载
>       }else{
>         ElMessage.error(res.msg)
>       }
>     })
>   }).catch(
>       console.error('删除局失败：',error)
>   )
> 
> 
> }
> 
> //利用这个函数依据选中情况不断更新ids的值
> const handleSelectionChange = (rows) => {//返回所有选中的行对象数组
>   // console.log(rows)
>   //从选中的行数组里面取出所有行的id，组成一个新的数组
>   data.ids = rows.map(row => row.id)
>   console.log(data.ids)
> 
> 
> }
> const delBatch = () =>{
>   if(data.ids.length === 0){
>     ElMessage.warning('请选择数据')
>     return;
>   }
>   //删除操作要做二次确认
>   ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗','删除确认',{type:'warning'}).then(()=>{
>     request.delete('/employee/deleteBatch',{data:data.ids}).then(res =>{//第二个位置定义一个对象，把参数传进去
>       if(res.code === '200'){
>         ElMessage.success('删除成功')
>         load()
>       }else{
>         ElMessage.error(res.msg)
>       }
>     })
>   }).catch()
> 
> }
> 
> 
> </script>
> ```
>
> 



#### employee中的表格需要department里的属性

使用**关联查询**，并给后端中的employee加上departmentName属性，这样查询之后返回给tabledata的对象组里面就包含有departmentName

* ![image-20250907080743140](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250907080743140.png)
* ![image-20250907080825005](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250907080825005.png)















### 导出为excel

> 通过流的形式下载成excel

* 前端

  ```vue
  <el-button type="success" @click="exportData">导出</el-button>
  
  
  const exportData = () =>{
    //
    window.open('http://localhost:9090/employee/export');//打开流的链接，浏览器会自动帮我们下载文件
  
  }
  
  ```

  

  > 前端不能直接用export作为方法名(是关键字)
  > 



* 后端
  export接口

  ```java
  /*
      * 导出数据
      * (流的形式、不是JSON，没有返回值)
      */
  
      @GetMapping("/export")
      public void export(HttpServletResponse response) throws Exception{
          //1. 拿到所有的员工数据
          List<Employee> employees = employeeService.selectAll(null);//传null表示没条件、查所有数据
          //2. 构建excelWriter(使用hutool)[writer可以把数据写到流里面]
          ExcelWriter writer = ExcelUtil.getWriter(true);
          //设置中文表头
          writer.addHeaderAlias("username","账号名");
          writer.addHeaderAlias("role","角色");
          writer.addHeaderAlias("name","姓名");
          writer.addHeaderAlias("sex","性别");
          writer.addHeaderAlias("num","工号");
          writer.addHeaderAlias("age","年龄");
          writer.addHeaderAlias("descr","简介");
          writer.addHeaderAlias("departmentName","部门");
  
  
  
          //设置了这个属性之后，只有添加了别名的字段才会被导出（避免吧密码也导出）
          writer.setOnlyAlias(true);
  
          //把数据放到writer里面
          writer.write(employees,true);
          //5. 设置输出的文件的名称
          // 设置浏览器响应的格式
          response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
          String fileName = null;
          try {
              fileName = URLEncoder.encode("员工信息","UTF-8");
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");//设置输出流的头信息
          //6. 把数据从writer写出到输出流并关闭writer
          ServletOutputStream os = response.getOutputStream();
          writer.flush(os);
          writer.close();
  
      }
  ```

  ![image-20250908100003185](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908100003185.png)

* 引入POI依赖

  (引入poi-ooxml，这个包会自动关联引入poi包，可以很好的支持Office2007+的文档格式)

  ```
  <dependency>
  	<groupId>org.apache.poi</groupIId>
  	<artifactId>poi-ooxml</artifactId>
  	<version>${poi.version}</version>
  </dependency>
  //版本号可以上mvnrepository看看最新版本号
  （5.4.1）
  
  ```

  

![image-20250908095845778](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908095845778.png)









### excel导入数据



* 后端接口

  ```java
  /*
      * 导入excel为数据（post请求）
       */
      @PostMapping("/import")
      public Result imp(MultipartFile file) throws IOException {
  
          //拿到输入流，构建reader
          InputStream inputStream = file.getInputStream();
          ExcelReader reader = ExcelUtil.getReader(inputStream);
          //读取excel里面的数据(通过javabean的方式读取excel内容，但是要求表头必须是英文，跟javabean的属性一致)
          //方法左边写excel表格中的标题名，右边写这个标题名对应的【数据库字段】
          reader.addHeaderAlias("账号名","username");
          reader.addHeaderAlias("角色","role");
          reader.addHeaderAlias("姓名","name");
          reader.addHeaderAlias("性别","sex");
          reader.addHeaderAlias("工号","num");
          reader.addHeaderAlias("年龄","age");
          reader.addHeaderAlias("简介","descr");
          reader.addHeaderAlias("部门","department_id");
  
  
          List<Employee> employees = reader.readAll(Employee.class);
          //写入数据到数据库
          for(Employee elem: employees){
              employeeService.add(elem);
          }
          return Result.success();
  
  
  
  
  
      }
  ```

  ![image-20250908104901953](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908104901953.png)

* 前端

  

  ```
  <el-upload
    action="http://localhost:9090/employee/import"
    :show-file-list="false"
    :on-success="importSuccess">
    <el-button type="info">导入</el-button>
    
  </el-upload>
  
  
  const importSuccess = () =>{
    ElMessage.success('导入成功')
    load()
  }
  
  
  ```





# 15.数据统计图表功能

#### 官网

https://echarts.apache.org/handbook/zh/get-started/

* 引入echarts

  ```
  import * as echarts from 'echarts'
  ```

* 准备好dom

  ```vue
  <!--    为Echarts准备一个定义了宽高的DOM（容器）-->
      <div id="main" style="width: 600px;height:400px;">
      </div>
  ```

  > id要对应

  > 引入onMounted并初始化实例

  ```javascript
  import {reactive,onMounted} from "vue"
  
  const option = {
    title: {
      text:'ECharts 入门示例'
    },
    tooltip:{},
    legend:{
      data:['销量']
    },
    xAxis:{
      data:['衬衫','羊毛衫','雪纺衫','裤子','高跟鞋','袜子']
    },
    series:[
      {
        name:'销量',
        type:'bar',
        data:[5,20,36,10,10,20]
      },
    	{
        name:'销售额',
        type:'bar',
        data:[566,200,360,100,100,200]
      }
    ]
  }
  
  
  //onMounted表示页面所有的DOM元素都初始化完成了
  onMounted(() => {
    //基于准备好的DOM，初始化echarts实例
    const myChart = echarts.init(document.getElementById('main'))
    //使用制定的配置项和数据显示图表
    myChart.setOption(option);
  })
  
  
  
  
  ```

  > 必须先准备好DOM，再初始化echarts示例
  >
  > ![image-20250908112548348](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908112548348.png)

  > xAxis：横轴
  > series：纵轴



> 折线图和柱状图的区别：
> series:[
>     {
>       type:'line',
>     }
>   ]
>
> series:[
>     {
>       type:'bar',
>     }
>   ]



### 柱状图







> 修改legend前
>
> ```javascript
> legend:{
>   data:['销量']
> },
> ```
>
> ![image-20250908115805451](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908115805451.png)
>
> 修改后
>
> ```javascript
> legend:{
>   data:['销量','销售额']
> },
> ```
>
> ![image-20250908115945289](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908115945289.png)
> 
>
> 

























### 饼图











### 折线图



* 准备好dom

  ```vue
  <!--    折线图DOM-->
      <div id="line" style="width: 600px;height:400px;">
      </div>
  ```

  > id要对应

  > 引入onMounted并初始化实例

  ```javascript
  import {reactive,onMounted} from "vue"
  const option1 = {
    title: {
      text:'ECharts 折线图入门'
    },
    xAxis:{
      type:'category',
      data:['Mon','Tue','Wed','Thu','Fri','Sat','Sun']
    },
    yAxis:{
      type:'value',
    },
    series:[
      {
        data:[820,932,901,934,1290,1330,1320],
        type:'line',
        smooth:true
      }
    ]
  }
  
  
  //onMounted表示页面所有的DOM元素都初始化完成了
  onMounted(() => {
    //基于准备好的DOM，初始化echarts实例
    const myChart = echarts.init(document.getElementById('line'))
    //使用制定的配置项和数据显示图表
    myChart2.setOption(option1);
  })
  
  
  
  
  ```

  > 必须先准备好DOM，再初始化echarts示例
  >
  > ![image-20250908112548348](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908112548348.png)

> smooth:true
> 将曲线设置为光滑化处理





### 动态图表-柱状图



* 后端把横轴和纵轴的数据包装之后返回给前端
  ![image-20250908133409768](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250908133409768.png)

  **接口**

  ```java
  	@GetMapping("/barData")
  	public Result getBarData(){
          List<Employee> employeeList = employeeService.selectAll(null);
          
      }
  ```
  
  
  
  
  
  ```java
  /*
       * 返回柱状图数据
       */
      @GetMapping("/barData")
      public Result getBarData(){
  
          List<Employee> employeeList = employeeService.selectAll(null);
          //拿出部门名称
          //（虽然employee表里面没有department属性，但是selectAll采用关联查询，也查询到了departmentName并存在对象中、于是后面employee就能get到departmentName）
          Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
          //set集合是一个去重好的集合（collect成set，实现去重的效果）
  
  
          List<Long> countList = new ArrayList<>();
          //遍历，拿到各个部门的员工数量
          for(String name:departmentNameSet){
              long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(name)).count();
              countList.add(count);
          }
          //用map返回横轴和纵轴的数据
          Map<String,Object> map = new HashMap<>();
          map.put("department",departmentNameSet);//返回几个部门的名称集合，作为横轴
          map.put("count",countList);//返回每个部门的员工数量，作为纵轴
          return Result.success(map);
      }
  
  ```
  
  ![image-20250909124748816](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250909124748816.png)
  
  
  
* 前端代码

  ```javascript
  const barOption = {
    title: {
      text:'各部门员工数量'
    },
    tooltip:{},
    legend:{
      data:['人数',]
    },
    xAxis:{
      data:[]
    },
    yAxis:{},
    series:[
      {
        name:'人数',
        type:'bar',
        data:[]
      },
    ]
  }
  ```

  ```javascript
  onMounted(() => {
    //基于准备好的DOM，初始化echarts实例
    const barChart = echarts.init(document.getElementById('bar'))
  
    request.get('/barData').then(res => {
      // console.log(res.data);
      barOption.xAxis.data = res.data.department  //横轴数据
      barOption.series[0].data = res.data.count        //纵轴数据//（注意这里是series[0].data，不是series[0]）
      //使用制定的配置项和数据显示图表
      barChart.setOption(barOption);
    })
  })
  ```

  




#### 员工信息统计

##### 职位统计

![image-20250915075953489](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915075953489.png)



> 另一种方法了解一下，感觉没有qgg的方法好用
>
> 
>
>  
>
> ```
> -- 统计每一种职位对应的人数
> select job,count(*) from emp group by job
> ```
>
> ![image-20250915080324130](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915080324130.png)
>
> > case函数
> >
> > case 表达式 when 条件表达式1 then res1 when 条件表达式2 then res2
>
> > if函数
> >
> > if(条件表达式，true_value，false_value)
> >
> > ```
> > select 
> > 	if(gender = 1, '男','女')
> > 	count(*)
> > from emp group by gender
> > ```
> >
> > 
>
> ```
> select 
> 	(case job
> 	when 1 then '班主任'
> 	when 2 then '讲师'
> 	when 3 then '学工主管'
> 	when 4 then '教研主管'
> 	when 5 then '咨询师'
>     else '其他' end) pos ,
> 	count(*) num
>     from emp group by job;
> ```
>
> ![image-20250915081152792](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915081152792.png)
>
> 
>
> 
>
> ```java
> /*
>      * 返回柱状图数据
>      */
>     @GetMapping("/barData")
>     public Result getBarData(){
>      	  
>         
>     }
> ```
>
> ```java
> 	public Map<Object,Object> getEmpJobData(){
>         List<Map<String,Object>> list = empMapper.countEmpJobData();
>         List<Object> jobList  = list.stream().map(dataMap -> dataMap.get("pos")).toList();
>         List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
>         Map<Object,Object> map = new HashMap<>();
>         map.put(jobList,dataList);
>         return map
>     }
> ```
>
> ![image-20250915092139984](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250915092139984.png)







##### 性别统计

























### 动态图表-折线图



* 后端把横轴和纵轴的数据包装之后返回给前端
  接口

  ![image-20250909213103757](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250909213103757.png)

  ```java
  /*
       * 返回折线图数据
       */
      @GetMapping("/lineData")
      public Result getLineData(){
          //文章发布数量随时间变化的折线图
          //返回日期
          Date date = new Date();
          DateTime startTime = DateUtil.offsetDay(date, -7);//现在时间-7，得到七天前的时间点（作为起始时间）
          List<DateTime> dateTimeList = DateUtil.rangeToList(startTime, date, DateField.DAY_OF_YEAR);//统计一段时间(DateField.DAY_OF_YEAR是单位)
  
          //dateTime类型数组转换成(日期)格式化的字符串数组(用于横轴展示)
          List<String> dateStrList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日")).sorted(Comparator.naturalOrder()).collect(Collectors.toList());//注意这里dd是小写
          //sorted——排序
  
          List<Integer> countList = new ArrayList<>();
          for (DateTime day :dateTimeList){
              //单独处理一下，才能模糊查询进行统计
              String dayFormat = DateUtil.formatDate(day);
              //拿到每天，通过day获取当天发布数量 --(返回值:Integer)
              Integer cnt = articleService.selectCountByDate(dayFormat);
              countList.add(cnt);
          }
  
  
          //用map返回横轴和纵轴的数据
          Map<String,Object> map = new HashMap<>();
          map.put("date",dateStrList);//返回日期集合，作为横轴
          map.put("count",countList);//返回各日发布文章的数量，作为纵轴
          return Result.success(map);
      }
  ```

  

* 前端代码

  

### 动态图表-饼图

* **接口**

  

  **返回的数据结构：**

  ![image-20250910081056860](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910081056860.png)

  

* 前端代码

  





















#### 如何设置不同的颜色

1. 提前准备颜色

   ```
   itemStyle:{
   	normal:{
   		color:function(){
   			return "#" + Math.floor(Math.random() * (256*256*256 - 1)).toString(16);
   		}
   	},
   }
   ```

   ![image-20250909131917919](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250909131917919.png)

   ```javascript
     series:[
       {
         name:'人数',
         type:'bar',
         data:[],
         itemStyle:{
           normal:{
             color:function(params){
               let colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']
               console.log(params.dataIndex)
               //【用index取余来对应不同的颜色；也可以采用随机数来取颜色】
               //0取余9=0 1取余9=1 2取余9=2 3取余9=3 4取余9=4 5取余9=5 6取余9=6 7取余9=7 8取余9=8 9取余9=0 10取余9=1 11取余9=2
               return colors[params.dataIndex % colors.length]
             }
           },
         }
       },
     ]
   ```

   ```java
   //返回日期
           Date date = new Date();
           DateTime startTime = DateUtil.offsetDay(date, -7);//现在时间-7，得到七天前的时间点（作为起始时间）
           List<DateTime> dateTimeList = DateUtil.rangeToList(startTime, date, DateField.DAY_OF_YEAR);//统计一段时间(DateField.DAY_OF_YEAR是单位)
           //dateTime类型数组转换成(日期)格式化的字符串数组
           List<String> dateStrList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月DD日")).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
           //sorted——排序
   ```

   











> []代表数组
>
> {}代表对象
>
> data:[
>
> ​	{},
>
> ​	{}
>
> ]
>
> > json数组







#### 流方式代码

> * 用流的方式代替遍历，一行代码完成操作
>
> 
>
> ```java
> List<Employee> employeeList = employeeService.selectAll(null);
>         //拿出部门名称
>         //（虽然employee表里面没有department属性，但是selectAll采用关联查询，也查询到了departmentName并存在对象中、于是后面employee就能get到departmentName）
> 
> Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
> ```
>
> > 首先，通过`stream()`方法将列表转换为流；然后，使用`map(Employee::getDepartmentName)`对流中的每个元素调用`getDepartmentName()`方法，提取出部门名称，形成一个新的流；最后，通过`collect(Collectors.toSet())`将流中的元素收集到一个集合中，并确保集合中的元素是唯一的，即去重。最终，`departmentNameSet`集合中存储了所有员工所属部门的唯一名称。
> >
> >
> > 它接受一个函数作为参数（在 Java 中，这个函数通常是用方法引用或 Lambda 表达式表示的），然后对流中的每个元素应用这个函数，并生成一个新的流,它不仅限于提取属性，还可以用来对元素进行各种操作，比如类型转换、格式化、甚至是数学运算。`map()` 和 `collect()` 是紧密配合的，前者负责数据的“加工”，后者负责数据的“收集”
>
> 











# 前后端





### Nginx



**Nginx反向代理**

Ningx代理访问后端Tomcat服务器

* 作用
  避免后端接口直接暴露给前端，不安全

* ![image-20250910131958733](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910131958733.png)

  > 前端只需要访问代理服务器，不需要关心后端那些服务器的端口号；这样就能隐藏后端的细节、更加安全
  >
  >
  > 新增加服务器不需要告诉前端项目自己增加了、并告诉每一台的ip和端口号
  >
  >
  > 能实现负载均衡，接收请求之后依据目前各服务器的压力情况来决定这次的请求交给哪一台服务器处理
  >
  > 
  > 



> ![image-20250910125908427](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910125908427.png)
>
> 

1. 安装nginx并启动

   ![image-20250910130735325](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910130735325.png)
   
2. 测试访问：http://localhost:90





> ![image-20250910132912938](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910132912938.png)
>
> 
>
> 监听端口号为90
> rewrite用来进行路径重写（这里使用了正则表达式）
>
> ![image-20250910133230295](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910133230295.png)
> 



> 这部分操作工作后可能由运维或架构师来完成
>
> 
>
> 





































