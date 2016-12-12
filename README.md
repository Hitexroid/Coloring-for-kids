# Coloring-for-kids
Coloring-for-kids
Update at 12 Dec 2016

## ScreenShot

<img src="./ScreenShot/photo_2016-12-12_12-24-54.jpg" alt="screenShot1" width="170" height="whatever">

## Usage
<pre>
  private Handler handler = new Handler() {
    @Override
        public void handleMessage(Message msg) {
        }
  };
</pre>

<pre>
 mPaintView.post(new Runnable() {
    @Override
        public void run() {
            mPaintView.loadFromBitmap(bitmap,handler);
        }
});
</pre>

<pre>
  mPaintView().setPaintColor(Color);
</pre>
