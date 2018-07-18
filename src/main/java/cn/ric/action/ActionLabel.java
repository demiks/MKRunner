package cn.ric.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.ric.action.check.CycleCheck;
import cn.ric.action.check.MKCheck;
import cn.ric.action.check.PhotoCheck;
import cn.ric.action.event.MKBeepEvent;
import cn.ric.action.event.MKEvent;
import cn.ric.action.event.MKMouseEvent;
import cn.ric.action.event.MKQuoteEvent;
import cn.ric.util.INI;
import cn.ric.util.StringUtils;
import cn.ric.view.ViewUtils;

public class ActionLabel {
 private String fileName;
 private String windowTitle;
 private ArrayList<MKCheck> checks;
 private ArrayList<MKEvent> events;
 private Thread thread;

 public interface OnDoLabel {
  void hanldMsg(String msg, boolean isEnd);
 }

 /**
  * 执行动作
  * 
  * @return
  */
 public void doLabel(Window win, OnDoLabel onDo) {
  if (thread != null && thread.isAlive()) {
   thread.interrupt();
   onDo.hanldMsg("已终止之前的任务", false);
  }
  thread = new Thread(() -> {
   // check
   if (getEvents().isEmpty()) {
    onDo.hanldMsg("未设置指令", true);
    return;
   }

   // init
   for (MKCheck check : getChecks()) {
    if (check instanceof CycleCheck)
     ((CycleCheck) check).setCurrent(0);
   }
   for (MKEvent event : getEvents()) {
    if (event instanceof MKQuoteEvent)
     try {
      ((MKQuoteEvent) event).reload();
     } catch (Exception e) {
      onDo.hanldMsg(e.getMessage(), false);
     }
   }
   // init end

   for (int i = 1; true; i++) {
    if (win == null || !win.hasWindow()) {
     onDo.hanldMsg("未发现窗口", true);
     return;
    }
    try {
     onDo.hanldMsg("开始执行第 " + i + "次", false);
     boolean pass = true;
     Map<String, XY> xyMap = new HashMap<String, XY>();
     for (MKCheck check : getChecks()) {
      pass = pass && check.doCheck(win);
      if (check instanceof PhotoCheck) {
       PhotoCheck _check = (PhotoCheck) check;
       xyMap.put(_check.getPicPath(), _check.getPosition());
      }
      if (!pass) {
       onDo.hanldMsg(check.getClass().getSimpleName() + " 检测未通过，停止执行", true);
       return;
      }
     }
     for (MKEvent event : getEvents()) {
      if (event instanceof MKMouseEvent) {
       MKMouseEvent _event = (MKMouseEvent) event;
       _event.setPosition(xyMap.get(_event.getPicPath()));
      } else if (event instanceof MKBeepEvent) {
       MKBeepEvent event2 = (MKBeepEvent) event;
       event2.setSavePath(INI.INSTANCE.get("screen-path", "/MKRunner/") + '/' + ViewUtils.cutFilePath(fileName) + '/');

      }
      event.doEvent(win);
     }
    } catch (InterruptedException e) {
     e.printStackTrace();
     break;
    } catch (Exception e) {
     onDo.hanldMsg(e.getMessage(), false);
    }
   }
   onDo.hanldMsg("任务结束", true);
  });
  thread.start();
 }

 /**
  * 停止播放
  */
 public void stopLabel() {
  if (thread != null)
   thread.interrupt();
 }

 public String getFileName() {
  return fileName;
 }

 public void setFileName(String fileName) {
  if (StringUtils.isEmpty(fileName))
   return;
  if (fileName.endsWith(".mk"))
   this.fileName = fileName;
  else
   this.fileName = fileName + ".mk";
 }

 public void saveToFile() throws Exception {
  FileOutputStream fo = null;
  try {
   fo = new FileOutputStream(getFileName());
   fo.write(("*window title*" + getWindowTitle() + '\n').getBytes("utf-8"));
   fo.write(("*check*\n").getBytes("utf-8"));
   for (MKCheck mkCheck : checks) {
    fo.write((mkCheck.toString() + '\n').getBytes("utf-8"));
   }
   fo.write(("*event*\n").getBytes("utf-8"));
   for (MKEvent mkEvent : events) {
    fo.write((mkEvent.toString() + '\n').getBytes("utf-8"));
   }
   fo.flush();
  } finally {
   if (fo != null) {
    fo.close();
   }
  }
  throw new Exception("保存成功:" + getFileName());
 }

 public void loadFromFile() throws Exception {
  checks = new ArrayList<MKCheck>(5);
  events = new ArrayList<MKEvent>();
  if (StringUtils.isEmpty(fileName)) {
   return;
  }
  File file = new File(getFileName());
  if (!file.exists() || file.isDirectory() || !file.getName().endsWith(".mk")) {
   throw new Exception("错误的文件地址:" + getFileName());
  }
  BufferedReader br = null;
  try {
   br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
   String line = br.readLine();
   short flag = 0;
   while (line != null) {
    if (StringUtils.startsWithAndNotEquals("*window title*", line)) {
     setWindowTitle(line.substring(14));
    } else if ("*check*".equals(line)) {
     flag = 1;
     checks = new ArrayList<MKCheck>();
    } else if ("*event*".equals(line)) {
     flag = 2;
     events = new ArrayList<MKEvent>();
    } else if (!StringUtils.isEmpty(line)) {
     if (flag == 1) {
      checks.add(MKCheck.getFromStr(line));
     } else if (flag == 2) {
      MKEvent fromStr = MKEvent.getFromStr(line);
      events.add(fromStr);
     }
    }
    line = br.readLine();
   }
  } catch (final Exception e1) {
   throw new Exception("文件非法:" + fileName + "\n  " + e1.getMessage());
  } finally {
   if (br != null) {
    try {
     br.close();
    } catch (final IOException e1) {
     e1.printStackTrace();
    }
   }
  }
 }

 public ArrayList<MKCheck> getChecks() {
  return checks;
 }

 public void setChecks(ArrayList<MKCheck> checks) {
  this.checks = checks;
 }

 public ArrayList<MKEvent> getEvents() {
  return events;
 }

 public void setEvents(ArrayList<MKEvent> events) {
  this.events = events;
 }

 public String getWindowTitle() {
  return windowTitle == null ? "" : windowTitle;
 }

 public void setWindowTitle(String windowTitle) {
  this.windowTitle = windowTitle;
 }

}
