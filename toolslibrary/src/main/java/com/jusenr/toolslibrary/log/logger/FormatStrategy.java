package com.jusenr.toolslibrary.log.logger;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
