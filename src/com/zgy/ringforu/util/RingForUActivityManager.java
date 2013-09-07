package com.zgy.ringforu.util;

import java.util.Stack;

import android.app.Activity;
/**
 * activity 管理
 * @Description:
 * @author:huangyx2  
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-7-24
 */
public class RingForUActivityManager {

	private static Stack<Activity> stack = new Stack<Activity>();// Activity栈
	/**
	 * 移除所有activity
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void popAll() {
		while (!stack.isEmpty()) {
			pop();
		}
	}
	/**
	 * 移除位于栈顶的activity
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void pop() {
		Activity activity = stack.pop();
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
		}
	}
	/**
	 * 移除指定activity
	 * @Description:
	 * @param activity
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void pop(Activity activity) {
		if(!activity.isFinishing()) {
			activity.finish();
		}
		stack.remove(activity);
	}
	/**
	 * 移除并关闭指定某一类的activity
	 * @Description:
	 * @param cls
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void popClass(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for(Activity a : stack) {
			if(a.getClass().equals(cls)) {
				if(!a.isFinishing()) {
					a.finish();
				}
			}else {
				newStack.push(a);
			}
		}
		stack = newStack;
	}
	/**
	 * 获取在栈顶的activity
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static Activity current() {
		if(stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}
	/**
	 * 添加activity到栈中
	 * @Description:
	 * @param activity
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void push(Activity activity) {
		stack.push(activity);
	}
	/**
	 * 保留某一类的activity，其它的都关闭并移出栈
	 * @Description:
	 * @param cls
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public static void retain(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for(Activity a : stack) {
			if(a.getClass().equals(cls)) {
				newStack.push(a);
			}else {
				if(!a.isFinishing()) {
					a.finish();
				}
			}
		}
		stack = newStack;
	}
}
