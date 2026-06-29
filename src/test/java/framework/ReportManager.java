package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Path;

public final class ReportManager {
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ReportManager() {
    }

    public static synchronized void init() {
        if (extentReports == null) {
            Path reportPath = Path.of("target", "extent-report", "AutomationReport.html");
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath.toString());
            reporter.config().setReportName("API UI DB Automation Report");
            reporter.config().setDocumentTitle("Automation Results");
            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
        }
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static void createTest(String name) {
        TEST.set(extentReports.createTest(name));
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void removeTest() {
        TEST.remove();
    }
}
