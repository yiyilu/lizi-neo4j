<template>
  <div class="nav">
    <!--default-active设置默认页面-->
    <el-menu default-active="user"
             :default-openeds=[1]
             class="el-menu-vertical-demo"
             @open="handleOpen"
             @close="handleClose"
             background-color="#161619"
             text-color="#fff"
             >
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span slot="title">导航一</span>
        </template>
        <el-menu-item index="home" :route="{path:'/userMapping'}" @click="goRegister">主界面</el-menu-item>

      </el-submenu>

      <el-menu-item index="2">
        <i class="el-icon-menu"></i>
        <span slot="title">导航二</span>
      </el-menu-item>
      <el-menu-item index="3" disabled>
        <i class="el-icon-document"></i>
        <span slot="title">导航三</span>
      </el-menu-item>

      <el-menu-item index="user" :route="{path:'/user'}" @click="goFriends">
        <i class="el-icon-setting"></i>
        <span slot="title">我的好友</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
    export default {
        name: "UserAside",
      data() {
        return {
          isCollapse: true
        };
      },
      methods: {
        handleOpen(key, keyPath) {
          console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
          console.log(key, keyPath);
        },
        goFriends(){
          this.$router.push({ path: '/layout/user' })
        },
        goRegister(){
          this.$router.push({ path: '/layout/userMapping' })
        }
      }
    }
</script>

<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 800px;
  }
</style>
