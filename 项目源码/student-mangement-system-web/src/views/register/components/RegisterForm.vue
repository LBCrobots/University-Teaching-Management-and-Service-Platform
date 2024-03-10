<template>
<el-form
    ref="ruleFormRef"
    :model="registerRuleForm"
    :rules="rules">

  <!-- input: 姓名-->
  <el-form-item label="" prop="realname">
    <el-input placeholder="请输入真实姓名" autoComplete="on" style="position: relative" v-model="registerRuleForm.realname">
      <template #prefix>
        <el-icon class="el-input__icon"><User /></el-icon>
      </template>
    </el-input>
  </el-form-item>

  <!-- choose: 性别 & 身份-->
  <el-form-item label="" prop="sex">
    <el-select v-model="registerRuleForm.sex" 
      placeholder="请选择性别" size="large" style="width: 47%">
      <el-option
        v-for="item in sexOptions"
        :key="item.value"
        :label="item.label"
        :value="item.value"/>
      <template #prefix>
      </template>
    </el-select>

    <el-select v-model="registerRuleForm.sysRole.id"
      placeholder="请选择身份" size="large" style="width: 47%; margin-left: 6%;">
      <el-option
        v-for="item in roleOptions"
        :key="item.value"
        :label="item.label"
        :value="item.value"/>
      <template #prefix>
      </template>
    </el-select>
  </el-form-item>

  <!-- input: 邮箱 TODO-->
  <el-form-item label="" prop="email" >
    <el-input placeholder="请输入电子邮箱" autoComplete="on" style="position: relative" v-model="registerRuleForm.email">
      <template #prefix>
        <el-icon class="el-input__icon"><Message /></el-icon>
      </template>
    </el-input>
  </el-form-item>
  
  <!-- input: 用户名 -->
  <el-form-item label="" prop="username">
    <el-input placeholder="请输入用户名" autoComplete="on" style="position: relative" v-model="registerRuleForm.username">
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
        v-model="registerRuleForm.password"
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
        class="secondary-btn"
        color="#ffffff80"
        @click="navigateToLogin">
      返回登录
    </el-button>

    <el-button
        :loading="loading"
        class="primary-btn"
        type="success"
        color="#c41717cc"
        @click="submitForm(ruleFormRef)">
      注册
    </el-button>

    <!-- 测试按钮 -->
    <!-- <el-button
        class="secondary-btn"
        type="success"
        color="#c41717cc"
        @click="showLog">
      查看表单Log
    </el-button> -->
  </el-form-item>

</el-form>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue'
  import type { FormInstance } from 'element-plus'
  import { ElNotification } from "element-plus";
  import { useRouter } from 'vue-router'
  import { registerApi } from '../../../api/login/login'
  import { useUserStore } from '../../../store/modules/user'
  const router = useRouter()
  const ruleFormRef = ref<FormInstance>()
  const passwordType = ref('password')
  const loading = ref(false)
  // 下拉框选项
  const sexOptions = [
    {value:'男', label:'男'},
    {value:'女', label:'女'},
  ]
  const roleOptions = [
    {value:1, label:'系统管理员'},    
    {value:2, label:'老师'},    
    {value:3, label:'学生'},
  ]

  // 填写注冊表单检查规则
  const rules = reactive({
    username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
    password: [{ required: true, message: "请输入密码", trigger: "blur" }],
    realname: [{ required: true, message: "请输入真实姓名", trigger: "blur" }],
    sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
    email:[
      { required: true, message: '请输入邮箱', trigger: 'blur'},
      { type: 'email',  message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
    ],
  })
  // 表单数据 - 注册
  const registerRuleForm = reactive({
    username: '',
    password: '',
    realname: '',
    sex: '',
    status: 1,
    email: '',
    userIcon: null,
    sysRole: {id:3}
  })
  // 显示密码图标
  const showPwd = () => {
    passwordType.value = passwordType.value === 'password'?'':'password'
  }

  // 导航：返回登录
  function navigateToLogin() {
    router.push({
      path: '/login',
    })
  }
  // （检查表单状态）
  function showLog() {
    console.log(registerRuleForm)
    console.log(ruleFormRef)
    console.log('---')
  }

  const userStore = useUserStore()
  // 提交：注册表单（未测试）
  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(async(valid) => {
      if (valid) {
        loading.value = true
        // 注册
          const { data } = await registerApi({ ...registerRuleForm });
          if(data.status===200){
            // 前往登录
            await router.push({
              path: '/login',
            })
            ElNotification({
              title: '注册成功',
              message: "请使用您的账户登录 高校教学管理与服务平台",
              type: "success",
              duration: 10000
            })
          }else {
            ElNotification({
              title: '温馨提示',
              message: data.message,
              type: "error",
              duration: 10000
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
