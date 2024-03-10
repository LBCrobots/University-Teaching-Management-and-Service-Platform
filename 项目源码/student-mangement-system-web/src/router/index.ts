/**
 * @/router/index.ts
 * 定义页面路由 Vue Router
 */
import { createRouter, createWebHashHistory } from 'vue-router'
import NProgress from "../config/nprogress";
import { useUserStore } from '../store/modules/user'
import { useMenuStore } from '../store/modules/menu'

// 定义静态路由
export const staticRouter = [
    {
        path: '/',
        redirect: "/login",
        isMenu: false
    },
    {
        path: '/login',
        name: 'Login',
        meta: { title: '学生信息管理系统 - 登录'},
        component: ()=> import('../views/login/Login.vue'),
        isMenu: false

    },
    {
        path: '/register',
        name: 'Register',
        meta: { title: '注册'},
        component: ()=> import('../views/register/Register.vue'),
        isMenu: false

    },
    {
        path: '/index',
        name: 'index',
        component: ()=> import('../views/layout/Index.vue'),
        redirect: "/home",
        isMenu: true,
        funcNode:1,
        children: [{
            path: '/home',
            name: 'home',
            meta: { title: '首页', icon: 'House',affix: true },
            component: ()=> import('../views/home/Index.vue')
        }]

    },
    {
        path: '/user',
        name: 'UserSetting',
        redirect: '/user/setting',
        component: ()=> import('../views/layout/Index.vue'),
        isMenu: true,
        funcNode:1,
        children: [
            {
                path: 'setting',
                name: 'PersonalSettings',
                meta: { title: '个人设置', icon: 'Basketball'},
                component: ()=> import('../views/user/components/PersonalSettings.vue')
            }
        ]
    }
]

// 定义动态路由
export const asyncRoutes = [
    {
        path: '/system',
        name: 'system',
        meta: {
            title: '系统管理',
            icon: 'GoldMedal',
            role: ['ROLE_ADMIN']
        },
        redirect: '/system/user',
        component: ()=> import('../views/layout/Index.vue'),
        isMenu: true,
        funcNode:2,
        children: [
            {
                path: 'user',
                name: 'User',
                meta: {
                    title: '用户管理',
                    icon: 'UserFilled',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('../views/user/UserList.vue')
            },
            {
                path: 'role',
                name: 'Role',
                meta: {
                    title: '角色管理',
                    icon: 'Stamp',
                    role: []//['ROLE_ADMIN']隐藏角色管理功能
                },
                component: ()=> import('../views/role/RoleList.vue')
            }
        ]
    },
    {
        path: '/base',
        name: 'base',
        meta: {
            title: '数据管理',
            icon: 'DataAnalysis',
            role: ['ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT']
        },
        redirect: '/base/gradeclass',
        component: ()=> import('../views/layout/Index.vue'),
        isMenu: true,
        funcNode:2,
        children: [
            {
                path: 'gradeclass',
                name: 'gradeclass',
                meta: {
                    title: '班级管理',
                    icon: 'Box',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('../views/gradeclass/GradeClassList.vue')
            },
            {
                path: 'student',
                name: 'student',
                meta: {
                    title: '学生管理',
                    icon: 'User',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('../views/student/StudentList.vue')
            },
            {
                path: 'course',
                name: 'course',
                meta: {
                    title: '课程管理',
                    icon: 'Tickets',
                    role: ['ROLE_ADMIN']
                },
                component: ()=> import('../views/course/CourseList.vue')
            },
            {
                path: 'teacher',
                name: 'teacher',
                meta: {
                    title: '教师管理',
                    icon: 'Avatar',
                    role: ['ROLE_ADMIN','ROLE_TEACHER']
                },
                component: ()=> import('../views/teacher/TeacherList.vue')
            },
            {
                path: 'studentcourse',
                name: 'studentcourse',
                meta: {
                    title: '选课管理',
                    icon: 'Management',
                    role: ['ROLE_ADMIN','ROLE_STUDENT']
                },
                component: ()=> import('../views/studentcourse/StudentCourseList.vue')
            }
        ]
    },
    {
        path: '/scores',
        name: 'scores',
        meta: {
            title: '成绩管理',
            icon: 'GoldMedal',
            role: ['ROLE_ADMIN','ROLE_TEACHER']
        },
        redirect: '/scores/index',
        component: ()=> import('../views/layout/Index.vue'),
        isMenu: true,
        funcNode:2,
        children: [
            {
                path: 'index',
                name: 'scoresIndex',
                meta: {
                    title: '班级科目成绩',
                    icon: 'Money',
                    role: ['ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT']
                },
                component: ()=> import('../views/scores/ScoresList.vue')
            }
        ]
    },
    {
        path: '/census',
        name: 'census',
        meta: {
            title: '数据统计',
            icon: 'Medal',
            role: ['ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT']
        },
        redirect: '/census/index',
        component: ()=> import('../views/layout/Index.vue'),
        isMenu: true,
        funcNode:2,
        children: [
            {
                path: 'index',
                name: 'scoresCensusIndex',
                meta: {
                    title: '班级科目成绩统计',
                    icon: 'Histogram',
                    role: ['ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT']
                },
                component: ()=> import('../views/census/ScoresCensus.vue')
            },
            {
                path: 'contrast',
                name: 'scoresContrastCensusIndex',
                meta: {
                    title: '班级科目对比统计',
                    icon: 'Notification',
                    role: ['ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT']
                },
                component: ()=> import('../views/census/ScoresContrastCensus.vue')
            }
        ]
    }
]

// 创建静态路由实例 createRouter -> Hash Mode
const router = createRouter({
    history: createWebHashHistory(),
    routes: staticRouter
})

// 路由拦截器 beforeEach
router.beforeEach(async (to, from, next) => {
    // 进度条触发
    NProgress.start();

    // 若访问登录/注册页，直接放行
    if(to.path==='/login')return next()
    if(to.path==='/register')return next()

    // 判断是否有Token,没有重定向到login
    const userStore = useUserStore()
    if(!userStore.token)return next({path:`/login?redirect=${to.path}`,replace:true})

    // 获取登录用户的角色
    const { userInfo } = userStore
    const roles = []
    roles.push(userInfo.role.code)

    // 根据角色动态生成路由访问映射
   const menuStore = useMenuStore()
    if (!menuStore.routers.length) {
        const accessRoutes =  menuStore.generateRoutes({roles: roles})
        accessRoutes.forEach(item => router.addRoute(item)) // 动态添加访问路由表
        next({ ...to, replace: true }) // 这里相当于push到一个页面 不在进入路由拦截
    }else {
        // 正常访问页面
        next();
    }
});

/**
 * @description 路由跳转结束
 * */
router.afterEach(() => {
    NProgress.done();
});
/**
 * @description 路由跳转错误
 * */
router.onError(error => {
    NProgress.done();
    console.warn("路由错误", error.message);
});
export default router
