package com.cheng.test.baskballboard.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.test.baskballboard.R;
import com.cheng.test.baskballboard.adapter.ColorSpinnerAdapter;
import com.cheng.test.baskballboard.adapter.ListViewAdapter;
import com.cheng.test.baskballboard.adapter.WidthSpinnerAdapter;
import com.cheng.test.baskballboard.javabean.ButtonLocation;
import com.cheng.test.baskballboard.javabean.PaintColor;
import com.cheng.test.baskballboard.javabean.PaintWidth;
import com.cheng.test.baskballboard.javabean.Player;
import com.cheng.test.baskballboard.utils.SPUtils;
import com.cheng.test.baskballboard.utils.UIUtils;
import com.cheng.test.baskballboard.view.AutoTextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        View.OnTouchListener {

    private int mBluePaintColor = Color.BLUE;
    private float mPaintWidth = 12f;
    private int mRedPaintColor = Color.RED;

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private ImageButton mBottomMenu;
    private ImageButton mBottomPlay;
    private ImageButton mBottomAdd;
    private ImageButton mBottomReset;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private int mStartX;
    private int mStartY;
    private Button mBall;
    private Button mBlue1;
    private Button mBlue2;
    private Button mBlue3;
    private Button mBlue4;
    private Button mBlue5;
    private Button mRed1;
    private Button mRed2;
    private Button mRed3;
    private Button mRed4;
    private Button mRed5;
    private boolean isPlaying = false;
    private RelativeLayout mTitleBar;
    private RelativeLayout mBottomBar;
    private ImageView mImageSrc;
    private Bitmap mSrcBitmap;
    private Bitmap mCopyBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private float mWidthProportion;
    private float mHeightProportion;

    private Player ball;
    private ArrayList<Player> ballList = new ArrayList<>();
    private Player bluePlayer1;
    private ArrayList<Player> bluePlayer1List = new ArrayList<>();
    private Player bluePlayer2;
    private ArrayList<Player> bluePlayer2List = new ArrayList<>();
    private Player bluePlayer3;
    private ArrayList<Player> bluePlayer3List = new ArrayList<>();
    private Player bluePlayer4;
    private ArrayList<Player> bluePlayer4List = new ArrayList<>();
    private Player bluePlayer5;
    private ArrayList<Player> bluePlayer5List = new ArrayList<>();
    private Player redPlayer1;
    private ArrayList<Player> redPlayer1List = new ArrayList<>();
    private Player redPlayer2;
    private ArrayList<Player> redPlayer2List = new ArrayList<>();
    private Player redPlayer3;
    private ArrayList<Player> redPlayer3List = new ArrayList<>();
    private Player redPlayer4;
    private ArrayList<Player> redPlayer4List = new ArrayList<>();
    private Player redPlayer5;
    private ArrayList<Player> redPlayer5List = new ArrayList<>();
    private int mCount;

    private ButtonLocation[] mOriginalLocation = new ButtonLocation[11];
    private boolean isOriginalLocation = true;
    private AutoTextView mAutoText;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mAutoText.next();
                mAutoText.setText(mText[mCount]);
            } else if (msg.what == 2) {
                mAutoText.next();
                mAutoText.setText("Tip: 移动球员和篮球，开始布阵");
            } else if (msg.what == 3) {
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, "读取成功！", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 5){
                //清除要删除战术的数据
                int position = msg.arg1;
                String deleteFile = mFileNameList.get(position);

                //mFileNameListSize = SPUtils.getInt(MainActivity.this, "FILE_NAME_LIST_SIZE", 0);
                //先删除存储的文件名数据
                if (mFileNameListSize > 0) {
                    for (int i = 0; i < mFileNameListSize; i++) {
                        SPUtils.removePath(MainActivity.this, "FILE_NAME"+i);
                        Log.d("MainActivity", "删除了："+mFileNameList.get(i)+"----position = " + i);
                    }
                }
                //再将选中数据删除
                mFileNameList.remove(position);
                //更新长度
                mFileNameListSize = mFileNameList.size();
                //重新保存长度
                SPUtils.putInt(MainActivity.this, "FILE_NAME_LIST_SIZE", mFileNameListSize);
                //重新保存文件名列表
                if (mFileNameListSize > 0) {
                    for (int i = 0; i < mFileNameListSize; i++) {
                        SPUtils.putPathString(MainActivity.this, "FILE_NAME"+i, mFileNameList.get(i));
                        Log.d("MainActivity", "保存了："+mFileNameList.get(i)+"----position = " + i);
                    }
                }
                mListViewAdapter.notifyDataSetChanged();
                //删除保存的轨迹
                deletePath(deleteFile);
            }

        }
    };

    private String[] mText;
    private ListViewAdapter mListViewAdapter;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        WindowManager wManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Point point = new Point();
        wManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;

        initView();
        initDialogData();
    }

    private void initView() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//关闭左右手势滑动
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        //在侧滑打开的时候取消锁定模式，侧滑关闭的时候打开锁定模式
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        mBottomBar = (RelativeLayout) findViewById(R.id.title_bar);
        // mTips = (TextView) findViewById(R.id.title_tips);
        mAutoText = (AutoTextView) findViewById(R.id.autoTextView);
        mText = new String[]{"Tip: 移动球员和篮球，开始布阵",
                "Tip: 点击\"+\"开始画战术轨迹",
                "Tip: 再次点击\"+\"开始可分段画轨迹",
                "Tip: 点击左侧按钮可以进入侧边菜单栏",
                "Tip: 点击播放按钮可以播放动画",
                "Tip: 注意保存战术",
                "Tip: 点击右侧按钮清除当前战术"
        };
        //    Log.d("AutoTextView", "mCount = " + mCount);
