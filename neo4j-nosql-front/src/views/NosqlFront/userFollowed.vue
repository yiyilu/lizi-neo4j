<template>
  <div class="page-container">
    <!--这一部分是neo4j的图形展示-->
    <div class="echarts">
      <div class="bin" id="main"></div>
    </div>

    <div class="table">
      <!--这一部分是表格，即列出成员的信息-->
      <el-table v-loading="loading"  :data="personList" style="width: 100%" >
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
              @click="deleteConnect(scope.row)"
            >取关
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
  import {
    getPersonWebConnectId,
    getOutNeighbors,
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
    name: "userFollowed",
    created() {

      this.user=JSON.parse(sessionStorage.getItem("user")) //获取登录信息,要用JSON来转换一下
      //console.log(this.user)
      this.getOutNeighbors();
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
        "dictLabel": "已上架",
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
        "dictLabel": "未发布",
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
        //用户登录信息
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
        //图片路径
        // 路径
        person_pic_url: process.env.VUE_APP_BASE_API + "/profile/nosql/",
        //echarts中node的图片
        //symbol:[],
        from:'浅色回忆',
        to:'被炒的炒饭',
        // 遮罩层
        loading: true,
        isCollapse: true,
        personList: [],
        // 表单参数
        form: {},

        // 是否显示弹出层
        open: false,
        openConnect: false,

        // 弹出层标题
        title: "",
        titleConnect: "",
        // 临时标签列表
        personWebPlatformTemp: undefined,
        personWebFieldTemp: undefined,

        graph: {
          nodes: [],
          links: []
        },
        mapCheckList: [],
        personMap: [],
        myChart: undefined,
      };
    },
    methods:{
      //删除联系
      deleteConnect(row) {
        const from = this.user.personWebName
        const to = row.personWebName
         let connectId =""
        getPersonWebConnectId(from,to).then(response => {
          connectId = response.rows[0].connectWebId;
          console.log(connectId)
          this.loading = false;
        });
        const connectWebId = connectId
        this.$confirm('是否确认删除' , "警告", {
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
      //图片展示
      picShow(pic) {
        console.log(pic);
        this.picVisible = !this.picVisible;
        console.log(this.picVisible );
        this.answerPicImageUrl = this.person_pic_url + pic
        console.log(this.answerPicImageUrl);
      },
      // 字典翻译
      personShowFormat(row, column) {
        return this.selectDictLabel(this.personWebShowOptions, row.personWebShow);
      },
      personPlatformFormat(row, column) {
        return this.selectDictLabel(this.personWebPlatformOptions, row.personWebPlatform);
      },
     async getOutNeighbors() {
        this.loading = true;
       // console.log(this.user.personWebName)
       await getOutNeighbors(this.user.personWebName).then(response => {
          this.personList = response.rows;
          Object.keys(this.personList).forEach(key => {
            this.personList[key].personWebPlatform = JSON.parse(this.personList[key].personWebPlatform)
            this.personList[key].personWebField = JSON.parse(this.personList[key].personWebField)
          });
          this.loading = false;
        });
      },
      /*增删改查之后需要刷新一下，这个刷新是异步的*/
      async refresh() {
        await this.getOutNeighbors();
        this.myEcharts(); //更新echarts
        await this.getMap();
      },
      myEcharts() {
        const that = this;
        this.myChart.showLoading();
        //var symbol = [];
        this.graph.nodes.forEach(function (node) {
          node.label = {
            show: true
          };
        });

        //console.log(symbol)
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
                repulsion: 200,
                edgeLength: 100,
                gravity: 0.2
              },
              symbolSize: [55,55],
              /*symbol: 'image://'+ require('@/assets/avatar.jpg'),*/
              /*symbol:function(){
                console.log('image://'+require('/base/nosql/574596515969118208.jpg'))
                return 'image://'+require('/base/nosql/574596515969118208.jpg')

              },*/
/*
              symbol: ['image://'+require('/base/nosql/574596515969118208.jpg'),'image://'+require('/base/nosql/574601017375862784.jpg'),'image://'+require('/base/nosql/574605900321665024.jpg'),'image://'+require('/base/nosql/574607331829231616.jpg')],
*/
              //symbol:'circle',
              edgeSymbol : [ 'none', 'arrow' ], //边关系两边的标记
              data: this.graph.nodes,
              links: this.graph.links,
              // data: graph.nodes,
              // links: graph.links,
              roam: true,
              label: {
                show: true,
              },
              draggable: true,
              legendHoverLink : true,//是否启用图例 hover(悬停) 时的联动高亮。
              labelLayout: {
                hideOverlap: false
              },
              lineStyle: {
                color: "#a33996",
                curveness: 0.1,
                width : '4',  //边的粗细程度
              },
              itemStyle: {
                /*color: "#443e65",*/
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(30, 144, 255，0.5)'
                },

              },
              emphasis: {
                focus: 'adjacency',
                lineStyle: {
                  width: 15
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
      async getMap() {
        this.loading = true;
        await getPersonWebMapFrom(this.user.personWebName).then(response => {
          this.graph = {
            nodes: [],
            links: []
          };
          this.mapCheckList = [];
          this.personMap = response.rows;
          //console.log(response)
          //console.log(this.personMap)
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
         //console.log(this.graph)
        this.loading = false;
      },
      pushAlpha(key) {

        this.graph.nodes.push({
          id: this.personMap[key].PersonAlpha.personWebId,
          /*name: function(){
            console.log(this.personMap[key].PersonAlpha.personWebName)
            if(this.user.personWebName==this.personMap[key].PersonAlpha.personWebName){
              return "我";
            }else{
              return this.personMap[key].PersonAlpha.personWebName;
            }
          },*/
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
          /*name: function(){
            if(this.user.personWebName==this.personMap[key].PersonBeta.personWebName){
              return "我";
            }else{
              return this.personMap[key].PersonAlpha.personWebName;
            }
          },*/
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
  .echarts {
    background: url("~@/assets/bg4.gif") no-repeat;
    background-size:100% ;
    width: 100%;
    text-align: center;
  //height: 800px;
  }

  .table{
    background-color: #1f1e2d;
    width: 100%;
    //height: 100%;
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
  .bin {
    /*text-align: center;*/
    /*padding: 50px;*/
    width: 100%;
    height: 600px
  }



</style>
