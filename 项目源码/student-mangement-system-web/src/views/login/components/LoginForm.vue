<template>
<el-form
    ref="ruleFormRef"
    :model="ruleForm"
    :rules="rules">

  <!-- input: 用户名 -->
  <el-form-item label="" prop="username">
    <el-input placeholder="请输入用户名" autoComplete="on" style="position: relative" v-model="ruleForm.username">
      <template #prefix>
        <el-icon class="el-input__icon"><UserFilled /></el-icon>
      </template>
    </el-input>
  </el-form-item>

  <!-- input: 密码 -->
  <el-form-item label="" prop="password">
    <el-input
        placeholder="请输入密码"
        autoComplete="on"
        v-model="ruleForm.password"
        :type="passwordType">
      <template #prefix>
        <el-icon class="el-input__icon"><GoodsFilled /></el-icon>
      </template>
      <template #suffix>
        <div class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
        </div>
      </template>
    </el-input>
  </el-form-item>

  <!-- button: 登录/注册 -->
  <el-form-item style="width: 100%">

    <el-button
        :loading="loading_register"
        class="secondary-btn"
        color="#ffffff80"
        style="border: 1px solid rgb(94, 161, 228);"
        @click="navigateToRegister">
      我要注册
    </el-button>

    <el-button
        :loading="loading"
        class="primary-btn"
        type="success"
        color="#c41717cc"
        @click="submitForm(ruleFormRef)">
      登录
    </el-button>

  </el-form-item>

</el-form>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue'
  import type { FormInstance } from 'element-plus'
  import { ElNotification } from "element-plus";
  import { useRouter } from 'vue-router'
  import { loginApi } from '../../../api/login/login'
  import { useUserStore } from '../../../store/modules/user'
import { id } from 'element-plus/es/locale';
  const router = useRouter()
  const ruleFormRef = ref<FormInstance>()
  const passwordType = ref('password')
  const loading = ref(false)
  const loading_register = ref(false)
  const rules = reactive({
    password: [{ required: true, message: "请输入密码", trigger: "blur" }],
    username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  })
  // 表单数据
  const ruleForm = reactive({
    username: 'admin',
    password: '123456',
  })
  // 显示密码图标
  const showPwd = () => {
    passwordType.value = passwordType.value === 'password'?'':'password'
  }

  const userStore = useUserStore()

  // 导航：前往注册
  function navigateToRegister() {
    loading_register.value = true
    router.push({
      path: '/register',
    })
  }

  // 提交：登录表单
  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(async(valid) => {
      if (valid) {
        loading.value = true
        // 登录
          const { data } = await loginApi({ ...ruleForm });
          if(data.status===200){
            login(data)
          }else {
            ElNotification({
              title: '温馨提示',
              message: data.message,
              type: "error",
              duration: 3000
            });
            loading.value = false
          }
      } else {
        console.log('error submit!')
        loading.value = false
        return false
      }
    })
  }

// 登录到首页
async function login(data): Promise<void> {
  const value = await setDatas(data);
  console.log(value);
  
  router.push({
    path: '/index',
  })
  console.log("[Login] Push to Index");

  ElNotification({
    title: '登录成功',
    message: "欢迎登录 高校教学管理与服务平台",
    type: "success",
    duration: 3000
  })
}

// 设置用户数据
async function setDatas(data):Promise<string> {
  userStore.setToken(data.result.token)
  userStore.setUserInfo({
    username: data.result.username,
    realname: data.result.realname,
    email: data.result.email,
    sex: data.result.sex,
    userIcon: data.result.userIcon,
    createTime: data.result.createTime,
    role: data.result.role,
    id: data.result.id
  })
  return "[Login] Set Datas"
}

</script>

<style scoped>
  .primary-btn{
    margin-top: 20px;
    margin-left: 6%;
    width: 67%; height: 47px;
  }
  .secondary-btn{
    margin-top: 20px;
    width: 27%; height: 47px;
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    cursor: pointer;
    user-select: none;
  }
  ::v-deep(.svg-icon){
    vertical-align: 0;
  }
</style>
