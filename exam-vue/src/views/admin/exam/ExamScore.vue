<template>
    <my-el-top-bottom
        title="考试成绩"
    >
      <template #main>
        <el-table
            width="100%"
            :height="useMainHeight().mainHeight"
            :data="tableData" stripe>
          <el-table-column align="center" prop="seId" label="考试号" width="100" sortable/>
          <el-table-column align="center" prop="name" label="考试名" sortable/>
          <el-table-column align="center" prop="courseName" label="课程名" sortable/>
          <el-table-column align="center" prop="studentName" label="学生" sortable/>
          <el-table-column align="center" prop="score" label="成绩" sortable/>
          <el-table-column align="center" prop="createTime" label="提交时间" sortable/>
          <el-table-column align="center" prop="review" label="批注"/>
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
                    icon="Position"
                    size="large"
                    circle
                    :disabled="!submitEdEIdList.includes(scope.row.eId)"
                    @click="showScore(scope.row)"
                />
              </el-tooltip>
              <el-tooltip
                  class="box-item"
                  effect="light"
                  content="修改成绩"
                  placement="bottom"
              >
                <el-button
                    type="primary"
                    icon="Edit"
                    size="large"
                    circle
                    :disabled="!submitEdEIdList.includes(scope.row.eId)"
                    @click="reviseScore(scope.row)"
                />
              </el-tooltip>
              <el-tooltip
                  class="box-item"
                  effect="light"
                  content="添加批注"
                  placement="bottom"
              >
                <el-button
                    type="info"
                    icon="Message"
                    size="large"
                    circle
                    :disabled="!submitEdEIdList.includes(scope.row.eId)"
                    @click="comments(scope.row)"
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
  import {useExamAnswerStore} from "@/store/modules/answerPage.ts";
  import {useRoute, useRouter} from "vue-router";
  import router from "@/router";
  import {loadExamStatus} from "@/hook/updateExamStatus.ts";
  
  const userStore = useUserStore();
  const teacherId = userStore.uId;
  
  onMounted(() => {
    getTableData();
    loadExamStatus();
    getSubmitList();
  })
  const showScore = (row) => {
    ElMessageBox.confirm(`本场考试成绩为: ${submitEdDict.value[row.eId].totalScore}`, '查看分数', {
      confirmButtonText: '查看详情',
      cancelButtonText: '关闭',
      type: 'warning'
    }).then(async () => {
      await examAnswerStore.getExamAnswerInfo(submitEdSeIdList.value[row.eId])
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
  const submitEdEIdList = ref([])
  const submitEdSeIdList = ref([])
  const getSubmitList = async () => {
    const res = await myRequest.get<any, ApiResult>(`/exam/review/${teacherId}`)
    const data = res.data as []
    const dictData = data.reduce((obj, item) => {
      obj[item.eId] = JSON.parse(item.rightStudentAnswer);
      return obj;
    }, {});
    const seData = data.reduce((obj, item) => {
      obj[item.eId] = JSON.parse(item.seId);
      return obj;
    }, {});
    submitEdDict.value = dictData;
    submitEdEIdList.value = data.map((item: any) => item.eId)
    submitEdSeIdList.value = seData
  }
  const tableData = ref([])
  
  const getTableData = async () => {
    const res = await myRequest.get<any, ApiResult>(`/exam/review/${teacherId}`)
    tableData.value = res.data as []
  }
  
  const $router = useRouter()
  const examAnswerStore = useExamAnswerStore()
  
  const reviseScore = (row) => {
    // 通过 ElMessageBox.prompt 创建一个带有输入框的对话框
    ElMessageBox.prompt('请输入修改后的成绩', '修改成绩', {
        confirmButtonText: '提交',
        cancelButtonText: '取消',
        inputPattern: /^\d+(\.\d{1,2})?$/, // 设置输入框的正则验证，这里假设成绩是数字，最多保留两位小数
        inputErrorMessage: '成绩格式不正确，成绩必须是数字，最多保留两位小数',
        input2Placeholder: '请输入评论',
    }).then(async ({ value, action }) => {
        if (action === 'confirm') { // 如果点击了提交按钮
            const res = await myRequest.get<any, ApiResult>(`/exam/review/score`, {params: {seId: row.seId, score: value}});
            ElNotification({
                title: '提示信息',
                message: '提交成功！',
                type: 'success',
                duration: setting.duration
            });
            // 延迟一秒后刷新页面
            setTimeout(() => {window.location.reload()}, 1000);
        } else {
            // 点击取消按钮后的回调函数
            ElNotification({
                title: '提示信息',
                message: '您取消了该操作！',
                type: 'warning',
                duration: setting.duration
            });
        }
    }).catch(() => {
        // 异常情况处理，比如点击关闭按钮或者按下 ESC 键
        ElNotification({
            title: '提示信息',
            message: '操作失败！',
            type: 'error',
            duration: setting.duration
        });
    });
  };
  
  const comments = (row) => {
    // 通过 ElMessageBox.prompt 创建一个带有输入框的对话框
    ElMessageBox.prompt('请输入批注', '添加批注', {
        confirmButtonText: '提交',
        cancelButtonText: '取消',
        input2Placeholder: '请输入评论',
    }).then(async ({ value, action }) => {
        if (action === 'confirm') { // 如果点击了提交按钮
            const res = await myRequest.get<any, ApiResult>(`/exam/review/comments`, {params: {seId: row.seId, comments: value}});
            ElNotification({
                title: '提示信息',
                message: '提交成功！',
                type: 'success',
                duration: setting.duration
            });
            // 延迟一秒后刷新页面
            setTimeout(() => {window.location.reload()}, 1000);
        } else {
            // 点击取消按钮后的回调函数
            ElNotification({
                title: '提示信息',
                message: '您取消了该操作！',
                type: 'warning',
                duration: setting.duration
            });
        }
    }).catch(() => {
        // 异常情况处理，比如点击关闭按钮或者按下 ESC 键
        ElNotification({
            title: '提示信息',
            message: '操作失败！',
            type: 'error',
            duration: setting.duration
        });
    });
  };

  </script>
  
  <style scoped lang="scss">
  @import "@/styles/admin-style";
  
  </style>