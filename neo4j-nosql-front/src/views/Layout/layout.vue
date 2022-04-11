<template>
  <div>
    <!--头部-->
    <UserHeader/>
    <!--主体-->
    <div style="display: flex">
      <UserAside/>
      <router-view style="flex: 1" />
    </div>
  </div>
</template>

<script>
    import UserHeader from "../NosqlFront/UserHeader";
    import UserAside from "../NosqlFront/UserAside";
    export default {
        name: "layout",
      components: {UserAside, UserHeader},

    }

</script>

<style scoped>

</style>
