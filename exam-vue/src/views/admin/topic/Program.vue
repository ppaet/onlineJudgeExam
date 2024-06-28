<template>
    <my-el-top-bottom
      title="编程题创建"
    >
    <template #main>
        <el-form
          ref="ruleFormRef"
          :model="form"
          :rules="rules"
        >

        <el-form-item
            prop="question"
            label="题目内容"
        >
          <el-input
              rows="20"
              type="textarea"
              v-model="form.question"></el-input>
        </el-form-item>

        <el-form-item
            prop="tagId"
            label="标签"
        >
          <el-select v-model="form.tagId" placeholder="请选择标签">
            <el-option v-for="item in tagList"
                       :key="item.tagId"
                       :label="item.name"
                       :value="item.tagId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
            prop="difficultyId"
            label="难度"
        >
          <el-rate :max="3" v-model="form.difficultyId"/>
        </el-form-item>
        <el-form-item
            prop="status"
            label="是否与其他老师共享题目"
        >

          <el-switch
              v-model="switchValue"
              @change="switchChange"
          />
        </el-form-item>

        <el-form-item
            prop="answer"
            label="示范代码"
        >
        <el-input
            v-model="form.answer"
            :rows="20"
            type="textarea"
            placeholder="请输入示范代码"
        />
        </el-form-item>

        <el-form-item
            prop="testIn1"
            label="测试用例1"
        >
        <el-input type="textarea" v-model="form.testIn1" placeholder="请输入测试用例1" />
        </el-form-item>

        <el-form-item
            prop="testOut1"
            label="理想输出1"
        >
        <el-input type="textarea" v-model="form.testOut1" placeholder="理想输出1" />
        </el-form-item>

        <el-form-item class="my-item-deep">
          <el-button
            type="primary"
            @click="testCode(1)">发起测试
          </el-button>
        </el-form-item>

        <el-form-item
            label="控制台输出1"
        >
        <el-input type="textarea" v-model="form.console1" disabled placeholder="测试输出" />
        </el-form-item>
        <el-text class="mx-1">----------------------------------------------------------------------------------</el-text>
        <br><br><br>
        
        <el-form-item
            prop="testIn2"
            label="测试用例2"
        >
        <el-input type="textarea" v-model="form.testIn2" placeholder="请输入测试用例2" />
        </el-form-item>

        <el-form-item
            prop="testOut2"
            label="理想输出2"
        >
        <el-input type="textarea" v-model="form.testOut2" placeholder="理想输出1" />
        </el-form-item>

        <el-form-item class="my-item-deep">
          <el-button
            type="primary"
            @click="testCode(2)">发起测试
          </el-button>
        </el-form-item>

        <el-form-item
            label="控制台输出2"
        >
        <el-input type="textarea" v-model="form.console2" disabled placeholder="测试输出" />
        </el-form-item>
        <el-text class="mx-1">----------------------------------------------------------------------------------</el-text>
        <br><br><br>
        
        <el-form-item
            prop="testIn3"
            label="测试用例3"
        >
        <el-input type="textarea" v-model="form.testIn3" placeholder="请输入测试用例3" />
        </el-form-item>

        <el-form-item
            prop="testOut3"
            label="理想输出3"
        >
        <el-input type="textarea" v-model="form.testOut3" placeholder="理想输出3" />
        </el-form-item>

        <el-form-item class="my-item-deep">
          <el-button
            type="primary"
            @click="testCode(3)">发起测试
          </el-button>
        </el-form-item>

        <el-form-item
            label="控制台输出3"
        >
        <el-input type="textarea" v-model="form.console3" disabled placeholder="测试输出" />
        </el-form-item>
        <el-text class="mx-1">----------------------------------------------------------------------------------</el-text>
        <br><br><br>

        
        <el-form-item class="my-item-deep">
          <el-button
              type="primary"
              @click="addTopic">提交
          </el-button>
        </el-form-item>
        </el-form>
    </template>

    </my-el-top-bottom>
