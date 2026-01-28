![image-20250721144734418](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721144734418.png) 

![image-20250721144921081](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721144921081.png)

![image-20250721145025869](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721145025869.png)

#### 快速入门

> 本质就是基于数据渲染出用户看到的页面

![image-20250721192516318](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721192516318.png)





```html

<body>
    <div id="app">
        <h1>
            {{message}}
        </h1>//插值表达式
    </div>
    <script type="module">//使用模块化的js
    //引入vue模块
        import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
        createApp({
            data() {
              const message = ref('Hello Vue!')
              return {
                message:'hello vue'
              }
            }
        }).mount('#app')//接管id为app的div实例
        
        
    </script>
    
</body>









```

![image-20250721193434193](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721193434193.png)



#### vue常用指令

![image-20250721194012648](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721194012648.png)

> 这些指令是作用在<u>html标签</u>上的特殊属性，需要加在html标签上

#####  v-for![image-20250721194147370](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721194147370.png)

![image-20250721194354260](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721194354260.png)



![image-20250721195422352](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721195422352.png)

##### v-bind(:)

![image-20250721200404425](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721200404425.png)

> 解决插值表达式不能出现在标签内部的问题
>
> 为标签动态绑定属性值

![image-20250721200240319](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721200240319.png)



##### v-if&v-show

![image-20250721200734035](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721200734035.png)



![image-20250721202056252](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721202056252.png)

> true则显示 这个元素 

![image-20250721202550646](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721202550646.png)

![image-20250721202612525](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721202612525.png)



 ![image-20250721202748142](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721202748142.png)

![image-20250721202853406](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721202853406.png)

##### v-model

![image-20250721203040103](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721203040103.png)







##### v-on

![image-20250721211710615](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721211710615.png)



![image-20250721211730113](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721211730113.png)