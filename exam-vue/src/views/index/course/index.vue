<template>
  <my-el-top-bottom
      title="课程中心"
  >
    <template #header>
      <el-button
          type="primary"
          @click="dialogAddVisible=true"
          style="margin-left: 30px">
        <span>加入课程</span>
        <template #icon>
          <Plus/>
        </template>
      </el-button>
      <el-dialog v-model="dialogAddVisible" title="加入课程">
        <el-form
            ref="formEl"
            :rules="addRule"
            :model="addForm"
        >
          <el-form-item
              prop="courseCode"
              label="课程口令"
          >
            <el-input
                v-model="addForm.courseCode"
                placeholder="请输入课程口令"
                autocomplete="off"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogAddVisible = false">取消</el-button>
            <el-button type="primary" @click="joinCourse">
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
        <el-table-column align="center" prop="cId" label="课程号" sortable/>
        <el-table-column align="center" prop="courseName" label="课程名称" sortable/>
        <el-table-column align="center" prop="teacherName" label="科任老师" sortable/>
        <el-table-column align="center" label="操作">
          <template #default="scope">
            <el-button
                type="danger"
                icon="Delete"
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
const studentId = userStore.uId;

onMounted(() => {
  getTableData();
})
const tableData = ref([])

const getTableData = async () => {
  const res = await myRequest.get<any, ApiResult>(`/course/student/${studentId}`)
  tableData.value = res.data as []
}

const dialogAddVisible = ref(false)
const formEl = ref()
const addForm = reactive({
  uId: studentId,
  courseCode: ''
})
const addRule = reactive<FormRules>({
  courseCode: {required: true, message: '请输入课程口令', trigger: 'blur'},
})
const joinCourse = async () => {
  formEl.value?.validate(async (flag) => {
    if (!flag) return;
    const res = await request.post<any, ApiResult>('/course/joinCourse', addForm)
    MyElNotification(res, Code.SAVA_OK, "添加")
    await getTableData();
    dialogAddVisible.value = false;
  })
}
const handleDelete = (row) => {
  ElMessageBox.confirm('此操作将永久删除数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request.get<any, ApiResult>(`/course/student/quit`, {params: {uId: studentId, cId: row.cId}})
    MyElNotification(res, Code.DELETE_OK, '删除');
    await getTableData();
    // 延迟一秒后刷新页面
    setTimeout(() => {window.location.reload()}, 1000);
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