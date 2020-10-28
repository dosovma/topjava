package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestRuleUtil implements TestRule {

    private static TestRuleUtil testRuleUtil;

    private final Logger log = LoggerFactory.getLogger(TestRuleUtil.class);

    private final Map<String, Long> resultTimeTest = new HashMap<>();

    private TestRuleUtil() {
    }

    public static TestRuleUtil getTestRule() {
        if (testRuleUtil == null) {
            testRuleUtil = new TestRuleUtil();
        }
        return testRuleUtil;
    }

    @Override
    public Statement apply(Statement statement, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String methodName = description.getMethodName();
                log.info("Start test : " + methodName);
                Date startTestMethod = new Date();
                statement.evaluate();
                log.info("End test : " + methodName);
                Date endTestMethod = new Date();
                Long duration = endTestMethod.getTime() - startTestMethod.getTime();
                resultTimeTest.merge(methodName, duration, (key, value) -> value + duration);
                log.info(String.format("Duration of %s is %dms", methodName, duration));
            }
        };
    }

    public void print() {
        log.info("Test method" + " : " + "Duration time");
        for (Map.Entry<String, Long> pair : resultTimeTest.entrySet()) {
            log.info(pair.getKey() + " : " + pair.getValue());
        }
    }
}

