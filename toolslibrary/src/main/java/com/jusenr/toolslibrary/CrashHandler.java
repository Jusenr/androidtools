package com.jusenr.toolslibrary;

/**
 * UncaughtException processing class, when the program has a Uncaught exception,
 * it has the class to take over the program and record the error report.
 * <p>
 * Created by sunnybear on 15/3/13.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();

    //The default UncaughtException processing class of the system.
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler instance.
    private static CrashHandler INSTANCE = new CrashHandler();
    //Custom exception handling callback.
    private OnCrashHandler mOnCrashHandler;
    //Format the date as part of the log file name
//    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());

    /**
     * Ensure that there is only one instance of CrashHandler.
     */
    private CrashHandler() {
    }

    /**
     * Get the CrashHandler instance, singleton mode.
     *
     * @return CrashHandler instance
     */
    public static CrashHandler instance() {
        return INSTANCE;
    }

    /**
     * initialise
     */
    public void init(OnCrashHandler onCrashHandler) {
        mOnCrashHandler = onCrashHandler;
        //Gets the default UncaughtException processor for the system.
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //Set the CrashHandler as the default processor for the program.
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        if (handlerException(t) && mDefaultHandler != null)
            mDefaultHandler.uncaughtException(thread, t);//If the user is not processing, let the default processor of the system handle it.
        if (mOnCrashHandler != null)
            mOnCrashHandler.onCrashHandler(/*log, phoneInfo,*/ t);
    }

    /**
     * Custom error handling, error collection, error reporting, and other operations are completed here.
     *
     * @param t Exception
     * @return If this exception message is processed, otherwise true. is returned.
     */
    private boolean handlerException(Throwable t) {
        return false;
    }

    /**
     * Custom exception handling callback.
     */
    public interface OnCrashHandler {

        void onCrashHandler(Throwable t);
    }
}
