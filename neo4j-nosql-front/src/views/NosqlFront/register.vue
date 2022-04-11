<template>
  <div class="container">
    <div class="register-container">
      <!--新增成员模块-->
      <el-form ref="form" class="register-form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户昵称" prop="personWebName">
          <el-input v-model="form.personWebName" placeholder="请输入成员昵称"/>
        </el-form-item>
        <el-form-item label="用户密码" prop="personWebPwd">
          <el-input v-model="form.personWebPwd" type="textarea" placeholder="请输入成员密码"/>
        </el-form-item>
        <el-form-item label="用户简介" prop="personWebInfo">
          <el-input v-model="form.personWebInfo" type="textarea" placeholder="请输入成员简介"/>
        </el-form-item>
        <el-form-item label="跳转链接" prop="personWebLink">
          <el-input v-model="form.personWebLink" placeholder="请输入跳转链接"/>
        </el-form-item>
        <el-form-item label="用户头像" prop="personWebPic">
          <el-button  v-show="!form.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            上传头像
          </el-button>
          <el-button v-show="form.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            更新头像
          </el-button>
          <el-button v-show="form.personWebPic" type="primary" size="mini" @click="picShow(form.personWebPic)">
            预览头像
          </el-button>
        </el-form-item>

        <el-form-item label="活跃平台">
          <el-checkbox-group v-model="form.personWebPlatform">
            <el-checkbox
              v-for="dict in personWebPlatformOptions"
              :key="dict.dictValue"
              :label="dict.dictValue">
              {{dict.dictLabel}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="常驻领域" prop="personWebField">
          <el-tag
            :key="tag"
            v-for="(tag, index) in this.form.personWebField"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{tag}}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新标签</el-button>
        </el-form-item>

        <el-form-item label="沟通要旨" prop="personWebInfo">
          <el-input v-model="form.personWebKey" type="textarea" placeholder="请输入沟通要旨"/>
        </el-form-item>
        <el-form-item label="状态设置">
          <el-radio-group v-model="form.personWebShow">
            <el-radio
              v-for="dict in personWebShowOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <div class="footer" style="padding-left: 225px">
        <el-button type="primary" @click="submitForm">注册</el-button>
        <el-button @click="cancel">重置</el-button>
      </div>


<!--上传图片模块-->
      <my-upload
        method="POST"
        field="file"
        v-model="upload.cropperShow"
        :headers="upload.headers"
        :width=300
        :height=300
        :url="this.upload.logo_url"
        lang-type='zh'
        img-format='jpg'
        img-bgc='#FFF'
        :no-circle=true
        @crop-upload-success="cropUploadSuccess">
      </my-upload>
      <!--“图片预览”-->
      <el-dialog title="图片预览" :visible.sync="picVisible" width="500px" center>
        <div style="text-align: center">
          <img :src="answerPicImageUrl" alt="" class="pic-in-dialog">
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="picVisible = false">确定</el-button>
      </span>
      </el-dialog>
    </div>
  </div>

</template>

