<template>
  <div>
    <el-container>
      <el-menu
        default-active="1"
        class="el-menu-vertical-demo"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-menu-item index="/videobase">
          <i class="el-icon-folder"></i>
          <span slot="title">视频库</span>
        </el-menu-item>
        <el-menu-item index="/audioeditor">
          <i class="el-icon-scissors"></i>
          <span slot="title">语音处理</span>
        </el-menu-item>
        <el-menu-item index="/userecharts">
          <i class="el-icon-pie-chart"></i>
          <span slot="title">统计和审计</span>
        </el-menu-item>
      </el-menu>
      <el-container>
        <el-header style="text-align: right; font-size: 12px;position: relative;top:-50px;">
          <el-menu  class="el-menu-demo" mode="horizontal" @select="handleSelect"  router style="width: 1200px;">
            <el-menu-item index="/userlayout" class="item"><i class="el-icon-s-home" style="font-size: 25px;" ></i></el-menu-item>
            <el-menu-item class="item"><i class="el-icon-share" style="font-size: 25px;"></i></el-menu-item>
            <el-menu-item  class="item"><i class="el-icon-set-up" style="font-size: 25px;"></i></el-menu-item>
            <el-submenu index="4" class="item">
              <template slot="title"><i class="el-icon-user" style="font-size: 25px ;"></i>{{this.$store.getters.getUserName}}</template>
              <el-menu-item index="/login" @click="loginOut">退出登录</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-header>
        <el-main style="height: 600px">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  export default {
    methods:{
      loginOut(){
        this.$store.commit('setUserId',null)
        this.$store.commit('setUserName',null)
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('identity')
      },
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style scoped>
  .el-menu-vertical-demo{
    position: relative;
    top:-55px;
    height: 730px;
    width:230px;
  }
  .item{
    position: relative;
    left: 900px;
  }
</style>
