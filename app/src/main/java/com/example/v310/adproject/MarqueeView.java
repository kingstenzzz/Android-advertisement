package com.example.v310.adproject;
import com.example.v310.adproject.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2015/12/4.
 */
public class MarqueeView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private RefreshThread myThread;

    protected TextPaint mTextPaint;

    protected int ShadowColor=Color.WHITE;//艺术字阴影颜色
    protected float textSize = 50; //时间数字的字体大小
    protected int textColor = Color.BLUE; //时间数字的颜色
    private String margueeString;
    private int textWidth=0,textHeight1=0,textHeight2=0,textHeight=0;
    protected float padTextSize=0;


    public MarqueeView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(null, 0);
    }
    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(AttributeSet attrs, int defStyle){
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs,R.styleable.marquee, defStyle, 0);
        textColor = a.getColor(
                R.styleable.marquee_textcolor,
                Color.BLUE);
        textSize = a.getDimension(
                R.styleable.marquee_textSize,
                48);

        holder = this.getHolder();
        holder.addCallback(this);
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        myThread = new RefreshThread(holder);//创建一个绘图线程
    }

    public void setText(String msg){
        if(!TextUtils.isEmpty(msg)){
            measurementsText(msg);
        }
    }
    protected void measurementsText(String msg) {
        margueeString=msg;
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setFakeBoldText(true);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mTextPaint.setShadowLayer(5, 3, 3, ShadowColor);
        textWidth = (int)mTextPaint.measureText(margueeString);//文本长度
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        textHeight1 = (int) fontMetrics.bottom;
        textHeight2 = (int) fontMetrics.ascent;
        textHeight= textHeight2-textHeight1;//字符高度
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.isRun = true;
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.isRun = false;
    }



    //线程内部类
    class RefreshThread extends Thread
    {
        private SurfaceHolder holder;
        public boolean isRun ;
        public int currentX=0;
        public int sepX=15;

        public  RefreshThread(SurfaceHolder holder)
        {
            this.holder =holder;
            isRun = true;
        }

        public void onDraw() {
            try
            {
                synchronized (holder)
                {

                    if(TextUtils.isEmpty(margueeString)){
                        Thread.sleep(100);//睡眠时间为0.1秒
                        return;
                    }

                    Canvas canvas = holder.lockCanvas();
                    // TODO: consider storing these as member variables to reduce



                    int paddingLeft = getPaddingLeft();
                    int paddingTop = getPaddingTop();
                    int paddingRight = getPaddingRight();
                    int paddingBottom = getPaddingBottom();

                    // int contentWidth = getWidth() - paddingLeft - paddingRight;
                    int contentWidth = getWidth() - paddingRight - paddingLeft;//文本框宽度的

                    int contentHeight = getHeight() - paddingTop - paddingBottom;//文本框高度的

                    int centeYLine =  paddingTop+contentHeight/2 - textHeight/2;//中心线


/*
                    if(currentX>=contentWidth){
                    currentX=-textWidth;
                    }else{
                        currentX+=sepX;
                    }
                    */
                    //横坐标右对齐，滚动到最左端时字符串坐标小于0，让其坐至加上文本框宽度和字符串长度
                    if( currentX<0){
                        currentX+=textWidth+contentWidth;
                    }else{
                        currentX-=sepX;//移动速度
                    }

                    canvas.drawColor(Color.WHITE);//背景颜色
                    canvas.drawText(margueeString,
                            currentX,//当前字符串最右端的坐标值
                            centeYLine ,//字符串底部是纵坐标
                            mTextPaint);
                    holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
                    Thread.sleep(100);//睡眠时间为0.1秒
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

        @Override
        public void run()
        {

            while(isRun)
            {

                onDraw();

            }
        }
    }

}
