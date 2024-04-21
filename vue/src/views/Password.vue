<template xmlns="http://www.w3.org/1999/html">
  <div>
    <el-card style="width: 60%; margin-left: 120px; margin-top: 40px" >
      <el-form
          ref="form"
          :model="form"
          status-icon
          :rules="rules"
          label-width="200px"
          class="demo-ruleForm"
      >
        <el-form-item label="Verification code" prop="code">
          <el-input
              v-model="form2.code"
              type="code"
              autocomplete="off"
              style="width: 150px"
          ></el-input>
          <el-button type="success" plain  style="margin-left: 20px" @click="getcode" :disabled="isDisabled">{{buttonName}}</el-button>
        </el-form-item>
        <el-form-item label="New password" prop="password">
          <el-input
              v-model="form2.password"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="Confirm new password" prop="checkpassword">
          <el-input
              v-model="form.checkpassword"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" style="text-align: center;margin-left: 120px">Submit</el-button>
          <el-button @click="resetForm('form')" style="text-align: center;margin-left: 20px">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
  name: "Password",
  data() {

    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter new password'))
      } else {
        callback()
      }
    }
    const validatePass3 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please confirm new password'))
      } else if (value !== this.form2.password) {
        callback(new Error("Password confirmation doesn't match"))
      } else {
        callback()
      }
    }
    return {

      buttonName: "Get from SMS",
      isDisabled: false,
      time: 60,

      phone:'',
      form: {
        checkpassword: '',
      },
      form2:{
        phone:'',
        code: '',
        password:'',
        id:0
      },
      rules: {
        password: [{ validator: validatePass, trigger: 'blur' ,required: true}],
        checkpassword: [{ validator: validatePass3, trigger: 'blur',required: true,}],
      },
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson)
    {
      router.push("/login")
    }
    let user = JSON.parse(sessionStorage.getItem("user"))
    this.phone= user.phone
    this.form2.id = user.id
    this.form2.phone = user.phone
  },
  methods: {

    getcode(){

      if (this.phone == null){
        ElMessage.error("Couldn't get! Please complete your user profile")
        this.$router.push("/person")//跳转个人信息界面
        return;
      }

      let me = this;
      me.isDisabled = true;
      let interval = window.setInterval(function() {
        me.buttonName = 'Resend after （' + me.time + 'seconds）';
        --me.time;
        if(me.time < 0) {
          me.buttonName = "Resend";
          me.time = 60;
          me.isDisabled = false;
          window.clearInterval(interval);
        }
      }, 1000);
      request.get("user/getcode",{
        params:{
          phone:this.phone
        }
      }).then(res=>{
        if (res.code == 0) {
          ElMessage.success("Send success")
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {

          request.put("/user", this.form2).then(res => {
            if (res.code == 0) {
              ElMessage.success("Password change success, please login again")
              sessionStorage.removeItem("user")//清空缓存的用户信息
              this.$router.push("/login")//跳转登录界面
            } else {
              ElMessage.error(res.msg)
            }
          })
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>

<style scoped>

</style>