</template>
  
<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import {MyElNotification} from "@/hook/requestTooltip.ts";
import {FormInstance, FormRules} from "element-plus";
import request from "@/utils/request.ts";

import MyElTopBottom from "@/views/admin/components/MyElTopBottom.vue";
import useUserStore from "@/store/modules/user.ts";
import useLayOutSettingStore from "@/store/modules/layoutTabBar.ts";
import { stringifyQuery } from 'vue-router';
import {Code} from "@/utils/Code.ts";

onMounted(() => {
  getTagList()
})

const uId = useUserStore().uId;

const switchValue = ref(false)
const switchChange = (val) => {
  form.status = val ? 1 : 0
}

const getTagList = async () => {
  const res = await request.get<any, ApiResult>(`/tag/${uId}`)
  tagList.value = res.data as [];
}

let tagList = ref([])

const testCode = async (num) => {
  const testCase = {
    answer: form.answer,
    testIn: "",
    testOut: "",
  };
  if (num == 1) {
    testCase.testIn = form.testIn1;
    testCase.testOut = form.testOut1;
  }else if (num == 2) {
    testCase.testIn = form.testIn2;
    testCase.testOut = form.testOut2;
  }else {
    testCase.testIn = form.testIn3;
    testCase.testOut = form.testOut3;
  }
  const res = await request.post<any, ApiResult>('/oj/testCase', testCase)
  if (num == 1) {
    form.console1 = res.msg;
  }else if (num == 2) {
    form.console2 = res.msg;
  }else {
    form.console3 = res.msg;
  }
}

const addTopic = () => {
  ruleFormRef.value?.validate(async (flag) => {
    if (!flag) return
    const res = await request.post<any, ApiResult>('/oj', form)
    MyElNotification(res, Code.SAVA_OK, '添加')
    settingStore.refresh = !settingStore.refresh;
  })
}

const settingStore = useLayOutSettingStore();

const rules = reactive<FormRules>({
  code: {required: true, message: '请输入代码', trigger: 'blur'},
  tagId: {required: true, message: '请选择标签', trigger: 'blur'},
  difficultyId: {required: true},
  question: {required: true, message: '请输入题目', trigger: 'blur'},
  answer: {required: true, message: '请输入答案', trigger: 'blur'},
  status: {required: true},
  testIn1: {required: true, message: '请输入测试用例', trigger: 'blur'},
  testIn2: {required: true, message: '请输入测试用例', trigger: 'blur'},
  testIn3: {required: true, message: '请输入测试用例', trigger: 'blur'},
  testOut1: {required: true, message: '请输入理想输出', trigger: 'blur'},
  testOut2: {required: true, message: '请输入理想输出', trigger: 'blur'},
  testOut3: {required: true, message: '请输入理想输出', trigger: 'blur'},
})


const ruleFormRef = ref<FormInstance>()

interface Form {
  uId: string,
  tagId: string,
  difficultyId: number,
  status: number
  question: string,
  code: string,
  testIn1: string,
  testIn2: string,
  testIn3: string,
  testOut1: string,
  testOut2: string,
  testOut3: string,
  console1:string,
  console2:string,
  console3:string
}

const form = reactive<Form>({
  uId: uId,
  tagId: '',
  difficultyId: 1,
  status: 0,
  question: '',
  answer: '',
  testIn1: '',
  testIn2: '',
  testIn3: '',
  testOut1: '',
  testOut2: '',
  testOut3: '',
  console1: '',
  console2: '',
  console3: ''
})


</script>
  


<style scoped lang="scss">
.el-form {
  max-width: 500px;
  margin: 0 auto;
}
.my-item-deep {
  :deep(.el-form-item__content) {
    justify-content: center;
  }
}
.my-item-deep {
  :deep(.el-form-item__content) {
    justify-content: center;
  }
}

</style>