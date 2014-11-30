package org.rickosborne.badger;

import android.app.Activity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVGImageView;

/**
 * Created by rosborne on 10/26/14.
 */
public class ActivityWithSVG extends Activity {

    protected void loadSvgResource(int resourceId, int containerId, int dimensionId) {
        LinearLayout container = (LinearLayout) findViewById(containerId);
        if (container != null) {
            SVGImageView svg = new SVGImageView(this);
            svg.setImageResource(resourceId);
            svg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int iconSize = Math.round(getResources().getDimension(dimensionId));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            container.addView(svg, layoutParams);
        }
    }

}
