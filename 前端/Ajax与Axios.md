

#### 介绍

![image-20250721212251568](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721212251568.png)

![image-20250721212534254](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721212534254.png)

![image-20250721213048883](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721213048883.png)



> 请求发送到服务器端，服务器处理完毕之后，回来之后所调用的函数就叫做成功回调函数
>
> 



```html
...
<body>
   <script src="js/axios.js">
    </script> 
    <script>
    	document.querySelector('#btnGet').addEventListener('click',() => {
            //axios发起异步请求
            axios({
                url:'https://mock.apifox.cn/ml/3038103-0-default/emps/list'
                method:'GET'
            }).then((result) => {
                console.log(result.data);
            }).catch((err) => {
                console.log(err);
            })
            
        })
    </script>
</body>

...
```

![image-20250721221435963](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721221435963.png)

```html
...
<body>
   <script src="js/axios.js">
    </script> 
    <script>
    	document.querySelector('#btnGet').addEventListener('click',() => {
            //axios发起异步请求
            axios.get('https://mock.apifox.cn/ml/3038103-0-default/emps/list').then((result) => {
                console.log(result.data);
            }).catch((err) => {
                console.log(err);
            })
            
        })
    </script>
</body>

...



```

![image-20250721221805695](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250721221805695.png)



 



```html
<body>
    <script type="module">
    	createApp({
            data(){
                return {
                    searchForm: {
                        name:'',
                        gender:'',
                        job:''
                    },
                    empList:[]
                }
            },
        })
        
        methods:{
            search(){
                //发送ajax请求，获取数据
                axios.get('https://web-server.itheima.net/emps/list?name=${this.searchForm.name}&gender=${this.searchForm.gender}&job=${this.searchForm.job}')
                
            }
        },
        clear(){
            //清空表单数据
            this.searchForm = {name:'',gender:'',job:''}
            this.search()
        }
            
    </script>
    
    
    
</body>



```

![image-20250722094256958](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094256958.png)

保证search函数从上往下执行，增加可读性和可维护性



![image-20250722094425600](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094425600.png)



> 不需要在写成功回调函数

#### 生命周期与钩子函数

![image-20250722094631971](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094631971.png)

![image-20250722094713478](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094713478.png)

![image-20250722094827708](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094827708.png)

![image-20250722094917073](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722094917073.png)

![image-20250722095035126](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722095035126.png)

![image-20250722095118956](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722095118956.png)