//        mAutoText.next();
//        mAutoText.setText("Tip: 移动球员和篮球，开始布阵");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    //    Log.d("AutoTextView", "mCount = " + mCount);
                    try {
                        Thread.sleep(3000);
                        //当还没开始画轨迹时，就停在这里
                        if (!isMove) {
                            mHandler.sendEmptyMessage(2);
                            while (true) {
                                //当开始布阵后，跳出死循环开始轮巡
                                // Log.d("AutoTextView", "While循环中");
                                if (isMove) {
                                    break;
                                }
                            }
                        }
                        //  Log.d("AutoTextView", "跳出While死循环");
                        mHandler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mCount == mText.length - 1) {
                        mCount = 0;
                    } else {
                        mCount++;
                    }

                }
            }
        }).start();
        mBottomMenu = (ImageButton) findViewById(R.id.bottom_menu);
        //  mBottomPre = (ImageButton) findViewById(R.id.bottom_pre);
        mBottomPlay = (ImageButton) findViewById(R.id.bottom_play);
        // mBottomNext = (ImageButton) findViewById(R.id.bottom_next);
        mBottomAdd = (ImageButton) findViewById(R.id.bottom_add);
        //mBottomDelete = (ImageButton) findViewById(R.id.bottom_delete);
        mBottomReset = (ImageButton) findViewById(R.id.bottom_reset);

        mBall = (Button) findViewById(R.id.bt_ball);
        mBlue1 = (Button) findViewById(R.id.bt_blue1);
        mBlue2 = (Button) findViewById(R.id.bt_blue2);
        mBlue3 = (Button) findViewById(R.id.bt_blue3);
        mBlue4 = (Button) findViewById(R.id.bt_blue4);
        mBlue5 = (Button) findViewById(R.id.bt_blue5);
        mRed1 = (Button) findViewById(R.id.bt_red1);
        mRed2 = (Button) findViewById(R.id.bt_red2);
        mRed3 = (Button) findViewById(R.id.bt_red3);
        mRed4 = (Button) findViewById(R.id.bt_red4);
        mRed5 = (Button) findViewById(R.id.bt_red5);

        mBottomMenu.setOnClickListener(this);
        // mBottomPre.setOnClickListener(this);
        mBottomPlay.setOnClickListener(this);
        // mBottomNext.setOnClickListener(this);
        mBottomAdd.setOnClickListener(this);
        // mBottomDelete.setOnClickListener(this);
        mBottomReset.setOnClickListener(this);

        mBall.setOnTouchListener(this);
        mBlue1.setOnTouchListener(this);
        mBlue2.setOnTouchListener(this);
        mBlue3.setOnTouchListener(this);
        mBlue4.setOnTouchListener(this);
        mBlue5.setOnTouchListener(this);
        mRed1.setOnTouchListener(this);
        mRed2.setOnTouchListener(this);
        mRed3.setOnTouchListener(this);
        mRed4.setOnTouchListener(this);
        mRed5.setOnTouchListener(this);

        initPaint();
    }

    /**
     * 初始化画笔
     */
    public void initPaint() {
        mImageSrc = (ImageView) findViewById(R.id.iv_src);

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap._full);


        mBitmapWidth = mSrcBitmap.getWidth();
        mBitmapHeight = mSrcBitmap.getHeight();

        //获取实际bitmap图片长宽与屏幕中显示的长宽的比例
        mWidthProportion = (mBitmapWidth * 1f) / mScreenWidth;
        mHeightProportion = (mBitmapHeight * 1f) / (mScreenHeight - UIUtils.dip2px(this, 40f) - UIUtils.dip2px(this, 40f));
