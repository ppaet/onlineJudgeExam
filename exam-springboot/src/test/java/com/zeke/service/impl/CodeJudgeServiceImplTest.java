package com.zeke.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class CodeJudgeServiceImplTest {

    @Autowired
    private CodeJudgeServiceImpl codeJudgeService;
    @Test
    void setJudge() {
        Boolean ret = codeJudgeService.doJudge(0, "import java.util.Scanner;\npublic class Main{ \n    public static void main(String[] args) { \n        int a,b;\n        Scanner scanner = new Scanner(System.in);\n        a = scanner.nextInt();\n        b = scanner.nextInt();\n        System.out.println(a+b);\n    }\n}", "1 2", "3\n");
        if (ret) {
            System.out.println("*********正确**********");
        } else {
            System.out.println("*********错误**********");
        }
    }
}