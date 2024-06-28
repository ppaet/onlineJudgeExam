import {defineStore} from "pinia";
import myRequest from "@/utils/request.ts";
import {ApiResult} from "@/utils/type.ts";

interface ExamAnswerInfo {
    aqId: number;
    seId: number;
    content: TopicAnswerVO[];
}

interface TopicAnswerVO {
    tId: number;
    typeId: number;
    score: number;
    status: number;
    question: string;
    rightAnswer: string;
    answer: string;
}

// @ts-ignore
export const useExamAnswerStore = defineStore("examAnswerStore", {
  state: () => {
    return {
      // @ts-ignore
      examAnswerInfo: JSON.parse(localStorage.getItem("examAnswerInfo")),
    }
  },
  getters: {
    answerContent(): TopicAnswerVO[] {
        return this.examAnswerInfo.content;
      },
    aqId(): string {
      return this.examAnswerInfo.aqId;
    },
    seId(): string {
      return this.examAnswerInfo.seId;
    },
  },
  actions: {
    async getExamAnswerInfo(seId: number) {
      localStorage.removeItem("examAnswerInfo")
      const {data} = await myRequest.get<any, ApiResult<ExamAnswerInfo>>(`/exam/selectOne/student/answer/${seId}`)
      this.examAnswerInfo = data;
      localStorage.setItem("examAnswerInfo", JSON.stringify(data))
    }
  }
})