<script>
  import {
    getPersonWebListByName,
    getPersonWebMap,
    getPersonWebList,
    getPersonWebListByPage,
    getPersonWebSearch,
    getPersonWebInfo,
    addPersonWeb,
    editPersonWeb,
    removePersonWeb,
    getInfoConnectWeb,
    addConnectWeb,
    editConnectWeb,
    removeConnectWeb,
    getPersonWebSearchOther
  } from '@/api/person';
  import myUpload from 'vue-image-crop-upload';  //头像图片上传组件
  import 'babel-polyfill'; // es6 shim
  export default {
    name: "register",
    components: {myUpload},  //注册头像上传组件
    created() {
      this.personWebShowOptions = [{
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-02-21 19:29:04",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 124,
        "dictSort": 0,
        "dictLabel": "在线",
        "dictValue": "0",
        "dictType": "material_public",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-02-21 19:29:39",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 125,
        "dictSort": 1,
        "dictLabel": "隐身",
        "dictValue": "1",
        "dictType": "material_public",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }];
      this.personWebPlatformOptions = [{
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:20:51",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 135,
        "dictSort": 0,
        "dictLabel": "哔哩哔哩",
        "dictValue": "0",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:21:03",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 136,
        "dictSort": 1,
        "dictLabel": "今日头条",
        "dictValue": "1",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:21:12",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 137,
        "dictSort": 2,
        "dictLabel": "抖音",
        "dictValue": "2",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:21:21",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 138,
        "dictSort": 3,
        "dictLabel": "西瓜视频",
        "dictValue": "3",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:21:29",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 139,
        "dictSort": 4,
        "dictLabel": "YouTube",
        "dictValue": "4",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:22:06",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 140,
        "dictSort": 5,
        "dictLabel": "知乎",
        "dictValue": "5",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:22:17",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 141,
        "dictSort": 6,
        "dictLabel": "小红书",
        "dictValue": "6",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:22:27",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 142,
        "dictSort": 7,
        "dictLabel": "快手",
        "dictValue": "7",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:22:49",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 143,
        "dictSort": 8,
        "dictLabel": "Acfun",
        "dictValue": "8",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:23:05",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 144,
        "dictSort": 9,
        "dictLabel": "网易云音乐",
        "dictValue": "9",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:23:34",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 145,
        "dictSort": 10,
        "dictLabel": "豆瓣",
        "dictValue": "10",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:23:52",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 146,
        "dictSort": 11,
        "dictLabel": "微博",
        "dictValue": "11",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:24:34",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 147,
        "dictSort": 12,
        "dictLabel": "新片场",
        "dictValue": "12",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }, {
        "searchValue": null,
        "createBy": "admin",
        "createTime": "2021-03-26 02:24:50",
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "params": {},
        "dictCode": 148,
        "dictSort": 13,
        "dictLabel": "图虫",
        "dictValue": "13",
        "dictType": "media_platform",
        "cssClass": null,
        "listClass": null,
        "isDefault": "N",
        "status": "0",
        "default": false
      }]
    },
    data() {
      return {
        // 表单参数
        form: {
          personWebId: null,
          personWebName: null,
          personWebPwd: null,
          personWebPic: null,
          personWebShow: "0",
          personWebLink: "",
          personWebPlatform: [],
          personWebField: [],
          personWebInfo: "",
          personWebKey: ""
        },
        /*图像上传相关*/
        upload: {
          // 显示上传图片的弹出框
          cropperShow: false,
          // 图标路径
          logo_url: process.env.VUE_APP_BASE_API + "/common/upload",
        },
        // 图片预览框
        picVisible: false, //初始为false，登上上传图片成功后设置为true，显示该按钮
        // 拼接
        answerPicImageUrl: "",
        // 路径
        person_pic_url: process.env.VUE_APP_BASE_API + "/profile/nosql/",
        // 临时标签列表
        personWebPlatformTemp: undefined,
        personWebFieldTemp: undefined,

        // 具体内容
        inputVisible: false,
        inputValue: '',
        personWebShowOptions: [],  //是否展示选项
        personWebPlatformOptions: [], //活跃平台选项

        //注册表单校验
        rules: {
          personWebName: [{
            required: true,
            message: '成员名称为必填项',
            trigger: 'blur'
          }],
          personWebPwd: [{
            required: true,
            message: '成员密码为必填项',
            trigger: 'blur'
          }],
          personWebPic: [{
            required: true,
            message: '成员头图为必填项',
            trigger: 'change'
          }],
          personWebShow: [{
            required: true,
            message: '是否展示为必填项',
            trigger: 'change'
          }]
        }

      }


    },
    methods: {
      /*常驻领域添加tag*/
      handleClose(tag) {
        this.form.personWebField.splice(this.form.personWebField.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.form.personWebField.push(inputValue);
          console.log(inputValue);
          console.log(this.form.personWebField);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      // 显示图片上传模块
      imageCropperShow() {
        this.upload.cropperShow = !this.upload.cropperShow
      },
      // 图片上传成功后执行
      cropUploadSuccess(jsonData, field) {
        this.form.personWebPic = jsonData.fileName
         console.log("图片上传成功")
      },
      picShow(pic) {
        console.log(pic);
        this.picVisible = !this.picVisible;
        console.log(this.picVisible );
        this.answerPicImageUrl = this.person_pic_url + pic
        console.log(this.answerPicImageUrl);
      },
      // 取消按钮
      cancel() {
        //this.open = false;
        this.reset();
      },
      // 取消按钮
      cancelConnect() {
        //this.openConnect = false;
        this.resetConnect();
      },
      // 表单重置
      reset() {
        this.form = {
          personWebId: null,
          personWebName: null,
          personWebPwd: null,
          personWebPic: null,
          personWebShow: "0",
          personWebLink: "",
          personWebPlatform: [],
          personWebField: [],
          personWebInfo: "",
          personWebKey: ""
        };
        this.answerPicImageUrl = null;
        this.resetForm("form");
      },

      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {


            getPersonWebListByName(this.form.personWebName).then(response => {
              console.log(response);
              if (response.rows.length>0) {
                this.$message({
                  type:"success",
                  message:"用户名已经存在"
                })

              }else{
                //this.open = false;
                this.personWebPlatformTemp = this.form.personWebPlatform;
                this.form.personWebPlatform = JSON.stringify(this.personWebPlatformTemp);
                this.personWebFieldTemp = this.form.personWebField;
                this.form.personWebField = JSON.stringify(this.personWebFieldTemp);
                console.log(this.form.personWebField);
                addPersonWeb(this.form).then(response => {
                  if (response.code === 200) {
                    this.msgSuccess("注册成功");
                    this.$message({
                      type:"success",
                      message:"注册成功"
                    })
                    //this.refresh();
                    this.$router.push({
                      path: "/user"
                    })
                  }
                });
              }
            });



          }
        });
      }
    }
  }
</script>

<style scoped lang="scss">
  $bg: #283443;

  $light_gray: #fff;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  .container {
    width: 100%;
    overflow: hidden;
    background: url("~@/assets/bg5.gif") no-repeat;
    background-size: cover;
    height: 1200px;
  }
  /* reset element-ui css */
  .register-container {
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
  .register-container {

    width: 70%;
    height: 60%;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;

    .register-form {
      position: relative;
      width: 520px;
      height: 850px;
      max-width: 100%;
      padding: 25px 35px 0;
      margin: 0 auto;
      overflow: hidden;

    }
    .input-new-tag {
      width: 90px;
      margin-left: 10px;
      vertical-align: bottom;
    }
    .button-new-tag {
      margin-left: 10px;
      height: 32px;
      line-height: 30px;
      padding-top: 0;
      padding-bottom: 0;
    }


  }
</style>
