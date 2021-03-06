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

package li.klass.fhem.domain.culhm;

import li.klass.fhem.domain.CULHMDevice;
import li.klass.fhem.domain.core.DeviceXMLParsingBase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ThermostatTest extends DeviceXMLParsingBase {
    @Test
    public void testForCorrectlySetAttributes() {
        CULHMDevice device = getDefaultDevice();

        assertThat(device.getName(), is(DEFAULT_TEST_DEVICE_NAME));
        assertThat(device.getRoomConcatenated(), is(DEFAULT_TEST_ROOM_NAME));

        assertThat(device.getState(), is("12%"));
        assertThat(device.getSubType(), is(CULHMDevice.SubType.THERMOSTAT));

        assertThat(device.isSupported(), is(true));

        CULHMDevice device1 = getDeviceFor("device1");
        assertThat(device1, is(notNullValue()));
        assertThat(device1.isSupported(), is(true));
        assertThat(device.getSubType(), is(CULHMDevice.SubType.THERMOSTAT));

        CULHMDevice device2 = getDeviceFor("device2");
        assertThat(device2, is(notNullValue()));
        assertThat(device2.isSupported(), is(true));
        assertThat(device2.getSubType(), is(CULHMDevice.SubType.HEATING));
        assertThat(device2.getDesiredTemp(), is(closeTo(16, 0.01)));
        assertThat(device2.getDesiredTempDesc(), is("16.0 (°C)"));
        assertThat(device2.getActuator(), is("86 (%)"));
    }

    @Override
    protected String getFileName() {
        return "thermostat.xml";
    }
}
