<template>
<div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">Sign up</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="username" clearable >
          <template #prefix>
            <el-icon class="el-input__icon"><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="confirm password" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="role">
        <el-radio v-model="form.role" label="3">reader</el-radio>
<!--        <el-radio v-model="form.role" label="1">管理员</el-radio>-->
      </el-form-item>
      <el-form-item prop="authorize" v-if="form.role==1">
        <el-input v-model="form.authorize" placeholder="enter the administrator registration code" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <div style="display: flex">
          <el-input  v-model="form.validCode" style="width: 52%;" placeholder="verification code"></el-input>
          <ValidCode @input="createValidCode" style="width: 50%"/>
        </div>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 20px"  @click="register">sign up</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px" @click="$router.push('/login')">Log in>> </el-button></el-form-item>
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
        authorize:[
          {
            required:true,
            message:"Registration code",
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
            ElMessage.error("verification code error")
            return
          }
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("The two password entries do not match")
            return
          }
          if(this.form.role == 1 && this.form.authorize != "2236")
          {
            ElMessage.error("Please enter the correct registration code")
            return
          }
          request.post("user/register",this.form).then(res=>{
            if(res.code == 0)
            {
              ElMessage.success("Signup successful")
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
  background: url('../img/bg5.jpg');
  background-size: cover;
}
.login-page {
  position: relative;
  border-radius: 15px;
  width: 350px;
  padding: 0 50px;
  background: #fff;
  border: 1px solid #eaeaea;
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