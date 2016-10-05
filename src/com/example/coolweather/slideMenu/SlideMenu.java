package com.example.coolweather.slideMenu;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlideMenu extends FrameLayout {
	
	private Scroller scroller;
	
	
	public SlideMenu(Context context){
		
		super(context);
		
		init();
	}
    
	

	public SlideMenu(Context context, AttributeSet attrs){
		
		super(context,attrs);
		
		init();
	}
	
	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		
		init();
	}
	
	private void init() {
		
		scroller = new Scroller(getContext());
	}
	
	private View main;
	
	private View menu;
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		
		//获取主view
		main = getChildAt(0);  
		//获取menu 的view
		menu = getChildAt(1);
		
		//设置view显示
		
		main.layout(0, 0, main.getMeasuredWidth(), main.getMeasuredHeight());
		
		menu.layout(-menu.getMeasuredWidth(), 0, 0, menu.getMeasuredHeight());
	}
	
	//设置触摸按下的x 和 y
	
	private float downX;
	private float downY;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			
			downX = ev.getX();
			
			downY = ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			float moveX = ev.getX();
			float moveY = ev.getY();
			
			double deltaX = moveX - downX;
			double detlaY = moveY - downY;
			
			//如果手指水平移动大于垂直移动
			
			if(Math.abs(deltaX) > Math.abs(detlaY)){
				
				//将触摸事件当前touch view处理
				return true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		
		return super.onInterceptHoverEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			
			downX = event.getX();
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			float moveX = event.getX();
			
			double deltaX = moveX - downX;
			
			int scrollX = (int) (getScrollX() - deltaX);
			
			//限制menuView归位
			
			if(scrollX > 0){
				
				scrollX = 0;
			}
			
			if(scrollX < - menu.getMeasuredWidth()){
				
				scrollX = - menu.getMeasuredWidth();
			}
			
			scrollTo(scrollX, 0);
			
			downX = moveX;
			
			break;
			
		case MotionEvent.ACTION_UP:
			
			if(getScrollX() < -menu.getMeasuredWidth() / 2){
				
				int dx = (int) (-menu.getMeasuredWidth() - getScrollX());
				
				scroller.startScroll(getScrollX(), 0, dx,0, 350);
				
				invalidate();
			}else {
				
				int dx = (int) (0 - getScrollX());
				
				scroller.startScroll(getScrollX(), 0, dx, 0 ,350);
				
				invalidate();
			}
			
			break;

		default:
			break;
		}
		
		
		return true;
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		
		if(scroller.computeScrollOffset()){
			
			int currX = scroller.getCurrX();
			
			int currY = scroller.getCurrY();
			
			scrollTo(currX, currY);
			
			invalidate();
		}
	}
	
	public void showOrHideMenu(){
		
		int scrollX = 0;
		
		if(getScrollX() == 0){
			
			scrollX = - menu.getMeasuredWidth();
			
			
			
		}else {
			
			scrollX = menu.getMeasuredWidth();
			
			
		}
		
		scroller.startScroll(getScrollX(), 0, scrollX ,0,500);
		invalidate();
	}

}



















