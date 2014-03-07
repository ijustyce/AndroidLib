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
	private int viewWidth;		//�ؼ��Ŀ�
	private int viewHeight;		//�ؼ��ĸ�
	private int radius;
	
	private PointF[][] centerCxCy;	//Բ�ľ���
	private int[][] data;		//������ݾ���
	private boolean[][] selected;	//��ѡ���� ѡ��֮������ѡ
	private List<PointF> selPointList;	//ѡ�е�Բ�е㼯��
	
	private boolean isPressedDown = false;
	
	private String lockPin="";	//������������ַ�

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
//		linePaint.setStrokeWidth(15);	//���ʿ��
		linePaint.setStyle(Style.FILL);		//������ʽ
		linePaint.setAntiAlias(true);
		
		circlePaint = new Paint();
//		circlePaint.setColor(Color.rgb(155, 160, 170));
		circlePaint.setStrokeWidth(4);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.STROKE);  //������ʽ����
		
		centerCxCy = new PointF[3][3];
		data = new int[3][3];
		selected = new boolean[3][3];
		selPointList = new ArrayList<PointF>();
		initData();
	}
	
	//���ѡ�м�¼
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
	
	//�ж��Ƿ���ĳ��Բ��
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
		linePaint.setStrokeWidth(2*radius/3-2);		//���ʿ��
		
		//���ƿ�ʼǰ�Ļ�Բ
		for(int i=0;i<selected[0].length;i++){
			for(int j=0;j<selected[0].length;j++){
				PointF center = centerCxCy[i][j];
				if(selected[i][j]){		//�Ƿ�ѡ��
					circlePaint.setColor(Color.rgb(255, 110, 2));	//������Բ��ʱ���û�ɫ����
					linePaint.setColor(Color.rgb(255, 110, 2));
					canvas.drawCircle(center.x, center.y, radius/3, linePaint); //ѡ��ʱ��ʵ����Բ
					linePaint.setColor(Color.argb(96,255, 110, 2));
					canvas.drawCircle(center.x, center.y, radius, linePaint); //ѡ��ʱ����Բ��͸�����
				}else{
					circlePaint.setColor(Color.rgb(155, 160, 170));  //�������ûҰ�ɫ����
				}
				canvas.drawCircle(center.x, center.y, radius, circlePaint);  //����Բ
			}
		}
		
		linePaint.setColor(Color.argb(96,255, 110, 2));
		//������·��
		if(isPressedDown){
			for(int i=0;i<selPointList.size()-1;i++){		//����ѡ��Բ֮���·��
				PointF preCenter = selPointList.get(i);		//ǰһ��Բ�е�
				PointF curCenter = selPointList.get(i+1);	//����Բ�е�
				canvas.drawLine(preCenter.x, preCenter.y, curCenter.x, curCenter.y, linePaint);
			}
			
			if(selPointList.size()>0){
				PointF center = selPointList.get(selPointList.size()-1); //���һ��ѡ��Բ�е�
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

	//����Բ�İ뾶��Բ��
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
		// �õ�����������
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
				if(isInCircle(center, x, y)){	//�ж��Ƿ���Բ��
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
