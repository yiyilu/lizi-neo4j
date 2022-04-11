<!--1. 这一部分是模板，基于html语法-->
<template>
  <div class="app-container">
    <!--这一部分是neo4j的图形展示-->
    <div class="echarts">
      <div class="bin" id="main"></div>
    </div>

    <div class="table">

      <!--这一行代表新增成员、修改成员、新增关系这几个按钮-->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增成员
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
          >修改成员
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-plus"
            size="mini"
            @click="handleAddConnect"
          >新增关系
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="refresh"></right-toolbar>
      </el-row>
      <!--这一部分是表格，即列出成员的信息-->
      <el-table v-loading="loading" @selection-change="handleSelectionChange" :data="personList.slice((pageform.currentPage-1)*pageform.pageSize,pageform.currentPage*pageform.pageSize)"  style="width: 100%" >
        <!--第一列，表示复选框-->
        <el-table-column type="selection" width="55" align="center"/>
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
        <!--第八列，是否展示-->
        <el-table-column label="状态设置" align="center" width="100" prop="personWebShow" :formatter="personShowFormat">
          <template slot-scope="scope">
            <el-tag :type="scope.row.personWebShow | statusFilter">
              {{ scope.row.personWebShow | statusShowFilter}}
            </el-tag>
          </template>
        </el-table-column>
        <!--成员密码-->
        <el-table-column label="成员密码" align="center" prop="personWebPwd" :show-overflow-tooltip="true"/>


        <!--第九列，操作-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除
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


    <!--新增关系模块-->
    <el-dialog :visible.sync="openConnect" width="600px" append-to-body :title="titleConnect">
      <el-form ref="formConnect" :model="formConnect" :rules="rulesConnect" size="medium" label-width="100px">
       <!--关联下属，关联上级-->

        <el-row type="flex" justify="start" align="top">
          <el-col :span="12">
            <el-form-item
              label="关联实体A"
              prop="personWebIdAlpha"
              :rules="requireFlag ? rulesConnect.personWebIdAlpha:[{
                    required: false
                    }]"
            >

              <el-select
                :disabled="!requireFlag"
                v-model="formConnect.personWebIdAlpha"
                class="filter-item"
                filterable
                remote
                reserve-keyword
                clearable
                :placeholder="formConnect.personWebIdAlphaName"
                :remote-method="remoteMethodStart"
                :loading="selectLoading" value="">
                <el-option
                  v-for="item in optionsStart"
                  :key="item.personWebId"
                  :label="item.personWebName"
                  :value="item.personWebId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!--:rules="requireFlag ? rulesConnect.personWebIdBeta:[{
                required: false
              }]"-->
          <el-col :span="12">
            <el-form-item
              label="关联实体B"
              prop="personWebIdBeta"

            >

              <el-select
                :disabled="!requireFlag"
                v-model="formConnect.personWebIdBeta"
                class="filter-item"
                filterable
                remote
                reserve-keyword
                clearable
                :placeholder="formConnect.personWebIdBetaName"
                :remote-method="remoteMethodEnd"
                :loading="selectLoading" value="">
                <el-option
                  v-for="item in optionsEnd"
                  :key="item.personWebId"
                  :label="item.personWebName"
                  :value="item.personWebId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!--关联关系-->
        <el-form-item label="关联关系" prop="connectWebName">
          <el-input v-model="formConnect.connectWebName" placeholder="请输入关联关系" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
        <!--关联信息-->
        <el-form-item label="关联信息" prop="connectWebInfo">
          <el-input v-model="formConnect.connectWebInfo" type="textarea" placeholder="请输入关联信息"
                    :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
        </el-form-item>
      </el-form>
      <!--取消，确定按钮-->
      <div slot="footer">
        <el-button v-if="!requireFlag" type="danger" @click="deleteConnect">删除</el-button>
        <el-button @click="cancelConnect">取消</el-button>
        <el-button type="primary" @click="handelConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!--新增成员模块-->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="成员昵称" prop="personWebName">
          <el-input v-model="form.personWebName" placeholder="请输入成员昵称"/>
        </el-form-item>
        <el-form-item label="成员密码" prop="personWebPwd">
          <el-input v-model="form.personWebPwd" type="textarea" placeholder="请输入成员密码"/>
        </el-form-item>
        <el-form-item label="成员简介" prop="personWebInfo">
          <el-input v-model="form.personWebInfo" type="textarea" placeholder="请输入成员简介"/>
        </el-form-item>
        <el-form-item label="跳转链接" prop="personWebLink">
          <el-input v-model="form.personWebLink" placeholder="请输入跳转链接"/>
        </el-form-item>
        <el-form-item label="成员头像" prop="personWebPic">
          <el-button v-show="!form.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            上传图片
          </el-button>
          <el-button v-show="form.personWebPic" type="primary" size="mini" @click="imageCropperShow()">
            更新图片
          </el-button>
          <el-button v-show="form.personWebPic" type="primary" size="mini" @click="picShow(form.personWebPic)">
            预览
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
            :key="admin"
            v-for="(tag, index) in form.personWebField"
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
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
   <!--修改成员管理模块中的“图片预览”-->
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
  // import graph from '@/assets/binray/les-miserables';
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
  import 'babel-polyfill'; // es6 shim
  import myUpload from 'vue-image-crop-upload';  //头像图片上传组件

  export default {
    name: 'Echarts',
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
    data() {
      return {
        //分页设置
        pageform:{
          currentPage:1, //初始页
          peopleNum:0,  //people总数
          pageSize:10,  //每页显示条目数
        },

        // 新增or更新，新增为true，更新为false
        requireFlag: false,
        myChart: undefined,
        // 表单参数
        form: {},
        formConnect: {},
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
        },
        rulesConnect: {
          personWebIdAlpha: [{
            required: true,
            message: '',
            trigger: 'blur'
          }],
          personWebIdBeta: [{
            required: true,
            message: '',
            trigger: 'blur'
          }],
          connectWebName: [{
            required: true,
            message: '请输入关联关系',
            trigger: 'blur'
          }],
          connectWebInfo: [{
            required: true,
            message: '请输入关联信息',
            trigger: 'blur'
          }],
        },
        selectLoading: false,
        optionsStart: [],
        optionsEnd: [],
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
        // 选中数组
        ids: [],
        // 复选框非单个禁用
        single: true,
        // 复选框非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        upload: {
          // 显示上传图片的弹出框
          cropperShow: false,
          // 图标路径
          logo_url: process.env.VUE_APP_BASE_API + "/common/upload",
        },
        // 弹出层标题
        title: "",
        titleConnect: "",
        // 是否显示弹出层
        open: false,
        openConnect: false,
        inputVisible: false,
        // 具体内容
        inputValue: '',
        personWebShowOptions: [],
        personWebPlatformOptions: [],
        searchQueryParams: {
          // 这个一定得有
          personWebIdStart: null,
          // 这个可有可无
          personWebIdEnd: null,
        },
        graph: {
          nodes: [],
          links: []
        },
        mapCheckList: []
      }
    },
    created() {
      this.getList();
      //this.getListByPage();
      this.getListNum();
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
    methods: {
      /*分页*/

      handleSizeChange(val) {
        this.pageform.pageSize = val;
        //console.log(this.pageform.pageSize); //每页下拉显示数据
      },
      handleCurrentChange(val) {
        this.pageform.currentPage = val;
       // console.log(this.pageform.currentPage)  //点击第几页
      },
      myEcharts() {
        const that = this;
        this.myChart.showLoading();

        this.graph.nodes.forEach(function (node) {
          node.label = {
            show: true
          };
        });
        const option = {
          title: {
            text: '',
            subtext: '',
            top: 'bottom',
            left: 'right'
          },
          tooltip: {
            trigger: "item",
            // textStyle: {
            //   width: 10,
            //   fontWeight: "bold",
            //   overflow: "truncate"
            // },
            confine: 'true',
            formatter: function (param) {
              if (param.dataType === 'edge') {
                // 连接
                return [
                  '关系：' + param.data["connectWebName"] + '<br/>',
                  '详情：' + param.data["connectWebInfo"] + '<br/>'
                ].join('');
              } else if (param.dataType === 'node') {
                // 处理标签
                let graphTag = '';
                const arr1 = JSON.parse(param.data["personWebField"]);
                for (let i = 0; i < arr1.length; i++) {
                  if (i === 0) {
                    graphTag = `<span style="
                      display: inline-block;
                      border-radius: 4px;
                      min-width: min-content;
                      padding: 0 10px;
                      margin: 5px;
                      background-color: #e7faf0;
                      border-color: #d0f5e0;
                      color: #13ce66;
                      border-width: 1px;
                      border-style: solid;
                    ">${arr1[i]}</span>`
                  } else {
                    graphTag = graphTag + `<span style="
                      display: inline-block;
                      border-radius: 4px;
                      min-width: min-content;
                      padding: 0 10px;
                      margin: 5px;
                      background-color: #e7faf0;
                      border-color: #d0f5e0;
                      color: #13ce66;
                      border-width: 1px;
                      border-style: solid;
                    ">${arr1[i]}</span>`;
                  }
                }
                // 处理字典
                let graphDict = '';
                const arrTemp = that.selectDictLabel(that.personWebPlatformOptions, JSON.parse(param.data["personWebPlatform"]));
                const arr2 = arrTemp.split(', ')
                for (let i = 0; i < arr2.length; i++) {
                  if (i === 0) {
                    graphDict = `<span style="
                      display: inline-block;
                      border-radius: 4px;
                      min-width: min-content;
                      padding: 0 10px;
                      margin: 5px;
                      background-color: #faece7;
                      border-color: #f5dad0;
                      color: #ff7d27;
                      border-width: 1px;
                      border-style: solid;
                    ">${arr2[i]}</span>`
                  } else {
                    graphDict = graphDict + `<span style="
                      display: inline-block;
                      border-radius: 4px;
                      min-width: min-content;
                      padding: 0 10px;
                      margin: 5px;
                      background-color: #faece7;
                      border-color: #f5dad0;
                      color: #ff7d27;
                      border-width: 1px;
                      border-style: solid;
                    ">${arr2[i]}</span>`;
                  }
                }
                // 节点
                return [
                  '<div style="text-align:left;max-width:1000px;">昵称：' + param.data["name"] + '<br/>',
                  '<div style="display:block;word-break: break-all;word-wrap: break-word;white-space: pre-line;">简介：' + param.data["personWebInfo"] + '</div>',
                  '常驻领域：' + graphTag + '<br/>',
                  '活跃平台：' + graphDict + '</div><br/>',
                ].join('');
              }

            }
          },
          legend: [],
          animationDuration: 150,
          animationEasingUpdate: 'quinticInOut',
          series: [
            {
              name: '',
              type: 'graph',
              layout: "force",
              force: {
                repulsion: 200, //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大
                edgeLength: 100, //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
                gravity: 0.2  //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢
              },
              symbolSize: 55, //item大小
              //symbol:'imgae://'+require('../../assets/planet.gif'),
              edgeSymbol : [ 'none', 'arrow' ], //边关系两边的标记
              data: this.graph.nodes,
              links: this.graph.links,
              roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
              label: {
                show: true,  //展示节点上的标签
              },
              draggable: true, //节点是否可拖拽，只在使用力引导布局的时候有用。
              labelLayout: {
                hideOverlap: false
              },
              lineStyle: {
                color: "#a33996",
                curveness: 0.1,  //边的曲度
                width : '4',  //边的粗细程度
              },
              itemStyle: {  //这边用了自定义的头像图片，设置itemStyle不起效果
              },
              emphasis: {
                focus: 'adjacency', //邻接点边高亮
                lineStyle: {
                  width: 20         //高亮时边宽
                }
              }
            }
          ]
        };
        this.myChart.clear();
        option && this.myChart.setOption(option, true);
        // console.log(option)
        this.myChart.hideLoading();
        this.myChart.on("click", function (e) {
          if (e.dataType === 'edge') {
            that.handleUpdateConnect(e.data)
          } else if (e.dataType === 'node') {
            console.log(e)
            e.data.personWebId = e.data.id
            that.handleUpdate(e.data)
          }
        })
      },
      getListNum(){
        this.loading = true;
        getPersonWebList().then(response => {
          //console.log(response);
          this.pageform.peopleNum = response.rows.length;
          //console.log(this.pageform.peopleNum);
          this.loading = false;
        });
      },
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
      getListByPage() {
        this.loading = true;
        const pageNum = this.pageform.currentPage;
        const pageSize = this.pageform.pageSize;
        getPersonWebListByPage(pageNum,pageSize).then(response => {
          this.personList = response.rows;
          Object.keys(this.personList).forEach(key => {
            this.personList[key].personWebPlatform = JSON.parse(this.personList[key].personWebPlatform)
            this.personList[key].personWebField = JSON.parse(this.personList[key].personWebField)
          });
          this.loading = false;
        });
      },
      async getMap() {

        this.loading = true;
        await getPersonWebMap().then(response => {
          this.graph = {
            nodes: [],
            links: []
          };
          this.mapCheckList = [];
          this.personMap = response.rows;
          console.log(response)
          console.log(this.personMap)
          Object.keys(this.personMap).forEach(key => {
            // 导入节点，列表为空表示首次加入元素，反之亦然
            if (this.graph.nodes !== []) {
              // 如果导入过就不用重复导入了
              if (!this.mapCheckList.includes(this.personMap[key].PersonAlpha.personWebId)) {
                this.pushAlpha(key);
              }
              if (!this.mapCheckList.includes(this.personMap[key].PersonBeta.personWebId)) {
                this.pushBeta(key);
              }
            } else {
              this.pushAlpha(key);
              this.pushBeta(key);
            }
            // 把导入过的节点id存进checkList
            this.mapCheckList.push(this.personMap[key].PersonAlpha.personWebId)
            this.mapCheckList.push(this.personMap[key].PersonBeta.personWebId)
            // 导入关系
            this.pushConnect(key);
          });
        });
         console.log(this.graph)
        this.loading = false;
      },
      pushAlpha(key) {

        this.graph.nodes.push({
          id: this.personMap[key].PersonAlpha.personWebId,
          name: this.personMap[key].PersonAlpha.personWebName,
          personWebPic: this.personMap[key].PersonAlpha.personWebPic,
          personWebShow: this.personMap[key].PersonAlpha.personWebShow,
          personWebLink: this.personMap[key].PersonAlpha.personWebLink,
          personWebPlatform: this.personMap[key].PersonAlpha.personWebPlatform,
          personWebField: this.personMap[key].PersonAlpha.personWebField,
          personWebInfo: this.personMap[key].PersonAlpha.personWebInfo,
          personWebKey: this.personMap[key].PersonAlpha.personWebKey,
          //自己的头像作为symbol
          symbol: 'image://'+require('/base/nosql/'+this.personMap[key].PersonAlpha.personWebPic)
        })
      },
      pushBeta(key) {
        this.graph.nodes.push({
          id: this.personMap[key].PersonBeta.personWebId,
          name: this.personMap[key].PersonBeta.personWebName,
          personWebPic: this.personMap[key].PersonBeta.personWebPic,
          personWebShow: this.personMap[key].PersonBeta.personWebShow,
          personWebLink: this.personMap[key].PersonBeta.personWebLink,
          personWebPlatform: this.personMap[key].PersonBeta.personWebPlatform,
          personWebField: this.personMap[key].PersonBeta.personWebField,
          personWebInfo: this.personMap[key].PersonBeta.personWebInfo,
          personWebKey: this.personMap[key].PersonBeta.personWebKey,
          //自己的头像作为symbol
          symbol: 'image://'+require('/base/nosql/'+this.personMap[key].PersonBeta.personWebPic)
        })
      },
      pushConnect(key) {
        this.graph.links.push({
          source: this.personMap[key].PersonAlpha.personWebId,
          target: this.personMap[key].PersonBeta.personWebId,
          connectWebId: this.personMap[key].Connect.connectWebId,
          connectWebName: this.personMap[key].Connect.connectWebName,
          connectWebInfo: this.personMap[key].Connect.connectWebInfo,
        })
      },

      // 字典翻译
      personShowFormat(row, column) {
        return this.selectDictLabel(this.personWebShowOptions, row.personWebShow);
      },
      personPlatformFormat(row, column) {
        return this.selectDictLabel(this.personWebPlatformOptions, row.personWebPlatform);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 取消按钮
      cancelConnect() {
        this.openConnect = false;
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

      resetConnect() {
        this.formConnect = {
          personWebIdAlpha: null,
          personWebIdBeta: null,
          personWebIdAlphaName: null,
          personWebIdBetaName: null,
          connectWebId: null,
          connectWebName: null,
          connectWebInfo: null,
        };
        this.resetForm("formConnect");
      },

      handleAddConnect() {
        this.optionsStart = []
        this.optionsEnd = []
        this.requireFlag = true;
        this.resetConnect();
        this.openConnect = true;
        this.titleConnect = "添加成员关系";
      },

      /** 修改按钮操作 */
      handleUpdateConnect(row) {
        this.optionsStart = []
        this.optionsEnd = []
        this.requireFlag = false;
        this.reset();
        const connectWeb = row.connectWebId;
        getInfoConnectWeb(connectWeb).then(response => {
          this.formConnect = response.data;
          // console.log(this.formConnect)
          this.formConnect.personWebIdAlpha = null;
          this.formConnect.personWebIdBeta = null;
          this.openConnect = true;
          this.titleConnect = "修改成员关系";
        });
      },

      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.personWebId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },

      remoteMethodStart(keyword) {
        if (keyword.trim() === '') {
          this.optionsStart = []
          return
        }
        // 当后一个对象不为空的情况
        if (this.formConnect.personWebIdBeta) {
          let others = [];
          this.selectLoading = true;
          getPersonWebSearch(keyword).then(data => {
            getPersonWebSearchOther(this.formConnect.personWebIdBeta, keyword).then(dataOther => {
              others = dataOther.rows;
              this.optionsStart = [];
              data.rows.forEach(ma => {
                let exist = others.some(sa => {
                  return ma.personWebId === sa.personWebId
                });
                if (!exist) {
                  this.optionsStart.push(ma);
                }
              });
              this.optionsStart = this.optionsStart.filter(item => item.personWebId !== this.formConnect.personWebIdBeta)
            })
            this.selectLoading = false;
          })
        }
        // 后一个对象为空时
        else {
          this.selectLoading = true;
          getPersonWebSearch(keyword).then(data => {
            this.selectLoading = false;
            this.optionsStart = data.rows;
          })
        }
      },
      remoteMethodEnd(keyword) {
        if (keyword.trim() === '') {
          this.optionsEnd = []
          return
        }
        // 当前一个对象不为空的情况
        if (this.formConnect.personWebIdAlpha) {
          let others = [];
          this.selectLoading = true;
          getPersonWebSearch(keyword).then(data => {
            getPersonWebSearchOther(this.formConnect.personWebIdAlpha, keyword).then(dataOther => {
              others = dataOther.rows;
              this.optionsEnd = [];
              data.rows.forEach(ma => {
                let exist = others.some(sa => {
                  return ma.personWebId === sa.personWebId
                });
                if (!exist) {
                  this.optionsEnd.push(ma);
                }
              });
              this.optionsEnd = this.optionsEnd.filter(item => item.personWebId !== this.formConnect.personWebIdAlpha)
            })
            this.selectLoading = false;
          })
        }
        // 前一个对象为空时
        else {
          this.selectLoading = true;
          getPersonWebSearch(keyword).then(data => {
            this.selectLoading = false;
            this.optionsEnd = data.rows;
          })
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加成员管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const personWeb = row.personWebId || this.ids;
        getPersonWebInfo(personWeb).then(response => {
          this.form = response.data;
          this.personWebPlatformTemp = this.form.personWebPlatform;
          this.form.personWebPlatform = JSON.parse(this.personWebPlatformTemp);
          this.personWebFieldTemp = this.form.personWebField;
          this.form.personWebField = JSON.parse(this.personWebFieldTemp);
          this.open = true;
          this.title = "修改成員管理";
        });
      },
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.open = false;
            this.personWebPlatformTemp = this.form.personWebPlatform;
            this.form.personWebPlatform = JSON.stringify(this.personWebPlatformTemp);
            this.personWebFieldTemp = this.form.personWebField;
            this.form.personWebField = JSON.stringify(this.personWebFieldTemp);
            if (this.form.personWebId != null) {
              editPersonWeb(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.refresh();
                }
              });
            } else {
              addPersonWeb(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.refresh();
                }
              });
            }
          }
        });
      },

      handelConfirm() {
        this.$refs["formConnect"].validate(valid => {
          if (valid) {
            this.openConnect = false;
            if (this.formConnect.connectWebId != null) {
              editConnectWeb(this.formConnect).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.refresh();
                }
              });
            } else {
              addConnectWeb(this.formConnect).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.refresh();
                }
              });
            }
          }
        });
      },
      deleteConnect() {
        const connectWebId = this.formConnect.connectWebId
        this.$confirm('是否确认删除「' + this.formConnect.personWebIdAlphaName + '」和「' + this.formConnect.personWebIdBetaName + '」的关系？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return removeConnectWeb(connectWebId);
        }).then(() => {
          this.openConnect = false;
          this.refresh();
          this.msgSuccess("删除成功");
        }).catch(function (e) {
          console.log(e)
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const personWebIds = row.personWebId || this.ids;
        this.$confirm('是否确认删除成员编号为"' + personWebIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return removePersonWeb(personWebIds);
        }).then(() => {
          this.refresh();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      // 显示图片上传模块
      imageCropperShow() {
        this.upload.cropperShow = !this.upload.cropperShow
      },
      // 图片上传成功后执行
      cropUploadSuccess(jsonData, field) {
        this.form.personWebPic = jsonData.fileName
        // console.log(jsonData)
      },
      picShow(pic) {
        console.log(pic);
        this.picVisible = !this.picVisible;
        console.log(this.picVisible );
        this.answerPicImageUrl = this.person_pic_url + pic
        console.log(this.answerPicImageUrl);
      },
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
      /*增删改查之后需要刷新一下，这个刷新是异步的*/
      async refresh() {
        await this.getMap();
        this.myEcharts(); //更新echarts
        this.getList(); //更新list
        this.getListNum(); //更新条数
      },
    },

    async mounted() {
      await this.getMap();
      // 基于准备好的dom，初始化echarts实例
      let echarts = require('echarts')
      this.myChart = echarts.init(document.getElementById('main'));
      this.myEcharts();
    }
  }
</script>


<style lang="scss" scoped>

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 178px;
    height: 178px;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    line-height: 140px;
  }

  .avatar {
    height: 144px;
  }

  .image-preview {
    width: 178px;
    height: 178px;
    position: relative;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    float: left;
  }

  .image-preview .image-preview-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
  }
  .image-preview .image-preview-wrapper img {
    width: 100%;
    height: 100%;
  }

  .image-preview .image-preview-action {
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    text-align: center;
    color: #fff;
    opacity: 0;
    font-size: 20px;
    background-color: rgba(0, 0, 0, .5);
    transition: opacity .3s;
    cursor: pointer;
    line-height: 200px;
  }

  .image-preview .image-preview-action .el-icon-delete {
    font-size: 32px;
  }

  .image-preview:hover .image-preview-action {
    opacity: 1;
  }

  .el-upload--picture-card {
    display: block;
    width: 258px;
    height: 146px;
    overflow: hidden;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

 .table{
   background-color: #1f1e2d;
   width: 100%;
   height: 100%;
 }
 /* .table /deep/ .el-table--fit{
    padding: 20px;
  }*/
  .table /deep/  .el-table, .el-table__expanded-cell {
    background-color: transparent;
  }

  .table /deep/ .el-table tr{
    background-color: transparent!important;
  }
  /*.table /deep/  .el-table--enable-row-transition .el-table__body td, .el-table .cell{
    background-color: transparent;
  }*/

  .echarts {
    background: url("~@/assets/bg1.gif") no-repeat;
    background-size:100% ;
    width: 100%;
    text-align: center;
    //height: 800px;
  }

  .bin {
    /*text-align: center;*/
    /*padding: 50px;*/
    width: 100%;
    height: 800px
  }

  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  .graph-tag {
    display: inline-block;
    border-radius: 4px;
    min-width: min-content;
    padding: 5px;
    background-color: #e7faf0;
    border-color: #d0f5e0;
    color: #13ce66;
  }

  @media (max-width: 1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

</style>
