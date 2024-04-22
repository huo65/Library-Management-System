<template>
  <div class="home" style ="padding: 10px">
<!-- 按钮-->
<!-- 搜索-->
    <div style="margin: 10px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="Admin id" >
      <el-input v-model="search1" placeholder="Please enter Admin id"  clearable>
        <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
      </el-input>
          </el-form-item >
        <el-form-item label="Name" >
          <el-input v-model="search2" placeholder="Please enter Name"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Email" >
          <el-input v-model="search3" placeholder="Please enter Email"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="Address" >
          <el-input v-model="search4" placeholder="Please enter Address"  clearable>
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
    <!-- 按钮-->
    <div style="margin: 10px 0;" >
<!--      <el-popconfirm title="Confirm ban?" @confirm="deleteBatch" v-if="user.role == 1">-->
<!--        <template #reference>-->
          <el-button type="primary" @click = "add" v-if="user.role == 1">Add Admin </el-button>
<!--          <el-button type="danger" size="mini" >Multi ban</el-button>-->
<!--        </template>-->
<!--      </el-popconfirm>-->
    </div>
<!-- 数据字段-->
    <el-table :data="tableData" stripe border="true"  @selection-change="handleSelectionChange" >
      <el-table-column v-if="user.role ==1 "
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="id" label="Admin id" sortable />
      <el-table-column prop="username" label="Username" />
      <el-table-column prop="nickName" label="Name" />
      <el-table-column prop="phone" label="Email" />
      <el-table-column prop="sex" label="Gender" />
      <el-table-column prop="address" label="Address" />
      <el-table-column fixed="right" label="Operation" >
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)">Process/Edit </el-button>
          <el-popconfirm title="Confirm ban?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="mini" style="margin-top: 10px; margin-left: 50px">Account ban</el-button>
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

      <el-dialog v-model="dialogVisible" title="Edit Admin info" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="Username">
            <el-input style="width: 80%" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="Nickname">
            <el-input style="width: 80%" v-model="form.nickName"></el-input>
          </el-form-item>
          <el-form-item label="Email">
            <el-input style="width: 80%" v-model="form.phone"></el-input>
          </el-form-item>

          <el-form-item label="role">
            <el-radio v-model="form.role" label="3">Reader</el-radio>
            <el-radio v-model="form.role" label="2">Admin </el-radio>
          </el-form-item>

          <el-form-item label="Password(123 by default)">
            <el-input style="width: 80%" v-model="form.password"></el-input>
          </el-form-item>
<!--          <el-form-item label="Gender">-->
<!--            <div>-->
<!--              <el-radio v-model="form.sex" label="男">男</el-radio>-->
<!--              <el-radio v-model="form.sex" label="女">女</el-radio>-->
<!--            </div>-->
<!--          </el-form-item>-->
          <el-form-item label="Address">
            <el-input type="textarea" style="width: 80%" v-model="form.address"></el-input>
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
import router from "@/router";

export default {
  created(){
    let userJson = sessionStorage.getItem("user")
    if(!userJson)
    {
      router.push("/login")
    }
    this.load()
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
  },
  name: 'User',
  methods: {
    handleSelectionChange(val){
      this.ids = val.map(v => v.id)
    },
    deleteBatch(){
      if (!this.ids.length) {
        ElMessage.warning("Please select data！")
        return
      }
      //  一个小优化，直接发送这个数组，而不是一个一个的提交delete
      request.post("/user/deleteBatch",this.ids).then(res =>{
        if(res.code === '0'){
          ElMessage.success("Multi delete success")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    load(){
      request.get("user/usersearch",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search1: this.search1,
          search2: this.search2,
          search3: this.search3,
          search4: this.search4,
          role:2
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    clear(){
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },

    handleDelete(id){
      request.delete("user/" + id ).then(res =>{
        console.log(res)
        if(res.code == 0 ){
          ElMessage.success("delete success")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },


    add(){
      this.dialogVisible= true
      this.form ={}
    },
    save(){
      if(this.form.id){
        request.put("/user/update",this.form).then(res =>{
          console.log(res)
          if(res.code == 0){
            ElMessage({
              message: 'Update success',
              type: 'success',
            })
          }
          else {
            ElMessage.error(res.msg)
          }

          this.load() //不知道为啥，Update必须要放在这里面
          this.dialogVisible = false
        })
      }
      else {
        request.post("/user",this.form).then(res =>{
          console.log(res)
          if(res.code == 0){
            ElMessage.success('Add success')
          }
          else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible = false
        })
      }

    },

    handleAlow(id){
      request.put("user/" + id ).then(res =>{
        console.log(res)
        if(res.code == 0 ){
          ElMessage.success("Authorise success")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },

    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
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
      dialogVisible : false,
      search1:'',
      search2:'',
      search3:'',
      search4:'',
      total:10,
      currentPage:1,
      pageSize: 10,
      tableData: [

      ],
      user:{},
      ids:[],
    }
  },
}
</script>
