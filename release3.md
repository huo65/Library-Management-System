## 后端

### 任务1：数据库修改

> 修改前先备份一下，小心出现问题数据丢失

具体每本书 **specificBook：** id（主键（要求8位纯数字00000000）  isbn status  location  name（冗余存储提高效率）

每种书 **book：** isbn（修改为主键） name  author  publisher left_number total_number

price可以删掉

条形码信息 **codeInfo：** specificBook的id（主键）+参照原book（包括数据）（去掉borrownum leftnumber totalnumber

添加几本在book表中没有的书

### 任务2：加书逻辑修改

> 后端将图片解析封装成工具类，参数为前端传回的base64编码，返回值是codeInfo表中对应id的信息
>
> 做一个接口，根据id查出信息返回，便于前端回填
>
> 如此扫码对于后端是透明的

接收参数：codeInfo/bookAddVO

- 根据ISBN，存在则book对应数量+1，不存在则插入一条新记录，数量为1
- 根据id，插入specificBook表（原则上不会重复id）

### 任务3：查书

- 封装一个searchBookDTO：book表字段+List< specificBook >

前端展示直接放出每种书的信息，点击详情放出具体每本书信息

### 任务4：还书

> 注意两个表信息的同步

- 用户端，彻底不需要还书功能；管理端不需要还书按钮
- 修改所有的借书逻辑，借书的bookid都改为specificBook的id
- 提供一个查记录接口，根据**specificBook**的id查到未还记录。加入没有返回信息显示该书已经入库
- 还书接口：
  - 接收参数：List< specificBook>(前端扫码构造出一个列表，批量还书)
  - 批量还书（修改两个表（book with user； record）的状态），同时同步book信息（数量）

## 前端

### 任务1：加书

- 对于上架书的button，点击后在填书信息页加一个上传图片的button，上传条形码后向后端请求条形码解析，后端返回codeInfo的信息，进行对应位置回填
- 其他逻辑照常

### 任务2：查书

- 原表单字段改为book表对应字段，操作button改为详情
- 点击详情后弹窗展示具体图书信息，后面加上借书按钮

### 任务3：还书

- 仿照其他表格views做一个还书界面，初始没有如何数据，上面加一个扫描按钮
- 点击后上传对应条形码，后端请求查出对应**specificBook**id的书的借书记录，展示在列表中

- 在上面加一个还书按钮，点击后发送现有表单中的所有数据给后端还书接口