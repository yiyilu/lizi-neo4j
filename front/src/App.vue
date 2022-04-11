<template>
  <div id="app">
    <!--背景音乐，切换页面不会中断-->
    <audio class="audio" src="@/assets/bgmusic.mp3" controls autoplay="autoplay" loop="loop"
           :hidden="true"></audio>
    <!--动画切换效果-->
    <transition name="slide-fade">
      <!--路由出口-->
      <router-view/>  &lt;!&ndash;路由匹配到的组件将渲染到这里，这边是最外层的路由，也就是整个页面&ndash;&gt;
    </transition>


  </div>
</template>

<script>
  import UserHeader from "@/views/NosqlFront/UserHeader";
  import UserAside from "@/views/NosqlFront/UserAside";
export default {
  name: 'App',
  components: {UserAside, UserHeader},
}
</script>

<style>
  .slide-fade{
    position: fixed;left:0;right: 0;
    width: 100%;
    background-color: white;
  }
  .slide-fade-enter, .slide-fade-leave-to
  {
    left:0;top: 0;right: 0;
    position: absolute;
    transform:translateX(-500px) translateY(-500px) rotate(-150deg) scale(0.5);
    opacity:1;
  }
  .slide-fade-enter-active {
    background-color: white;
    transition: all 0.6s ease;
  }
  .slide-fade-leave-active {
    transition: all 0.7s ease;
    background-color: white;
    transform:translateX(500px) translateY(500px) rotate(150deg) scale(1) ;
    z-index: 100;
  }

</style>
