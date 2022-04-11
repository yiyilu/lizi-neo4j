<template>
  <div class="table">
    <!--这一部分是表格，即列出成员的信息-->
    <el-table v-loading="loading"  style="width: 100%" >
      <!--第二列，成员昵称列-->
      <el-table-column label="成员昵称" align="center" prop="personWebName" :show-overflow-tooltip="true">
        <!--slot-scope="scope"是来取得作用域插槽：data绑定的数据，scope可以随便替换其他名称，只是定义对象来代表取得的data数据，便于使用-->
        <template slot-scope="scope">
          <!--scope.row.personWebLink表示取出personWebLink的属性值-->
          <a v-if="scope.row.personWebLink" target="_blank" :href="scope.row.personWebLink" class="link-type"
             style="margin-right: 10px;">
            <span>{{ scope.row.personWebName }}</span>
          </a>
          <span v-else>{{ scope.row.personWebName }}</span>
        </template>
      </el-table-column>
      <!--第三列，成员简介-->
      <el-table-column label="成员简介" align="center" prop="personWebInfo" :show-overflow-tooltip="true"/>
      <!--第四列，成员头像-->
      <el-table-column label="成员头像" align="center">
        <template slot-scope="scope">
          <img :src="person_pic_url + scope.row.personWebPic" alt="" class="pic-in-list" width="120" height="120">
        </template>
      </el-table-column>
      <!--第五列，活跃平台-->
      <el-table-column label="活跃平台" align="center" prop="personWebPlatform" :formatter="personPlatformFormat">
      </el-table-column>
      <!--第六列，常驻领域-->
      <el-table-column label="常驻领域" align="center" prop="personWebField">
        <template slot-scope="scope">
          <el-tag v-for="ikey in scope.row.personWebField" :key="ikey" type="success" style="margin: 10px;">
            {{ikey}}
          </el-tag>
        </template>
      </el-table-column>
      <!--第七列，沟通要旨-->
      <el-table-column label="沟通要旨" align="center" prop="personWebKey" :show-overflow-tooltip="true"/>

      <!--第九列，操作-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handle(scope.row)"
          >认识一下
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleRefuse(scope.row)"
          >不认识
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageform.currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageform.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pageform.peopleNum">
    </el-pagination>
  </div>
</template>

<script>
  import {
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
    export default {
      name: "UserApply",
      created() {
        this.getList();
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
          loading: true,
          personList: [],
          personMap: [],
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
            personWebRequest:[],
            personWebReceive:[],
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
        getList() {
          this.loading = true;
          getPersonWebList().then(response => {
            this.personList = response.rows;
            Object.keys(this.personList).forEach(key => {
              this.personList[key].personWebPlatform = JSON.parse(this.personList[key].personWebPlatform)
              this.personList[key].personWebField = JSON.parse(this.personList[key].personWebField)
            });
            this.loading = false;
          });
        },
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

<style scoped>

</style>
