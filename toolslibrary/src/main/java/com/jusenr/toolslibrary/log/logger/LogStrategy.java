package com.jusenr.toolslibrary.log.logger;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
