package com.ijustyce.contacts;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LockScreenView extends View{
	
	private int endX;
	private int endY;
	private int startX;
	private int startY;
	private Paint linePaint;
	private Paint circlePaint;
	private int viewWidth;		//控件的宽
	private int viewHeight;		//控件的高
	private int radius;
	
	private PointF[][] centerCxCy;	//圆心矩阵
	private int[][] data;		//密码数据矩阵
	private boolean[][] selected;	//已选矩阵 选中之后不能重选
	private List<PointF> selPointList;	//选中的圆中点集合
	
	private boolean isPressedDown = false;
	
	private String lockPin="";	//结果锁屏密码字符串

	public LockScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LockScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LockScreenView(Context context) {
		super(context);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		linePaint = new Paint();
//		linePaint.setColor(Color.rgb(255, 110, 2));
//		linePaint.setStrokeWidth(15);	//画笔宽度
		linePaint.setStyle(Style.FILL);		//画笔样式
		linePaint.setAntiAlias(true);
		
		circlePaint = new Paint();
//		circlePaint.setColor(Color.rgb(155, 160, 170));
		circlePaint.setStrokeWidth(4);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.STROKE);  //画笔样式空心
		
		centerCxCy = new PointF[3][3];
		data = new int[3][3];
		selected = new boolean[3][3];
		selPointList = new ArrayList<PointF>();
		initData();
	}
	
	//清除选中记录
	private void clearSelected(){
		for(int i=0;i<selected.length;i++){
			for(int j=0;j<selected.length;j++){
				selected[i][j] = false;
			}
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		int num = 1;
		for(int i=0;i<data[0].length;i++){
			for(int j=0;j<data[0].length;j++){
				data[j][i] = num;
				num++;
			}
		}
	}
	
	//判断是否在某个圆内
	private boolean isInCircle(PointF p, int x,int y){
		int distance = (int)Math.sqrt((p.x-x)*(p.x-x)+(p.y-y)*(p.y-y));
		if(distance <= radius){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		linePaint.setStrokeWidth(2*radius/3-2);		//画笔宽度
		
		//手势开始前的画圆
		for(int i=0;i<selected[0].length;i++){
			for(int j=0;j<selected[0].length;j++){
				PointF center = centerCxCy[i][j];
				if(selected[i][j]){		//是否选中
					circlePaint.setColor(Color.rgb(255, 110, 2));	//滑动到圆内时设置黄色画笔
					linePaint.setColor(Color.rgb(255, 110, 2));
					canvas.drawCircle(center.x, center.y, radius/3, linePaint); //选中时的实心内圆
					linePaint.setColor(Color.argb(96,255, 110, 2));
					canvas.drawCircle(center.x, center.y, radius, linePaint); //选中时的外圆半透明填充
				}else{
					circlePaint.setColor(Color.rgb(155, 160, 170));  //否则设置灰白色画笔
				}
				canvas.drawCircle(center.x, center.y, radius, circlePaint);  //画外圆
			}
		}
		
		linePaint.setColor(Color.argb(96,255, 110, 2));
		//画线条路径
		if(isPressedDown){
			for(int i=0;i<selPointList.size()-1;i++){		//画已选中圆之间的路径
				PointF preCenter = selPointList.get(i);		//前一个圆中点
				PointF curCenter = selPointList.get(i+1);	//现在圆中点
				canvas.drawLine(preCenter.x, preCenter.y, curCenter.x, curCenter.y, linePaint);
			}
			
			if(selPointList.size()>0){
				PointF center = selPointList.get(selPointList.size()-1); //最后一个选中圆中点
				canvas.drawLine(center.x, center.y, endX, endY, linePaint);
			}
		
		}
		
		
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		if (changed) {
			viewWidth = getWidth();	
			viewHeight = getHeight();
			setRadius();
			
			Log.i("info", "viewWidth="+viewWidth+"viewHeight="+viewHeight);
		}
		
		super.onLayout(changed, left, top, right, bottom);
	}

	//设置圆的半径和圆心
	private void setRadius() {

		int w = viewWidth/3;	
		int h = viewHeight/3;
		radius = w/4;
		for(int i=0;i<centerCxCy[0].length;i++){
			for(int j=0;j<centerCxCy[0].length;j++){
				PointF p = new PointF();
				p.x = (i*w)+w/2;
				p.y = (j*h)+h/2;
				centerCxCy[i][j]=p;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 得到触摸点的坐标
		int pin=0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lockPin = "";
			selPointList.clear();
			
			isPressedDown = true;
			startX = (int)event.getX();
			startY = (int)event.getY();
			
			pin = getLockPinData(startX,startY);
			if(pin > 0){
				lockPin+=pin;
				invalidate();
			}
			break;

		case MotionEvent.ACTION_MOVE:
			endX = (int)event.getX();
			endY = (int)event.getY();

			pin = getLockPinData(endX,endY);
			if(pin > 0){
				lockPin+=pin;
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			endX = (int)event.getX();
			endY = (int)event.getY();
			isPressedDown = false;

			Log.i("info", "lockPin = "+lockPin);
			Password.lockPin = lockPin;
			clearSelected();
			invalidate();
			return false;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

	private int getLockPinData(int x, int y) {
		// TODO Auto-generated method stub
		for(int i=0;i<data[0].length;i++){
			for(int j=0;j<data[0].length;j++){
				PointF center = centerCxCy[i][j];
				if(isInCircle(center, x, y)){	//判断是否在圆内
					if(!selected[i][j]){
						selected[i][j] = true;
						selPointList.add(center);
						return data[i][j];
					}
				}
			}
		}
		return 0;
	}
}
