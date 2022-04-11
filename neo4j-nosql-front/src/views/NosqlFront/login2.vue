<template>
  <div class="login-page">
    <vue-particles class="login-bg"
      color="#fff"
      :particleOpacity="0.7"
      :particlesNumber="60"
      shapeType="circle"
      :particleSize="4"
      linesColor="#fff"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="2"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
    ></vue-particles>
      <div class="login-container">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
         <!--用户名-->
          <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
            <el-input ref="username" v-model="loginForm.username" placeholder="Username" name="username" type="text" tabindex="1" auto-complete="on" />
          </el-form-item>
          <!--密码-->
          <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
            <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="Password" name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
            <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
          </el-form-item>

          <el-form-item>
            <JcRange @successFun="onSuccessFun" />
          </el-form-item>

          <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:20px;margin-top: 20px;" @click.native.prevent="handleLogin">Login</el-button>

          <div class="tips">
            <span style="color: #409EFF;" @click="handleRegister">注册</span>
            <span style="color: #ccc;" @click="handleGet">忘记密码</span>
          </div>

        </el-form>

      </div>
  </div>
</template>

<script >
  import { validUsername } from "@/utils/validate";
  import JcRange from "@/views/NosqlFront/jcRange.vue";
  import axios from "axios";
  var music = new Audio();
  export default ({

    name: "Login",
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!validUsername(value)) {
          callback(new Error("Please enter the correct user name"));
        } else {
          callback();
        }
      };
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error("The password can not be less than 6 digits"));
        } else {
          callback();
        }
      };
      return {
        loginForm: {
          /*username: "admin",
          password: "123456"*/
        },
        loginRules: {
          username: [
            { required: true, trigger: "blur", validator: validateUsername }
          ],
          password: [
            { required: true, trigger: "blur", validator: validatePassword }
          ]
        },
        loading: false,
        passwordType: "password",
        redirect: undefined,
        status: false
      };
    },
    components: {
      JcRange
    },
    watch: {
      // $route: {
      //   handler: function(route) {
      //     this.redirect = route.query && route.query.redirect;
      //   },
      //   immediate: true
      // }
    },
    methods: {
      showPwd() {
        if (this.passwordType === "password") {
          this.passwordType = "";
        } else {
          this.passwordType = "password";
        }
        this.$nextTick(() => {
          this.$refs.password.focus();
        });
      },
       handleLogin() {
        const that = this;
        const { username, password } = this.loginForm;
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            if (!that.status) {
              return this.$message.error("请按住滑块，拖动到最右边");
            }
            this.loading = true;
            axios({
              method: "post",
              url: "http://localhost:8081//people/web/login",
              data: {
                username,
                password
              }
            }).then(res => {
              that.loading = false;
              if (res.data.code == 200) {
                if(res.data.data.personWebName=="admin"){
                  this.$router.push({
                    path: "/admin",
                    //query: { data: res.data.data }
                  });
                }else {
                  sessionStorage.setItem("user", JSON.stringify(res.data.data))  // 缓存用户信息
                  //console.log(sessionStorage.getItem("user"))

                  this.$router.push({
                    path: "/layout",
                    //query: { data: res.data.data }
                  });
                }



              } else {
                this.$router.push({
                  path: "/error",
                  query: { message: res.data.message }
                });

              }
            });
          } else {
            console.log("参数验证不合法！");
            return false;
          }
        });
      },
      handleRegister() {
        this.$router.push({path:"/register"})
      },


      // 监听滑块成功事件
      onSuccessFun(e) {
        this.status = e;
      },
      handleGet() {
        this.$message("正在开发中");
      },
      /*音乐播放*/
      audioAutoPlay(){
        let audio = document.getElementById("audio");
        audio.play();
        document.removeEventListener("touchstart", this.audioAutoPlay);
      }
    }
  });
</script>

<style lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */


  $bg: #283443;

  $light_gray: #fff;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  /* reset element-ui css */
  .login-container {

    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }
</style>

<style lang="scss" scoped>
/*
  $bg: #2d3a4b;
*/
  $dark_gray: #889aa4;
  $light_gray: #eee;
  .login-page{
    width: 100%;
    overflow: hidden;
    background: url("~@/assets/bg2.gif") no-repeat;
    background-size: cover;
    height: 1000px;
   }

  .login-container {

    width: 70%;
    height: 60%;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;

    .login-form {
      position: relative;
      width: 520px;
      height: 700px;
      max-width: 100%;
      padding: 160px 35px 0;
      margin: 0 auto;
      overflow: hidden;

    }

    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
      display: flex;
      justify-content: space-between;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    /*.title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: #cec6c6;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }
*/
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: aqua;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
