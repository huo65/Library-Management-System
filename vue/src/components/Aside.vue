<template>
<div>


  <el-menu
      style="width: 200px; min-height: calc(100vh - 50px);"
      :default-active="path"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      router
      background-color="#30333c" text-color="#fff"
  >
    <el-menu-item index="/welcome" >
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icondashboard "></use>
            </svg>
            <span>Home</span>
    </el-menu-item>
    <el-menu-item index="/dashboard" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icondashboard "></use>
      </svg>
      <span>Dashboard</span>
    </el-menu-item>

    <el-sub-menu index="2" text-color="#fff">
      <template #title>
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mingpian "></use>
        </svg>
        <span>User</span>
      </template>
      <el-menu-item index="/person" style="font-color: white">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-a-bianji1 "></use>
        </svg>
        <span>Profile</span>
      </el-menu-item>
      <el-menu-item index="/password">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-mima "></use>
        </svg>
        <span>Change password</span>
      </el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/userAdmin" v-if="user.role == 1">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconreader "></use>
      </svg>
      <span>Manage admins</span>
    </el-menu-item>
    <el-menu-item index="/user" v-if="user.role == 2">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconreader "></use>
      </svg>
      <span>Manage readers</span>
    </el-menu-item>
    <el-menu-item index="/book" v-if="user.role == 2" >
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Manage books</span>
    </el-menu-item>
    <el-menu-item index="/book" v-if="user.role == 3">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconbook "></use>
      </svg>
      <span>Search books</span>
    </el-menu-item>
    <el-menu-item index="/lendrecord" v-if="user.role == 2">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconlend-record "></use>
      </svg>
      <span>Lending record</span>
    </el-menu-item>
<!--    <el-menu-item index="/lendrecord" v-if="user.role == 3">-->
<!--      <svg class="icon" aria-hidden="true">-->
<!--        <use xlink:href="#iconlend-record"></use>-->
<!--      </svg>-->
<!--      <span>借阅信息</span>-->
<!--    </el-menu-item>-->
    <el-menu-item index="/bookwithuser" v-if="user.role == 3">
      <el-icon><grid /></el-icon>
      <span>Borrowing record</span>
    </el-menu-item>
  </el-menu>

</div>
</template>

<script>



export default {
  name: "Aside",
  components:{},
  created(){
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
  },
  data(){
    return {
      user:{},
      path: this.$route.path
    }
  }
}
</script>

<style scoped>
.icon {
  width: 30px;
  height: 30px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>