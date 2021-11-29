<template>
  <div class="login">
    <!-- 登录面板 -->
    <div class="login-box">
      <div class="login-box-title">

        教学视频语音提取文本系统

      </div>
      <div class="login-box-from">
        <el-form :model="loginForm" :rules="rules" ref="loginForm"  class="demo-ruleForm">
          <el-form-item prop="identity">
            <el-button slot="prepend" icon="el-icon-s-check"></el-button>
            <el-radio-group v-model="loginForm.identity">
              <el-radio label="1" style="width: 65px"><b>用户</b></el-radio>
              <el-radio label="2" style="width: 75px"><b>管理员</b></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" size="medium">
              <el-button slot="prepend" icon="el-icon-user"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input :type="passw" v-model="loginForm.password"  placeholder="请输入密码" size="medium" @keyup.enter.native="submitForm('loginForm')">
              <i slot="suffix" :class="icon" @click="showPass"></i>
              <el-button slot="prepend" icon="el-icon-key"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="medium"  style="width:100%" @click="submitForm('loginForm')">立即登录</el-button>

          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="medium"  style="width:100%" @click="jump" v-if="loginForm.identity==1">用户注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="back">
    <router-link to='/'>
      <el-button round icon="el-icon-back"></el-button>
    </router-link>
    </div>
  </div>

</template>

<script>
  export default {
    data(){
      let letterRule = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('输入内容不能为空'));
        } else{
          callback();
        }
      };
      return {
        //用于改变Input类型
        passw:"password",
        //用于更换Input中的图标
        icon:"el-input__icon el-icon-view",
        loginForm:{  // 登陆表单
          username: '',
          password: '',
          identity:'1',
          id:''
        },
        rules:{  //登陆验证规则
          username:[
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' },
            { validator: letterRule, trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' },
            { validator: letterRule, trigger: 'blur' }
          ]
        }
      }
    },
    methods:{
      //密码的隐藏和显示
      showPass(){
        //点击图标是密码隐藏或显示
        if( this.passw=="text"){
          this.passw="password";
          //更换图标
          this.icon="el-input__icon el-icon-view";
        }else {
          this.passw="text";
          this.icon="el-input__icon el-icon-loading";
        }
      },
      jump(){
          this.$router.push({path: '/userregister'})
      },
      submitForm(formName){
        const _this=this
        this.$refs[formName].validate((valid) => {
          if (valid) {
            axios.get('http://localhost:8181/checklogin/'+this.loginForm.identity+'/'+this.loginForm.username+'/'+this.loginForm.password+'').then(function (resp) {
              console.log(resp)
              if(_this.loginForm.identity==1&&resp.data.login){
                sessionStorage.setItem('token',resp.data.token)
                sessionStorage.setItem('identity',_this.loginForm.identity)
                sessionStorage.setItem('userId',resp.data.id)
                _this.$store.commit('setUserId',resp.data.id)
                _this.$store.commit('setUserName',resp.data.name)
                _this.$router.push({
                  path:'/userlayout',
                })
              }else if(_this.loginForm.identity==2&&resp.data.login){

                sessionStorage.setItem('token',resp.data.token)
                sessionStorage.setItem('identity',_this.loginForm.identity)
                _this.$store.commit('setAdminId',resp.data.id)
                _this.$store.commit('setAdminName',resp.data.name)
                _this.$router.push({
                  path:'/adminlayout',
                })
              } else{
                _this.$alert('用户名或密码输入错误','提示');
              }
            })
          }else{
            _this.$alert('用户名或密码格式错误','提示');
          }
        });
      },
    }
  }
</script>

<style scoped>
  .login-box{
    width: 350px;
    background: hsla(0,0%,100%,.3);
    border-radius: 5px;
    box-shadow: 0 0 2px #f7f7f7;
    border: 1px solid #f7f7f7;
    position: absolute;
    left: 50%;
    top: 50%;
    margin-left: -175px;
    margin-top: -225px;

  }
  .login-box-title{
    line-height: 50px;
    font-size: 20px;
    text-align: center;
  }
  .login-box-from{
    width: 100%;
    height: auto;
    padding: 30px;
    box-sizing: border-box;
  }
  b{
    color: orange;
  }
  .back{
    position: relative;
    top:500px;
  }
</style>

