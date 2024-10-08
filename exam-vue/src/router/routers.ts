// @ts-ignore
// @ts-ignore
export const constantRouter = [
  {
    path: '/',
    // @ts-ignore
    redirect: '/login',
    name: 'gen',
    meta: {
      title: 'gen',
      hidden: true
    }
  },
  {
    path: '/login',
    // @ts-ignore 路由懒加载
    component: () => import('@/views/user/Login.vue'),
    name: 'login',
    meta: {
      title: '登录',
      hidden: true
    }
  },
  {
    path: '/register',
    // @ts-ignore 路由懒加载
    component: () => import('@/views/user/Register.vue'),
    name: 'register',
    meta: {
      title: '注册',
      hidden: true
    }
  },
  {
    path: '/forgetpw',
    // @ts-ignore 路由懒加载
    component: () => import('@/views/user/ForgetPW.vue'),
    name: 'forgetpw',
    meta: {
      title: '忘记密码',
      hidden: true
    }
  },
  {
    path: '/teacher',
    name: 'teacher',
    // @ts-ignore
    component: () => import('@/views/admin/index.vue'),
    redirect: {name: 'student'},
    meta: {
      title: '教师页面',
      hidden: true
    },
    children: [
      {
        path: 'home',
        // @ts-ignore
        component: () => import('@/views/admin/home/Index.vue'),
        meta: {
          title: '主页',
          hidden: true,
          icon: 'HomeFilled'
        },
        name: 'home'
      },
      {
        path: 'user',
        name: 'user',
        meta: {
          title: '用户管理',
          hidden: false,
          icon: 'UserFilled',
        },
        children: [
          {
            path: 'student',
            // @ts-ignore
            component: () => import('@/views/admin/user/Student.vue'),
            meta: {
              title: '学生列表',
              hidden: false,
            },
            name: 'student'
          },
        ]
      },
      {
        path: 'courseTeacher',
        name: 'courseTeacher',
        meta: {
          title: '课程管理',
          hidden: false,
          icon: 'Collection'
        },
        children: [
          {
            path: 'courseList',
            name: 'courseList',
            // @ts-ignore
            component: () => import('@/views/admin/course/List.vue'),
            meta: {
              title: '课程列表',
              hidden: false,
            },
          },
          {
            path: 'tagList',
            name: 'tagList',
            // @ts-ignore
            component: () => import('@/views/admin/tag/List.vue'),
            meta: {
              title: '标签列表',
              hidden: false,
            },
          },
        ]
      },
      {
        path: 'topic',
        name: 'topic',
        meta: {
          title: '题库管理',
          hidden: false,
          icon: 'OfficeBuilding'
        },
        children: [
          {
            path: 'topicList',
            name: 'topicList',
            // @ts-ignore
            component: () => import('@/views/admin/topic/List.vue'),
            meta: {
              title: '题目列表',
              hidden: false,
            }
          },
          {
            path: 'topicCreate',
            name: 'topicCreate',
            // @ts-ignore
            component: () => import('@/views/admin/topic/Create.vue'),
            meta: {
              title: '题目创建',
              hidden: false,
            }
          },
          {
            path: 'programCreat',
            name: 'programCreat',
            // @ts-ignore
            component: () => import('@/views/admin/topic/Program.vue'),
            meta: {
              title: '编程题创建',
              hidden: false,
            }
          },
        ]
      },
      {
        path: 'papers',
        name: 'papers',
        meta: {
          title: '试卷管理',
          hidden: false,
          icon: 'Reading'
        },
        children: [
          {
            path: 'papersList',
            name: 'papersList',
            // @ts-ignore
            component: () => import('@/views/admin/papers/List.vue'),
            meta: {
              title: '试卷列表',
              hidden: false,
            },
          },
          {
            path: 'papersHandCreate',
            name: 'papersHandCreate',
            // @ts-ignore
            component: () => import('@/views/admin/papers/HandCreate.vue'),
            meta: {
              title: '手动组卷',
              hidden: false,
            },
          },
          {
            path: 'papersRandomCreate',
            name: 'papersRandomCreate',
            // @ts-ignore
            component: () => import('@/views/admin/papers/RandomCreate.vue'),
            meta: {
              title: '随机组卷',
              hidden: false,
            },
          },
        ]
      },
      {
        path: 'exam',
        name: 'exam',
        meta: {
          title: '考试管理',
          hidden: false,
          icon: 'DataLine'
        },
        children: [
          {
            path: 'examList',
            name: 'examList',
            // @ts-ignore
            component: () => import('@/views/admin/exam/List.vue'),
            meta: {
              title: '考试列表',
              hidden: false,
            },
          },
          {
            path: 'examCreate',
            name: 'examCreate',
            // @ts-ignore
            component: () => import('@/views/admin/exam/Create.vue'),
            meta: {
              title: '考试创建',
              hidden: false,
            },
          },{
            path: 'examScore',
            name: 'examScore',
            // @ts-ignore
            component: () => import('@/views/admin/exam/ExamScore.vue'),
            meta: {
              title: '考试成绩',
              hidden: false,
            },
          },
        ]
      },
    ]
  },
  {
    path: '/index',
    name: 'index',
    redirect: {name: 'userCourse'},
    // @ts-ignore
    component: () => import('@/views/admin/index.vue'),
    meta: {
      title: '用户页面',
      hidden: true
    },
    children: [
      {
        path: 'userHome',
        name: 'userHome',
        // @ts-ignore
        component: () => import('@/views/index/home/index.vue'),
        meta: {
          title: '主页',
          hidden: true,
          icon: 'HomeFilled'
        },
      },
      {
        path: 'userCourse',
        name: 'userCourse',
        // @ts-ignore
        component: () => import('@/views/index/course/index.vue'),
        meta: {
          title: '课程中心',
          hidden: false,
          icon: 'Collection'
        },
      },
      {
        path: 'userExam',
        name: 'userExam',
        // @ts-ignore
        component: () => import('@/views/index/exam/index.vue'),
        meta: {
          title: '考试中心',
          hidden: false,
          icon: 'DataLine'
        },
      }
    ]
  },
  {
    path: '/admin',
    name: 'admin',
    redirect: {name: 'userManager'},
    // @ts-ignore
    component: () => import('@/views/admin/index.vue'),
    meta: {
      title: '超级管理员',
      hidden: true
    },
    children: [
      {
        path: 'userManager',
        name: 'userManager',
        meta: {
          title: '用户管理',
          hidden: false,
          icon: 'UserFilled',
        },
        children: [
          {
            path: 'allUser',
            // @ts-ignore
            component: () => import('@/views/super/User.vue'),
            meta: {
              title: '用户列表',
              hidden: false,
            },
            name: 'allUser'
          },
        ]
      }
    ]
  },
  {
    path: '/detailsPage',
    name: 'detailsPage',
    // @ts-ignore
    component: () => import('@/views/admin/exam/DetailsPage.vue'),
    meta: {
      title: '考试详情页',
      hidden: true
    }
  },
  {
    path: '/answerPage',
    name: 'answerPage',
    // @ts-ignore
    component: () => import('@/views/index/exam/AnswerPage.vue'),
    meta: {
      title: '答题详情页',
      hidden: true
    }
  },
  {
    path: '/test',
    // @ts-ignore
    component: () => import('@/components/Test/Test.vue'),
    name: 'test',
    meta: {
      title: '测试页',
      hidden: true
    }
  },
  {
    path: '/404',
    // @ts-ignore
    component: () => import('@/views/404/index.vue'),
    name: '404',
    meta: {
      title: '404',
      hidden: true
    }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    name: 'Any',
    meta: {
      title: 'Any',
      hidden: true
    }
  },
]