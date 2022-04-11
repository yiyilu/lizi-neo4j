<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; background-color: #161619">
    <div style="width: 300px; padding-left: 30px; font-weight: bold; color: dodgerblue">寻找每一颗宇宙中的粒子</div>
    <div style="flex: 2"></div>
    <div style="padding-right: 10px"> <el-tag @click="goFollowed">关注数量：{{this.followed}}</el-tag></div>
    <div style="padding-right: 10px"><el-tag @click="goFollower">粉丝数量：{{this.followers}}</el-tag></div>
    <div style="width: 200px; padding-left: 50px" >
      <!--右上角下拉框-->
      <el-dropdown>
      <span class="el-dropdown-link">
      {{user.personWebName}}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleUpdate">个人信息</el-dropdown-item>
          <el-dropdown-item @click.native="toLogin">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
    <!--修改模块-->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="user" :model="user" :rules="rules" label-width="80px">
        <el-form-item label="成员昵称" prop="personWebName">
          <el-input v-model="user.personWebName" placeholder="请输入成员昵称"/>
        </el-form-item>
        <el-form-item label="成员密码" prop="personWebPwd">
          <el-input v-model="user.personWebPwd" type="textarea" placeholder="请输入成员密码"/>
        </el-form-item>
        <el-form-item label="成员简介" prop="personWebInfo">
          <el-input v-model="user.personWebInfo" type="textarea" placeholder="请输入成员简介"/>
        </el-form-item>
        <el-form-item label="跳转链接" prop="personWebLink">
          <el-input v-model="user.personWebLink" placeholder="请输入跳转链接"/>
        </el-form-item>
        <el-form-item label="成员头像" prop="personWebPic">
          <el-button v-show="!user.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            上传图片
          </el-button>
          <el-button v-show="user.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            更新图片
          </el-button>
          <el-button v-show="user.personWebPic" type="primary" size="mini" @click="picShow(user.personWebPic)">
            预览
          </el-button>
        </el-form-item>
        <el-form-item label="常驻领域" prop="personWebField">
          <el-tag
            :key="admin"
            v-for="(tag, index) in user.personWebField"
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
          <el-input v-model="user.personWebKey" type="textarea" placeholder="请输入沟通要旨"/>
        </el-form-item>

        <el-form-item label="状态设置">
          <el-radio-group v-model="user.personWebShow">
            <el-radio
              v-for="dict in personWebShowOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--个人信息修改中的“图片预览”-->
    <el-dialog title="图片预览" :visible.sync="picVisible" width="500px" center>
      <div style="text-align: center">
        <img :src="answerPicImageUrl" alt="" class="pic-in-dialog">
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="picVisible = false">确定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import 'babel-polyfill'; // es6 shim
  import myUpload from 'vue-image-crop-upload';
  import {
    getOutNeighbors,
    getInNeighbors,
    getPersonWebMapFrom,
    getPersonWebMap,
    getPersonWebMapFromTo,
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

  export default {
    name: "UserHeader",
    components: {myUpload},  //注册头像上传组件
    filters: {
      statusShowFilter(status) {
        const statusMap = {
          0: '正常',
          1: '已下线',
        }
        return statusMap[status]
      },
      statusFilter(status) {
        const statusMap = {
          0: 'success',
          1: 'warning',
        }
        return statusMap[status]
      }
    },
    created() {
      this.user = JSON.parse(sessionStorage.getItem("user")) //获取登录信息,要用JSON来转换一下}
      this.getOutNeighbors()
      this.getInNeighbors()
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
      }];
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
      }]
    },
    data() {
      return {
        user: {
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
        upload: {
          // 显示上传图片的弹出框
          cropperShow: false,
          // 图标路径
          logo_url: process.env.VUE_APP_BASE_API + "/common/upload",
        },
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        //关注数量
        followed:null,
        //粉丝数量
        followers:null,
        //标签输入
        inputVisible: false,
        // 具体内容
        inputValue: '',
        personWebShowOptions: [],
        personWebPlatformOptions: [],
        personList: [],
        personMap: [],
        // 临时标签列表
        personWebPlatformTemp: undefined,
        personWebFieldTemp: undefined,
        // 路径
        person_pic_url: process.env.VUE_APP_BASE_API + "/profile/nosql/",
        // 图片预览框
        picVisible: false,
        // 拼接
        answerPicImageUrl: "",
        // 路径
        // 遮罩层
        loading: true,
        // 表单校验
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
      cancel() {
        this.open = false;
      },
      // 显示图片上传模块
      imageCropperShow() {
        console.log("显示图片上传模块")
        this.upload.cropperShow = !this.upload.cropperShow
        console.log(this.upload.cropperShow)
      },
      // 图片上传成功后执行
      cropUploadSuccess(jsonData, field) {
        this.user.personWebPic = jsonData.fileName
        // console.log(jsonData)
      },
      picShow(pic) {
        console.log(pic);
        this.picVisible = !this.picVisible;
        console.log(this.picVisible);
        this.answerPicImageUrl = this.person_pic_url + pic
        console.log(this.answerPicImageUrl);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleClose(tag) {
        this.user.personWebField.splice(this.user.personWebField.indexOf(tag), 1);
      },

      /** 修改信息操作 */
      handleUpdate() {
        this.open = true;
        this.title = "修改个人信息";
      },
      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.user.personWebField.push(inputValue);
          console.log(inputValue);
          console.log(this.user.personWebField);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      /** 退出，重新返回到登录页 */
      toLogin() {
        sessionStorage.removeItem("user") //删掉user记录
        // console.log(sessionStorage.getItem("user"))
        this.$router.push({path: '/'})
      },
      goFriends() {
        this.$router.push({path: '/layout/user'})
      },
      submitForm() {
        this.$refs["user"].validate(valid => {
          if (valid) {
            this.open = false;
            this.personWebPlatformTemp = this.user.personWebPlatform;
            this.user.personWebPlatform = JSON.stringify(this.personWebPlatformTemp);
            this.personWebFieldTemp = this.user.personWebField;
            this.user.personWebField = JSON.stringify(this.personWebFieldTemp);
            if (this.user.personWebId != null) {
              editPersonWeb(this.user).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.refresh();
                }
              });
            }
          }
        });
      },

      async getInNeighbors() {
        this.loading = true;
        // console.log(this.user.personWebName)
        await getInNeighbors(this.user.personWebName).then(response => {
          this.followers = response.rows.length;
          this.loading = false;
        });
      },
      async getOutNeighbors() {
        this.loading = true;
        // console.log(this.user.personWebName)
        await getOutNeighbors(this.user.personWebName).then(response => {
          this.followed = response.rows.length;
          this.loading = false;
        });
      },
      goFollowed(){
        this.$router.push({ path: '/layout/userFollowed' })
      },
      goFollower(){
        this.$router.push({ path: '/layout/userFollower' })
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
    width: 100%;
    /*//min-height: 400 px;*/
    height: 100%;
  }
</style>
