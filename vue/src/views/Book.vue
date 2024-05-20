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
      <el-popconfirm title="Confirm Take down?" @confirm="deleteBatch" v-if="user.role == 2">
        <template #reference>
          <el-button type="danger" size="mini">Multi take down</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!-- 数据字段-->
    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="isbn" label="ISBN" sortable/>
      <el-table-column prop="name" label="Book name"/>
      <el-table-column prop="price" label="Price" sortable/>
      <el-table-column prop="author" label="author"/>
      <el-table-column prop="publisher" label="Publisher"/>
<!--      <el-table-column prop="createTime" label="Date of publication" sortable/>-->
      <el-table-column prop="location" label="Location"/>
      <el-table-column prop="borrownum" label="Borrowed time" sortable v-if="user.role == 2"/>

<!--      <el-table-column prop="leftNumber" label="leftNumber">-->
<!--        <template v-slot="scope">-->
<!--          <el-tag v-if="scope.row.leftNumber == 1" type="warning">Borrowed</el-tag>-->
<!--          <el-tag v-else type="success">Not borrowed</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column prop="totalNumber" label="Total" sortable/>
      <el-table-column prop="leftNumber" label="Remaining" sortable/>
      <el-table-column fixed="right" label="Operation">
        <template v-slot="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role == 2">Modify</el-button>
          <el-popconfirm title="Confirm Take down?" @confirm="handleDelete(scope.row.id)" v-if="user.role == 2">
            <template #reference>
              <el-button type="danger" size="mini">Take down</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini" @click="handlelend(scope.row.id,scope.row.isbn,scope.row.name,scope.row.borrownum,scope.row.leftNumber)"
                     v-if="user.role == 3 " :disabled="scope.row.leftNumber == 0">borrow
          </el-button>
