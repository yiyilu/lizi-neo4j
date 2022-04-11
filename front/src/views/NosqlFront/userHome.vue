<template>
  <div>
    <!--头部-->
    <UserHeader/>
    <!--主体-->
    <div>
      <UserAside/>
      <router-view style="flex: 1"/>
    </div>
  </div>

</template>

<script>
    import UserHeader from "./UserHeader";
    import UserAside from "./UserAside";
    export default {
        name: "userHome",
      components: {UserAside, UserHeader}
    }
</script>

<style scoped>

</style>
