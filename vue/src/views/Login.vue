<template>
<div  class="login-container"  >

    <el-form ref="form" :model="form"   :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px;text-align: center">Login</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username"  placeholder="Username" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" placeholder="Password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <div style="display: flex">
          <el-input  v-model="form.validCode" style="width: 45%;" placeholder="Captcha"></el-input>
          <ValidCode @input="createValidCode" style="width: 50%"/>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  style=" width: 100%;height: 130%;font-size: 16px" @click="login">Log in</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="text" style="font-size: 16px;width: 20%; color: #ee7463" @click="$router.push('/forget')">Forgot password?</el-button>
        <el-button type="text" style="font-size: 16px;margin-left: 139px" @click="$router.push('/register')">Sign up</el-button>
      </el-form-item>
    </el-form>
</div>

</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import ValidCode from "../components/Validate";

export default {
  name: "Login",
  components:{
    ValidCode
  },
  data() {
    return {
      validCode: '',//通过valicode获取的验证码
      form: {},
      rules: {
        username: [
          {
            required: true,
            message: 'username',
            trigger: 'blur',
          }
        ],
        password: [
          {
            required: true,
            message: 'password',
            trigger: 'blur',
          }
        ]

      }

    }
  },
  methods: {
    createValidCode(data){
      this.validCode = data
    },
    login(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            ElMessage.error("enter the verification code")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            ElMessage.error("verification code error")
            return
          }

          request.post("user/login", this.form).then(res => {
            if (res.code == 0) {
              ElMessage.success("Login successful")
              sessionStorage.setItem("user",JSON.stringify(res.data))//缓存用户信息
              this.$router.push("/welcome")
            } else {
              ElMessage.error(res.msg)
            }
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
  height: 100vh;
  background: url('../img/lib.jpg');
  background-size: cover;
  overflow: hidden;
}

.login-page {
  position: relative;
  border-radius: 15px;
  width: 420px;
  padding: 0 50px;
  border: 1px solid #eaeaea;
  background-color: #fff;
  box-shadow: 0 5px 25px rgba(0,0,0,0.25);
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.title {
            font-size: 35px;
            font-weight: bold;
            text-align: center;
            line-height: 100px;
        }


::v-deep .el-input__inner{
  height: 50px;
}
</style>
