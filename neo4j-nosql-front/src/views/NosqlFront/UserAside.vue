<template>
  <div class="nav">
    <!--default-active设置默认页面-->
    <!--:default-openeds=[1]-->
    <el-menu default-active="followed"
             class="el-menu-vertical-demo"
             @open="handleOpen"
             @close="handleClose"
             background-color="#161619"
             text-color="#fff"
             >
      <!--<el-submenu index="1">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span slot="title">导航一</span>
        </template>
        <el-menu-item index="home" :route="{path:'/userMapping'}" @click="goRegister">主界面</el-menu-item>

      </el-submenu>-->



      <el-menu-item index="followed" :route="{path:'/userFollowed'}" @click="goFollowed">
        <i class="el-icon-star-on"></i>
        <span slot="title">我的关注</span>
      </el-menu-item>
      <el-menu-item index="follower" :route="{path:'/userFollower'}" @click="goFollower">
        <i class="el-icon-star-off"></i>
        <span slot="title">我的粉丝</span>
      </el-menu-item>
      <el-menu-item index="mapping" :route="{path:'/userMapping'}" @click="goMapping" >
        <i class="el-icon-loading"></i>
        <span slot="title">用户匹配</span>
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
        goFollowed(){
          this.$router.push({ path: '/layout/userFollowed' })
        },
        goFollower(){
          this.$router.push({ path: '/layout/userFollower' })
        },
        goMapping(){
          this.$router.push({ path: '/layout/userMapping' })
        }
      }
    }
</script>

<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    //min-height: 800px;
    height: 100%;
  }
</style>
