<template>
 <div style="height: 50px; line-height:50px; border-bottom: 1px solid #ccc; display: flex">
   <div style="width: auto; padding-left:30px; font-weight: bold; color:dodgerblue">
     <img :src="imgUrl" class="icon" >
     Library Management System</div>
   <div style="flex: 1"></div>
   <div style="width: 100px">
     <el-dropdown>
      <span class="el-dropdown-link">
        {{user.nickName}} <el-icon class="el-icon--right">
          <arrow-down />
          </el-icon>
      </span>
       <template #dropdown>
         <el-dropdown-menu>
           <el-dropdown-item @click="exit">Log out</el-dropdown-item>
         </el-dropdown-menu>
       </template>
     </el-dropdown>
   </div>
 </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "Header",
  props: ['user'],
  data(){
    return{
      user:[],
      imgUrl:require("../assets/icon/login.png")
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user = JSON.parse(userStr)
  },
  methods:{
    exit(){
      sessionStorage.removeItem("user")
      this.$router.push("/login")
      ElMessage.success("Log out success")
    }
  }

}
</script>

<style scoped>
.icon {
  width: 40px;
  height: 40px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>