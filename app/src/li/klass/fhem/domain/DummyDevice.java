/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2011, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */

package li.klass.fhem.domain;

import li.klass.fhem.domain.core.ToggleableDevice;
import li.klass.fhem.domain.genericview.DetailOverviewViewSettings;
import li.klass.fhem.util.ArrayUtil;
import org.w3c.dom.NamedNodeMap;

@DetailOverviewViewSettings(showState = true)
@SuppressWarnings("unused")
public class DummyDevice extends ToggleableDevice<DummyDevice> {

    private boolean timerDevice = false;

    public void readSTATE(String tagName, String value, NamedNodeMap attributes) {
        this.measured = attributes.getNamedItem("measured").getNodeValue();
    }

    @Override
    public boolean supportsToggle() {
        return ArrayUtil.contains(getAvailableTargetStates(), "on", "off");
    }

    @Override
    public boolean isOnByState() {
        return super.isOnByState() || getState().equalsIgnoreCase("on");
    }

    @Override
    public void afterXMLRead() {
        super.afterXMLRead();

        if (ArrayUtil.contains(getAvailableTargetStates(), "time")) {
            timerDevice = true;
        }
    }

    public boolean isTimerDevice() {
        return timerDevice;
    }
}