//        Log.d("MainActivity", "mWidthProportion = " + mWidthProportion);
//        Log.d("MainActivity", "mHeightProportion = " + mHeightProportion);
        mCopyBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), mSrcBitmap.getConfig());//Bitmap.Config.ARGB_8888);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置是否使用抗锯齿功能
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setStrokeWidth(5f);
        mPaint.setDither(true);//防抖动

        mCanvas = new Canvas(mCopyBitmap);
        mCanvas.drawBitmap(mSrcBitmap, new Matrix(), mPaint);

        mImageSrc.setImageBitmap(mCopyBitmap);

    }

    /**
     * 设置画笔的颜色和宽度
     *
     * @param color
     * @param width
     */
    public void setPaint(int color, float width) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(width);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_open_folder) {
            showOpenDialog();
            // Log.d("NavigationItem", "战术库");
        } else if (id == R.id.nav_pencil_color) {
            showColorDialog();
            // item.setCheckable(false);
            // Log.d("NavigationItem", "画笔颜色");
        } else if (id == R.id.nav_pencil_width) {
            showWidthDialog();
            // item.setCheckable(false);
            //  Log.d("NavigationItem", "画笔粗细");
        } else if (id == R.id.nav_save_floder) {
            showSaveDialog();
            //  Log.d("NavigationItem", "保存");
        } else if (id == R.id.nav_about_me) {
            //   Log.d("NavigationItem", "关于");
            showAboutDialog();
        }

        item.setChecked(false);
        return true;
    }

    /**
     * 重写事件的分发，拦截多点触控，每次只能移动一个球员
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            //当触摸点数多于一点时，接不向下分发触摸事件
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // mTips.setText("Tip: 点击"+"+"+"开始画战术轨迹");
        isMove = true;
        switch (v.getId()) {
            case R.id.bt_ball:
                setPaint(0x66000000, 4f);
                dealingTouchEvent(v, event, ball);
                break;
            case R.id.bt_blue1:
                setPaint(mBluePaintColor, mPaintWidth);
                dealingTouchEvent(v, event, bluePlayer1);
                break;
            case R.id.bt_blue2:
                setPaint(mBluePaintColor, mPaintWidth);
                dealingTouchEvent(v, event, bluePlayer2);
                break;
            case R.id.bt_blue3:
                setPaint(mBluePaintColor, mPaintWidth);
                dealingTouchEvent(v, event, bluePlayer3);
                break;
            case R.id.bt_blue4:
                setPaint(mBluePaintColor, mPaintWidth);
                dealingTouchEvent(v, event, bluePlayer4);
                break;
            case R.id.bt_blue5:
                setPaint(mBluePaintColor, mPaintWidth);
                dealingTouchEvent(v, event, bluePlayer5);
                break;
            case R.id.bt_red1:
                setPaint(mRedPaintColor, mPaintWidth);
                dealingTouchEvent(v, event, redPlayer1);
                break;
            case R.id.bt_red2:
                setPaint(mRedPaintColor, mPaintWidth);
                dealingTouchEvent(v, event, redPlayer2);
                break;
            case R.id.bt_red3:
                setPaint(mRedPaintColor, mPaintWidth);
                dealingTouchEvent(v, event, redPlayer3);
                break;
            case R.id.bt_red4:
                setPaint(mRedPaintColor, mPaintWidth);
                dealingTouchEvent(v, event, redPlayer4);
                break;
            case R.id.bt_red5:
                setPaint(mRedPaintColor, mPaintWidth);
                dealingTouchEvent(v, event, redPlayer5);
                break;
        }
        return true;
    }

    /**
     * 获取球员和球的初始位置
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isOriginalLocation) {
            Log.d("MainActivity", "位置2：" + mBall.getLeft() + mBall.getTop() + mBall.getRight() + mBall.getBottom() + "");
            isOriginalLocation = false;
            for (int i = 0; i < 11; i++) {
                mOriginalLocation[i] = new ButtonLocation();
            }
            mOriginalLocation[0].left = mBall.getLeft();
            mOriginalLocation[0].right = mBall.getRight();
            mOriginalLocation[0].top = mBall.getTop();
            mOriginalLocation[0].bottom = mBall.getBottom();

            mOriginalLocation[1].left = mBlue1.getLeft();
            mOriginalLocation[1].right = mBlue1.getRight();
            mOriginalLocation[1].top = mBlue1.getTop();
            mOriginalLocation[1].bottom = mBlue1.getBottom();

            mOriginalLocation[2].left = mBlue2.getLeft();
            mOriginalLocation[2].right = mBlue2.getRight();
            mOriginalLocation[2].top = mBlue2.getTop();
            mOriginalLocation[2].bottom = mBlue2.getBottom();

            mOriginalLocation[3].left = mBlue3.getLeft();
            mOriginalLocation[3].right = mBlue3.getRight();
            mOriginalLocation[3].top = mBlue3.getTop();
            mOriginalLocation[3].bottom = mBlue3.getBottom();

            mOriginalLocation[4].left = mBlue4.getLeft();
            mOriginalLocation[4].right = mBlue4.getRight();
            mOriginalLocation[4].top = mBlue4.getTop();
            mOriginalLocation[4].bottom = mBlue4.getBottom();

            mOriginalLocation[5].left = mBlue5.getLeft();
            mOriginalLocation[5].right = mBlue5.getRight();
            mOriginalLocation[5].top = mBlue5.getTop();
            mOriginalLocation[5].bottom = mBlue5.getBottom();

            mOriginalLocation[6].left = mRed1.getLeft();
            mOriginalLocation[6].right = mRed1.getRight();
            mOriginalLocation[6].top = mRed1.getTop();
            mOriginalLocation[6].bottom = mRed1.getBottom();

            mOriginalLocation[7].left = mRed2.getLeft();
            mOriginalLocation[7].right = mRed2.getRight();
            mOriginalLocation[7].top = mRed2.getTop();
            mOriginalLocation[7].bottom = mRed2.getBottom();

            mOriginalLocation[8].left = mRed3.getLeft();
            mOriginalLocation[8].right = mRed3.getRight();
            mOriginalLocation[8].top = mRed3.getTop();
            mOriginalLocation[8].bottom = mRed3.getBottom();

            mOriginalLocation[9].left = mRed4.getLeft();
            mOriginalLocation[9].right = mRed4.getRight();
            mOriginalLocation[9].top = mRed4.getTop();
            mOriginalLocation[9].bottom = mRed4.getBottom();

            mOriginalLocation[10].left = mRed5.getLeft();
            mOriginalLocation[10].right = mRed5.getRight();
            mOriginalLocation[10].top = mRed5.getTop();
            mOriginalLocation[10].bottom = mRed5.getBottom();
        }

    }

    /**
     * 重置球员和球的位置，清除数据
     */
    public void resetLocation() {

        isBegin = false;
        isMove = false;
        mCount = 0;
        //清除画板
        mCanvas = null;
        mCanvas = new Canvas(mCopyBitmap);
        mCanvas.drawBitmap(mSrcBitmap, new Matrix(), mPaint);
        mImageSrc.setImageBitmap(mCopyBitmap);

        //重置位置
        mBall.layout(mOriginalLocation[0].left, mOriginalLocation[0].top, mOriginalLocation[0].right, mOriginalLocation[0].bottom);
        mBlue1.layout(mOriginalLocation[1].left, mOriginalLocation[1].top, mOriginalLocation[1].right, mOriginalLocation[1].bottom);
        mBlue2.layout(mOriginalLocation[2].left, mOriginalLocation[2].top, mOriginalLocation[2].right, mOriginalLocation[2].bottom);
        mBlue3.layout(mOriginalLocation[3].left, mOriginalLocation[3].top, mOriginalLocation[3].right, mOriginalLocation[3].bottom);
        mBlue4.layout(mOriginalLocation[4].left, mOriginalLocation[4].top, mOriginalLocation[4].right, mOriginalLocation[4].bottom);
        mBlue5.layout(mOriginalLocation[5].left, mOriginalLocation[5].top, mOriginalLocation[5].right, mOriginalLocation[5].bottom);
        mRed1.layout(mOriginalLocation[6].left, mOriginalLocation[6].top, mOriginalLocation[6].right, mOriginalLocation[6].bottom);
        mRed2.layout(mOriginalLocation[7].left, mOriginalLocation[7].top, mOriginalLocation[7].right, mOriginalLocation[7].bottom);
        mRed3.layout(mOriginalLocation[8].left, mOriginalLocation[8].top, mOriginalLocation[8].right, mOriginalLocation[8].bottom);
        mRed4.layout(mOriginalLocation[9].left, mOriginalLocation[9].top, mOriginalLocation[9].right, mOriginalLocation[9].bottom);
        mRed5.layout(mOriginalLocation[10].left, mOriginalLocation[10].top, mOriginalLocation[10].right, mOriginalLocation[10].bottom);

        ballList.clear();
        bluePlayer1List.clear();
        bluePlayer2List.clear();
        bluePlayer3List.clear();
        bluePlayer4List.clear();
        bluePlayer5List.clear();
        redPlayer1List.clear();
        redPlayer2List.clear();
        redPlayer3List.clear();
        redPlayer4List.clear();
        redPlayer5List.clear();

    }

    /**
     * 画轨迹并保存
     *
     * @param v
     * @param event
     * @param player
     */
    public void dealingTouchEvent(View v, MotionEvent event, Player player) {
        Point point = new Point();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int stopX = (int) event.getRawX();
                int stopY = (int) event.getRawY();
                int dx = stopX - mStartX;
                int dy = stopY - mStartY;

                //v.getLft()等是相对于其父控件而言的
                int l = v.getLeft() + dx;
                int b = v.getBottom() + dy;
                int r = v.getRight() + dx;
                int t = v.getTop() + dy;

                // Log.d("MainActivity", "isBegin = " + isBegin);
                //当点击 mBottomAdd后，才开始画线并记录轨迹
                if (isBegin) {

                    point.x = (v.getLeft() + v.getRight()) / 2;
                    point.y = (v.getTop() + v.getBottom()) / 2;
                    player.addPoint(point);
                    // Log.d("MainActivity", "point = " + point);

                    mCanvas.drawLine(
                            ((v.getLeft() + v.getRight()) / 2) * mWidthProportion,
                            ((v.getTop() + v.getBottom()) / 2) * mHeightProportion,
                            ((l + r) / 2) * mWidthProportion,
                            (t + b) / 2 * mHeightProportion, mPaint);
                    mImageSrc.setImageBitmap(mCopyBitmap);
                    // Log.d("MainActivity", "Left = "+l+"-- Top = "+t+"-- Right = "+r+"-- Bottom = "+b);
                }
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }
                //v.getTop()等是相对于其父控件而言的,所以是直接与0比较
                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }
                if (r > mScreenWidth) {
                    r = mScreenWidth;
                    l = r - v.getWidth();
                }
                //v.getBottom()等是相对于其父控件而言的, 是由屏幕高度减去上下栏的高度
                if (b > (mScreenHeight - mTitleBar.getHeight() - mBottomBar.getHeight())) {
                    b = mScreenHeight - mTitleBar.getHeight() - mBottomBar.getHeight();
                    t = b - v.getHeight();
                }
                v.layout(l, t, r, b);


                mStartX = stopX;
                mStartY = stopY;
                v.postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                //break;
            default:
                break;
        }
    }

    private boolean isBegin = false;
    private boolean isMove = false;
    private boolean isRead = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_menu://菜单
                mDrawer.openDrawer(GravityCompat.START);
                break;

            case R.id.bottom_add://添加一段新轨迹
                isBegin = true;
                isRead = false;
                addNewPath();
                break;

            case R.id.bottom_play://播放
                //showAnimation();
                if (!isPlaying) {
                    isPlaying = true;
                    showAnimation();
                    mBottomPlay.setBackgroundResource(R.drawable.selector_pause);
//                    isPlaying = false;
//                    mBottomPlay.setBackgroundResource(R.drawable.selector_play);
                }
                break;

            case R.id.bottom_reset://重置复位
                resetLocation();
                break;
            default:
                break;
        }
    }

    private void addNewPath() {
        ball = null;
        ball = new Player();
        ballList.add(ball);

        bluePlayer1 = null;
        bluePlayer1 = new Player();
        bluePlayer1List.add(bluePlayer1);

        bluePlayer2 = null;
        bluePlayer2 = new Player();
        bluePlayer2List.add(bluePlayer2);

        bluePlayer3 = null;
        bluePlayer3 = new Player();
        bluePlayer3List.add(bluePlayer3);

        bluePlayer4 = null;
        bluePlayer4 = new Player();
        bluePlayer4List.add(bluePlayer4);

        bluePlayer5 = null;
        bluePlayer5 = new Player();
        bluePlayer5List.add(bluePlayer5);

        redPlayer1 = null;
        redPlayer1 = new Player();
        redPlayer1List.add(redPlayer1);

        redPlayer2 = null;
        redPlayer2 = new Player();
        redPlayer2List.add(redPlayer2);

        redPlayer3 = null;
        redPlayer3 = new Player();
        redPlayer3List.add(redPlayer3);

        redPlayer4 = null;
        redPlayer4 = new Player();
        redPlayer4List.add(redPlayer4);

        redPlayer5 = null;
        redPlayer5 = new Player();
        redPlayer5List.add(redPlayer5);
    }

    public void showAnimation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ballList.size(); i++) {
                    Vector<Thread> threadJoin = new Vector<>();
                    //篮球
                    if (ballList.get(i).size() > 0) {
                       // setPaint(0x66000000, 4f);
                        Thread threadBall = new Thread(new AnimationRunnable(ballList.get(i), mBall, 0x66000000));
                        threadJoin.add(threadBall);
                        threadBall.start();
                    }
                    //蓝色1号
                    if (bluePlayer1List.get(i).size() > 0) {
                        //setPaint(mBluePaintColor, mPaintWidth);
                        Thread threadBluePlayer1 = new Thread(new AnimationRunnable(bluePlayer1List.get(i), mBlue1, mBluePaintColor));
                        threadJoin.add(threadBluePlayer1);
                        threadBluePlayer1.start();
                    }
                    //蓝色2号
                    if (bluePlayer2List.get(i).size() > 0) {
                        //setPaint(mBluePaintColor, mPaintWidth);
                        Thread threadBluePlayer2 = new Thread(new AnimationRunnable(bluePlayer2List.get(i), mBlue2, mBluePaintColor));
                        threadJoin.add(threadBluePlayer2);
                        threadBluePlayer2.start();
                    }
                    //蓝色3号
                    if (bluePlayer3List.get(i).size() > 0) {
                        //setPaint(mBluePaintColor, mPaintWidth);
                        Thread threadBluePlayer3 = new Thread(new AnimationRunnable(bluePlayer3List.get(i), mBlue3, mBluePaintColor));
                        threadJoin.add(threadBluePlayer3);
                        threadBluePlayer3.start();
                    }
                    //蓝色4号
                    if (bluePlayer4List.get(i).size() > 0) {
                        //setPaint(mBluePaintColor, mPaintWidth);
                        Thread threadBluePlayer4 = new Thread(new AnimationRunnable(bluePlayer4List.get(i), mBlue4, mBluePaintColor));
                        threadJoin.add(threadBluePlayer4);
                        threadBluePlayer4.start();
                    }
                    //蓝色5号
                    if (bluePlayer5List.get(i).size() > 0) {
                        //setPaint(mBluePaintColor, mPaintWidth);
                        Thread threadBluePlayer5 = new Thread(new AnimationRunnable(bluePlayer5List.get(i), mBlue5,mBluePaintColor));
                        threadJoin.add(threadBluePlayer5);
                        threadBluePlayer5.start();
                    }

                    //红色1号
                    if (redPlayer1List.get(i).size() > 0) {
                        //setPaint(mRedPaintColor, mPaintWidth);
                        Thread threadRedPlayer1 = new Thread(new AnimationRunnable(redPlayer1List.get(i), mRed1, mRedPaintColor));
                        threadJoin.add(threadRedPlayer1);
                        threadRedPlayer1.start();
                    }
                    //红色2号
                    if (redPlayer2List.get(i).size() > 0) {
                        //setPaint(mRedPaintColor, mPaintWidth);
                        Thread threadRedPlayer2 = new Thread(new AnimationRunnable(redPlayer2List.get(i), mRed2, mRedPaintColor));
                        threadJoin.add(threadRedPlayer2);
                        threadRedPlayer2.start();
                    }
                    //红色3号
                    if (redPlayer3List.get(i).size() > 0) {
                        //setPaint(mRedPaintColor, mPaintWidth);
                        Thread threadRedPlayer3 = new Thread(new AnimationRunnable(redPlayer3List.get(i), mRed3, mRedPaintColor));
                        threadJoin.add(threadRedPlayer3);
                        threadRedPlayer3.start();
                    }
                    //红色4号
                    if (redPlayer4List.get(i).size() > 0) {
                        //setPaint(mRedPaintColor, mPaintWidth);
                        Thread threadRedPlayer4 = new Thread(new AnimationRunnable(redPlayer4List.get(i), mRed4, mRedPaintColor));
                        threadJoin.add(threadRedPlayer4);
                        threadRedPlayer4.start();
                    }
                    //红色5号
                    if (redPlayer5List.get(i).size() > 0) {
                        //setPaint(mRedPaintColor, mPaintWidth);
                        Thread threadRedPlayer5 = new Thread(new AnimationRunnable(redPlayer5List.get(i), mRed5, mRedPaintColor));
                        threadJoin.add(threadRedPlayer5);
                        threadRedPlayer5.start();
                    }

                    for (Thread t : threadJoin) {
                        try {
                            t.join();
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }
                    //  System.out.println("主线执行。");

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPlaying = false;
                        mBottomPlay.setBackgroundResource(R.drawable.selector_play);
                    }
                });

            }


        }).start();

    }

    class AnimationRunnable implements Runnable {

        private Player mPlayer;
        private View mView;
        private int mPaintColor;
        public AnimationRunnable(Player player, View v,int paintColor) {
            this.mPlayer = player;
            this.mView = v;
            this.mPaintColor = paintColor;
        }

        @Override
        public void run() {
            Log.d("MainActivity", "线程:" + Thread.currentThread().getName());

            for (int i = 0; i < mPlayer.size(); i++) {
                final int index = i;
                final int l = mPlayer.getPoint(i).x - mView.getWidth() / 2;
                final int t = mPlayer.getPoint(i).y - mView.getHeight() / 2;
                final int r = mPlayer.getPoint(i).x + mView.getWidth() / 2;
                final int b = mPlayer.getPoint(i).y + mView.getHeight() / 2;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRead && (index != 0)) {
                            setPaint(mPaintColor, mPaintWidth);
                            //如果是读取出来的数据，就需要画轨迹；不是的话，直接移动球员和球就行
                            mCanvas.drawLine(
                                    ((mView.getLeft() + mView.getRight()) / 2) * mWidthProportion,
                                    ((mView.getTop() + mView.getBottom()) / 2) * mHeightProportion,
                                    ((l + r) / 2) * mWidthProportion,
                                    (t + b) / 2 * mHeightProportion, mPaint);
                            mImageSrc.setImageBitmap(mCopyBitmap);
                        }
                        mView.layout(l, t, r, b);
                    }
                });
                //移动第一个点时不需要延时
                if (!(isRead && (index == 0))){
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

//                Log.d("MainActivity", "移动中 = " + mPlayer.isFinishAnimation);
            }
        }
    }


    private ArrayList<PaintColor> mColorList = new ArrayList<>();
    private ArrayList<PaintWidth> mWidthList = new ArrayList<>();
    private ArrayList<String> mFileNameList = new ArrayList<>();
    private int mBlueColorSelectPos;
    private int mRedColorSelectPos;
    private int mWidthColorSelectPos;
    private int mFileNameListSize;

    public void initDialogData() {
        mColorList.add(new PaintColor("白色", 0xFFFFFFFF));
        mColorList.add(new PaintColor("灰色", 0xFF808080));
        mColorList.add(new PaintColor("水绿色", 0xFF00FFFF));
        mColorList.add(new PaintColor("绿黄色", 0xFF00FF00));
        mColorList.add(new PaintColor("黄色", 0xFFFFFF00));
        mColorList.add(new PaintColor("绿色", 0xFF008000));
        mColorList.add(new PaintColor("红色", 0xFFFF0000));
        mColorList.add(new PaintColor("紫红色", 0xFFFF00FF));
        mColorList.add(new PaintColor("蓝色", 0xFF0000FF));
        mColorList.add(new PaintColor("紫色", 0xFF800080));
        mColorList.add(new PaintColor("红褐色", 0xFF800000));
        mColorList.add(new PaintColor("黑色", 0xFF000000));


        mWidthList.add(new PaintWidth("1 px", 1f));
        mWidthList.add(new PaintWidth("2 px", 2f));
        mWidthList.add(new PaintWidth("3 px", 3f));
        mWidthList.add(new PaintWidth("4 px", 4f));
        mWidthList.add(new PaintWidth("5 px", 5f));
        mWidthList.add(new PaintWidth("6 px", 6f));
        mWidthList.add(new PaintWidth("7 px", 7f));
        mWidthList.add(new PaintWidth("8 px", 8f));
        mWidthList.add(new PaintWidth("9 px", 9f));
        mWidthList.add(new PaintWidth("10 px", 10f));
        mWidthList.add(new PaintWidth("11 px", 11f));
        mWidthList.add(new PaintWidth("12 px", 12f));

        //读取保存结果
        mRedColorSelectPos = SPUtils.getInt(MainActivity.this, "RED_COLOR_SELECT_POS", 6);
        mBlueColorSelectPos = SPUtils.getInt(MainActivity.this, "BLUE_COLOR_SELECT_POS", 8);
        //更改颜色
        mRedPaintColor = mColorList.get(mRedColorSelectPos).mColor;
        mBluePaintColor = mColorList.get(mBlueColorSelectPos).mColor;

        //读取保存结果
        mWidthColorSelectPos = SPUtils.getInt(MainActivity.this, "WIDTH__SELECT_POS", 4);
        //更改宽度
        mPaintWidth = mWidthList.get(mWidthColorSelectPos).mWidth;

        mFileNameListSize = SPUtils.getInt(MainActivity.this, "FILE_NAME_LIST_SIZE", 0);
        Log.d("MainActivity", "初始化 mFileNameListSize = "+mFileNameListSize);
        if (mFileNameListSize > 0) {
            for (int i = 0; i < mFileNameListSize; i++) {
                String fileName = SPUtils.getPathString(MainActivity.this, "FILE_NAME"+i, "");
                mFileNameList.add(fileName);
                Log.d("MainActivity", "文件名："+mFileNameList.get(i)+"----position = " + i);
                Log.d("MainActivity", "文件名数："+mFileNameList.size());
            }
        }

        mListViewAdapter = new ListViewAdapter(MainActivity.this, mFileNameList, mHandler);
    }


    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_about_layout, null);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }

    /**
     * 更改颜色的对话框
     */
    public void showColorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_color_layout, null);

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final Spinner redSpinner = (Spinner) view.findViewById(R.id.red_spinner);
        final Spinner blueSpinner = (Spinner) view.findViewById(R.id.blue_spinner);

        //设置适配器
        redSpinner.setAdapter(new ColorSpinnerAdapter(this, mColorList));
        blueSpinner.setAdapter(new ColorSpinnerAdapter(this, mColorList));
