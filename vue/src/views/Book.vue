<template>
  <div class="home" style="padding: 10px">

    <!-- 搜索-->
    <div style="margin: 10px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="ISBN">
          <el-input v-model="search1" placeholder="Please enter ISBN" clearable>
            <template #prefix>
              <el-icon class="el-input__icon">
                <search/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="Book name">
          <el-input v-model="search2" placeholder="Please enter Book name" clearable>
            <template #prefix>
              <el-icon class="el-input__icon">
                <search/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="author">
          <el-input v-model="search3" placeholder="Please enter author" clearable>
            <template #prefix>
              <el-icon class="el-input__icon">
                <search/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">
            <svg-icon iconClass="search"/>
            Search
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">Reset</el-button>
        </el-form-item>
<!--        过期弹窗提醒-->
        <el-form-item style="float: right" v-if="numOfOutDataBook!=0">
          <el-popconfirm
              confirm-button-text="Confirm"
              cancel-button-text="Cancel"
              :icon="InfoFilled"
              icon-color="red"
              title="Some of books you borrowed have been overdue, please return them as soon as possible."
              @confirm="toLook"
          >
            <template #reference>
              <el-button type="warning">Overdue notice</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮-->
    <div style="margin: 10px 0;">
      <el-button type="primary" @click="add" v-if="user.role == 2">Go onto</el-button>
    </div>
    <div style="margin: 10px 0;" >
      <el-button type="primary" @click = "scanOpen" v-if="user.role == 3"> Scan Book </el-button>
    </div>
    <!-- 数据字段-->
    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="isbn" label="ISBN" sortable />
      <el-table-column prop="name" label="Book name"/>
      <el-table-column prop="author" label="author" width="80%"/>
      <el-table-column prop="publisher" label="Publisher"/>
      <el-table-column prop="borrownum" label="Borrowed time" sortable v-if="user.role == 2 " width="90%"/>
      <el-table-column prop="totalNumber" label="Total" sortable width="90%"/>
      <el-table-column prop="leftNumber" label="Remaining" sortable />
      <el-table-column fixed="right" label="Operation">
        <template v-slot="scope">

<!--          用户查看详情-->
          <el-button size="mini" @click="handleSpecific(scope.row.specificbooks)"
                     >Specific
          </el-button>

        </template>
      </el-table-column>
    </el-table>
    <!--过期提醒-->
    <el-dialog
        v-model="dialogVisible3"
        v-if="numOfOutDataBook!=0"
        title="Overdue status"
        width="50%"
        :before-close="handleClose"
    >
      <el-table :data="outDateBook" style="width: 100%">
        <el-table-column prop="bookId" label="ISBN"/>
        <el-table-column prop="bookName" label="Book name"/>
        <el-table-column prop="lendtime" label="Borrowing date"/>
        <el-table-column prop="deadtime" label="Latest return date"/>
      </el-table>

      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible3 = false"
        >Confirm </el-button>
      </span>
      </template>
    </el-dialog>
    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 12]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>

<!--      管理员添加-->
      <el-dialog v-model="dialogVisible" title="Go onto book " width="30%">
        <div>
          <el-upload
              class="upload-demo"
              action="javascript:void(0);"
              :before-upload="beforeUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
          <img v-if="imageUrl" :src="imageUrl" alt="Preview" style="max-width: 300px;">

        </div>

        <el-form :model="form" label-width="120px">
          <el-form-item label="BookId">
            <el-input style="width: 80%" v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item label="ISBN">
            <el-input style="width: 80%" v-model="form.isbn"></el-input>
          </el-form-item>
          <el-form-item label="Book name">
            <el-input style="width: 80%" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="author">
            <el-input style="width: 80%" v-model="form.author"></el-input>
          </el-form-item>
          <el-form-item label="Publisher">
            <el-input style="width: 80%" v-model="form.publisher"></el-input>
          </el-form-item>
<!--          自己填写-->
          <el-form-item label="location">
            <el-input style="width: 80%" v-model="form.location"></el-input>
          </el-form-item>
          <el-form-item label="Date publication">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable
                              v-model="form.createTime"></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
        </template>
      </el-dialog>
<!--管理员编辑-->
      <el-dialog v-model="dialogVisible2" title="Modify book info" width="30%">
        <el-form :model="form" label-width="120px">

          <el-form-item label="ISBN">
            <el-input style="width: 80%" v-model="form.isbn"></el-input>
          </el-form-item>
          <el-form-item label="Book name">
            <el-input style="width: 80%" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="Price">
            <el-input style="width: 80%" v-model="form.price"></el-input>
          </el-form-item>
          <el-form-item label="author">
            <el-input style="width: 80%" v-model="form.author"></el-input>
          </el-form-item>
          <el-form-item label="Publisher">
            <el-input style="width: 80%" v-model="form.publisher"></el-input>
          </el-form-item>
          <el-form-item label="Date of publication">
            <div>
              <el-date-picker value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable
                              v-model="form.createTime"></el-date-picker>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
        </template>
      </el-dialog>
