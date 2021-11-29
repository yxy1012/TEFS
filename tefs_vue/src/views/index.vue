<template>
    <div>
      <div class="login">
      <router-link to='/login'>
        <el-button>登录</el-button>
      </router-link>
      </div>
      <h1 class="title">TEFS</h1>
    </div>
</template>

<script>
  export default {
    name: 'index'
  }
</script>

<style scoped>
.login{
  position: relative;
  right: -600px;
}
.title{
  font-size: 100px;
  padding: 100px;
}
</style>
