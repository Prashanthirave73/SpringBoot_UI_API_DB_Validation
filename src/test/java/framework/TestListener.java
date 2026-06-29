package framework;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        ReportManager.init();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportManager.getTest().pass("Test passed");
        ReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ReportManager.getTest().fail(result.getThrowable());
        ReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ReportManager.getTest().skip(result.getThrowable());
        ReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.flush();
    }
}
