![image-20250720170814225](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720170814225.png)

> 不用编译、可以直接运行



# 1. js核心语法





![image-20250720220753298](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720220753298.png)

> 定义在script标签，在代码中可以定义任意数量的script标签，但是一般建议放在body元素的底部（避免影响页面的显示速度，减少语法报错）

> 每行结尾的分号可有可无，建议都加上

#### 变量

![image-20250720221552932](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720221552932.png)

> 弱类型语言，不用指定类型

![image-20250720222047722](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720222047722.png)

#### 数据类型

![image-20250720222227813](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720222227813.png)

> 整数和小数都是number

![image-20250720222729946](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720222729946.png)

![ ](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250720223009928.png)

#### 函数



![image-20250721073033717](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721073033717.png) 

> 不用指定返回值类型（弱类型语言）

![image-20250721114436570](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721114436570.png)

```
function add(a,b){
	return a+b;
}
```

```
let add = function(a,b){
	return a+b;
}
let res = add(100,200);
```

```
let add = (a,b) => {
	return a+b;
}
let res = add(100,200);
```

#### 对象

![image-20250721115127562](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721115127562.png)

> 对象中的函数不要使用箭头函数
> ![image-20250721115339068](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721115339068.png)

![image-20250721122511429](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721122511429.png)

#### json

![image-20250721122817534](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721122817534.png)

> js对象和json只是看起来一样，本质不一样，json是字符串

**前后端交互使用**

```
let person = {
	name:'itcast',
	age:18,
	gender:'男'
}
alert(JSON.stringify(person));//js对象->json
```

```
let personJson = '{"name":"heima","age":18}';
alert(JSON.parse(personJson).name);//转成对象，进一步获取name
```

![image-20250721123317819](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721123317819.png)



#### DOM

![image-20250721123632621](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721123632621.png)

 ![image-20250721123823642](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721123823642.png)

```
<body>
	<h1 id="title1">11111</h1>
	<h1>22222</h1>
	<h1>33333</h1>
	<script>
	let h1 = document.querySelector('#title1');
	h1.innerHTML = '修改后的文本内容'
	</script>
</body>


```

 ![image-20250721124809194](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721124809194.png)

![image-20250721125100226](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721125100226.png)





# 2. 事件监听

![image-20250721125158305](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721125158305.png)

![image-20250721125314731](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721125314731.png)



```
document.querySelector('#btn').addEventListener('click',()=>{
	alert('试试就试试');
})

```

![image-20250721141216914](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721141216914.png)

 

 

**<u>鼠标移入，表格行变色实现</u>**

![image-20250721141734169](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721141734169.png)

#### 常见事件

![image-20250721141919038](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721141919038.png)

#### js模块化

xx.html

```html
...
<body>
	<script src="./js/eventDemo.js" type="module"></script>
    //注意用的是module模块化的写法
    
    
</body>
...
```

eventDemo.js

```javascript
import {printLog} from "./utils.js";

//click：鼠标点击事件
document.querySelector('#b2').addEventListener('click',()=>{
    console.log("我被点击了...");
})

document.querySelector('#last').addEventListener('mouseenter',()=>{
    console.log("鼠标移入了...");
})


```

utils.js

```javascript
export function pringLog(msg){
	console.log(msg);
}
//暴露（公开）这个函数






```











