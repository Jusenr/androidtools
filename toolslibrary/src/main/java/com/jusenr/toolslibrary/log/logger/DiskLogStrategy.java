/*
 * Copyright 2017 androidtools Jusenr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jusenr.toolslibrary.log.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 */
public class DiskLogStrategy implements LogStrategy {

  private final Handler handler;

  public DiskLogStrategy(Handler handler) {
    this.handler = handler;
  }

  @Override
  public void log(int level, String tag, String message) {
    // do nothing on the calling thread, simply pass the tag/msg to the background thread
    handler.sendMessage(handler.obtainMessage(level, message));
  }

  static class WriteHandler extends Handler {

    private final String folder;
    private final int maxFileSize;

    WriteHandler(Looper looper, String folder, int maxFileSize) {
      super(looper);
      this.folder = folder;
      this.maxFileSize = maxFileSize;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override
    public void handleMessage(Message msg) {
      String content = (String) msg.obj;

      FileWriter fileWriter = null;
      File logFile = getLogFile(folder, "logs");

      try {
        fileWriter = new FileWriter(logFile, true);

        writeLog(fileWriter, content);

        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        if (fileWriter != null) {
          try {
            fileWriter.flush();
            fileWriter.close();
          } catch (IOException e1) { /* fail silently */ }
        }
      }
    }

    /**
     * This is always called on a single background thread.
     * Implementing classes must ONLY write to the fileWriter and nothing more.
     * The abstract class takes care of everything else including close the stream and catching IOException
     *
     * @param fileWriter an instance of FileWriter already initialised to the correct file
     */
    private void writeLog(FileWriter fileWriter, String content) throws IOException {
      fileWriter.append(content);
    }

    private File getLogFile(String folderName, String fileName) {

      File folder = new File(folderName);
      if (!folder.exists()) {
        //TODO: What if folder is not created, what happens then?
        folder.mkdirs();
      }

      int newFileCount = 0;
      File newFile;
      File existingFile = null;

      newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
      while (newFile.exists()) {
        existingFile = newFile;
        newFileCount++;
        newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
      }

      if (existingFile != null) {
        if (existingFile.length() >= maxFileSize) {
          return newFile;
        }
        return existingFile;
      }

      return newFile;
    }
  }
}