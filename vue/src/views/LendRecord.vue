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
    <!-- 按钮-->
    <div style="margin: 10px 0;" >
      <el-button type="primary" @click = "scanOpen"> Scan Book </el-button>
    </div>

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
          <el-form-item label="returnTime">

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


      <el-dialog v-model="scanDialogVisible" title="LendRecord info" width="50%">
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

        <el-table :data="scanForm" >
          <el-table-column prop="bookId" label="Book ID" sortable />
          <el-table-column prop="bookName" label="BookName"  />
          <el-table-column prop="readerId" label="Reader ID" />
          <el-table-column prop="readerName" label="Name" />
          <el-table-column prop="lendTime" label="LendTime" />

        </el-table>

        <template #footer>
      <span class="dialog-footer">
<!--        <el-button @click="dialogVisible = false">Cancel</el-button>-->
        <el-button type="primary" @click="handleScanReturn(scanForm[0].recordId)">return</el-button>
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
        // TODO 同步
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


    scanOpen(){
      this.scanDialogVisible= true
      this.form ={}
    },
    handleScanReturn(scan){
      request.put("LendRecordByScan", scan).then(res => {
        console.log(res)
        if (res.code == 0) {
          ElMessage.success("return success")
        } else
          ElMessage.error(res.msg)
        this.load()
      })

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
        request.post("barCode/getReturnInfo",imageBase64).then(res=>{
          if(res.code == 0)
          {
            let value = res.data
            console.log(value)
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
      scanForm:[0],
      search1: '',
      search2: '',
      search3: '',
      total: 10,
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      user: {},
      dialogVisible: false,
      scanDialogVisible:false,
      imageUrl: '', // 用于预览的图片base64编码
    }
  },

})
</script>
<style scoped src="..\..\dist\css\responsive_ui.css"></style>
