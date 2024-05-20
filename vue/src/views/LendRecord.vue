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
        <el-form-item label="Reader id">
          <el-input v-model="search3" placeholder="Please enter Reader id" clearable>
            <template #prefix>
              <el-icon class="el-input__icon">
                <search/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">Search</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--按钮-->
<!--    <div style="margin: 10px 0;" v-if="user.role == 2">-->
<!--      <el-popconfirm title="Confirm delete?" @confirm="deleteBatch">-->
<!--        <template #reference>-->
<!--          <el-button type="danger" size="mini">Multi delete</el-button>-->
<!--        </template>-->
<!--      </el-popconfirm>-->
<!--    </div>-->
    <!-- 数据字段-->

    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
<!--      <el-table-column v-if="user.role ==1"-->
<!--                       type="selection"-->
<!--                       width="55">-->
<!--      </el-table-column>-->
      <el-table-column prop="isbn" label="ISBN" sortable/>
      <el-table-column prop="bookname" label="Book name"/>
      <el-table-column prop="readerId" label="Reader id" sortable/>
      <el-table-column prop="readername" label="Reader Name" sortable/>
      <el-table-column prop="lendTime" label="Borrowing date" sortable/>
      <el-table-column prop="returnTime" label="Returning date" sortable/>
      <el-table-column prop="status" label="Status">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="warning">Not returned</el-tag>
          <el-tag v-else-if="scope.row.status == 2" type="info">Apply Return</el-tag>
          <el-tag v-else type="success">Returned</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="user.role === 2" label="Operation" width="230px">
        <template v-slot="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">Edit</el-button>

          <!-- 直接根据status显示不同按钮 -->
          <el-button
              v-if="scope.row.status == 0"
              type="danger"
              size="mini"
              @click="handleDelete(scope.row)">
            Return
          </el-button>

          <el-button
              v-else-if="scope.row.status == 1"
              type="default"
              size="mini"
              disabled>
            Returned
          </el-button>

          <el-button
              v-else
              type="primary"
              size="mini"
              @click="handleDelete(scope.row)">
            Agree
          </el-button>
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


      <el-dialog v-model="dialogVisible" title="Modify borrowing record" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="Borrowing date">
            <el-date-picker
                v-model="form.lendTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="归还时间">

            <el-date-picker
                v-model="form.returnTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
            >
            </el-date-picker>

          </el-form-item>
          <el-form-item label="Returningstatus" prop="status">
            <el-radio v-model="form.status" label="0">Not returned</el-radio>
            <el-radio v-model="form.status" label="1">Returned</el-radio>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save(form)">Confirm</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>

</template>

<script>

import request from "../utils/request";
import {ElMessage} from "element-plus";
import {defineComponent, reactive, toRefs} from 'vue'
import router from "@/router";

export default defineComponent({

  created() {
    let userJson = sessionStorage.getItem("user")
    if (!userJson) {
      router.push("/login")
    }
    this.load()
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
  },
  name: 'LendRecord',
  methods: {
    handleSelectionChange(val) {
      this.forms = val
    },
    deleteBatch() {
      if (!this.forms.length) {
        ElMessage.warning("Please select data！")
        return
      }
      request.post("/LendRecord/deleteRecords", this.forms).then(res => {
        if (res.code === '0') {
          ElMessage.success("Multi delete success")
          this.load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    load() {
      request.get("/LendRecord", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search1: this.search1,
          search2: this.search2,
          search3: this.search3
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save(form) {
        request.put("/LendRecord/" , this.form).then(res => {
          console.log(res)
          if (res.code == 0) {
            ElMessage({
              message: 'update success',
              type: 'success',
            })
          } else {
            ElMessage.error(res.msg)
          }

          this.load() //不知道为啥，更新必须要放在这里面
          this.dialogVisible2 = false
        })
    },
    clear() {
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleDelete(row) {
      const form3 = JSON.parse(JSON.stringify(row))
      request.post("LendRecord/deleteRecord", form3).then(res => {
        console.log(res)
        if (res.code == 0) {
          ElMessage.success("return success")
        } else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    add() {
      this.dialogVisible2 = true
      this.form = {}
    }
  },

  setup() {
    const state = reactive({
      shortcuts: [
        {
          text: 'Today',
          value: new Date(),
        },
        {
          text: 'Yesterday',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          },
        },
        {
          text: 'A week ago',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          },
        },
      ],
      value1: '',
      value2: '',
      value3: '',
      defaultTime: new Date(2000, 1, 1, 12, 0, 0), // '12:00:00'
    })

    return toRefs(state)
  },
  data() {
    return {
      form: {},
      search1: '',
      search2: '',
      search3: '',
      total: 10,
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      user: {},
      dialogVisible: false,
      dialogVisible2: false

    }
  },

})
</script>
<style scoped src="..\..\dist\css\responsive_ui.css"></style>