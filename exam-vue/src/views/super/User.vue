<template>
    <my-el-top-bottom
        title="用户列表"
    >
      <template #main>
        <el-table
            width="100%"
            :height="useMainHeight().mainHeight"
            :data="tableData" stripe>
          <el-table-column align="center" prop="uId" label="编号" sortable/>
          <el-table-column align="center" label="角色" sortable :sort-method="chineseSort">
            <template #default="{ row }">
              {{ getRoleName(row.rId) }}
            </template>
          </el-table-column>
          <el-table-column align="center" prop="username" label="姓名" sortable/>
          <el-table-column align="center" prop="email" label="邮箱" sortable/>
          <el-table-column align="center" prop="registerTime" label="注册时间" sortable/>
          <el-table-column align="center" label="操作">
            <template #default="scope">
              <el-button
                  type="danger"
                  icon="Delete"
                  v-if="scope.row.rId !== 0"
                  circle
                  @click="handleDelete(scope.row)"
              />
            </template>
          </el-table-column>
        </el-table>
      </template>
    </my-el-top-bottom>
  </template>
  
  <script setup lang="ts">
  import MyElTopBottom from "@/views/admin/components/MyElTopBottom.vue";
  import {useMainHeight} from "@/store/modules/mainHeight.ts";
  import {onMounted, reactive, ref} from "vue";
  import myRequest from "@/utils/request.ts";
  import {ApiResult} from "@/utils/type.ts";
  import request from "@/utils/request.ts";
  import {MyElNotification} from "@/hook/requestTooltip.ts";
  import {Code} from "@/utils/Code.ts";
  import {ElMessageBox, ElNotification} from "element-plus";
  import useUserStore from "@/store/modules/user.ts";
  import setting from "@/setting.ts";
  
  const userStore = useUserStore();
  
  onMounted(() => {
    getRoleData();
    getTableData();
  })

  const roleData = ref([])
  const getRoleData = async () => {
    const res = await request.get<any, ApiResult>('/role')
    roleData.value = res.data.reduce((map, obj) => {
      map.set(obj.rId, obj.name);
      return map;
    }, new Map());
  }

  const getRoleName = (rId) => {
    const name = roleData.value.get(rId);
    return name ? name : '未知角色';
  }
  
  const tableData = ref([])

  const getTableData = async () => {
    const res = await myRequest.get<any, ApiResult>(`/user`)
    tableData.value = res.data as []
  }
  
  const dialogFormVisible = ref(false)
  const formLabelWidth = '140px'
  
  const form = reactive({
    uId: '',
    rId: '',
    userName: '',
    email: '',
    registerTime: '',
  })

  // 自定义排序函数
  const chineseSort = (obj1, obj2) => {
    return getRoleName(obj1.rId).localeCompare(getRoleName(obj2.rId), 'zh-CN')
  }

  const handleDelete = (row) => {
    ElMessageBox.confirm('此操作将永久删除数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      const res = await myRequest.delete<any, ApiResult>(`/user/`, {params: {uId: row.uId}})
      MyElNotification(res, Code.DELETE_OK, '删除');
      await getTableData();
    }).catch(() => {
      // 点击取消按钮后的回调函数
      ElNotification({
        title: '提示信息',
        message: '您取消了该操作！',
        type: 'warning',
        duration: setting.duration
      })
    });
  }
  
  </script>
  
  <style scoped lang="scss">
  @import "@/styles/admin-style";
  
  </style>