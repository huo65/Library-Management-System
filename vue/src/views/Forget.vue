<template>
  <div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">Reset Password</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="username" clearable >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="new password" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="confirm password" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item>
          <el-input  v-model="form.code" style="width: 45%;" placeholder="verification code"></el-input>
          <el-button type="success" plain  style="margin-left: 10px" @click="getcode" :disabled="isDisabled">{{buttonName}}</el-button>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 18px" @click="register">reset password</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px"  @click="$router.push('/login')">Log in>> </el-button></el-form-item>
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
      buttonName: "Get verification code",
      isDisabled: false,
      time: 60,
      form:{},
      rules: {
        username: [
          {
            required: true,
            message: 'enter username',
            trigger: 'blur',
          },
          {
            min: 2,
            max: 13,
            message: 'Length requirement: 2~13',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: 'enter password',
            trigger: 'blur',
          }
        ],
        confirm:[
          {
            required:true,
            message:"confirm password",
            trigger:"blur"
          }
        ],
      }
    }
  },

  methods:{
    getcode(){
      if (!this.form.username) {
        ElMessage.error("username")
        return
      }
      if (!this.form.password) {
        ElMessage.error("new password")
        return
      }
      if(this.form.password != this.form.confirm)
      {
        ElMessage.error("The two password entries do not match")
        return
      }
      request.get("forget/getcode",{
        params:{
          username:this.form.username
        }
      }).then(res=>{
        if (res.code == 0) {
          ElMessage.success("Sent successful")
          let me = this;
          me.isDisabled = true;
          let interval = window.setInterval(function() {
            me.buttonName = '（' + me.time + 's）sent again';
            --me.time;
            if(me.time < 0) {
              me.buttonName = "sent again";
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
            ElMessage.error("verification")
            return
          }
          if (!this.form.password) {
            ElMessage.error("new password")
            return
          }
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("The two password entries do not match")
            return
          }
          request.post("forget/register",this.form).then(res=>{
            if(res.code == 0)
            {
              ElMessage.success("Password change successful")
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
  background: url('../img/bg4.jpg');
  background-size: cover;
}
.login-page {
  position:relative;
  border-radius: 15px;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 5px 25px rgba(0,0,0,0.25);
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.title {
    font-size: 33px;
    font-weight: bold;
    text-align: center;
    line-height: 100px;
}

</style>