//        //读取保存结果
//        mRedColorSelectPos = SPUtils.getInt(MainActivity.this,"RED_COLOR_SELECT_POS", 6);
//        mBlueColorSelectPos = SPUtils.getInt(MainActivity.this,"BLUE_COLOR_SELECT_POS", 8);
        //下拉条设置到当前选中颜色的序号
        redSpinner.setSelection(mRedColorSelectPos, true);
        blueSpinner.setSelection(mBlueColorSelectPos, true);
//        //更改颜色
//        mRedPaintColor = mColorList.get(mRedColorSelectPos).mColor;
//        mBluePaintColor = mColorList.get(mRedColorSelectPos).mColor;


        redSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRedColorSelectPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        blueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBlueColorSelectPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存当前选中颜色的序号
                SPUtils.putInt(MainActivity.this, "RED_COLOR_SELECT_POS", mRedColorSelectPos);
                SPUtils.putInt(MainActivity.this, "BLUE_COLOR_SELECT_POS", mBlueColorSelectPos);
                //下拉条设置到当前选中颜色的序号
                redSpinner.setSelection(mRedColorSelectPos, true);
                blueSpinner.setSelection(mBlueColorSelectPos, true);
                //更改颜色
                mRedPaintColor = mColorList.get(mRedColorSelectPos).mColor;
                mBluePaintColor = mColorList.get(mBlueColorSelectPos).mColor;
                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    /**
     * 更改画笔宽度的对话框
     */
    public void showWidthDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_width_layout, null);

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final Spinner widthSpinner = (Spinner) view.findViewById(R.id.width_spinner);

        //设置适配器
        widthSpinner.setAdapter(new WidthSpinnerAdapter(this, mWidthList));
