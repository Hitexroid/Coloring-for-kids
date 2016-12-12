package ir.hitexroid.color;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;

import com.swifty.fillcolor.PaintView;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.objects.CustomViewWrapper;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.collections.Map;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;

@TargetApi(8)
@SuppressWarnings("unused")
@BA.ShortName("Hitex_ColorImageViewPro")
@BA.Version(2.0f)
@BA.ActivityObject
public class Hitex_ColorImageViewPro extends ViewWrapper<PaintView> implements Common.DesignerCustomView {

    @BA.Hide
    public BA ba;
    public int VERTICAL=0;

    /**
     * Created By Sadeq Nameni (Hitexroid)
     */
    public void Initialize(BA ba) {
        _initialize(ba,null,"");
    }

    @Override
    public void DesignerCreateView(PanelWrapper base, LabelWrapper labelWrapper, Map map) {
        CustomViewWrapper.replaceBaseWithView2(base,getObject());
    }

    @BA.Hide
    @Override
    public void _initialize(BA ba, Object Ac, String EventName) {
        this.ba = ba;

        setObject(new PaintView(ba.context));

        getObject().performClick();

        getObject().postDelayed(new Runnable() {
            public void run() {
                getObject().invalidate();
            }
        }, 0);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        }
    };

    public void setImageBitmap(final CanvasWrapper.BitmapWrapper bitmap) {

        getObject().post(new Runnable() {
            @Override
            public void run() {
                getObject().loadFromBitmap(bitmap.getObject(),handler);
            }
        });
    }

    public void setPaintColor(int Color) {
        getObject().setPaintColor(Color);
    }

    public int getOrientation() {
        Display display = ((WindowManager) ba.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRotation();
    }

}