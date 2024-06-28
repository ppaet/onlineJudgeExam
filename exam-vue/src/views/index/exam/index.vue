<template>
  <my-el-top-bottom
      title="考试中心"
  >
    <template #main>
      <el-table
          width="100%"
          :height="useMainHeight().mainHeight"
          :data="tableData" stripe>
        <el-table-column align="center" prop="e_id" label="考试号" width="100" sortable/>
        <el-table-column align="center" prop="name" label="考试名" sortable/>
        <el-table-column align="center" prop="courseName" label="课程名" sortable/>
        <el-table-column align="center" prop="teacherName" label="科任老师" sortable/>
        <el-table-column align="center" prop="start_time" label="开始时间" sortable/>
        <el-table-column align="center" prop="end_time" label="结束时间" sortable/>
        <el-table-column align="center" prop="status" label="考试状态" sortable>
          <template #default="scope">
            <el-tag
                v-if="scope.row.status == 0"
                type="info"
            >
              未开放
            </el-tag>
            <el-tag
                type="success"
                v-if="submitEdEIdList.includes(scope.row.e_id)"
            >
              已提交
            </el-tag>
            <el-tag
                v-if="scope.row.status == 1 && !submitEdEIdList.includes(scope.row.e_id)"
            >
              进行中
            </el-tag>

          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="操作"
        >
          <template #default="scope">
            <el-tooltip
                class="box-item"
                effect="light"
                content="查看成绩"
                placement="bottom"
            >
              <el-button
                  type="success"
                  icon="Document"
                  size="large"
                  circle
                  :disabled="!submitEdEIdList.includes(scope.row.e_id)"
                  @click="showScore(scope.row)"
              />
            </el-tooltip>
            <el-tooltip
                class="box-item"
                effect="light"
                content="进入考试"
                placement="bottom"
            >
              <el-button
                  type="primary"
                  icon="Position"
                  size="large"
                  circle
                  :disabled="scope.row.status == 0 || submitEdEIdList.includes(scope.row.e_id)"
                  @click="joinExam(scope.row)"
              />
            </el-tooltip>
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
import {Action, ElMessage, ElMessageBox, ElNotification, FormRules} from "element-plus";
import {Code} from "@/utils/Code.ts";
import {MyElNotification} from "@/hook/requestTooltip.ts";
import MyElTopBottom from "@/views/admin/components/MyElTopBottom.vue";
import {useMainHeight} from "@/store/modules/mainHeight.ts";
import useUserStore from "@/store/modules/user.ts";
import setting from "@/setting.ts";
import {useJoinExamStore} from "@/store/modules/joinExam.ts";
import {useExamAnswerStore} from "@/store/modules/answerPage.ts";
import {useRoute, useRouter} from "vue-router";
import router from "@/router";
import {loadExamStatus} from "@/hook/updateExamStatus.ts";

const userStore = useUserStore();
const studentId = userStore.uId;

onMounted(() => {
  getTableData();
  loadExamStatus();
  getSubmitList();
})
const showScore = (row) => {
  ElMessageBox.confirm(`本场考试成绩为: ${submitEdDict.value[row.e_id]}<br/>批注: ${reviewList.value[row.e_id]}`, '查看分数', {
    confirmButtonText: '查看详情',
    cancelButtonText: '关闭',
    dangerouslyUseHTMLString: true,
    type: 'warning'
  }).then(async () => {
    await examAnswerStore.getExamAnswerInfo(submitEdSeIdList.value[row.e_id])
    await $router.push({name: 'answerPage'})
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
const submitEdDict = ref({})
const reviewList = ref({})
const submitEdEIdList = ref([])
const submitEdSeIdList = ref([])
const getSubmitList = async () => {
  const res = await myRequest.get<any, ApiResult>(`/exam/submitList/${studentId}`)
  const data = res.data as []
  const scoreData = data.reduce((obj, item) => {
    obj[item.eId] = item.score;
    return obj;
  }, {});
  const reviewData = data.reduce((obj, item) => {
    obj[item.eId] = item.review;
    return obj;
  }, {});
  const seData = data.reduce((obj, item) => {
    obj[item.eId] = JSON.parse(item.seId);
    return obj;
  }, {});
  submitEdDict.value = scoreData;
  reviewList.value = reviewData;
  submitEdEIdList.value = data.map((item: any) => item.eId)
  submitEdSeIdList.value = seData
}
const tableData = ref([])

const getTableData = async () => {
  const res = await myRequest.get<any, ApiResult>(`/exam/getStuExams/${studentId}`)
  tableData.value = res.data as []
}

const $router = useRouter()
const joinExamStore = useJoinExamStore()
const examAnswerStore = useExamAnswerStore()

const joinExam = (row) => {
  ElMessageBox.confirm('是否开始考试?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await joinExamStore.getExamInfo(row.e_id)
    await $router.push({name: 'detailsPage'})
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