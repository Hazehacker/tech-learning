**用于java程序中操作excel表格**

![image-20251011114644691](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251011114644691.png)

![image-20251011114702318](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251011114702318.png)

> ![image-20251011114717969](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251011114717969.png)
>
> ![image-20251011114727843](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251011114727843.png)
>
> 
>
> 

* 创建新的excel文件

  > ```
  > // 在内存中创建
  > XSSFWorkbook excel = new XSSWorkbook(); 
  > ```
  >
  > 

* 文件中创建新标签页

  > ```
  > XSSSheet sheet = excel.createSheet("info");
  > ```

* 创建行

  > ```
  > sheet.createRow(0);//第一行
  > ```

* 创建单元格

  > ```
  > XSSFCell cell = row.createCell(0);
  > ```
  >
  > 

* 单元格里面可以写入内容

  > ```
  > cell.setCellValue("姓名");
  > ```

* 把内存的excel文件写入磁盘（）输出流

  > ```
  > FileOutputStream out = new FileOutputStream(new File("D:\\info.xlsx"))
  > excel.write(out);
  > 
  > out.close();
  > excel.close();
  > ```

> #### 模版
>
> 在项目中，要导出的报表格式往往是复杂的，不会自己用POI创建，
> 一般提前准备一个表格模版文件，直接读取这个模版文件
>
> 然后写入数据即可



实战























































