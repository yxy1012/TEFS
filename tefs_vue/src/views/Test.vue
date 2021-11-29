<template>
    <div>
      <el-table :data="tableData" >
        <el-table-column prop="userId" label="用户id" width="250" align="center">
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="250" align="center">
        </el-table-column>
        <el-table-column prop="userRealname" label="姓名" width="250" align="center">
        </el-table-column>
        <el-table-column prop="phone" label="电话"  align="center">
        </el-table-column>
      </el-table>
    </div>
</template>

<script>
  export default {

    created () {
      const _this = this
      axios.get('http://localhost:8181/findAllUser').then(function (resp) {
        _this.tableData=resp.data
      })
    },
    data(){
      return{
        tableData: [{
          userId: '1',
          userName: '1',
          userRealname: '1',
          phone: '1',
        }]
      }
    }
  }
</script>

<style scoped>
</style>

