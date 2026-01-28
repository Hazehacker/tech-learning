#### 应用场景

* 解析方法返回的json字符串、获取想要的数值





## 操作

* 使用`org.json`库中的`JSONObject`类

  ```
  //json值样例
  {
    "status": 0,
    "result": {
      "location": {
        "lng": 116.30787799999993,
        "lat": 40.05702706489032
      },
      "precise": 1,
      "confidence": 100,
      "comprehension": 100,
      "level": ""
    }
  }
  ```

  

  ```
  import org.json.JSONObject;
  
  public class JSONParserExample {
  
      public static void main(String[] args) {
          // 示例JSON字符串（模拟从百度地图API返回的结果）
          String jsonString = "{\"status\":0,\"message\":\"ok\",\"result\":{\"location\":{\"lng\":116.313429,\"lat\":39.992215},\"precise\":5,\"confidence\":45,\"comprehension\":-1,\"level\":\"province\"}}";
  
          // 【创建JSONObject对象】
          JSONObject jsonResponse = JSONObject.parseObject(jsonString);
  
          // 解析JSON以获取所需的数值
          // 【检查状态码】
          if (jsonResponse.has("status") && jsonResponse.getInt("status") == 0) {
              // 获取"result"对象
              JSONObject result = jsonResponse.getJSONObject("result");
  
              // 获取"location"对象
              JSONObject location = result.getJSONObject("location");
  
              // 【获取数值】
              // 获取经度和纬度
              double longitude = location.getDouble("lng");
              double latitude = location.getDouble("lat");
  
              System.out.println("经度: " + longitude);
              System.out.println("纬度: " + latitude);
          } else {
              System.out.println("请求失败或数据格式不正确");
          }
      }
  }
  ```

  > 如果你的项目使用Maven管理依赖且依赖传递（阿里云oss携带有这个依赖）没有传递到的话，需要在`pom.xml`中添加`org.json`库的依赖：
  >
  > xml
  >
  > 复制
  >
  > ```xml
  > <dependency>
  >     <groupId>org.json</groupId>
  >     <artifactId>json</artifactId>
  >     <version>20211205</version>
  > </dependency>
  > ```
  >
  > 通过这种方式，你可以轻松解析JSON字符串并获取所需的数值。根据实际返回的JSON结构，调整字段名称和解析逻辑即可。



















