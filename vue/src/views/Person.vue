<template>
  <div>
    <el-card style="width: 60%; margin-left: 120px; margin-top: 40px" >
        <h2 style="padding: 30px">User profile</h2>
      <el-form :model="form" ref="form" label-width="160px">
        <el-form-item label="Username">
          <el-input style="width: 80%" v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="Name">
          <el-input style="width: 80%" v-model="form.nickName"></el-input>
        </el-form-item>
        <el-form-item label="Privilege">
            <span v-if="form.role==1" style="margin:5px">System admin</span>
            <span v-if="form.role==2" style="margin:5px">admin</span>
            <span v-if="form.role==3" style="margin:5px">Reader</span>
        </el-form-item>
        <el-form-item label="Phone number">
          <el-input style="width: 40%" v-model="form.phone"></el-input>
          <el-tag type="danger" style="margin-left: 20px">Please login again after changing phone number</el-tag>
        </el-form-item>
        <el-form-item label="Gender">
          <div>
            <el-radio v-model="form.sex" label="Male">Male</el-radio>
            <el-radio v-model="form.sex" label="Female">Female</el-radio>
          </div>
        </el-form-item>
        <el-form-item label="Address">
          <el-input type="textarea" style="width: 80%" v-model="form.address"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">Save</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
  name: "Person",
  data() {
    return {
      form: {}
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson)
    {
      router.push("/login")
    }
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      request.put("/user/update", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          ElMessage.success("Update success")
          sessionStorage.setItem("user", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
        } else {
          ElMessage.error(res.msg)
        }
      })

    }
  }
}
</script>

<style>
.avatar-uploader  {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.box-card {
  width: 60%;
  margin: auto;
  padding: 20px;
}
</style>
