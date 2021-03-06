/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 *  server.
 *
 *  Copyright (c) 2012, Matthias Klass or third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by Red Hat Inc.
 *
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLICLICENSE, as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 *  for more details.
 *
 *  You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 *  along with this distribution; if not, write to:
 *    Free Software Foundation, Inc.
 *    51 Franklin Street, Fifth Floor
 */

package li.klass.fhem.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import li.klass.fhem.AndFHEMApplication;

public class DisplayUtil {
    /**
     * @return either the x or the y dimension in dp. Which dimension is returned depends on whether x or y is bigger.
     */
    public static int getLargestDimensionInDP(Activity activity) {
        DisplayMetrics metrics = getDisplayMetrics(activity);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;

        int largerDimension = heightPixels > widthPixels ? heightPixels : widthPixels;
        return (int) (largerDimension * metrics.density);
    }

    public static int getWidthInDP(Activity activity) {
        return getDisplayMetrics(activity).widthPixels;
    }

    private static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics;
    }

    public static float dpToPx(int dp) {
        Resources resources = AndFHEMApplication.getContext().getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