<!--      图书详情展示-->
      <el-dialog v-model="bookdialogVisible" title="Specific Book info" width="50%">
        <el-table :data="bookTableData" stripe border="true" >

          <el-table-column prop="id" label="Book ID" sortable />
          <el-table-column prop="isbn" label="ISBN" sortable />
          <el-table-column prop="name" label="Book name"/>
          <el-table-column prop="location" label="Location"/>
          <el-table-column fixed="right" label="Operation">
            <template v-slot="scope">
<!--              <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role == 2">Modify</el-button>-->
              <el-popconfirm title="Confirm Take down?" @confirm="handleDelete(scope.row.id)" v-if="user.role == 2">
                <template #reference>
                  <el-button type="danger" size="mini">Take down</el-button>
                </template>
              </el-popconfirm>
              <!--          用户借书-->
              <el-button size="mini" @click="handlelend(scope.row.id,scope.row.isbn,scope.row.name,scope.row.status,scope.row.location)"
                         v-if="user.role == 3 " :disabled="scope.row.status == 0">borrow
              </el-button>

            </template>
          </el-table-column>
        </el-table>
        <template #footer>
      <span class="dialog-footer">
<!--        <el-button @click="dialogVisible = false ">Cancel</el-button>-->
<!--        <el-button type="primary" @click="save">Confirm</el-button>-->
      </span>
        </template>
      </el-dialog>


      <el-dialog v-model="FindBookDialogVisible" title="Book info" width="50%">
        <div>
          <el-upload
              class="upload-demo"
              action="javascript:void(0);"
              :before-upload="beforeUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
          <img v-if="imageUrl" :src="imageUrl" alt="Preview" style="max-width: 300px;">

        </div>

        <el-table :data="scanForm" stripe border="true" >

          <el-table-column prop="id" label="Book ID" sortable />
          <el-table-column prop="isbn" label="ISBN" sortable />
          <el-table-column prop="name" label="Book name"/>
          <el-table-column fixed="right" label="Operation">
            <template v-slot="scope">
              <!--          用户借书-->
              <el-button size="mini" @click="handlelend(scope.row.id,scope.row.isbn,scope.row.name,scope.row.status,scope.row.location)"
                         v-if="user.role == 3 " :disabled="scope.row.status == 0">borrow
              </el-button>

            </template>
          </el-table-column>
        </el-table>

      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "../utils/request";
import {ElMessage} from "element-plus";
import moment from "moment";
import router from "@/router";

