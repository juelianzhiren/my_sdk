package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztq.sdk.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {
    /**线程池，用于加载xml*/
    private ExecutorService mThreadPool;

    /**地图画笔*/
    private Paint mPaint;

    /**SVG地图资源Id*/
    private int mMapResId = -1;

    /**解析得到的省份列表*/
    private List<ProvinceItem> mItemList;

    /**整个地图的最大矩形边界*/
    private RectF mMaxRect;

    /**省份区块颜色列表*/
    private int[] mColorArray = new int[] { 0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1 };

    /**当前选择的省份Item*/
    private ProvinceItem mSelectItem;

    /**地图缩放比例*/
    private float mScale = 1f;
    private SelectInterface mSelectInterface;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 初始化线程池
        initThreadPool();
        // 解析自定义属性
        getMapResource(context, attrs, defStyleAttr);
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        };
        mThreadPool = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(10), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 解析自定义属性
     */
    private void getMapResource(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MapView, defStyleAttr, 0);
        int resId = a.getResourceId(R.styleable.MapView_map, -1);
        a.recycle();
        setMapResId(resId);
    }

    /**
     * 设置地图资源Id
     */
    public void setMapResId(int resId) {
        mMapResId = resId;
        executeLoad();
    }

    /**
     * 执行加载
     */
    private void executeLoad() {
        if (mMapResId <= 0) {
            return;
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 获取xml文件输入流
                InputStream inputStream = getResources().openRawResource(mMapResId);
                // 创建解析实例
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    // 解析输入流，得到Document实例
                    Document doc = builder.parse(inputStream);
                    // 获取根节点，即vector节点
                    Element rootElement = doc.getDocumentElement();
                    // 获取所有的path节点
                    NodeList items = rootElement.getElementsByTagName("path");

                    // 以下四个变量用来保存地图四个边界，用于确定缩放比例(适配屏幕)
                    float left = -1;
                    float right = -1;
                    float top = -1;
                    float bottom = -1;

                    // 解析path节点
                    List<ProvinceItem> list = new ArrayList<>();
                    for (int i = 0; i < items.getLength(); ++i) {
                        Element element = (Element) items.item(i);
                        // 获取pathData内容
                        String pathData = element.getAttribute("android:pathData");
                        // 将pathData转换为path
                        Path path = PathParser.createPathFromPathData(pathData);
                        String id = element.getAttribute("id");
                        String title = element.getAttribute("title");

                        // 封装成ProvinceItem对象
                        ProvinceItem provinceItem = new ProvinceItem(path, id, title);
                        provinceItem.setDrawColor(mColorArray[i % mColorArray.length]);

                        RectF rectF = new RectF();
                        // 计算当前path区域的矩形边界
                        path.computeBounds(rectF, true);
                        // 判断边界，最终获得的就是整个地图的最大矩形边界
                        left = left < 0 ? rectF.left : Math.min(left, rectF.left);
                        right = Math.max(right, rectF.right);
                        top = top < 0 ? rectF.top : Math.min(top, rectF.top);
                        bottom = Math.max(bottom, rectF.bottom);

                        list.add(provinceItem);
                    }
                    // 解析完成，保存节点列表和最大边界
                    mItemList = list;
                    mMaxRect = new RectF(left, top, right, bottom);
                    // 通知重新布局和绘制
                    post(new Runnable() {
                        @Override
                        public void run() {
                            requestLayout();
                            invalidate();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setSelectInterface(SelectInterface mSelectInterface) {
        this.mSelectInterface = mSelectInterface;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mMaxRect != null) {
            // 获取缩放比例
            double mapWidth = mMaxRect.width();
            mScale = (float) (width / mapWidth);
        }

        // 应用测量数据
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItemList != null) {
            // 使地图从画布左上角开始绘制（图片本身可能存在边距）
            canvas.translate(-mMaxRect.left, -mMaxRect.top);
            // 设置画布缩放，以(mMaxRect.left, mMaxRect.top)为基准进行缩放
            // 因为当前该点对应屏幕左上(0, 0)点
            canvas.scale(mScale, mScale, mMaxRect.left, mMaxRect.top);
            // 绘制所有省份区域，并设置是否选中状态
            for (ProvinceItem provinceItem : mItemList) {
                provinceItem.drawItem(canvas, mPaint, mSelectItem == provinceItem);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将事件分发给所有的区块，如果事件未被消费，则调用View的onTouchEvent，这里会默认范围false
        if (handleTouch((int) (event.getX() / mScale + mMaxRect.left), (int) (event.getY() / mScale + mMaxRect.top), event)) {
            if (event.getAction() == MotionEvent.ACTION_UP && mSelectInterface != null) {
                mSelectInterface.onSelect(mSelectItem);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 派发触摸事件
     */
    private boolean handleTouch(int x, int y, MotionEvent event) {
        if (mItemList == null) {
            return false;
        }

        boolean isTouch = false;

        ProvinceItem selectItem = null;
        for (ProvinceItem provinceItem : mItemList) {
            // 依次派发事件
            if (provinceItem.isTouch(x, y, event)) {
                // 选中省份区块
                selectItem = provinceItem;
                isTouch = true;
                break;
            }
        }

        if (selectItem != null && selectItem != mSelectItem) {
            mSelectItem = selectItem;
            // 通知重绘
            postInvalidate();
        }
        return isTouch;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mThreadPool != null && !mThreadPool.isShutdown() && !mThreadPool.isTerminated()) {
            // 释放线程池
            mThreadPool.shutdown();
        }
    }

    public static class ProvinceItem {
        private String id;
        private String title;
        /**省份路径*/
        private Path mPath;

        /**区块颜色*/
        private int mDrawColor;

        /**Path的有效区域*/
        private Region mRegion;

        public ProvinceItem(Path path, String id, String title) {
            this.mPath = path;
            this.id = id;
            this.title = title;

            RectF rectF = new RectF();
            // 计算path的边界, exact参数无所谓，该方法不再使用这个参数
            mPath.computeBounds(rectF, true);

            mRegion = new Region();
            // 得到path和其最大矩形范围的交集区域
            mRegion.setPath(mPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        }

        /**绘制区块*/
        public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
            if (isSelect) {
                // 选中状态
                paint.clearShadowLayer();
                paint.setStrokeWidth(1);

                // 绘制填充
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(mDrawColor);
                canvas.drawPath(mPath, paint);

                // 绘制描边
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                paint.setColor(Color.BLACK);
                canvas.drawPath(mPath, paint);
            } else {
                // 普通状态
                paint.setStrokeWidth(2);

                // 绘制底色+阴影
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                paint.setShadowLayer(8, 0, 0, 0xffffff);
                canvas.drawPath(mPath, paint);

                // 绘制填充
                paint.clearShadowLayer();
                paint.setColor(mDrawColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(mPath, paint);
            }
        }

        public boolean isTouch(int x, int y, MotionEvent event) {
            boolean isTouch = mRegion.contains(x, y);
            if (isTouch) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 按下
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 滑动
                        break;
                    case MotionEvent.ACTION_UP:
                        // 抬起
                        break;
                    default:
                        break;
                }
            }
            return isTouch;
        }

        /**
         * 设置区块绘制颜色
         */
        public void setDrawColor(int drawColor) {
            this.mDrawColor = drawColor;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public interface SelectInterface {
        public void onSelect(ProvinceItem item);
    }
}