<!--          <el-popconfirm title="Confirm Return?"-->
<!--                         @confirm="handlereturn(scope.row.id,scope.row.isbn,scope.row.borrownum)" v-if="user.role == 3 "-->
<!--                         :disabled="scope.row.status == 1">-->
<!--            <template #reference>-->
<!--              <el-button type="danger" size="mini"-->
<!--                         :disabled="(this.isbnArray.indexOf(scope.row.isbn)) == -1 ||scope.row.status == 1">Return-->
<!--              </el-button>-->
<!--            </template>-->
<!--          </el-popconfirm>-->
        </template>
      </el-table-column>
    </el-table>
    <!--测试,通知对话框-->
    <el-dialog
        v-model="dialogVisible3"
        v-if="numOfOutDataBook!=0"
        title="Overdue status"
        width="50%"
        :before-close="handleClose"
    >
      <el-table :data="outDateBook" style="width: 100%">
        <el-table-column prop="isbn" label="ISBN"/>
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

      <el-dialog v-model="dialogVisible" title="Go onto book " width="30%">
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
          <el-form-item label="location">
            <el-input style="width: 80%" v-model="form.location"></el-input>
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
    deleteBatch() {
      if (!this.ids.length) {
        ElMessage.warning("Please select data!")
        return
      }
      //  一个小优化，直接发送这个数组，而不是一个一个的提交Take down
      request.post("/book/deleteBatch", this.ids).then(res => {
        if (res.code === '0') {
          ElMessage.success("Multi take down success")
          this.load()
        } else {
          ElMessage.error(res.msg)
        }
      })
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
      //
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
          // TODO 判断过期逻辑
          console.log(res)
          this.bookData = res.data.records
          this.number = this.bookData.length;
          var nowDate = new Date();
          for (let i = 0; i < this.number; i++) {
            this.isbnArray[i] = this.bookData[i].isbn;
            let dDate = new Date(this.bookData[i].deadtime);
            if (dDate < nowDate && this.bookData[i].status == '0') {
              this.outDateBook[this.numOfOutDataBook] = {
                isbn: this.bookData[i].isbn,
                bookName: this.bookData[i].bookName,
                deadtime: this.bookData[i].deadtime,
                lendtime: this.bookData[i].lendtime,
              };
              this.numOfOutDataBook = this.numOfOutDataBook + 1;
            }
          }
          // 后端同步
          let fineForm = [];
          for (let i = 0; i < this.numOfOutDataBook; i++){
            fineForm[i] = {
              // id: 100,
              isbn : this.outDateBook[i].isbn,
              bookname:this.outDateBook[i].bookname,
              readerid : this.user.id,
              readername : this.user.username,
              number : 10
            }
          }
          console.log(fineForm)

          request.post("/fine/add", fineForm).then(res => {
            console.log(res)
          })

          console.log("okokoko  in load():" + this.numOfOutDataBook);
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
    handlereturn(id, isbn, bn) {
      this.form.status = "1"
      this.form.id = id
      request.put("/book", this.form).then(res => {
        console.log(res)
        if (res.code == 0) {
          ElMessage({
            message: 'Return success',
            type: 'success',
          })
        } else {
          ElMessage.error(res.msg)
        }
        //
        this.form3.isbn = isbn
        this.form3.readerId = this.user.id
        let endDate = moment(new Date()).format("yyyy-MM-DD HH:mm:ss")
        this.form3.returnTime = endDate
        this.form3.status = "1"
        console.log(bn)
        this.form3.borrownum = bn
        request.put("/LendRecord1/", this.form3).then(res => {
          console.log(res)
          let form3 = {};
          form3.isbn = isbn;
          form3.bookName = name;
          form3.nickName = this.user.username;
          form3.id = this.user.id;
          form3.lendtime = endDate;
          form3.deadtime = endDate;
          form3.prolong = 1;
          request.post("/bookwithuser/deleteRecord", form3).then(res => {
            console.log(res)
            this.load()
          })

        })
        //
      })
    },

    handlelend(id, isbn, name, bn,leftNumber) {

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

      this.form.status = "0"
      this.form.id = id
      this.form.borrownum = bn + 1
      this.form.leftNumber = leftNumber - 1
      console.log(bn)
      request.put("/book", this.form).then(res => {
        console.log(res)
        if (res.code == 0) {
          ElMessage({
            message: 'Borrow success',
            type: 'success',
          })
        } else {
          ElMessage.error(res.msg)
        }
      })

      this.form2.status = "0"
      this.form2.isbn = isbn
      this.form2.bookname = name
      this.form2.readerId = this.user.id
      this.form2.borrownum = bn + 1
      console.log(this.form2.borrownum)
      console.log(this.user)
      let startDate = moment(new Date()).format("yyyy-MM-DD HH:mm:ss");
      this.form2.lendTime = startDate
      console.log(this.user)
      request.post("/LendRecord", this.form2).then(res => {
        console.log(res)
        this.load();

      })
      let form3 = {};
      form3.isbn = isbn;
      form3.bookName = name;
      form3.nickName = this.user.username;
      form3.readerId = this.user.id;
      form3.lendtime = startDate;
      let nowDate = new Date(startDate);
      nowDate.setDate(nowDate.getDate() + 30);
      form3.deadtime = moment(nowDate).format("yyyy-MM-DD HH:mm:ss");
      form3.prolong = 1;
      let now = moment();
      let daysDifference = now.diff(form3.deadtime, 'days');

// 注意：如果daysDifference为负数，表示还未到期；正数表示已过期 TODO 测试
      console.log("#############过期了", Math.abs(daysDifference), "天")
      request.post("/bookwithuser/insertNew", form3).then(res => {
        console.log(res)
        this.load()
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
      //ES6语法
      //地址,但是？IP与端口？+请求参数
      // this.form?这是自动保存在form中的，虽然显示时没有使用，但是这个对象中是有它的
      if (this.form.id) {
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
    // formatter(row) {:formatter="formatter"
    //   return row.address
    // },

    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
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
  },
  data() {
    return {
      phone: '',
      flag: '',
      form: {},
      form2: {},
      form3: {},
      dialogVisible: false,
      dialogVisible2: false,
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
    }
  },
}
</script>
