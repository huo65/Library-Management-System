<template>
  <div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">Password Reset</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="User name" clearable >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="New password" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="Confirm password" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item>
          <el-input  v-model="form.code" style="width: 55%;" placeholder="Verification code"></el-input>
          <el-button type="success" plain  style="margin-left: 10px" @click="getcode" :disabled="isDisabled">{{buttonName}}</el-button>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 18px" @click="register">Reset password</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px"  @click="$router.push('/login')">Login>> </el-button></el-form-item>
    </el-form>
  </div>

</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Forget",
  data(){
    return{
      buttonName: "Get from SMS",
      isDisabled: false,
      time: 60,
      form:{},
      rules: {
        username: [
          {
            required: true,
            message: 'Please enter your username',
            trigger: 'blur',
          },
          {
            min: 2,
            max: 13,
            message: 'Password is too long or too short (minimum is 2 and maximum is 13 characters)',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: 'Please enter your password',
            trigger: 'blur',
          }
        ],
        confirm:[
          {
            required:true,
            message:"Please confirm your password",
            trigger:"blur"
          }
        ],
      }
    }
  },

  methods:{
    getcode(){
      if (!this.form.username) {
        ElMessage.error("Please enter your username")
        return
      }
      if (!this.form.password) {
        ElMessage.error("Please enter your password")
        return
      }
      if(this.form.password != this.form.confirm)
      {
        ElMessage.error("Password confirmation doesn't match")
        return
      }
      request.get("forget/getcode",{
        params:{
          username:this.form.username
        }
      }).then(res=>{
        if (res.code == 0) {
          ElMessage.success("Send success")
          let me = this;
          me.isDisabled = true;
          let interval = window.setInterval(function() {
            me.buttonName = 'Resend after（' + me.time + 'seconds）';
            --me.time;
            if(me.time < 0) {
              me.buttonName = "Resend";
              me.time = 60;
              me.isDisabled = false;
              window.clearInterval(interval);
            }
          }, 1000);
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    register(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.code) {
            ElMessage.error("Please enter the verification code")
            return
          }
          if (!this.form.password) {
            ElMessage.error("Please enter your password")
            return
          }
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("Password confirmation doesn't match")
            return
          }
          request.post("forget/register",this.form).then(res=>{
            if(res.code == 0)
            {
              ElMessage.success("Reset success")
              this.$router.push("/login")
            }
            else {ElMessage.error(res.msg)}
          })
        }
      })

    }
  }

}

</script>

<style scoped>
.login-container {
  position: fixed;
  width: 100%;
  height: 100%;
  background: url('../img/bg2.svg');
  background-size: contain;
}
.login-page {
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
</style>