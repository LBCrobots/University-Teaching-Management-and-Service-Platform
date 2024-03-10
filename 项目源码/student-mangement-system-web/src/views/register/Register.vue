<template>
    <div class="login-container">
      <div class="login-box">
        <!--登录表单 start-->
        <div class="login-form">
          <!--标题和logo start-->
          <div class="login-title">
            <!-- <h2 class="title">高校教学管理与服务平台</h2> -->
            <h2 class="title">注册用户</h2>
          </div>
          <!--标题和logo end-->
          <!--登录表单组件 start-->
          <RegisterForm/>
          <!--登录表单组件 end-->
        </div>
        <!--登录表单 end-->
      </div>
    </div>
</template>

<script setup lang="ts">
import RegisterForm from './components/RegisterForm.vue'
</script>

<style scoped>
  .login-container {
    background-color: #ffffff;
    height: 100vh;
    width: 100%;
    overflow: hidden;
    display: flex;
    background-image: url("../../assets/login/background2.png");
    justify-content: center;
    align-items: center;
    padding: 25px 25px;
    box-sizing: border-box;
  }
  .login-box {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .login-left{
    width: 50%;
  }
  .login-left img{
    width: 100%;
    max-width: 900px;
  }

  .login-form{
    max-width: 480px;
    width: 50%;
    padding: 40px;
    background-color: #ffffffbe;
    border-radius: 10px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    box-sizing: border-box;
  }

  .login-title{
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-bottom: 30px;
  }

  .login-title .title{
    margin: 0;
    font-size: 25px;
    white-space: nowrap;
    border-bottom: 3px solid rgb(94, 161, 228);
  }
  .login-title .icon{
    width: 60px;
  }

  ::v-deep(.el-input__inner){
    height: 40px;
  }

</style>
