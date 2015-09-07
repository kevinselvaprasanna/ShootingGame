package com.example.shootinggame;


import java.util.Random;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class ShootingGame extends Activity implements OnTouchListener{

	Myclasssurface oursurfaceview;
	float x,y,sx,sy,fx,fy,dx,dy,ax,ay,scx,scy,rx=0,ry,h1,h2,q;
	Bitmap test,plus,red,black;
	long time,st;
	Random r = new Random();
	SoundPool sp;
	int explosion=0,point=0,hs=0;
	MediaPlayer mp;
	 Paint textpaint = new Paint();
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		oursurfaceview = new Myclasssurface(this);
		oursurfaceview.setOnTouchListener(this);
		x=0;
		y=0;
		sx=0;
		sy=0;
		fx=0;
		fy=0;
		dx=dy=ax=ay=scx=scy=0;
		 test = BitmapFactory.decodeResource(getResources(), R.drawable.greenball);
		 plus = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
		 red = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
		 black = BitmapFactory.decodeResource(getResources(), R.drawable.black);
		 
		 sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			explosion = sp.load(this, R.raw.explosion, 1);
			mp= MediaPlayer.create(this, R.raw.backgroundmusic);
		 
		//Full screen
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(oursurfaceview);
			
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		oursurfaceview.pause();
		mp.stop();
		mp.reset();
		mp.release();
		finish();
		
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		oursurfaceview.resume();
		mp.start();
	}


	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		x=arg1.getX();
		y=arg1.getY();
		
		switch(arg1.getAction()){
		case MotionEvent.ACTION_DOWN:
			sx= arg1.getX();
			sy= arg1.getY();
			dx=dy=0;
			break;
		case MotionEvent.ACTION_UP:
			fx= arg1.getX();fy= arg1.getY();
			ax=ay=0;
			x=y=0;
			dx=fx-sx;
			dy=fy-sy;
			scx=dx/17;
			scy=dy/17;
			break;
		}
		return true;
	}
	public class Myclasssurface extends SurfaceView implements Runnable {
		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false,isOver=false,starting = true;
		
		
		public Myclasssurface(Context context) {
			super(context);
			ourHolder = getHolder();
		
			
		}
		
		public void pause(){
			isRunning = false;
			while(true){
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}
		
		public void resume(){
			isRunning = true;
			isOver = false;
			st  =System.currentTimeMillis();
			if(point>hs)
				hs = point;
			point =0;
			ourThread = new Thread(this);
			ourThread.start();

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(starting)
			{
				if(x!=0)
				{
					starting = false;
				}
				
				if(!ourHolder.getSurface().isValid())
					continue;
				Canvas start = ourHolder.lockCanvas();
				start.drawRGB(96,47, 2);
				textpaint.setARGB(255, 255, 255, 255);
				textpaint.setTextAlign(Align.CENTER);
				textpaint.setTextSize(45);
				start.drawText("Touch to play...", start.getWidth()/2,start.getHeight()/2-150, textpaint);
				textpaint.setTextSize(25);
				start.drawText("Swipe on the screen to shoot the green ball", start.getWidth()/2, start.getHeight()/2, textpaint);
				start.drawText("at the red ball to gain points...", start.getWidth()/2, start.getHeight()/2+30, textpaint);
				ourHolder.unlockCanvasAndPost(start);
				
			}
			
			
			while(isRunning){
				q=x;
				if(time-st>30000)
				{
					isOver = true;
					isRunning = false;
				}
				if(!ourHolder.getSurface().isValid())
					continue;
				Canvas canvas = ourHolder.lockCanvas();
				canvas.drawRGB(249, 219, 9);
				ax+=scx;
				ay+=scy;
				 if((mod((fx-ax)-rx)<65)&&(mod((fy-ay)-ry)<65)&&(fx!=0)&&(fy!=0)&&(dx!=0||dy!=0))
				 {
					 if(explosion!=0)
					 sp.play(explosion, 1, 1,0, 0, 1);
					 point++;
				 }
				if(x !=0 && y !=0)
				{
				   canvas.drawBitmap(test, x-(test.getWidth()/2), y-(test.getHeight()/2), null);
				}
				if(sx !=0 && sy !=0)
				{ 
				   //canvas.drawBitmap(plus, sx-(plus.getWidth()/2), sy-(plus.getHeight()/2), null);
				}
				if(fx !=0 && fy !=0&&(dx!=0||dy!=0))
				{
				   canvas.drawBitmap(test, fx-(test.getWidth()/2)-ax, fy-(test.getHeight()/2)-ay, null);
				   //canvas.drawBitmap(plus, fx-(plus.getWidth()/2), fy-(plus.getHeight()/2), null);
				   
				}
				
				time = System.currentTimeMillis();
				if((time%150)==0){
					int i = canvas.getHeight();
					h1 = r.nextInt(i);
					h2 = r.nextInt(i);
					rx=0;
					ry =h1;
				}
				rx+=4;
				ry+=(h2-h1)/120;
				 canvas.drawBitmap(red, rx-(red.getWidth()/2),ry-(red.getHeight()/2), null);
				 
				
					
					textpaint.setTextAlign(Align.LEFT);
					textpaint.setTextSize(50);
					canvas.drawText(" " + point, canvas.getWidth()/2+115, 100, textpaint);
					int sec=30-(((int)(time-st))/1000);
					int milli =1000-(((int)(time-st))%1000);
					if(sec<5)
					{
						if(milli>500)
							textpaint.setARGB(255, 255, 0, 0);
						else
							textpaint.setARGB(255, 255, 255, 255);
					}
					canvas.drawText("Time Left : " + sec + ":" + milli, canvas.getWidth()/2-200, canvas.getHeight()/2+350, textpaint);
				 ourHolder.unlockCanvasAndPost(canvas);
				 while(isOver)
					{
						isRunning = false;
						Canvas score = ourHolder.lockCanvas();
						score.drawRGB(96,47,2);
						textpaint.setARGB(255, 255,255, 255);
						textpaint.setTextAlign(Align.CENTER);
						textpaint.setTextSize(70);
						score.drawText("GAME OVER!!", score.getWidth()/2,score.getHeight()/2-150, textpaint);
						score.drawText("Score: " + point, score.getWidth()/2,score.getHeight()/2, textpaint);
						textpaint.setTextSize(55);
						if(q!=x)
							resume();
						q=x;
						score.drawText("Best: " + hs, score.getWidth()/2,score.getHeight()/2+120, textpaint);
						score.drawText("Touch to Replay", score.getWidth()/2,score.getHeight()/2+170, textpaint);
						ourHolder.unlockCanvasAndPost(score);
					}
				 
			}
			
			
			
		}
		
		

	}
	public static float mod(float n)
	{
		if(n>=0)
			return n;
		if(n<0)
			return (-n);
		return n;
	}

}
