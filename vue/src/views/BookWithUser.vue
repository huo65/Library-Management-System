<template>
  <div class="home" style ="padding: 10px">
    <!-- 搜索-->
    <div style="margin: 10px 0;">

      <el-form inline="true" size="small" >
        <el-form-item label="ISBN" >
          <el-input v-model="search1" placeholder="Please enter ISBN"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Book name" >
          <el-input v-model="search2" placeholder="Please enter Book name"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >

        <el-form-item label="Borrower" v-if="user.role == 1">
          <el-input v-model="search3" placeholder="Please enter Borrower name"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据字段-->
    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">

      <el-table-column prop="isbn" label="ISBN"  />
      <el-table-column prop="bookName" label="Book name" />
      <el-table-column prop="nickName" label="Borrower" />
      <el-table-column prop="lendtime" label="Borrowing date" />
      <el-table-column prop="deadtime" label="Latest return date" />
      <el-table-column prop="prolong" label="Renewable time" />

      <el-table-column fixed="right" label="Operation" >
        <template v-slot="scope">
          <!--          <el-button  size="mini" @click ="handleEdit(scope.row)" v-if="user.role == 1">Modify </el-button>-->
          <div v-if="scope.row.status == 1">
            <el-tag v-if="scope.row.status == 1" type="warning">returned</el-tag>
          </div>
          <div v-else-if = "scope.row.status == 0">

          <el-popconfirm title="Confirm renew(30 days)?" @confirm="handlereProlong(scope.row)" v-if="user.role == 3" :disabled="scope.row.prolong == 0">
            <template #reference>
              <el-button type="danger" size="mini" :disabled="scope.row.prolong == 0" >renew</el-button>
            </template>
          </el-popconfirm>
          </div>
          <div v-else>
            <el-tag  type="info">Applying</el-tag>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>

      <el-dialog v-model="dialogVisible2" title="Modify borrowing info" width="30%">
        <el-form :model="form" label-width="120px">

          <el-form-item label="ISBN">
            <el-input style="width: 80%" v-model="form.isbn"></el-input>
          </el-form-item>
          <el-form-item label="Book name">
            <el-input style="width: 80%" v-model="form.bookName"></el-input>
          </el-form-item>
          <el-form-item label="Borrower">
            <el-input style="width: 80%" v-model="form.nickName"></el-input>
          </el-form-item>
          <el-form-item label="renew次数">
            <el-input style="width: 80%" v-model="form.prolong"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">Cancel</el-button>
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
  created(){
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
    let userJson = sessionStorage.getItem("user")
    if(!userJson)
    {
      router.push("/login")
    }
    this.load()
  },
  name: 'bookwithuser',
  methods: {

    handleSelectionChange(val){
      this.forms = val
    },
    deleteBatch(){
      if (!this.forms.length) {
        ElMessage.warning("Please select data！")
        return
      }
    //  一个小优化，直接发送这个数组，而不是一个一个的提交return
      request.post("bookwithuser/deleteRecords",this.forms).then(res =>{
        if(res.code === '0'){
          ElMessage.success("Multi return success")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    load(){
      if(this.user.role == 1){
        request.get("/bookwithuser",{
          params:{
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            search1: this.search1,
            search2: this.search2,
            search3: this.search3,
          }
        }).then(res =>{
          console.log(res)
          this.tableData = res.data.records
          this.total = res.data.total
        })
      }
      else {
        request.get("/bookwithuser",{
          params:{
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            search1: this.search1,
            search2: this.search2,
            search3: this.user.id,
          }
        }).then(res =>{
          console.log(res)
          this.tableData = res.data.records
          this.total = res.data.total
        })
      }
    },
    clear(){
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },
    handlereProlong(row){
      var nowDate = new Date(row.deadtime);
      nowDate.setDate(nowDate.getDate()+30);
      row.deadtime = moment(nowDate).format("yyyy-MM-DD HH:mm:ss");
      row.prolong = row.prolong -1;
      request.post("/bookwithuser",row).then(res =>{
        console.log(res)
        if(res.code == 0){
          ElMessage({
            message: 'renew success',
            type: 'success',
          })
        }
        else {
          ElMessage.error(res.msg)
        }
        this.load()
        this.dialogVisible2 = false
      })
    },
    save(){
      //ES6语法
      //地址,但是？IP与端口？+请求参数
      // this.form?这是自动保存在form中的，虽然显示时没有使用，但是这个对象中是有它的
        request.post("/bookwithuser",this.form).then(res =>{
          console.log(res)
          if(res.code == 0){
            ElMessage({
              message: 'Modify info success',
              type: 'success',
            })
          }
          else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible2 = false
        })
    },

    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },

  },
  data() {
    return {
      form: {},
      form2:{},
      form3:{},
      dialogVisible: false,
      dialogVisible2: false,
      search1:'',
      search2:'',
      search3:'',
      total:10,
      currentPage:1,
      pageSize: 10,
      tableData: [],
      user:{},
      forms:[],
    }
  },
}
</script>
<style scoped src="..\..\dist\css\responsive_ui.css"></style>
