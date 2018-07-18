package cn.ric.action.check;

import cn.ric.action.Window;

public class CycleCheck extends MKCheck {
 private int times;
 private int current;

 @Override
 public boolean doCheck(Window win) {
  current++;
  boolean result = false;
  switch (getCheckType()) {
  case 0:
   result = current < times;
   break;
  case 1:
   result = current % times == 0;
   break;
  }
  return result;
 }

 @Override
 public String toString() {
  return MKCheck.Type.CycleCheck.name() + "\\*/" + getCheckType() + "\\*/" + times;
 }

 public int getTimes() {
  return times;
 }

 public void setTimes(int times) {
  this.times = times;
 }

 public int getCurrent() {
  return current;
 }

 public void setCurrent(int current) {
  this.current = current;
 }

}
