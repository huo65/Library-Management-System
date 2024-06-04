<template>
  <div class="home" style ="padding: 10px">
    <!-- 搜索-->
    <div style="margin: 10px 0;">

      <el-form inline="true" size="small" >
<!--        <el-form-item label="ISBN" >-->
<!--          <el-input v-model="search1" placeholder="Please enter ISBN"  clearable>-->
<!--            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>-->
<!--          </el-input>-->
<!--        </el-form-item >-->
        <el-form-item label="Book name" >
          <el-input v-model="search2" placeholder="Please enter Book name"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >

        <el-form-item label="ReaderID" v-if="user.role != 3">
          <el-input v-model="search3" placeholder="Please enter Reader ID"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Reader Name" v-if="user.role != 3">
          <el-input v-model="search4" placeholder="Please enter Reader name"  clearable>
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

    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">

      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="readerid" label="ReaderID" />
      <el-table-column prop="readername" label="ReaderName" />
      <el-table-column prop="bookId" label="BookId" />
      <el-table-column prop="bookname" label="BookName" />
      <el-table-column prop="number" label="Amount" />
      <el-table-column prop="status" label="Status" v-if="user.role != 3">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="warning">Not pay</el-tag>
          <el-tag v-else type="success">Paid</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="Operation" >
        <template v-slot="scope" v-if="user.role == 3 ">
          <div v-if="scope.row.status == 1">
            <el-tag v-if="scope.row.status == 1" type="warning">Paid</el-tag>
          </div>
          <div v-else>
            <el-popconfirm title="confirm pay?" @confirm="handlePay(scope.row) " >
              <template #reference>
                <el-button type="primary" size="mini" >pay</el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
        <template v-slot="scope" v-else>
            <el-popconfirm title="confirm edit?" @confirm="handleEdit(scope.row) " >
              <template #reference>
                <el-button type="primary" size="mini" >Edit</el-button>
              </template>
            </el-popconfirm>
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

          <el-form-item label="Amount">
            <el-input style="width: 80%" v-model="form.number"></el-input>
          </el-form-item>
          <el-form-item label="Returningstatus" prop="status">
            <el-radio v-model="form.status" label="0">Not pay</el-radio>
            <el-radio v-model="form.status" label="1">Paid</el-radio>
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
  name: 'fine',
  methods: {

    load(){
      if(this.user.role != 3){
        request.get("/fine",{
          params:{
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            search1: this.search1,//isbn
            search2: this.search2,//bookname
            search3: this.search3,
            search4: this.search4,//readername
          }
        }).then(res =>{
          console.log(res)
          this.tableData = res.data.records
          this.total = res.data.total
        })
      }
      else {
        request.get("/fine",{
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
    handlePay(row){
      // window.open('http://localhost:8090/alipay/pay?subject=%E4%B8%9C%E5%8C%97%E8%8F%9C&traceNo=1&totalAmount=100');
      window.open("http://localhost:8090/alipay/pay?subject="+"B5Lib"+"&traceNo="+row.id+"&totalAmount="+row.number,'_self')
    },
    save(){
      request.post("/fine/update",this.form).then(res =>{
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
      search4:'',
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
