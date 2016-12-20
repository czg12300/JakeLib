
package com.jake.library.global;

import android.content.Context;

import com.jake.library.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 是否上报奔溃日志
     */
    private boolean isReport = false;

    public boolean isReport() {
        return isReport;
    }

    public void setReport(boolean report) {
        isReport = report;
    }

    public AppUncaughtExceptionHandler(Context context) {
        this.mContext = context;
        setCrashReported(false);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // debug模式下 不上报崩溃日志
        if (!isReport) {
            return;
        }

        try {
            if (ex != null && !isCrashReported()) {
                setCrashReported(true);
                handleException(ex);
                mDefaultHandler.uncaughtException(thread, ex);
                Thread.setDefaultUncaughtExceptionHandler(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleException(Throwable ex) {
        // 发送统计--崩溃
//        StatisticsManager.StatPackage pack = new StatisticsManager.StatPackage();
//        pack.setActionid(StatId.ActionId.CRASH_LOG);
//        String crashReport = "";
//        if (ex != null) {
//            crashReport = getCrashReport(ex);
//        }
//        pack.setLog(crashReport);
//        StatisticsManager.onError(pack);
    }

    /**
     * 获取SDK崩溃异常报告
     */
    public static String getCrashReport(Throwable ex) {
        StringBuffer sb = new StringBuffer("");
        try {
            if (ex != null) {
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                Throwable cause = ex.getCause();
                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = cause.getCause();
                }
                printWriter.close();
                String result = writer.toString();
                sb.append(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString() + "\nPID=" + android.os.Process.myPid();
    }

    /**
     * 设置崩溃日志是否已经上传
     * 
     * @param reported
     */
    private void setCrashReported(boolean reported) {
        mContext.getSharedPreferences("crash_reported_setting", Context.MODE_PRIVATE).edit()
                .putBoolean("reported", reported).commit();
    }

    /**
     * 崩溃日志是否已经上传
     * 
     * @return
     */
    private boolean isCrashReported() {
        return mContext.getSharedPreferences("crash_reported_setting", Context.MODE_PRIVATE)
                .getBoolean("reported", false);
    }

}
