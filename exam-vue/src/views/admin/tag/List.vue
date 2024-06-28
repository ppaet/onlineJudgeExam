<template>
    <my-el-top-bottom
        title="标签列表"
        placeholder="根据标签名查找"
        isSearch
        requestUrl="/course"
        @update="updateTableData"
    >
      <template #header>
        <el-button
            type="primary"
            @click="dialogAddVisible=true"
            style="margin-left: 30px">
          <span>添加标签</span>
          <template #icon>
            <Plus/>
          </template>
        </el-button>
        <el-dialog v-model="dialogAddVisible" title="新建标签">
          <el-form
              ref="formEl"
              :rules="addRule"
              :model="addForm"
          >
            <el-form-item
                prop="name"
                label="标签名"
                :label-width="formLabelWidth"
            >
              <el-input
                  v-model="addForm.name"
                  placeholder="请输入标签名称"
                  autocomplete="off"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogAddVisible = false">取消</el-button>
              <el-button type="primary" @click="addTag">
                提交
              </el-button>
            </div>
          </template>
        </el-dialog>
      </template>
      <template #main>
        <el-table
            width="100%"
            :height="useMainHeight().mainHeight"
            :data="tableData" stripe>
          <el-table-column align="center" prop="tagId" label="标签号"/>
          <el-table-column align="center" prop="name" label="标签名称"/>
          <el-table-column align="center" label="操作">
            <template #default="scope">
              <el-button
                  type="primary"
                  icon="Edit"
                  circle
                  @click="handleEdit(scope.row)"
              />
              <el-button
                  type="danger"
                  icon="Delete"
                  circle
                  @click="handleDelete(scope.row)"
              />
            </template>
          </el-table-column>
        </el-table>
        <!-- 提示框 -->
        <el-dialog v-model="dialogFormVisible" title="修改标签数据">
          <el-form :model="form">
            <el-form-item label="标签名称" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off"/>
            </el-form-item>
          </el-form>
          <template #footer>
                  <span class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary" @click="updateName">
                      更改
                    </el-button>
                  </span>
          </template>
        </el-dialog>
      </template>
    </my-el-top-bottom>
  </template>
  
  <script setup lang="ts">
  import myRequest from "@/utils/request.ts";
  import request from "@/utils/request.ts";
  import {onMounted, reactive, ref} from 'vue'
  import {ApiResult} from "@/utils/type.ts";
  import {ElMessageBox, ElNotification, FormRules} from "element-plus";
  import {Code} from "@/utils/Code.ts";
  import {MyElNotification} from "@/hook/requestTooltip.ts";
  import MyElTopBottom from "@/views/admin/components/MyElTopBottom.vue";
  import {useMainHeight} from "@/store/modules/mainHeight.ts";
  import useUserStore from "@/store/modules/user.ts";
  import setting from "@/setting.ts";
  
  const userStore = useUserStore();
  
  const updateTableData = (res) => {
    tableData.value = res
  }
  
  onMounted(() => {
    getTableData();
  })
  const tableData = ref([])
  const form = reactive({
    tagId: '',
    name: ''
  })
  
  const getTableData = async () => {
    await myRequest.get<any, ApiResult>(`/tag/getTags/${userStore.uId}`)
        .then(res => {
          tableData.value = res.data as []
        })
  }
  
  const dialogFormVisible = ref(false)
  const formLabelWidth = '140px'
  
  
  const handleEdit = (row) => {
    dialogFormVisible.value = true;
    for (let key in form) {
      form[key] = row[key];
    }
  }
  const updateName = async () => {
    dialogFormVisible.value = false;
    try {
      const res = await request.post<any, ApiResult>('/tag/updateTag', form)
      MyElNotification(res, Code.UPDATE_OK, '修改');
      await getTableData();
    } catch (e) {
      console.log(e)
    }
  }
  const handleDelete = (row) => {
    ElMessageBox.confirm('此操作将永久删除数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      const res = await request.delete<any, ApiResult>(`/tag/${row.tagId}`)
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
  
  const dialogAddVisible = ref(false)
  const formEl = ref()
  const addForm = reactive({
    uId: userStore.uId,
    name: ''
  })
  const addRule = reactive<FormRules>({
    name: {required: true, message: '请输入标签名', trigger: 'blur'},
  })
  const addTag = async () => {
    formEl.value?.validate(async (flag) => {
      if (!flag) return;
      const res = await request.post<any, ApiResult>('/tag/addTag', addForm)
      MyElNotification(res, Code.UPDATE_OK, "添加")
      await getTableData();
      dialogAddVisible.value = false;
    })
  }
  
  </script>
  
  <style scoped lang="scss">
  @import "@/styles/admin-style";
  
  </style>