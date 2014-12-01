package org.rickosborne.badger;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.caverock.androidsvg.SVGImageView;

public class ActivityWithSVG extends Activity {

    protected SVGImageView loadSvgResource(int resourceId, int containerId, int dimensionId) {
        LinearLayout container = (LinearLayout) findViewById(containerId);
        SVGImageView svg = null;
        if (container != null) {
            svg = new SVGImageView(this);
            svg.setImageResource(resourceId);
            svg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int iconSize = Math.round(getResources().getDimension(dimensionId));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            container.addView(svg, layoutParams);
        }
        return svg;
    }

}