//        mWidthColorSelectPos = SPUtils.getInt(MainActivity.this,"WIDTH__SELECT_POS", 4);
        //下拉条设置到当前选中颜色的序号
        widthSpinner.setSelection(mWidthColorSelectPos, true);
//        //更改宽度
//        mPaintWidth = mWidthList.get(mWidthColorSelectPos).mWidth;

        widthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWidthColorSelectPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.putInt(MainActivity.this, "WIDTH__SELECT_POS", mWidthColorSelectPos);
                //下拉条设置到当前选中颜色的序号
                widthSpinner.setSelection(mWidthColorSelectPos, true);
                //更改宽度
                mPaintWidth = mWidthList.get(mWidthColorSelectPos).mWidth;
                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 战术库对话框
     */
    public void showOpenDialog() {
        //读取前先清除数据
        resetLocation();
        isRead = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_open_layout, null);

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        if (mFileNameList.size() == 0) {
            Toast.makeText(MainActivity.this, "你还没有添加战术，赶快去画战术吧！", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        }
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final ListView listView = (ListView) view.findViewById(R.id.lv_file);

        //设置适配器
        //mListViewAdapter = new ListViewAdapter(MainActivity.this, mFileNameList);
        listView.setAdapter(mListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow("正在读取，请稍候...");
                openFile(mFileNameList.get(position));
                dialog.dismiss();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 保存战术的对话框
     */
    public void showSaveDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_save_layout, null);

        final EditText editText = (EditText) view.findViewById(R.id.et_file_name);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballList.size() == 0) {
                    Toast.makeText(MainActivity.this, "当前还未画任何轨迹，请先画战术再保存！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                String fileName = editText.getText().toString().trim();
                if (TextUtils.isEmpty(fileName)) {
                    Toast.makeText(MainActivity.this, "文件名不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mFileNameList.contains(fileName)) {
                    Toast.makeText(MainActivity.this, "文件名重复，请重新命名！", Toast.LENGTH_SHORT).show();
                } else {
                    mFileNameList.add(fileName);
                    mFileNameListSize = mFileNameList.size();
                    Log.d("MainActivity", "保存： mFileNameListSize = " + mFileNameListSize);
                    //保存战术的数量
                    SPUtils.putInt(MainActivity.this, "FILE_NAME_LIST_SIZE", mFileNameListSize);
                    //保存战术名称
                    SPUtils.putPathString(MainActivity.this, "FILE_NAME"+(mFileNameListSize-1),fileName);
                    mListViewAdapter.notifyDataSetChanged();
                    showPopupWindow("正在保存，请稍后...");
                    savePath(fileName);
                    dialog.dismiss();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static final String SEG = "_SEG";//保存轨迹段数的附加key
    private static final String POINT = "_POINT";//保存轨迹段数的附加key

    public void savePath(final String key) {
        if (ballList.size() == 0) {
            Toast.makeText(MainActivity.this, "当前还未画任何轨迹，请先画战术再保存！", Toast.LENGTH_SHORT).show();
        } else {
            //【1】、先保存总段数(10个球员和球的段数是相同的)
            SPUtils.putPathInt(MainActivity.this, key + SEG, ballList.size());

            //【2】、开子线程分别保存每个对象
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < ballList.size(); i++) {
                        Vector<Thread> threadJoin = new Vector<>();
                        //篮球
                        //【3】、保存每一段的点数
                        SPUtils.putPathInt(MainActivity.this, key + "ballList" + SEG + i, ballList.get(i).size());
                        if (ballList.get(i).size() > 0) {
                            Thread threadBall = new Thread(new SavePathRunnable(ballList.get(i), key + "ballList" + SEG + i));
                            threadJoin.add(threadBall);
                            threadBall.start();
                        }
                        //蓝色1号
                        SPUtils.putPathInt(MainActivity.this, key + "bluePlayer1List" + SEG + i, bluePlayer1List.get(i).size());
                        if (bluePlayer1List.get(i).size() > 0) {
                            Thread threadBluePlayer1 = new Thread(new SavePathRunnable(bluePlayer1List.get(i), key + "bluePlayer1List" + SEG + i));
                            threadJoin.add(threadBluePlayer1);
                            threadBluePlayer1.start();
                        }
                        //蓝色2号
                        SPUtils.putPathInt(MainActivity.this, key + "bluePlayer2List" + SEG + i, bluePlayer2List.get(i).size());
                        if (bluePlayer2List.get(i).size() > 0) {
                            Thread threadBluePlayer2 = new Thread(new SavePathRunnable(bluePlayer2List.get(i), key + "bluePlayer2List" + SEG + i));
                            threadJoin.add(threadBluePlayer2);
                            threadBluePlayer2.start();
                        }
                        //蓝色3号
                        SPUtils.putPathInt(MainActivity.this, key + "bluePlayer3List" + SEG + i, bluePlayer3List.get(i).size());
                        if (bluePlayer3List.get(i).size() > 0) {
                            Thread threadBluePlayer3 = new Thread(new SavePathRunnable(bluePlayer3List.get(i), key + "bluePlayer3List" + SEG + i));
                            threadJoin.add(threadBluePlayer3);
                            threadBluePlayer3.start();
                        }
                        //蓝色4号
                        SPUtils.putPathInt(MainActivity.this, key + "bluePlayer4List" + SEG + i, bluePlayer4List.get(i).size());
                        if (bluePlayer4List.get(i).size() > 0) {
                            Thread threadBluePlayer4 = new Thread(new SavePathRunnable(bluePlayer4List.get(i), key + "bluePlayer4List" + SEG + i));
                            threadJoin.add(threadBluePlayer4);
                            threadBluePlayer4.start();
                        }
                        //蓝色5号
                        SPUtils.putPathInt(MainActivity.this, key + "bluePlayer5List" + SEG + i, bluePlayer5List.get(i).size());
                        if (bluePlayer5List.get(i).size() > 0) {
                            Thread threadBluePlayer5 = new Thread(new SavePathRunnable(bluePlayer5List.get(i), key + "bluePlayer5List" + SEG + i));
                            threadJoin.add(threadBluePlayer5);
                            threadBluePlayer5.start();
                        }

                        //红色1号
                        SPUtils.putPathInt(MainActivity.this, key + "redPlayer1List" + SEG + i, redPlayer1List.get(i).size());
                        if (redPlayer1List.get(i).size() > 0) {
                            Thread threadRedPlayer1 = new Thread(new SavePathRunnable(redPlayer1List.get(i), key + "redPlayer1List" + SEG + i));
                            threadJoin.add(threadRedPlayer1);
                            threadRedPlayer1.start();
                        }
                        //红色2号
                        SPUtils.putPathInt(MainActivity.this, key + "redPlayer2List" + SEG + i, redPlayer2List.get(i).size());
                        if (redPlayer2List.get(i).size() > 0) {
                            Thread threadRedPlayer2 = new Thread(new SavePathRunnable(redPlayer2List.get(i), key + "redPlayer2List" + SEG + i));
                            threadJoin.add(threadRedPlayer2);
                            threadRedPlayer2.start();
                        }
                        //红色3号
                        SPUtils.putPathInt(MainActivity.this, key + "redPlayer3List" + SEG + i, redPlayer3List.get(i).size());
                        if (redPlayer3List.get(i).size() > 0) {
                            Thread threadRedPlayer3 = new Thread(new SavePathRunnable(redPlayer3List.get(i), key + "redPlayer3List" + SEG + i));
                            threadJoin.add(threadRedPlayer3);
                            threadRedPlayer3.start();
                        }
                        //红色4号
                        SPUtils.putPathInt(MainActivity.this, key + "redPlayer4List" + SEG + i, redPlayer4List.get(i).size());
                        if (redPlayer4List.get(i).size() > 0) {
                            Thread threadRedPlayer4 = new Thread(new SavePathRunnable(redPlayer4List.get(i), key + "redPlayer4List" + SEG + i));
                            threadJoin.add(threadRedPlayer4);
                            threadRedPlayer4.start();
                        }
                        //红色5号
                        SPUtils.putPathInt(MainActivity.this, key + "redPlayer5List" + SEG + i, redPlayer5List.get(i).size());
                        if (redPlayer5List.get(i).size() > 0) {
                            Thread threadRedPlayer5 = new Thread(new SavePathRunnable(redPlayer5List.get(i), key + "redPlayer5List" + SEG + i));
                            threadJoin.add(threadRedPlayer5);
                            threadRedPlayer5.start();
                        }

                        for (Thread t : threadJoin) {
                            try {
                                t.join();
                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }
                        }
                        //  System.out.println("主线执行。");

                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //保存成功
                    mHandler.sendEmptyMessage(3);

                }


            }).start();

        }
    }

    class SavePathRunnable implements Runnable {

        private Player mPlayer;
        private String key;

        public SavePathRunnable(Player player, String key) {
            mPlayer = player;
            this.key = key;
        }

        @Override
        public void run() {
            Log.d("MainActivity", "线程:" + Thread.currentThread().getName());
            for (int i = 0; i < mPlayer.size(); i++) {
                //用String一次保存点(100.200)保存为String类型的"100+200"，取的时候从中间截取
                //此处的出入的key应该是(保存文件名+SEG(i))
                SPUtils.putPathString(MainActivity.this, key + POINT + i,
                        mPlayer.getPoint(i).x + "," + mPlayer.getPoint(i).y);

//                Log.d("MainActivity", "移动中 = " + mPlayer.isFinishAnimation);
            }
        }
    }

    public void openFile(final String key) {
        //【1】、先读取总段数(10个球员和球的段数是相同的)
        final int segCount = SPUtils.getPathInt(MainActivity.this, key + SEG, 0);
        if (segCount == 0) {
            Toast.makeText(MainActivity.this, "数据错误，请删除当前选择的战术！", Toast.LENGTH_SHORT).show();
        } else {

            //【2】、开子线程读取每个对象
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //有几段就循环几次
                    for (int i = 0; i < segCount; i++) {
                        int ballListSize = SPUtils.getPathInt(MainActivity.this, key + "ballList" + SEG + i, 0);
                        ballList.add(getPlayerPoint(ballListSize, key + "ballList" + SEG + i));

                        int bluePlayer1ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer1List" + SEG + i, 0);
                        bluePlayer1List.add(getPlayerPoint(bluePlayer1ListSize, key + "bluePlayer1List" + SEG + i));

                        int bluePlayer2ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer2List" + SEG + i, 0);
                        bluePlayer2List.add(getPlayerPoint(bluePlayer2ListSize, key + "bluePlayer2List" + SEG + i));

                        int bluePlayer3ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer3List" + SEG + i, 0);
                        bluePlayer3List.add(getPlayerPoint(bluePlayer3ListSize, key + "bluePlayer3List" + SEG + i));

                        int bluePlayer4ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer4List" + SEG + i, 0);
                        bluePlayer4List.add(getPlayerPoint(bluePlayer4ListSize, key + "bluePlayer4List" + SEG + i));

                        int bluePlayer5ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer5List" + SEG + i, 0);
                        bluePlayer5List.add(getPlayerPoint(bluePlayer5ListSize, key + "bluePlayer5List" + SEG + i));

                        int redPlayer1ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer1List" + SEG + i, 0);
                        redPlayer1List.add(getPlayerPoint(redPlayer1ListSize, key + "redPlayer1List" + SEG + i));

                        int redPlayer2ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer2List" + SEG + i, 0);
                        redPlayer2List.add(getPlayerPoint(redPlayer2ListSize, key + "redPlayer2List" + SEG + i));

                        int redPlayer3ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer3List" + SEG + i, 0);
                        redPlayer3List.add(getPlayerPoint(redPlayer3ListSize, key + "redPlayer3List" + SEG + i));

                        int redPlayer4ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer4List" + SEG + i, 0);
                        redPlayer4List.add(getPlayerPoint(redPlayer4ListSize, key + "redPlayer4List" + SEG + i));

                        int redPlayer5ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer5List" + SEG + i, 0);
                        redPlayer5List.add(getPlayerPoint(redPlayer5ListSize, key + "redPlayer5List" + SEG + i));
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //读取成功
                    mHandler.sendEmptyMessage(4);
                }
            }).start();

        }
    }


    public Player getPlayerPoint(int listSize, String key) {
        Player player = new Player();
        if (listSize > 0) {

            for (int j = 0; j < listSize; j++) {

                //此处的出入的key应该是(保存文件名+SEG(i))
                String value = SPUtils.getPathString(MainActivity.this, key + POINT + j, "0,0");
                String[] sPoint = value.split(",");
                Point point = new Point();
                point.x = Integer.parseInt(sPoint[0]);
                point.y = Integer.parseInt(sPoint[1]);
                player.addPoint(point);
            }
        }
        return player;
    }

    private void deletePath(final String key) {
        //Toast.makeText(MainActivity.this, key+"已删除！", Toast.LENGTH_SHORT).show();
        //【1】、先读取总段数(10个球员和球的段数是相同的)
        final int segCount = SPUtils.getPathInt(MainActivity.this, key + SEG, 0);
        if (segCount == 0) {
            Toast.makeText(MainActivity.this, key+"已删除！", Toast.LENGTH_SHORT).show();
        } else {
            //【2】、开子线程读取每个对象
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //有几段就循环几次
                    for (int i = 0; i < segCount; i++) {
                        int ballListSize = SPUtils.getPathInt(MainActivity.this, key + "ballList" + SEG + i, 0);
                        removePlayerPoint(ballListSize, key + "ballList" + SEG + i);


                        int bluePlayer1ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer1List" + SEG + i, 0);
                        removePlayerPoint(bluePlayer1ListSize, key + "bluePlayer1List" + SEG + i);

                        int bluePlayer2ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer2List" + SEG + i, 0);
                        removePlayerPoint(bluePlayer2ListSize, key + "bluePlayer2List" + SEG + i);

                        int bluePlayer3ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer3List" + SEG + i, 0);
                        removePlayerPoint(bluePlayer3ListSize, key + "bluePlayer3List" + SEG + i);

                        int bluePlayer4ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer4List" + SEG + i, 0);
                        removePlayerPoint(bluePlayer4ListSize, key + "bluePlayer4List" + SEG + i);

                        int bluePlayer5ListSize = SPUtils.getPathInt(MainActivity.this, key + "bluePlayer5List" + SEG + i, 0);
                        removePlayerPoint(bluePlayer5ListSize, key + "bluePlayer5List" + SEG + i);

                        int redPlayer1ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer1List" + SEG + i, 0);
                        removePlayerPoint(redPlayer1ListSize, key + "redPlayer1List" + SEG + i);

                        int redPlayer2ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer2List" + SEG + i, 0);
                        removePlayerPoint(redPlayer2ListSize, key + "redPlayer2List" + SEG + i);

                        int redPlayer3ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer3List" + SEG + i, 0);
                        removePlayerPoint(redPlayer3ListSize, key + "redPlayer3List" + SEG + i);

                        int redPlayer4ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer4List" + SEG + i, 0);
                        removePlayerPoint(redPlayer4ListSize, key + "redPlayer4List" + SEG + i);

                        int redPlayer5ListSize = SPUtils.getPathInt(MainActivity.this, key + "redPlayer5List" + SEG + i, 0);
                        removePlayerPoint(redPlayer5ListSize, key + "redPlayer5List" + SEG + i);
                    }

                }
            }).start();
            SPUtils.removePath(MainActivity.this, key + SEG);
            Toast.makeText(MainActivity.this, key+"已删除！", Toast.LENGTH_SHORT).show();
        }
    }

    public void removePlayerPoint(int listSize, String key) {
        if (listSize > 0) {

            for (int j = 0; j < listSize; j++) {
                //删除每一个点
                SPUtils.remove(MainActivity.this, key + POINT + j);
            }

            //删除保存的点的数量
            SPUtils.remove(MainActivity.this, key);
        }

    }

    private void showPopupWindow(String str) {

        View popView = View.inflate(getApplicationContext(), R.layout.layout_pop, null);

        TextView textView = (TextView) popView.findViewById(R.id.tv_pb);
        textView.setText(str);


        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaa666666));//2147483647

        mPopupWindow.showAtLocation(mDrawer, Gravity.CENTER, 0, 0);
        Log.d("MainActivity", "showPopupWindow ");
    }
}