export default {
  created() {
    let userJson = sessionStorage.getItem("user")
    if (!userJson) {
      router.push("/login")
    }
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    let user = JSON.parse(sessionStorage.getItem("user"))
    this.phone = user.phone
    this.load()
  },
  name: 'Book',
  methods: {
    // (this.isbnArray.indexOf(scope.row.isbn)) == -1
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)
    },
    load() {
      this.numOfOutDataBook = 0;
      this.outDateBook = [];
      request.get("/book", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search1: this.search1,
          search2: this.search2,
          search3: this.search3,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
      // 当用户登录，查用户视图记录判断过期
      if (this.user.role == 3) {
        request.get("/bookwithuser", {
          params: {
            pageNum: "1",
            pageSize: this.total,
            search1: "",
            search2: "",
            search3: this.user.id,
          }
        }).then(res => {
          //  判断过期逻辑
          console.log(res)
          this.bookData = res.data.records
          this.number = this.bookData.length;
          var nowDate = new Date();
          for (let i = 0; i < this.number; i++) {
            // this.isbnArray[i] = this.bookData[i].bookId;
            let dDate = new Date(this.bookData[i].deadtime);
            if (dDate < nowDate && this.bookData[i].status == '0') {
              this.outDateBook[this.numOfOutDataBook] = {
                bookId: this.bookData[i].bookId,
                bookName: this.bookData[i].bookName,
                deadtime: this.bookData[i].deadtime,
                lendtime: this.bookData[i].lendtime,
                buID : this.bookData[i].id
              };
              this.numOfOutDataBook = this.numOfOutDataBook + 1;
            }
          }
          // 后端同步
          let fineForm = [];
          for (let i = 0; i < this.numOfOutDataBook; i++){
            let now = moment();
            let daysDifference = now.diff(this.outDateBook[i].deadtime, 'days');
            fineForm[i] = {
              // id: 100,
              bookId : this.outDateBook[i].bookId,
              bookname:this.outDateBook[i].bookName,
              readerid : this.user.id,
              readername : this.user.username,
              number : daysDifference,
              status: 0,
              buID: this.outDateBook[i].buID,
            }
          }
          request.post("/fine/add", fineForm).then(res => {
            console.log(res)
          })

        })
      }
      //判断是否具有borrow权力
      request.get("/user/alow/" + this.user.id).then(res => {
        if (res.code == 0) {
          this.flag = true
        } else {
          this.flag = false
        }
      })
    },
    clear() {
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },
    handleSpecific(specificbooks){
      this.bookdialogVisible = true;
      this.bookTableData = specificbooks;
    } ,
    // 图书修改
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
    handleDelete(id) {
      request.delete("book/" + id).then(res => {
        console.log(res)
        if (res.code == 0) {
          ElMessage.success("Take down success")
        } else
          ElMessage.error(res.msg)
        this.load()
      })
    },

    handlelend(id, isbn, name,status, location) {

      if (this.phone == null) {
        ElMessage.error("Borrow failed! Please complete your user profile!")
        this.$router.push("/person")//跳转个人info界面
        return;
      }

      if (this.number == 50) {
        ElMessage.warning("You can't borrow more books")
        return;
      }
      // 未还书权限限制
      if (this.numOfOutDataBook != 0) {
        ElMessage.warning("You can't borrow more books before returning overdue books")
        return;
      }

      if (this.flag == false) {
        ElMessage({
          message: 'No permission, please ask admin to authorise',
          type: 'error',
        })
        return;
      }

      let specificbook = {};
      specificbook.id = id;
      specificbook.isbn = isbn;
      specificbook.name = name
      specificbook.status = status;
      specificbook.location = location;
      console.log(specificbook)
      request.post("/specificbook/borrow", specificbook).then(res => {
        console.log(res)
        if (res.code == 0) {

          // 插入两表记录
          this.form2.bookId = id;
          this.form2.readerId = this.user.id;
          this.form2.isbn = isbn;
          this.form2.bookname = name;
          let startDate = moment(new Date()).format("yyyy-MM-DD HH:mm:ss");
          this.form2.lendTime = startDate;
          this.form2.returnTime = null;
          this.form2.status = "0"
          console.log(this.user)
          request.post("/LendRecord", this.form2).then(res => {
            console.log(res)
            this.load();
          })
          let form3 = {};
          form3.readerId = this.user.id;
          form3.bookId = id;
          form3.isbn = isbn;
          form3.bookName = name;
          form3.nickName = this.user.username;
          form3.lendtime = startDate;
          let nowDate = new Date(startDate);
          nowDate.setDate(nowDate.getDate() + 30);
          form3.deadtime = moment(nowDate).format("yyyy-MM-DD HH:mm:ss");
          form3.prolong = 1;
          this.form2.status = 0;
          request.post("/bookwithuser/insertNew", form3).then(res => {
            console.log(res)
            this.bookdialogVisible = false;
            this.FindBookDialogVisible = false;
            this.load()
          })

          ElMessage({
            message: 'Borrow success',
            type: 'success',
          })
        } else {
          ElMessage.error(res.msg)
        }
      })


    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    scanOpen(){
      this.FindBookDialogVisible= true
      this.scanForm = [];
    },

    // 添加或者更新
    save() {
      //ES6语法
      //地址,但是？IP与端口？+请求参数
      // this.form?这是自动保存在form中的，虽然显示时没有使用，但是这个对象中是有它的
      if (this.form.id == -1) {
        request.put("/book", this.form).then(res => {
          console.log(res)
          if (res.code == 0) {
            ElMessage({
              message: 'Modify book info success',
              type: 'success',
            })
          } else {
            ElMessage.error(res.msg)
          }

          this.load()
          this.dialogVisible2 = false
        })
      } else {
        this.form.borrownum = 0
        this.form.status = 1
        request.post("/book", this.form).then(res => {
          console.log(res)
          if (res.code == 0) {
            ElMessage.success('Go onto book success')
          } else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible = false
        })
      }

    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    toLook() {
      this.dialogVisible3 = true;
    },
    beforeUpload(file) {
      const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt500K = file.size / 1024 < 5000;

      if (!isJPGorPNG) {
        console.error('上传图片只能是 JPG/PNG 格式!');
      }
      if (!isLt500K) {
        console.error('上传图片大小不能超过 500KB!');
      }

      if (isJPGorPNG && isLt500K) {
        // 将图片转换为base64
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.imageUrl = reader.result; // 用于预览
          // 发送请求
          this.sendToBackend(reader.result);
        };
        return false; // 阻止默认上传行为
      }
    },
    async sendToBackend(imageBase64) {
      try {
        // 注意：实际开发中应使用POST请求，并且可能需要调整后端接口以接收base64字符串
        request.post("barCode/getId",imageBase64).then(res=>{
          if(res.code == 0)
          {
            let value = res.data
            console.log(value)
            this.form = value;
            this.scanForm[0] = value;
            console.log(res)
          }
          else {ElMessage.error("错误")}
        })
        // 这里可以处理后端返回的数据
      } catch (error) {
        console.error('请求错误:', error);
      }
    },
  },
  // 上一个
  data() {
    return {
      phone: '',
      flag: '',
      form: {},
      form2: {},
      form3: {},
      dialogVisible: false,
      dialogVisible2: false,
      bookdialogVisible:false,
      scanForm:[],
      FindBookDialogVisible:false,
      bookTableData:{},
      search1: '',
      search2: '',
      search3: '',
      total: 10,
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      user: {},
      number: 0,
      bookData: [],
      isbnArray: [],
      outDateBook: [],
      numOfOutDataBook: 0,
      dialogVisible3: true,
      imageUrl: '', // 用于预览的图片base64编码
    }
  },
}
</script>
<style scoped src="..\..\dist\css\responsive_ui.css"></style>
