<!--1.相当于html模块-->
<template>
    <div>
      <!--这里只能有一对大div标签，也就是只能有一个根节点，但是可以嵌套-->
    {{msg}} <!--通过这种方式显示-->
    </div>
</template>

<!--2.想到与java script模块-->
<script>
    export default {
        name: "test",
        data(){   //data中存放的是json数据，可以显示出来
          return {
            msg:'Hello vue'
          }
        },
      /*初始化，里面的东西在页面创建的时候最先执行*/
      created(){
          alert("最先显示")
        /*保存一下当前vue对象*/
         const _this = this
        /*用axios获取后端数据，then是回调的意思，resp即后端传过来的数据*/
          axios.get('http://localhost:8081/error').then(function (resp) {
            //可以打印一下看看resp
            console.log(resp)
            /*这个_this表示的是当前的Vue,如果在这个区域写this，表示的是回调的this，不是vue*/
            _this.msg = resp.data

          })
      }
    }

</script>

<!--3、相当于css模块-->
<style scoped>

</style>
