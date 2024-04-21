<template>
<div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">Sign up</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="Username" clearable >
          <template #prefix>
            <el-icon class="el-input__icon"><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="Password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="Confirm password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="role">
        <el-radio v-model="form.role" label="3">As reader</el-radio>
<!--        <el-radio v-model="form.role" label="1">管理员</el-radio>-->
      </el-form-item>
      <el-form-item prop="authorize" v-if="form.role==1">
        <el-input v-model="form.authorize" placeholder="Admin authorize code" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <div style="display: flex">
          <el-input  v-model="form.validCode" style="width: 50%;" placeholder="Verification code"></el-input>
          <ValidCode @input="createValidCode" style="width: 50%"/>
        </div>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 20px"  @click="register">Sign up</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px" @click="$router.push('/login')">Login>> </el-button></el-form-item>
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
  data(){
    return{
      form:{},
      validCode: '',
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
        authorize:[
          {
            required:true,
            message:"Please enter your authorize code",
            trigger:"blur"
          }
        ],
      }
    }
    },

  methods:{
    createValidCode(data){
      this.validCode =data
    },
    register(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            ElMessage.error("Please enter the verification code")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            ElMessage.error("Incorrect verification code")
            return
          }
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("Password confirmation doesn't match")
            return
          }
          if(this.form.role == 1 && this.form.authorize != "2236")
          {
            ElMessage.error("Incorrect authorize code")
            return
          }
          request.post("user/register",this.form).then(res=>{
            if(res.code == 0)
            {
              ElMessage.success("Sign up success")
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