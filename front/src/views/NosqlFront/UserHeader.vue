<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; background-color: #161619">
    <div style="width: 250px; padding-left: 30px; font-weight: bold; color: dodgerblue">寻找每一颗宇宙中的粒子</div>
    <div style="flex: 1"></div>
    <div style="width: 100px">
      <!--右上角下拉框-->
      <el-dropdown>
      <span class="el-dropdown-link">
      {{user.personWebName}}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>个人信息</el-dropdown-item>
          <el-dropdown-item @click.native="toLogin">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
  </div>
</template>

<script>
    export default {
        name: "UserHeader",
        data(){
          return{
            user: {},
          }
        },
      created() {
        this.user=JSON.parse(sessionStorage.getItem("user")) //获取登录信息,要用JSON来转换一下
        console.log(sessionStorage.getItem("user"))

      },
      methods: {
        toLogin(){
          sessionStorage.removeItem("user") //删掉user记录
          console.log(sessionStorage.getItem("user"))
          this.$router.push({ path: '/' })
        },
        goFriends(){
          this.$router.push({ path: '/layout/user' })
        }

      }

    }
</script>

<style scoped>
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
</style>
